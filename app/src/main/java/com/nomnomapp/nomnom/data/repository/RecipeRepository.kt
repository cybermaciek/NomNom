package com.nomnomapp.nomnom.data.repository

import com.nomnomapp.nomnom.data.remote.ApiClient
import com.nomnomapp.nomnom.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository {
    private val api = ApiClient.mealApi

    suspend fun searchRecipes(query: String): List<Recipe> = withContext(Dispatchers.IO) {
        val response = api.searchMealsByName(query)
        response.meals?.map { mealDto ->
            Recipe(
                id = mealDto.id,
                title = mealDto.title,
                imageUrl = mealDto.imageUrl,
                category = mealDto.category,
                area = mealDto.area,
                instructions = mealDto.instructions,
                ingredients = listOfNotNull(
                    mealDto.ingredient1,
                    mealDto.ingredient2,
                    mealDto.ingredient3
                    // Można dodać więcej
                ).filter { it.isNotBlank() }
            )
        } ?: emptyList()
    }
}