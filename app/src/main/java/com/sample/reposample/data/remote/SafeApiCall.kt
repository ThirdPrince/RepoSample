package com.sample.reposample.data.remote

import com.sample.reposample.data.remote.model.BaseResponse
import retrofit2.HttpException
import java.io.IOException

/**
 * 包装网络请求：处理网络异常和业务异常 (errorCode != 0)
 */
suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> BaseResponse<T>
): Result<T> {
    return try {
        val response = apiCall()

        if (response.errorCode == 0) {
            // 业务成功
            Result.success(response.data)
        } else {
            // 业务失败
            Result.failure(ApiException(response.errorCode, response.errorMsg))
        }

    } catch (e: IOException) {
        // 网络异常 (超时、无网络等)
        Result.failure(NetworkException("Network error: ${e.message}", e))

    } catch (e: HttpException) {
        // HTTP 状态码异常 (4xx, 5xx)
        Result.failure(NetworkException("HTTP error: ${e.code()}", e))

    } catch (e: Exception) {
        // 其他未知异常
        Result.failure(e)
    }
}