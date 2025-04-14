package com.nomnomapp.nomnom.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import kotlinx.coroutines.launch

class AddRecipeViewModel(
    private val recipeRepository: LocalRecipeRepository
) : ViewModel() {

    fun addRecipe(recipe: UserRecipe, onSuccess: () -> Unit) {
        viewModelScope.launch {
            recipeRepository.addRecipe(recipe)
            Log.d("RECIPE_SAVE", "PRZEPIS ZAPISANY: $recipe")
            onSuccess()
        }
    }
    fun updateRecipe(recipe: UserRecipe, onSuccess: () -> Unit) {
        viewModelScope.launch {
            recipeRepository.updateRecipe(recipe)
            onSuccess()
        }
    }
}
