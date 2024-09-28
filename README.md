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

Solution:

- Implemented `CashRegister` that returns a `Change` object or fails with a `TransactionException`
- 
- Used strategy pattern to determine the algorithm to calculate the change.
- Although for the given case - EURO and most of the other currencies, work fine with greedy algorithm to get the change in minimal amount of coins/bills,
- there might be certain currencies which fails in greedy algorithm. Hence we can utilize other algorithms based on our requirement.
- The other strategy implemented in this solution is Dynamic programming algorithm.
- Dynamic programming algorithm take higher time complexity and higher space complexity with respective to Greedy algorithm.
- In this solution, returned `GreedyStrategy()` in `ChangeStrategyManager.determineChangeStrategy()`.
- Can use the required strategy based on various factors (like currency, payment type etc) and requirements

- In `Change` class, the map holds MonetaryElement and Int as key value pairs, which represents the MonetaryElement and its count in the change
- The maximum count of a single MonetaryElement in a change is 2,147,483,647 (MAX_INT) which is practically within limits.
- But if there is a use-case where the count of a single MonetaryElement needs to be greater than 2,147,483,647 we can use Long in place of Int in `Change` class.
- However, if we are using Long, we have to tradeoff the Memory Usage and Performance.

[GreedyStrategy](src/main/kotlin/money/strategy/GreedyStrategy.kt)