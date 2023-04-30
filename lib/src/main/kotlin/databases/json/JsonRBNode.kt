package databases.json

import kotlinx.serialization.Serializable

@Serializable
data class JsonRBNode(
    val value: String,
    val key: Int,
    val left: JsonRBNode?,
    val right: JsonRBNode?
)