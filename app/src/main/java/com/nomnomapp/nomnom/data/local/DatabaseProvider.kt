package com.nomnomapp.nomnom.data.local

import android.content.Context
import androidx.room.Room
import com.nomnomapp.nomnom.data.repository.RecipeRepository

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "nomnom_database"
            )
                .fallbackToDestructiveMigration() //pozwala usunac i odtworzyc baze przy zmianach
                .build().also { INSTANCE = it }
        }
    }

    fun provideRecipeRepository(context: Context): RecipeRepository {
        val db = getDatabase(context)
        return RecipeRepository(db.cachedRecipeDao())
    }

}
