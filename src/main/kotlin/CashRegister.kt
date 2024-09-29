import money.logger.Logger
import money.exception.TransactionException
import money.strategy.ChangeStrategy
import money.strategy.GreedyStrategy
import org.jetbrains.annotations.TestOnly

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(
    private val change: Change,
    private val logger: Logger = Logger(),
    private val changeStrategy: ChangeStrategy = GreedyStrategy(logger)
) {

    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    fun performTransaction(price: Long, amountPaid: Change): Change {

        logger.logVerbose("Performing transaction of price $price. Amount paid in change = $amountPaid ")

        val totalPaid = amountPaid.total
        if (totalPaid < price) {
            logger.logError("Amount paid $totalPaid is less than the price $price of item")
            throw TransactionException("Insufficient amount paid.")
        }

        synchronized(change) {

            amountPaid.getElements().forEach {
                change.add(it, amountPaid.getCount(it))
            }

            val amountToBeReturned = totalPaid - price

            if (change.total < amountToBeReturned) {
                logger.logError("No change to return for the amountPaid")
                throw TransactionException("No change to return for the amountPaid")
            }

            return try {
                val changeToGive = changeStrategy.makeChange(change, amountToBeReturned)
                // If successful, subtract the change given from the register's current change
                changeToGive.getElements().forEach { element ->
                    change.remove(element, changeToGive.getCount(element))
                }
                logger.logInfo("Successfully returning change, $changeToGive")
                changeToGive
            } catch (e: Exception) {
                // If making change fails, remove the paid amount from the register's change
                amountPaid.getElements().forEach { element ->
                    change.remove(element, amountPaid.getCount(element))
                }
                logger.logError("Error in returning change, ${e.message}")
                throw TransactionException(
                    "Unable to provide the requested change. ${e.message}",
                    e
                )
            }
        }
    }

    @TestOnly
    fun remainingChangeInRegister(): Change {
        return change
    }
}