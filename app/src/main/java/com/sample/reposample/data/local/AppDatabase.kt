package com.sample.reposample.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.reposample.data.local.dao.ArticleDao
import com.sample.reposample.data.local.entity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}