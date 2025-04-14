package com.nomnomapp.nomnom.data.repository

import com.nomnomapp.nomnom.data.local.dao.FavoriteDao
import com.nomnomapp.nomnom.data.local.entity.FavoriteEntity
import com.nomnomapp.nomnom.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepository(private val dao: FavoriteDao) {

    suspend fun add(recipe: Recipe) {
        dao.addFavorite(recipe.toFavoriteEntity())
    }

    suspend fun remove(id: String) {
        dao.removeFavoriteById(id)
    }

    suspend fun isFavorite(id: String) = dao.isFavorite(id)

    fun getAll(): Flow<List<FavoriteEntity>> = dao.getAll()

    fun getAllAsRecipes(): Flow<List<Recipe>> {
        return dao.getAll().map { list ->
            list.map { it.toRecipe() }
        }
    }

    private fun Recipe.toFavoriteEntity() = FavoriteEntity(
        id = id,
        title = title,
        imageUrl = imageUrl ?: "",
        instructions = instructions,
        ingredients = ingredients.joinToString(","),
        category = category,
        area = area
    )

    private fun FavoriteEntity.toRecipe() = Recipe(
        id = id,
        title = title,
        imageUrl = imageUrl,
        instructions = instructions,
        ingredients = ingredients.split(","),
        category = category,
        area = area
    )
}
