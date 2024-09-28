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
    private val changeStrategy: ChangeStrategy = GreedyStrategy()
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

        val totalPaid = amountPaid.total
        if (totalPaid < price) {
            throw TransactionException("Insufficient amount paid.")
        }

        synchronized(change) {

            amountPaid.getElements().forEach {
                change.add(it, amountPaid.getCount(it))
            }

            val amountToBeReturned = totalPaid - price

            if (change.total < amountToBeReturned) {
                throw TransactionException("No change to return for the amountPaid")
            }

            return try {
                val changeToGive = changeStrategy.makeChange(change, amountToBeReturned)
                // If successful, subtract the change given from the register's current change
                changeToGive.getElements().forEach { element ->
                    change.remove(element, changeToGive.getCount(element))
                }
                changeToGive
            } catch (e: Exception) {
                // If making change fails, remove the paid amount from the register's change
                amountPaid.getElements().forEach { element ->
                    change.remove(element, amountPaid.getCount(element))
                }
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

/**
private fun makeChange(amountToBeReturned: Long): Change {
var remainingAmount = amountToBeReturned
val changeToGive = Change.none()
println("current change in register =  $change")

// Iterate through bills and coins in descending order of value
val availableElements = (money.euro.Bill.values().toList() as List<MonetaryElement>) + (money.euro.Coin.values()
.toList() as List<MonetaryElement>)
.sortedByDescending { it.minorValue }


for (element in availableElements) {
val countInRegister = change.getCount(element)
val maxNeeded: Int = (remainingAmount / element.minorValue).toInt()

println("checking element = ${element.minorValue}, max need = $maxNeeded")

println("countInRegister = $countInRegister of element ${element.minorValue}")
// Take the smaller of what is available in the register or what is needed
val countToGive = maxNeeded.coerceAtMost(countInRegister)
println("countToGive = $countToGive of element ${element.minorValue}")

if (countToGive > 0) {
changeToGive.add(element, countToGive)
remainingAmount -= countToGive * element.minorValue
}

println("remaining amount = $remainingAmount")

if (remainingAmount == 0L) break
}

if (remainingAmount != 0L) {
throw TransactionException("Unable to provide exact change.")
}

return changeToGive
}

 **/