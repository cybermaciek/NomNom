package com.nomnomapp.nomnom.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomnomapp.nomnom.data.repository.RecipeRepository
import com.nomnomapp.nomnom.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RecipeListViewModel : ViewModel() {
    private val repository = RecipeRepository()

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _recipes.value = repository.searchRecipes(query)
        }
    }
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories



    private val _areas = MutableStateFlow<List<String>>(emptyList())
    val areas: StateFlow<List<String>> = _areas


    fun loadFilters() {
        viewModelScope.launch {
            _categories.value = listOf("All") + repository.getCategories()
            _areas.value = listOf("All") + repository.getAreas()
        }
    }

}
