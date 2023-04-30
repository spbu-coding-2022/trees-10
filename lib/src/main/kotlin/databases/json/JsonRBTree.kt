package databases.json

import kotlinx.serialization.Serializable

@Serializable
data class JsonRBTree (
    val root: JsonRBNode?
)