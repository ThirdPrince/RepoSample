package com.sample.reposample.di

import androidx.room.Room
import com.sample.reposample.data.local.AppDatabase
import com.sample.reposample.data.remote.ArticleApiService
import com.sample.reposample.data.repository.ArticleRepositoryImpl
import com.sample.reposample.domain.repository.ArticleRepository
import com.sample.reposample.domain.usecase.GetArticlesUseCase
import com.sample.reposample.ui.ArticleViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    // 1. 提供 Retrofit 实例
    single {
        Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 2. 提供 ApiService 实例
    single<ArticleApiService> {
        get<Retrofit>().create(ArticleApiService::class.java)
    }

    // 3. 提供 Room 数据库实例
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "article_db"
        ).build()
    }

    // 4. 提供 DAO
    single { get<AppDatabase>().articleDao() }

    // 5. 提供 Repository 实例
    single<ArticleRepository> {
        ArticleRepositoryImpl(get(), get())
    }

    // 6. 提供 UseCase 实例
    factory { GetArticlesUseCase(get()) }

    // 7. 提供 ViewModel 实例 (现在注入 UseCase 而非 Repository)
    viewModel { ArticleViewModel(get()) }
}