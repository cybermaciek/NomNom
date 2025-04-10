package com.nomnomapp.nomnom.viewmodel

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

class AddRecipeViewModel(
    private val recipeRepository: LocalRecipeRepository,
    private val shoppingItemDao: ShoppingItemDao
) : ViewModel() {

    private val _myRecipes = MutableStateFlow<List<UserRecipe>>(emptyList())
    val myRecipes: StateFlow<List<UserRecipe>> = _myRecipes.asStateFlow()

    init {
        loadMyRecipes()
    }

    fun addRecipe(recipe: UserRecipe, onSuccess: () -> Unit) {
        viewModelScope.launch {
            recipeRepository.addRecipe(recipe)
            Log.d("RECIPE_SAVE", "PRZEPIS ZAPISANY: $recipe")
            onSuccess()

        }
    }

    fun deleteRecipe(recipe: UserRecipe) {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(recipe)
            loadMyRecipes()
        }
    }

    
    private fun loadMyRecipes() {
        viewModelScope.launch {
            recipeRepository.getAllRecipes().collect { recipes ->
                _myRecipes.value = recipes
            }
        }
    }
}
