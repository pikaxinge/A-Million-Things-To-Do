package xyz.tcreopargh.amttd_web.controller.view

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import xyz.tcreopargh.amttd_web.annotation.LoginRequired
import xyz.tcreopargh.amttd_web.common.bean.request.WorkGroupViewRequest
import xyz.tcreopargh.amttd_web.common.bean.response.WorkGroupViewResponse
import xyz.tcreopargh.amttd_web.common.data.WorkGroupImpl
import xyz.tcreopargh.amttd_web.common.exception.AmttdException
import xyz.tcreopargh.amttd_web.controller.ControllerBase
import xyz.tcreopargh.amttd_web.entity.EntityWorkGroup
import xyz.tcreopargh.amttd_web.util.logger
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

@RestController
@LoginRequired
class WorkGroupPresenter : ControllerBase() {

    @PostMapping(
        "/workgroups",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun resolveWorkGroups(request: HttpServletRequest, @RequestBody body: WorkGroupViewRequest): WorkGroupViewResponse {

        return try {
            verifyUser(request, body.uuid)
            var workGroups: Set<EntityWorkGroup> = setOf()

            body.uuid?.let {
                val user = userService.findByIdOrNull(it)
                user?.joinedWorkGroups?.run Groups@{
                    workGroups = this@Groups
                }
            }
            val list = workGroups.stream().map {
                WorkGroupImpl(it)
            }.collect(Collectors.toList())
            list.sortByDescending { it.timeCreated }
            WorkGroupViewResponse(success = true, workGroups = list)
        } catch (e: Exception) {
            logger.error("Exception in WorkGroupPresenter: ", e)
            WorkGroupViewResponse(success = false, error = AmttdException.ErrorCode.getFromException(e).value)
        }
    }
}