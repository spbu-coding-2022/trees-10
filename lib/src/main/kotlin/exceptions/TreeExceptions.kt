package exceptions

/**
 * Общий класс для всех исключений, связанных с деревьями
 * */
open class TreeException(message: String) : Exception(message)

class NodeNotFound() : TreeException("Подходящий узел дерева не найден")

class NodeAlreadyExists() : TreeException("Такой узел дерева уже существует")
