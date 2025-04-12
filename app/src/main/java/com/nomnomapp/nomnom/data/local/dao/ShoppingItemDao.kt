package com.nomnomapp.nomnom.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nomnomapp.nomnom.data.local.entity.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItem)

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("SELECT * FROM shopping_items WHERE isInCart = 1 ORDER BY timestamp DESC")
    fun getItemsInCart(): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE isInCart = 0 ORDER BY timestamp DESC")
    fun getRecentItems(): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE name LIKE :query")
    suspend fun searchItems(query: String): List<ShoppingItem>

    @Query("DELETE FROM shopping_items WHERE isInCart = 0")
    suspend fun clearRecentItems()
}