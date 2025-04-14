package com.nomnomapp.nomnom.data.repository

import com.nomnomapp.nomnom.data.local.entity.CachedRecipeEntity
import com.nomnomapp.nomnom.data.remote.*
import com.nomnomapp.nomnom.data.remote.dto.MealDto
import com.nomnomapp.nomnom.model.Recipe
import com.nomnomapp.nomnom.data.local.dao.CachedRecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repozytorium do obsługi przepisów z API i cache (Room)
class RecipeRepository(private val dao: CachedRecipeDao) {

    private val api = ApiClient.mealApi  // Retrofit client

    // Pobiera przepisy z API na podstawie zapytani po ciagu znakow
    suspend fun searchRecipes(query: String): List<Recipe> = withContext(Dispatchers.IO) {
        val response = api.searchMealsByName(query)
        response.meals?.map(::mapMealDtoToRecipe) ?: emptyList()
    }

    // Pobiera konkretny przepis z API po ID
    suspend fun getMealById(mealId: String): Recipe? = withContext(Dispatchers.IO) {
        val response = api.lookupMealById(mealId)
        response.meals?.firstOrNull()?.let(::mapMealDtoToRecipe)
    }

    // Pobiera listę kategorii (np. Beef, Chicken)
    suspend fun getCategories(): List<String> = withContext(Dispatchers.IO) {
        api.getCategories().categories.map { it.name }
    }

    // Pobiera listę kuchni (np. Italian, Mexican)
    suspend fun getAreas(): List<String> = withContext(Dispatchers.IO) {
        api.getAreas().areas.map { it.name }
    }

    // Mapowanie danych z API (DTO → Recipe)
    private fun mapMealDtoToRecipe(mealDto: MealDto): Recipe {
        return Recipe(
            id = mealDto.id,
            title = mealDto.title,
            imageUrl = mealDto.imageUrl,
            category = mealDto.category,
            area = mealDto.area,
            instructions = mealDto.instructions,
            ingredients = listOfNotNull(  // tylko niepuste składniki
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

    // Zapisuje do cache 10 ostatnich przepisów
    suspend fun cacheRecipes(recipes: List<Recipe>) {
        dao.clearAll()  // czyści cache
        recipes.take(10).forEach {
            dao.insert(
                CachedRecipeEntity(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.imageUrl ?: "",
                    instructions = it.instructions,
                    ingredients = it.ingredients.joinToString(","), // lista → tekst
                    category = it.category,
                    area = it.area
                )
            )
        }
    }

    // Pobiera przepisy z lokalnego cache (z Rooma)
    suspend fun getCachedRecipes(): List<Recipe> {
        return dao.getLastRecipes().map {
            Recipe(
                id = it.id,
                title = it.title,
                imageUrl = it.imageUrl,
                instructions = it.instructions,
                ingredients = it.ingredients.split(","), // tekst → lista
                category = it.category,
                area = it.area
            )
        }
    }
}
