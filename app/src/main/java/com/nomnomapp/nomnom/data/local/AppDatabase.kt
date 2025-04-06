package com.nomnomapp.nomnom.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nomnomapp.nomnom.data.local.dao.FavoriteDao
import com.nomnomapp.nomnom.data.local.dao.ShoppingItemDao
import com.nomnomapp.nomnom.data.local.dao.UserRecipeDao
import com.nomnomapp.nomnom.data.local.entity.ShoppingItem
import com.nomnomapp.nomnom.data.local.entity.UserRecipe

@Database(
    entities = [FavoriteEntity::class, UserRecipe::class, ShoppingItem::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userRecipeDao(): UserRecipeDao
    abstract fun shoppingItemDao(): ShoppingItemDao
}
