package com.nomnomapp.nomnom.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_recipes")
data class UserRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val area: String,
    val instructions: String,
    val ingredients: String,
    val imageUri: String? = null
)