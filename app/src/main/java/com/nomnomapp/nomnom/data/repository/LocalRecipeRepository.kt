package com.nomnomapp.nomnom.data.repository

import com.nomnomapp.nomnom.data.local.dao.UserRecipeDao
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import kotlinx.coroutines.flow.Flow

class LocalRecipeRepository(private val dao: UserRecipeDao) {
    suspend fun addRecipe(recipe: UserRecipe) = dao.insert(recipe)
    suspend fun deleteRecipe(recipe: UserRecipe) = dao.delete(recipe)
    fun getAllRecipes(): Flow<List<UserRecipe>> = dao.getAll()
}
