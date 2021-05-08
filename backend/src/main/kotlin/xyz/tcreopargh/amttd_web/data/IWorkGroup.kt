package xyz.tcreopargh.amttd_web.data

import java.io.Serializable
import java.util.*

/**
 * @author TCreopargh
 */
interface IWorkGroup : Serializable {
    val groupId: UUID
    val name: String
    val timeCreated: Calendar

    val usersInGroup: List<IUser> get() = listOf()
}
