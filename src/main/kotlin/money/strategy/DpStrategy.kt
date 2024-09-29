package money.strategy

import Change
import MonetaryElement
import money.logger.Logger
import money.euro.Bill
import money.euro.Coin
import money.exception.TransactionException

class DpStrategy(private val logger: Logger) : ChangeStrategy {

    /**
     * Attempts to generate the minimal amount of change using dynamic programming.
     *
     * @param currentChangeInRegistry The current change available in registry.
     * @param amountToBeReturned The amount to be returned.
     * @return A Change object containing the minimal number of bills and coins, or throws TransactionException.
     * @throws TransactionException If exact change cannot be made.
     */

    @Throws(TransactionException::class)
    override fun makeChange(currentChangeInRegistry: Change, amountToBeReturned: Long): Change {
        logger.logVerbose("making change using dp algorithm")
        amountToBeReturned.toInt()
        val availableElements = (Bill.values().toList() as List<MonetaryElement>) + (Coin.values()
            .toList() as List<MonetaryElement>)
            .sortedByDescending { it.minorValue }

        val dp = mutableMapOf<Long, Int>()
        val usedElement = mutableMapOf<Long, MonetaryElement?>()

        dp[0] = 0

        for (element in availableElements) {
            val value = element.minorValue
            val count = currentChangeInRegistry.getCount(element)

            for (j in 1..count) {
                val currentValue = j * value

                for (currAmt in amountToBeReturned downTo currentValue) {
                    val previousAmount = currAmt - currentValue

                    val previousCount = dp[previousAmount]

                    if (previousCount != null) {

                        val currentCount = dp[currAmt] ?: Int.MAX_VALUE

                        if (currentCount > previousCount + j) {
                            dp[currAmt] = previousCount + j
                            usedElement[currAmt] = element
                        }
                    }
                }
            }
        }

        if (dp[amountToBeReturned] == null) {
            logger.logError("Unable to provide exact change.")
            throw TransactionException("Unable to provide exact change.")
        }

        // Backtrack to determine which elements (coins/bills) were used
        val changeToGive = Change.none()
        var remainingAmount = amountToBeReturned

        while (remainingAmount > 0) {
            val element = usedElement[remainingAmount]
                ?: run {
                    logger.logError("Unable to provide exact change.")
                    throw TransactionException("Unable to provide exact change.")
                }
            changeToGive.add(element, 1)
            remainingAmount -= element.minorValue
        }
        logger.logInfo("Successfully made change using dp algorithm")
        return changeToGive
    }

}