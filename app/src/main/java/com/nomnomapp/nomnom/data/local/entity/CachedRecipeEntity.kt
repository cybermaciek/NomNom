package com.nomnomapp.nomnom.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_recipes")
data class CachedRecipeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val instructions: String,
    val ingredients: String,
    val category: String,
    val area: String
)
