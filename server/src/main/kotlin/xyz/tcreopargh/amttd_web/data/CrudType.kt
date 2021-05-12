package xyz.tcreopargh.amttd_web.data

import java.io.Serializable

@Suppress("unused")
enum class CrudType : Serializable {
    CREATE,
    READ,
    UPDATE,
    DELETE
}
