package com.nomnomapp.nomnom.model

data class Ingredient(
    val name: String,
    val isInShoppingList: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)