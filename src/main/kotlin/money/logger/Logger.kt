package money.logger

import java.text.SimpleDateFormat
import java.util.Date

class Logger(logLevel: LogLevel = LogLevel.INFO) {

    private val tag = "Adyen"
    private var logLevel = LogLevel.ERROR.level

    init {
        when {
            logLevel.level > LogLevel.VERBOSE.level -> {
                this.logLevel = LogLevel.VERBOSE.level
            }

            logLevel.level < LogLevel.NONE.level -> {
                this.logLevel = LogLevel.NONE.level
            }

            else -> {
                this.logLevel = logLevel.level
            }
        }
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(Date())
    }

    fun logVerbose(message: String) {
        if (logLevel >= LogLevel.VERBOSE.level) {
            log("Verbose", message)
        }
    }

    fun logDebug(message: String) {
        if (logLevel >= LogLevel.DEBUG.level) {
            log("Debug", message)
        }
    }

    fun logInfo(message: String) {
        if (logLevel >= LogLevel.INFO.level) {
            log("Info", message)
        }
    }

    fun logWarn(message: String) {
        if (logLevel >= LogLevel.WARNING.level) {
            log("Warn", message)
        }
    }

    fun logError(message: String) {
        if (logLevel >= LogLevel.ERROR.level) {
            log("Error", message)
        }
    }

    private fun log(level: String, message: String) {
        val timeStamp = getCurrentTime()
        println("[$timeStamp] [$level] [$tag]: $message")
    }

    fun getLogLevel(): Int {
        return logLevel
    }

}

enum class LogLevel(val level: Int) {
    VERBOSE(5),
    DEBUG(4),
    INFO(3),
    WARNING(2),
    ERROR(1),
    NONE(0)
}

