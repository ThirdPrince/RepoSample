package com.sample.reposample.data.repository

import com.sample.reposample.data.local.dao.ArticleDao
import com.sample.reposample.data.local.entity.ArticleEntity
import com.sample.reposample.data.remote.ArticleApiService
import com.sample.reposample.data.remote.model.ArticleDto
import com.sample.reposample.data.remote.safeApiCall
import com.sample.reposample.domain.repository.ArticleRepository
import com.sample.reposample.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * 仓库层实现：实现 Single Source of Truth (SSOT) 逻辑
 */
class ArticleRepositoryImpl(
    private val api: ArticleApiService,
    private val dao: ArticleDao
) : ArticleRepository {

    override fun getArticles(page: Int): Flow<Resource<List<ArticleDto>>> = flow {
        // 1. 发射 Loading 状态
        emit(Resource.Loading())

        // 2. 真实读取 Room 缓存
        val localEntities = dao.getArticlesByPage(page)
        val localCache = localEntities.map { it.toDto() }

        // 3. 如果本地有缓存，先发射缓存数据，实现“秒开”
        if (localCache.isNotEmpty()) {
            emit(Resource.Loading(localCache))
        }

        // 4. 使用 safeApiCall 请求网络数据
        val result = safeApiCall {
            api.getArticles(page)
        }

        // 5. 处理请求结果
        result.onSuccess { articleListDto ->
            val remoteArticles = articleListDto.datas
            
            // 6. 将网络数据转换为实体并同步到本地数据库
            val entities = remoteArticles.map { it.toEntity(page) }
            dao.insertArticles(entities)
            
            // 7. 发射最新的网络数据
            emit(Resource.Success(remoteArticles))
            
        }.onFailure { exception ->
            // 8. 发生异常时，发射 Error 状态，但依然附带旧缓存，防止界面白屏
            emit(Resource.Error(exception.localizedMessage ?: "Unknown error", localCache))
        }

    }.flowOn(Dispatchers.IO)

    /**
     * Mapper: DTO 转换为数据库实体
     */
    private fun ArticleDto.toEntity(page: Int): ArticleEntity {
        return ArticleEntity(
            id = this.id,
            title = this.title,
            author = this.author,
            page = page
        )
    }

    /**
     * Mapper: 数据库实体转换为 DTO (或 Domain Model)
     */
    private fun ArticleEntity.toDto(): ArticleDto {
        return ArticleDto(
            id = this.id,
            title = this.title,
            author = this.author
        )
    }
}