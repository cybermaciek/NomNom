package com.nomnomapp.nomnom.model

data class Recipe(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val category: String,
    val area: String,
    val instructions: String,
    val ingredients: List<String>
)