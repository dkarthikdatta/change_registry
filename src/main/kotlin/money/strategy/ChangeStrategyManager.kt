package money.strategy

import money.logger.Logger

class ChangeStrategyManager(private val logger: Logger) {

    companion object {
        @Volatile
        private var strategyMgrInstance: ChangeStrategyManager? = null
        private val lock = Any()

        fun getChangeStrategyMgr(logger: Logger): ChangeStrategyManager {
            return strategyMgrInstance ?: synchronized(lock) {
                strategyMgrInstance ?: ChangeStrategyManager(logger).also { strategyMgrInstance = it }
            }
        }
    }

    fun determineChangeStrategy(): ChangeStrategy {
        //determine strategy based on requirements like currency type, and other factors
        return GreedyStrategy(logger)
    }
}