package com.nomnomapp.nomnom.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nomnomapp.nomnom.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun removeFavoriteById(id: String)

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Query("SELECT * FROM favorites")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<FavoriteEntity>>
}
