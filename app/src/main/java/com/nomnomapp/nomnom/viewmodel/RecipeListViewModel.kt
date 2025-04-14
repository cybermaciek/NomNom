package com.nomnomapp.nomnom.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import com.nomnomapp.nomnom.data.repository.FavoriteRepository
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import com.nomnomapp.nomnom.data.repository.RecipeRepository
import com.nomnomapp.nomnom.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeListViewModel(
    private val localRepository: LocalRecipeRepository,
    private val apiRepository: RecipeRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _favoriteIds = MutableStateFlow<List<String>>(emptyList())
    val favoriteIds: StateFlow<List<String>> = _favoriteIds

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    private val _areas = MutableStateFlow<List<String>>(emptyList())
    val areas: StateFlow<List<String>> = _areas

    private val _usingCache = MutableStateFlow(false)
    val usingCache: StateFlow<Boolean> = _usingCache

    init {
        loadFavorites()
    }


    private val _favoriteRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val favoriteRecipes: StateFlow<List<Recipe>> = _favoriteRecipes

    fun loadFavorites() {
        viewModelScope.launch {
            favoriteRepository.getAllAsRecipes().collect { recipes ->
                _favoriteRecipes.value = recipes
                _favoriteIds.value = recipes.map { it.id }
            }
        }
    }




    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            if (favoriteRepository.isFavorite(id)) {
                favoriteRepository.remove(id)
            } else {
                val recipe = _recipes.value.find { it.id == id }
                if (recipe != null) {
                    favoriteRepository.add(recipe)
                } else {
                    Log.w("RECIPE_VM", "Nie znaleziono przepisu z id: $id")
                }
            }
        }
    }


    fun loadAllRecipes() {
        viewModelScope.launch {
            val apiRecipes = try {
                val fromApi = apiRepository.searchRecipes("")
                apiRepository.cacheRecipes(fromApi)
                _usingCache.value = false
                fromApi
            } catch (e: Exception) {
                Log.w("RECIPE_VM", "Brak internetu – uzywam cache: ${e.message}")
                _usingCache.value = true
                apiRepository.getCachedRecipes()
            }

            val localRecipes = localRepository.getAllRecipesOnce().map { it.toRecipe() }
            _recipes.value = apiRecipes + localRecipes

            Log.d("RECIPE_VM", "LOCAL: ${localRecipes.map { it.id }}")
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            val apiRecipes = try {
                apiRepository.searchRecipes(query)
            } catch (e: Exception) {
                Log.w("RECIPE_VM", "search fallback to cache")
                apiRepository.getCachedRecipes().filter { it.title.contains(query, ignoreCase = true) }
            }

            val localRecipes = localRepository.searchRecipes(query).map { it.toRecipe() }
            _recipes.value = apiRecipes + localRecipes
        }
    }

    fun deleteUserRecipeById(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            localRepository.deleteUserRecipeById(id)
            loadAllRecipes()
            onSuccess()
        }
    }

    fun loadFilters() {
        viewModelScope.launch {
            try {
                _categories.value = listOf("All") + apiRepository.getCategories()
                _areas.value = listOf("All") + apiRepository.getAreas()
            } catch (e: Exception) {
                Log.w("RECIPE_VM", "Nie udalo sie zaladować filtrow: ${e.message}")
            }
        }
    }

    private fun UserRecipe.toRecipe(): Recipe {
        return Recipe(
            id = "user_${this.id}",
            title = this.title,
            imageUrl = this.imageUri.takeIf { !it.isNullOrEmpty() },
            instructions = this.instructions,
            ingredients = this.ingredients.split(",").map { it.trim() },
            category = this.category,
            area = this.area
        )
    }
}
