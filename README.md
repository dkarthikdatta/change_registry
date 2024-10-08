# Adyen Kotlin Assignment

This repository contains the coding assignment for candidates applying for a Kotlin Engineer role at Adyen.
The goal of the assignment is to implement a cash register.

Criteria:
- The `CashRegister` gets initialized with some `Change`.
- When performing a transaction, it either returns a `Change` object or fails with a `TransactionException`.
- The `CashRegister` keeps track of the `Change` that's in it.

Bonus points:
- The cash register returns the minimal amount of change (i.e. the minimal amount of coins / bills).

Feel free to make any changes to the code in the assignment.
As part of the zip file you received is the local Git repository, you can use that to show us your development and thought process in the commit history.

# Solution:

## CashRegister Class

The `CashRegister` class holds the logic for performing transactions.

### Constructor

Construct `CashRegister`  with initial change, Logger and ChangeStrategy. Logger and ChangeStrategy arguments are optional. They are set to default values. Default log level of logger is LogLevel.INFO and default ChangeStrategy is GreedyStrategy.
```kotlin
class CashRegister(
    private val change: Change,
    private val logger: Logger = Logger(),
    private val changeStrategy: ChangeStrategy = GreedyStrategy(logger)
)
```

### Method

The `performTransaction` method takes the price of the item and the amount paid by the user as arguments and returns a `Change` object or fails with a `TransactionException` if sufficient change is not available.

```kotlin
fun performTransaction(price: Long, amountPaid: Change): Change
```


### Example usage

```kotlin
val logger: Logger = Logger(LogLevel.INFO)
val changeStrategy =
    ChangeStrategyManager.getChangeStrategyMgr(logger).determineChangeStrategy()

val initialChange = Change()
    .add(Coin.FIVE_CENT, 10)
    .add(Coin.TEN_CENT, 4)
    .add(Coin.TWENTY_CENT, 2)
    .add(Coin.FIFTY_CENT, 3)
    .add(Coin.TWO_EURO, 3)
    .add(Bill.TWENTY_EURO, 1)
val cashRegister = CashRegister(change = initialChange, logger = logger, changeStrategy = changeStrategy)
val changeToBeReturned = cashRegister.performTransaction(price = 1000, amountPaid = Change().add(Bill.TWENTY_EURO, 1))
```

### Explanation
- Implemented [CashRegister](src/main/kotlin/CashRegister.kt) that returns a [Change](src/main/kotlin/money/Change.kt) object or fails with a [TransactionException](src/main/kotlin/money/exception/TransactionException.kt)

#### Determining Algorithm
- Used strategy pattern to determine the algorithm to calculate the change.
- Although for the given case - EURO and most of the other currencies, work fine with greedy algorithm to get the change in minimal amount of coins/bills,
- there might be certain currencies which fails in greedy algorithm. Hence we can utilize other algorithms based on our requirement.
- The other strategy implemented in this solution is Dynamic programming algorithm.
- Dynamic programming algorithm take higher time complexity and higher space complexity with respective to Greedy algorithm.
- In this solution, returned [GreedyStrategy](src/main/kotlin/money/strategy/GreedyStrategy.kt) in [ChangeStrategyManager](src/main/kotlin/money/strategy/ChangeStrategyManager.kt) `determineChangeStrategy()` method.
- Can use the required strategy based on various factors (like currency, payment type etc) and requirements

| Algorithm    |Greedy Algorithm|Dynamic Programming Algorithm|
| ------------- |:-------------:| -----:|
| Time complexity| O(n) | O(n* m *k) |
| Space complexit| O(1) | O(m)   |

n =  number of denominations, m =  maximum amount of change to be returned, k = maximum count of any single monetary element

#### Int vs Long
- In [Change](src/main/kotlin/money/Change.kt) class, the map holds MonetaryElement and Int as key value pairs, which represents the MonetaryElement and its count in the change
- The maximum count of a single MonetaryElement in a change is 2,147,483,647 (MAX_INT) which is practically within limits. (Just the count of a single MonetaryElement is Int. The total of change is still Long)
- But if there is a use-case where the count of a single MonetaryElement needs to be greater than 2,147,483,647 we can use Long in place of Int in `Change` class.
- However, if we are using Long, we have to tradeoff the Memory Usage and Performance.
