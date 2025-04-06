package com.nomnomapp.nomnom.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomnomapp.nomnom.data.local.DatabaseProvider
import com.nomnomapp.nomnom.data.local.entity.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoppingListViewModel(context: Context) : ViewModel() {
    private val shoppingItemDao = DatabaseProvider.getDatabase(context).shoppingItemDao()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val itemsInCart: Flow<List<ShoppingItem>> = shoppingItemDao.getItemsInCart()
    val recentItems: Flow<List<ShoppingItem>> = shoppingItemDao.getRecentItems()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addOrRemoveItem(name: String) {
        viewModelScope.launch {
            val existingItem = shoppingItemDao.searchItems(name).firstOrNull()
            if (existingItem != null) {
                shoppingItemDao.update(existingItem.copy(
                    isInCart = !existingItem.isInCart,
                    timestamp = System.currentTimeMillis()
                ))
            } else {
                shoppingItemDao.insert(ShoppingItem(name = name, isInCart = true))
            }
        }
    }

    fun moveToRecent(name: String) {
        viewModelScope.launch {
            shoppingItemDao.searchItems(name).firstOrNull()?.let { item ->
                shoppingItemDao.update(item.copy(
                    isInCart = false,
                    timestamp = System.currentTimeMillis()
                ))
            }
        }
    }
}