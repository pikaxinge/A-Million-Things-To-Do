package xyz.tcreopargh.amttd.common.bean.response

data class SimpleResponse(
    override var success: Boolean? = false,
    override var error: Int? = null
) : IResponseBody