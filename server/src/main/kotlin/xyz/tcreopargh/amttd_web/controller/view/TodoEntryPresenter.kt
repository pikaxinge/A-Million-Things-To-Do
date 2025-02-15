package xyz.tcreopargh.amttd_web.controller.view

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import xyz.tcreopargh.amttd_web.annotation.LoginRequired
import xyz.tcreopargh.amttd_web.common.bean.request.TodoEntryViewRequest
import xyz.tcreopargh.amttd_web.common.bean.response.TodoEntryViewResponse
import xyz.tcreopargh.amttd_web.common.data.TodoEntryImpl
import xyz.tcreopargh.amttd_web.common.exception.AmttdException
import xyz.tcreopargh.amttd_web.controller.ControllerBase
import xyz.tcreopargh.amttd_web.entity.EntityTodoEntry
import xyz.tcreopargh.amttd_web.util.logger
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

/**
 * @author TCreopargh
 *
 * present a list of to-do entries associated with the workgroup.
 */
@RestController
@LoginRequired
class TodoEntryPresenter : ControllerBase() {
    @PostMapping(
        "/todo",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun resolveByWorkGroup(
        request: HttpServletRequest,
        @RequestBody body: TodoEntryViewRequest
    ): TodoEntryViewResponse {
        return try {
            verifyWorkgroup(request, body.groupId)
            var entries: List<EntityTodoEntry> = listOf()
            body.groupId?.let {
                entries = workGroupService.findByIdOrNull(it)?.entries ?: listOf()
            }
            val list = entries.stream().filter { it != null }.map {
                TodoEntryImpl(it)
            }.collect(Collectors.toList()) ?: throw AmttdException(AmttdException.ErrorCode.REQUESTED_ENTITY_NOT_FOUND)
            list.sortByDescending { it.timeCreated }
            TodoEntryViewResponse(success = true, entries = list)
        } catch (e: Exception) {
            logger.error("Exception in TodoEntryPresenter: ", e)
            TodoEntryViewResponse(success = false, error = AmttdException.ErrorCode.getFromException(e).value)
        }
    }
}