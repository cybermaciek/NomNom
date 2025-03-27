package com.nomnomapp.nomnom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddRecipeViewModel(
    private val repository: LocalRecipeRepository
) : ViewModel() {

    private val _myRecipes = MutableStateFlow<List<UserRecipe>>(emptyList())
    val myRecipes: StateFlow<List<UserRecipe>> = _myRecipes.asStateFlow()

    init {
        loadMyRecipes()
    }

    fun addRecipe(recipe: UserRecipe, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.addRecipe(recipe)
            loadMyRecipes()
            onSuccess()
        }
    }

    fun deleteRecipe(recipe: UserRecipe) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe)
            loadMyRecipes()
        }
    }

    private fun loadMyRecipes() {
        viewModelScope.launch {
            repository.getAllRecipes().collect { recipes ->
                _myRecipes.value = recipes
            }
        }
    }
}
