package com.nomnomapp.nomnom.data.repository

import com.nomnomapp.nomnom.data.local.dao.FavoriteDao
import com.nomnomapp.nomnom.data.local.entity.FavoriteEntity

class FavoriteRepository(private val dao: FavoriteDao) {
    suspend fun add(recipeId: String) = dao.addFavorite(FavoriteEntity(recipeId))
    suspend fun remove(recipeId: String) = dao.removeFavorite(FavoriteEntity(recipeId))
    suspend fun isFavorite(id: String) = dao.isFavorite(id)
    suspend fun getAll() = dao.getAllFavorites()

}
