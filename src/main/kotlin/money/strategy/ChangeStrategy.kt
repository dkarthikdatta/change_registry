package money.strategy

import Change

interface ChangeStrategy {
    fun makeChange(currentChangeInRegistry: Change, amountToBeReturned: Long): Change
}