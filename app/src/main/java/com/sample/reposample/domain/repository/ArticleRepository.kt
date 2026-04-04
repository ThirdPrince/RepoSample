package com.sample.reposample.domain.repository

import com.sample.reposample.data.remote.model.ArticleDto
import com.sample.reposample.util.Resource
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    /**
     * 获取文章列表，优先读取本地缓存，然后从网络同步
     */
    fun getArticles(page: Int): Flow<Resource<List<ArticleDto>>>
}