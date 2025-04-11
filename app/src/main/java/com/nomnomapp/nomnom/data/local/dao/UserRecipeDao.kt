package com.nomnomapp.nomnom.data.local.dao

import android.util.Log
import androidx.room.*
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRecipeDao {
    @Query("SELECT * FROM user_recipes ORDER BY id DESC")
    fun getAllRecipes(): Flow<List<UserRecipe>>

    @Query("SELECT * FROM user_recipes ORDER BY id DESC")
    suspend fun getAllRecipesOnce(): List<UserRecipe>

    @Query("SELECT * FROM user_recipes WHERE id = :id")
    suspend fun getRecipeById(id: Int): UserRecipe?

    @Query("SELECT * FROM user_recipes WHERE title LIKE '%' || :query || '%'")
    suspend fun searchRecipes(query: String): List<UserRecipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: UserRecipe): Long

    @Delete
    suspend fun delete(recipe: UserRecipe)

    @Update
    suspend fun update(recipe: UserRecipe)

}

