package exceptions

/**
 * Общий класс для всех исключений, связанных с деревьями
 * */
open class TreeException(message: String, cause: Throwable? = null) : Exception(message, cause)