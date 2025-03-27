package com.nomnomapp.nomnom.data.local.dao

import androidx.room.*
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: UserRecipe)

    @Delete
    suspend fun delete(recipe: UserRecipe)

    @Query("SELECT * FROM user_recipes ORDER BY id DESC")
    fun getAll(): Flow<List<UserRecipe>>
}
