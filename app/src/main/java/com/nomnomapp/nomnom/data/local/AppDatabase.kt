package com.nomnomapp.nomnom.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nomnomapp.nomnom.data.local.dao.CachedRecipeDao
import com.nomnomapp.nomnom.data.local.dao.FavoriteDao
import com.nomnomapp.nomnom.data.local.dao.ShoppingItemDao
import com.nomnomapp.nomnom.data.local.dao.UserRecipeDao
import com.nomnomapp.nomnom.data.local.entity.CachedRecipeEntity
import com.nomnomapp.nomnom.data.local.entity.FavoriteEntity
import com.nomnomapp.nomnom.data.local.entity.ShoppingItem
import com.nomnomapp.nomnom.data.local.entity.UserRecipe

@Database(
    entities = [
        FavoriteEntity::class,
        UserRecipe::class,
        ShoppingItem::class,
        CachedRecipeEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userRecipeDao(): UserRecipeDao
    abstract fun shoppingItemDao(): ShoppingItemDao
    abstract fun cachedRecipeDao(): CachedRecipeDao
}
