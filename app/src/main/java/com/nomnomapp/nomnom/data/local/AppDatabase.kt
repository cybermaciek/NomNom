package com.nomnomapp.nomnom.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nomnomapp.nomnom.data.local.dao.FavoriteDao
import com.nomnomapp.nomnom.data.local.dao.UserRecipeDao

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userRecipeDao(): UserRecipeDao
}
