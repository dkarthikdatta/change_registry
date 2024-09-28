package money.strategy

import Change
import MonetaryElement
import money.euro.Bill
import money.euro.Coin
import money.exception.TransactionException

class DpStrategy : ChangeStrategy {

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
        val maxAmount =
            amountToBeReturned.toInt()  // Assuming the amount fits into an Int for simplicity
        val availableElements = (Bill.values().toList() as List<MonetaryElement>) + (Coin.values()
            .toList() as List<MonetaryElement>)
            .sortedByDescending { it.minorValue }
        println("current change in register =  $currentChangeInRegistry")

        // Initialize the dp array where dp[i] represents the minimum number of coins/bills to make i
        val dp = IntArray(maxAmount + 1) { Int.MAX_VALUE }
        val usedElement = Array<MonetaryElement?>(maxAmount + 1) { null }

        dp[0] = 0  // 0 coins are needed to make 0 amount

        // Dynamic programming to fill the dp array
        for (element in availableElements) {
            val value = element.minorValue
            for (i in value..maxAmount) {
                if (dp[i - value] != Int.MAX_VALUE && currentChangeInRegistry.getCount(element) > 0) {
                    if (dp[i] > dp[i - value] + 1) {
                        dp[i] = dp[i - value] + 1
                        usedElement[i] = element
                    }
                }
            }
        }

        if (dp[maxAmount] == Int.MAX_VALUE) {
            throw TransactionException("Unable to provide exact change.")
        }

        // Backtrack to determine which elements (coins/bills) were used
        val changeToGive = Change.none()
        var remainingAmount = maxAmount
        while (remainingAmount > 0) {
            val element = usedElement[remainingAmount]
                ?: throw TransactionException("Unable to provide exact change.")
            changeToGive.add(element, 1)
            remainingAmount -= element.minorValue
        }

        return changeToGive
    }

}