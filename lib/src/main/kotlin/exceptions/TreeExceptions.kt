package exceptions

/**
 * Общий класс для всех исключений, связанных с деревьями
 * */
open class TreeException(message: String) : Exception(message)

class NodeNotFoundException() : TreeException("Подходящий для выполнения операции узел дерева не найден")

class NodeAlreadyExistsException() : TreeException("Узел дерева с таким ключом уже существует")

class NullNodeException() : TreeException("Узел дерева принял значение null")