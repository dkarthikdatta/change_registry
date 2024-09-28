package money.exception

class TransactionException(message: String, cause: Throwable? = null) :
    Exception(message, cause)