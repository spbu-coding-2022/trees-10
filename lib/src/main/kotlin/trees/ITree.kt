package trees

interface ITree<T: Comparable<T>, NodeType>  {
    fun search(key : T) : NodeType?
    fun remove(key : T)
    fun add(key : T, value : NodeType? = null)

}