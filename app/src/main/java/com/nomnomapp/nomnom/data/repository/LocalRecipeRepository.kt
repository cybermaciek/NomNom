package com.nomnomapp.nomnom.data.repository

import com.nomnomapp.nomnom.data.local.dao.UserRecipeDao
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import com.nomnomapp.nomnom.model.Recipe
import kotlinx.coroutines.flow.Flow

class LocalRecipeRepository(private val dao: UserRecipeDao) {

    fun getAllRecipes(): Flow<List<UserRecipe>> = dao.getAllRecipes()

    suspend fun getAllRecipesOnce(): List<UserRecipe> = dao.getAllRecipesOnce()

    suspend fun getRecipeById(id: Int): UserRecipe? = dao.getRecipeById(id)

    suspend fun searchRecipes(query: String): List<UserRecipe> = dao.searchRecipes(query)

    suspend fun addRecipe(recipe: UserRecipe) = dao.insert(recipe)

    suspend fun deleteUserRecipeById(id: Int) {
        val recipe = dao.getRecipeById(id)
        if (recipe != null) {
            dao.delete(recipe)
        }
    }

    suspend fun updateRecipe(recipe: UserRecipe) = dao.update(recipe)

    suspend fun saveRecipe(recipe: Recipe) {
        val userRecipe = UserRecipe(
            id = 0,
            title = recipe.title,
            instructions = recipe.instructions,
            ingredients = recipe.ingredients.joinToString(","),
            imageUri = recipe.imageUrl,
            category = recipe.category,
            area = recipe.area
        )
        dao.insert(userRecipe)
    }

}
