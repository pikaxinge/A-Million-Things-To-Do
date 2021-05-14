package xyz.tcreopargh.amttd.data.login

import android.util.Log
import com.google.gson.reflect.TypeToken
import okhttp3.Request
import xyz.tcreopargh.amttd.AMTTD
import xyz.tcreopargh.amttd.data.bean.request.LoginRequest
import xyz.tcreopargh.amttd.data.bean.request.RegisterRequest
import xyz.tcreopargh.amttd.data.bean.response.LoginResponse
import xyz.tcreopargh.amttd.data.exception.LoginFailedException
import xyz.tcreopargh.amttd.user.LocalUser
import xyz.tcreopargh.amttd.util.gson
import xyz.tcreopargh.amttd.util.rootUrl
import xyz.tcreopargh.amttd.util.toJsonRequest
import xyz.tcreopargh.amttd.util.withPath
import java.io.IOException
import java.util.*


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(email: String, password: String): LoginResult<LocalUser> {
        // TODO: acquire UUID and authToken from server
        val loginRequest = Request.Builder()
            .post(
                LoginRequest(email = email, password = password).toJsonRequest()
            ).url(rootUrl.withPath("/login"))
            .build()
        return sendRequest(loginRequest)
    }

    fun register(email: String, password: String, username: String): LoginResult<LocalUser> {
        val registerRequest = Request.Builder()
            .post(
                RegisterRequest(
                    email = email,
                    password = password,
                    username = username
                ).toJsonRequest()
            ).url(rootUrl.withPath("/register"))
            .build()
        return sendRequest(registerRequest)

    }

    fun loginWithAuthToken(uuid: UUID, authToken: String): LoginResult<LocalUser> {
        val loginRequest = Request.Builder()
            .post(
                LoginRequest(uuid = uuid, token = authToken).toJsonRequest()
            ).url(rootUrl.withPath("/login"))
            .build()
        return sendRequest(loginRequest)
    }

    private fun sendRequest(request: Request): LoginResult<LocalUser> {
        try {
            val response = AMTTD.okHttpClient.newCall(request).execute()
            val body = try {
                gson.fromJson<LoginResponse>(
                    response.body?.string(),
                    object : TypeToken<LoginResponse>() {}.type
                )
            } catch (e: RuntimeException) {
                throw IOException(e)
            }
            if (body.success == true) {
                val username = body.username
                val email = body.email
                val uuid = body.uuid
                val authToken = body.token
                if (username != null && uuid != null && email != null) {
                    val user = LocalUser(
                        username = username,
                        email = email,
                        uuid = uuid,
                        authToken = authToken
                    )
                    return LoginResult.Success(user)
                } else {
                    throw IllegalArgumentException("Invalid JSON arguments!")
                }
            } else {
                throw LoginFailedException(body.reason ?: "null")
            }
        } catch (e: IOException) {
            Log.e(AMTTD.logTag, "Login Error: ", e)
            return LoginResult.Error(e)
        } catch (e: IllegalArgumentException) {
            Log.e(AMTTD.logTag, "Login Error: ", e)
            return LoginResult.Error(e)
        }
    }

}