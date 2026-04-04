package com.sample.reposample.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.reposample.data.local.entity.ArticleEntity

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles WHERE page = :page")
    suspend fun getArticlesByPage(page: Int): List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles WHERE page = :page")
    suspend fun deleteArticlesByPage(page: Int)
}