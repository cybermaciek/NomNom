package com.nomnomapp.nomnom.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nomnomapp.nomnom.data.local.entity.CachedRecipeEntity

@Dao
interface CachedRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: CachedRecipeEntity)

    @Query("SELECT * FROM cached_recipes ORDER BY rowid DESC LIMIT 10")
    suspend fun getLastRecipes(): List<CachedRecipeEntity>

    @Query("DELETE FROM cached_recipes")
    suspend fun clearAll()
}
