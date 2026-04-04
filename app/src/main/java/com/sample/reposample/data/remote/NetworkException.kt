package com.sample.reposample.data.remote

import java.io.IOException

/**
 * 业务异常：例如 errorCode != 0
 */
class ApiException(val errorCode: Int, override val message: String) : Exception(message)

/**
 * 网络异常：例如超时、无网络
 */
class NetworkException(message: String, cause: Throwable) : IOException(message, cause)