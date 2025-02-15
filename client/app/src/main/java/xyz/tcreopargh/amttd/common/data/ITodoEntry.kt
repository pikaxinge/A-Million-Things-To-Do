package xyz.tcreopargh.amttd.common.data

import xyz.tcreopargh.amttd.common.data.action.IAction
import java.io.Serializable
import java.util.*
import java.util.stream.Collectors

/**
 * @author TCreopargh
 */
interface ITodoEntry : Serializable {
    val entryId: UUID
    val creator: IUser?
    val title: String
    val description: String
    val status: TodoStatus
    val timeCreated: Calendar
    val deadline: Calendar?
    val tasks: List<ITask>
    val actionHistory: List<IAction>
    val completedTasks: List<ITask>
        get() = tasks.stream().filter { it.completed }.collect(Collectors.toList())
}