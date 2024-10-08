package money.strategy

import Change
import MonetaryElement
import money.logger.Logger
import money.euro.Bill
import money.euro.Coin
import money.exception.TransactionException

class GreedyStrategy(private val logger: Logger,) : ChangeStrategy {

    /**
     * Attempts to generate the minimal amount of change using Greedy algorithm.
     *
     * @param currentChangeInRegistry The current change available in registry.
     * @param amountToBeReturned The amount to be returned.
     * @return A Change object containing the minimal number of bills and coins, or throws TransactionException.
     * @throws TransactionException If exact change cannot be made.
     */

    @Throws(TransactionException::class)
    override fun makeChange(currentChangeInRegistry: Change, amountToBeReturned: Long): Change {
        logger.logVerbose("making change using greedy algorithm")
        var remainingAmount = amountToBeReturned
        val changeToGive = Change.none()

        // Iterate through all the bills and coins in descending order of value
        val availableElements = (Bill.values().toList() as List<MonetaryElement>) + (Coin.values()
            .toList() as List<MonetaryElement>)
            .sortedByDescending { it.minorValue }


        for (element in availableElements) {
            val countInRegister = currentChangeInRegistry.getCount(element)
            val maxNeeded: Int = (remainingAmount / element.minorValue).toInt()

            // Take the smaller of what is available in the register or what is needed
            val countToGive = maxNeeded.coerceAtMost(countInRegister)

            if (countToGive > 0) {
                changeToGive.add(element, countToGive)
                remainingAmount -= countToGive * element.minorValue
            }

            if (remainingAmount == 0L) break
        }

        if (remainingAmount != 0L) {
            logger.logError("Unable to provide exact change.")
            throw TransactionException("Unable to provide exact change.")
        }
        logger.logInfo("Successfully made change using greedy algorithm")
        return changeToGive
    }
}