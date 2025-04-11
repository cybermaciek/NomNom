package com.nomnomapp.nomnom.viewmodel

import android.R.id.input
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomnomapp.nomnom.data.local.dao.ShoppingItemDao
import com.nomnomapp.nomnom.data.local.entity.ShoppingItem
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

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
