package databases

import java.awt.Point

/**
 * Общий интерфейс для всех баз данных
 */
interface IBase<TreeType, K> {

    /**
     * Сохраняет переданное дерево в базу данных.
     */
    fun saveTree(tree: TreeType)

    /**
     * Выгружает дерево из базы данных.
     */
    fun loadTree(): TreeType

    /**
     * Выполняет поиск координаты в сохранённом в базе данных дереве.
     */
    fun getPoint(key: K): Point

    /**
     * Изменяет координату в сохранённой ноде с указанной координатой.
     */
    fun setPoint(key: K, p: Point)
}