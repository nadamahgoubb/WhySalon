package com.example.whysalon.base

import okhttp3.Headers
import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {

    companion object {
        fun <T : Any, U : Any> unknownError(
            error: Throwable,
            code: Int? = null,
            headers: Headers? = null
        ): NetworkResponse<T, U> =
            UnknownError(error, code, headers)

        fun <T : Any, U : Any> success(
            body: T,
            headers: Headers? = null,
            code: Int
        ): NetworkResponse<T, U> =
            Success(body, headers, code)
    }

    /**
     * A request that resulted in a response with a 2xx status code that has a body.
     */
    data class Success<T : Any>(
        val body: T,
        val headers: Headers? = null,
        val code: Int
    ) : NetworkResponse<T, Nothing>()

    /**
     * A request that resulted in a response with a non-2xx status code.
     */
    data class ServerError<U : Any>(
        val body: U?,
        val code: Int,
        val headers: Headers? = null
    ) : NetworkResponse<Nothing, U>()

    /**
     * A request that didn't result in a response.
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    /**
     * A request that resulted in an error different from an IO or Server error.
     *
     * An example of such an error is JSON parsing exception thrown by a serialization library.
     */
    data class UnknownError(
        val error: Throwable,
        val code: Int? = null,
        val headers: Headers? = null
    ) : NetworkResponse<Nothing, Nothing>()


}