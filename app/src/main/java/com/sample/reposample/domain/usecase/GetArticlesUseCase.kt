package com.sample.reposample.domain.usecase

import com.sample.reposample.data.remote.model.ArticleDto
import com.sample.reposample.domain.repository.ArticleRepository
import com.sample.reposample.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * 获取文章列表的业务用例
 */
class GetArticlesUseCase(private val repository: ArticleRepository) {
    // 使用 invoke 操作符，让调用者可以像函数一样调用类实例
    operator fun invoke(page: Int): Flow<Resource<List<ArticleDto>>> {
        return repository.getArticles(page)
    }
}