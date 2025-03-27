package com.nomnomapp.nomnom.data.repository

import com.nomnomapp.nomnom.data.remote.*
import com.nomnomapp.nomnom.data.remote.dto.MealDto
import com.nomnomapp.nomnom.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository {
    private val api = ApiClient.mealApi

    suspend fun searchRecipes(query: String): List<Recipe> = withContext(Dispatchers.IO) {
        val response = api.searchMealsByName(query)
        response.meals?.map(::mapMealDtoToRecipe) ?: emptyList()
    }

    suspend fun getMealById(mealId: String): Recipe? = withContext(Dispatchers.IO) {
        val response = api.lookupMealById(mealId)
        response.meals?.firstOrNull()?.let(::mapMealDtoToRecipe)
    }

    suspend fun getCategories(): List<String> = withContext(Dispatchers.IO) {
        api.getCategories().categories.map { it.name }
    }

    suspend fun getAreas(): List<String> = withContext(Dispatchers.IO) {
        api.getAreas().areas.map { it.name }
    }

    private fun mapMealDtoToRecipe(mealDto: MealDto): Recipe {
        return Recipe(
            id = mealDto.id,
            title = mealDto.title,
            imageUrl = mealDto.imageUrl,
            category = mealDto.category,
            area = mealDto.area,
            instructions = mealDto.instructions,
            ingredients = listOfNotNull(
                mealDto.ingredient1,
                mealDto.ingredient2,
                mealDto.ingredient3,
                mealDto.ingredient4,
                mealDto.ingredient5,
                mealDto.ingredient6,
                mealDto.ingredient7,
                mealDto.ingredient8,
                mealDto.ingredient9,
                mealDto.ingredient10
            ).filter { it.isNotBlank() }
        )
    }
}
