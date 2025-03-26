package com.nomnomapp.nomnom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomnomapp.nomnom.data.repository.RecipeRepository
import com.nomnomapp.nomnom.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealDetailViewModel(
    private val repository: RecipeRepository = RecipeRepository()
) : ViewModel() {

    private val _mealState = MutableStateFlow<Recipe?>(null)
    val mealState: StateFlow<Recipe?> = _mealState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadMeal(mealId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val meal = repository.getMealById(mealId)
                _mealState.value = meal
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
