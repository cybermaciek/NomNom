package com.nomnomapp.nomnom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import com.nomnomapp.nomnom.data.repository.RecipeRepository
import com.nomnomapp.nomnom.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val apiRepository: RecipeRepository,
    private val localRepository: LocalRecipeRepository
) : ViewModel() {

    private val _mealState = MutableStateFlow<Recipe?>(null)
    val mealState: StateFlow<Recipe?> = _mealState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadMeal(mealId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (mealId.startsWith("user_")) {
                    val localId = mealId.removePrefix("user_").toInt()
                    val userRecipe = localRepository.getRecipeById(localId)
                    _mealState.value = userRecipe?.toRecipe()
                } else {
                    val apiRecipe = try {
                        apiRepository.getMealById(mealId)
                    } catch (e: Exception) {
                        //z cache
                        apiRepository.getCachedRecipes().find { it.id == mealId }
                    }

                    _mealState.value = apiRecipe

                    if (apiRecipe == null) {
                        _errorMessage.value = "Offline – no cached data available"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteUserRecipeById(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            localRepository.deleteUserRecipeById(id)
            onSuccess()
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
