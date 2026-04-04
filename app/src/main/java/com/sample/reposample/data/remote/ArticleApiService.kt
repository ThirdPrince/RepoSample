package com.sample.reposample.data.remote

import com.sample.reposample.data.remote.model.ArticleListDto
import com.sample.reposample.data.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticleApiService {
    @GET("article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): BaseResponse<ArticleListDto>
}