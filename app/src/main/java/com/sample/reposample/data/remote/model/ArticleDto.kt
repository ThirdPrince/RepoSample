package com.sample.reposample.data.remote.model

data class BaseResponse<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)

data class ArticleListDto(
    val datas: List<ArticleDto>
)

data class ArticleDto(
    val id: Int,
    val title: String,
    val author: String
)