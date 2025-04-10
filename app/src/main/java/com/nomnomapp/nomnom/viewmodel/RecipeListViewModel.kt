package com.nomnomapp.nomnom.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import com.nomnomapp.nomnom.data.repository.RecipeRepository
import com.nomnomapp.nomnom.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeListViewModel(
    private val localRepository: LocalRecipeRepository,
    private val apiRepository: RecipeRepository = RecipeRepository()
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    private val _areas = MutableStateFlow<List<String>>(emptyList())
    val areas: StateFlow<List<String>> = _areas

    fun loadAllRecipes() {
        viewModelScope.launch {
            val apiRecipes = apiRepository.searchRecipes("")
            val localRecipes = localRepository.getAllRecipesOnce().map { it.toRecipe() }
            _recipes.value = apiRecipes + localRecipes

            Log.d("RECIPE_VM", "LOCAL: ${localRecipes.map { it.id }}")
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            val api = apiRepository.searchRecipes(query)
            val local = localRepository.searchRecipes(query).map { it.toRecipe() }
            _recipes.value = api + local
        }
    }

    fun loadFilters() {
        viewModelScope.launch {
            _categories.value = listOf("All") + apiRepository.getCategories()
            _areas.value = listOf("All") + apiRepository.getAreas()
        }
    }

    private fun UserRecipe.toRecipe(): Recipe {
        return Recipe(
            id = "user_${this.id}",
            title = this.title,
            imageUrl = this.imageUri ?: "",
            instructions = this.instructions,
            ingredients = this.ingredients.split(",").map { it.trim() },
            category = this.category,
            area = this.area
        )
    }

}
