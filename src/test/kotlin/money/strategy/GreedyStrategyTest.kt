package money.strategy

import money.euro.Bill
import Change
import money.logger.LogLevel
import money.logger.Logger
import money.euro.Coin
import money.exception.TransactionException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GreedyStrategyTest {
    private val logger: Logger = Logger(LogLevel.DEBUG)
    private lateinit var currentChangeInRegistry: Change
    private val greedyStrategy = GreedyStrategy(logger)

    @BeforeEach
    fun setUp() {
        currentChangeInRegistry = Change()
            .add(Bill.FIVE_HUNDRED_EURO, 1)
            .add(Bill.TWO_HUNDRED_EURO, 2)
            .add(Bill.ONE_HUNDRED_EURO, 0)
            .add(Bill.FIFTY_EURO, 4)
            .add(Bill.TWENTY_EURO, 10)
            .add(Bill.TEN_EURO, 7)
            .add(Bill.FIVE_EURO, 8)
            .add(Coin.TWO_EURO, 12)
            .add(Coin.FIFTY_CENT, 3)
            .add(Coin.TWENTY_CENT, 20)
            .add(Coin.FIVE_CENT, 9)
    }

    @Test
    fun testMakeChangeEquals() {
        println("currentChangeInRegistry = ${currentChangeInRegistry.total}")
        val changeReturned = greedyStrategy.makeChange(currentChangeInRegistry, 30000)
        val changeFor30kWrtRegistry = Change().add(Bill.TWO_HUNDRED_EURO, 1).add(Bill.FIFTY_EURO, 2)
        println("changeReturned = $changeReturned")
        assertEquals(changeReturned, changeFor30kWrtRegistry)
    }

    @Test
    fun testMakeChangeNotEquals() {
        println("currentChangeInRegistry = ${currentChangeInRegistry.total}")
        val changeReturned = greedyStrategy.makeChange(currentChangeInRegistry, 30000)
        val inCorrectChangeFor30kWrtRegistry =
            Change().add(Bill.TWO_HUNDRED_EURO, 1).add(Bill.ONE_HUNDRED_EURO, 1)
        println("changeReturned = $changeReturned")
        assertNotEquals(changeReturned, inCorrectChangeFor30kWrtRegistry)
    }

    @Test
    fun testTransactionException() {
        val exception = org.junit.jupiter.api.assertThrows<TransactionException> {
            greedyStrategy.makeChange(currentChangeInRegistry, 12)
        }
        assertEquals("Unable to provide exact change.", exception.message)
    }

}