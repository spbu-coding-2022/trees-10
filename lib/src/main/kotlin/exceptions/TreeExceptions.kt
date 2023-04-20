package exceptions

/**
 * Общий класс для всех исключений, связанных с деревьями
 * */
open class TreeException(message: String) : Exception(message)

class NodeNotFound() : TreeException("Подходящий для выполнения операции узел дерева не найден")

class NodeAlreadyExists() : TreeException("Узел дерева с таким ключом уже существует")
