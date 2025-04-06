package com.nomnomapp.nomnom.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey val name: String,
    val isInCart: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
