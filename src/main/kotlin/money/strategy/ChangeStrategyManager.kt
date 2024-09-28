package money.strategy

class ChangeStrategyManager {

    companion object {
        @Volatile
        private var strategyMgrInstance: ChangeStrategyManager? = null
        private val lock = Any()

        fun getChangeStrategyMgr(): ChangeStrategyManager {
            return strategyMgrInstance ?: synchronized(lock) {
                strategyMgrInstance ?: ChangeStrategyManager().also { strategyMgrInstance = it }
            }
        }
    }

    fun determineChangeStrategy(): ChangeStrategy {
        //determine strategy based on requirements like currency type, and other factors
        return GreedyStrategy()
    }
}