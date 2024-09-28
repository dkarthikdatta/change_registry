import money.euro.Bill
import money.euro.Coin
import money.strategy.ChangeStrategyManager
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


/**
 * coins - 1,2,5,10,20,50,100,200
 * bills - 5,10,20,50,100,200,500
 */

class CashRegisterTest {

    private lateinit var initialChange: Change
    private lateinit var cashRegister: CashRegister

    @BeforeTest
    fun setUpInitialChange() {
        initialChange = Change()
            .add(Coin.FIVE_CENT, 10) // 50
            .add(Coin.TEN_CENT, 4)  // 40
            .add(Coin.TWENTY_CENT, 2) // 40
            .add(Coin.FIFTY_CENT, 3) //150
            .add(Coin.TWO_EURO, 3)  //800
            .add(Bill.TWENTY_EURO, 1)  //1000
//        // total - 2880


        val changeStrategy =
            ChangeStrategyManager.getChangeStrategyMgr().determineChangeStrategy()
        cashRegister = CashRegister(initialChange, changeStrategy)
    }

    @Test
    fun testTransactionEquals() {
        println("initial total  = ${cashRegister.remainingChangeInRegister().total}")
        val paidAmount: Change = Change().add(Coin.TWO_EURO, 1).add(Coin.FIFTY_CENT, 1)
        val change = cashRegister.performTransaction(215, paidAmount)
        val expectedChangeToBeReturnedWrtRegister =
            Change().add(Coin.FIVE_CENT, 1).add(Coin.TEN_CENT, 1).add(Coin.TWENTY_CENT, 1)
        assertEquals(expectedChangeToBeReturnedWrtRegister, change)
    }

    @Test
    fun testTransactionNotEquals() {
        val paidAmount: Change = Change().add(Coin.TWO_EURO, 1).add(Coin.FIFTY_CENT, 1)
        val change = cashRegister.performTransaction(215, paidAmount)
        val expectedChangeToBeReturnedWrtRegister =
            Change().add(Coin.FIVE_CENT, 1).add(Coin.TEN_CENT, 3)
        assertNotEquals(expectedChangeToBeReturnedWrtRegister, change)
    }
}
