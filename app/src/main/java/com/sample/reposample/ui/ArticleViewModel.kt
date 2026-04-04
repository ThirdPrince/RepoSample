package com.sample.reposample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.reposample.data.remote.model.ArticleDto
import com.sample.reposample.domain.usecase.GetArticlesUseCase
import com.sample.reposample.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleViewModel(private val getArticlesUseCase: GetArticlesUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<ArticleDto>>>(Resource.Loading())
    val uiState: StateFlow<Resource<List<ArticleDto>>> = _uiState.asStateFlow()

    init {
        fetchArticles(0) // 默认加载第一页
    }

    fun fetchArticles(page: Int) {
        viewModelScope.launch {
            // 调用 UseCase 而不是直接调用 Repository
            getArticlesUseCase(page).collect { result ->
                _uiState.value = result
            }
        }
    }
}