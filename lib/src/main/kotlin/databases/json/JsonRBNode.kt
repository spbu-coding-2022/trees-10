package databases.json

import kotlinx.serialization.Serializable
import nodes.Color

@Serializable
data class JsonRBNode(
    val value: String,
    val key: Int,
    val color: Color,
    val left: JsonRBNode?,
    val right: JsonRBNode?
)