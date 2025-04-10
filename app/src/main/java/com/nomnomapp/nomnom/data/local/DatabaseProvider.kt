package com.nomnomapp.nomnom.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "nomnom_database"
            )
            .fallbackToDestructiveMigration() // 🔥 To pozwala usunąć i odtworzyć bazę przy zmianach
            .build().also { INSTANCE = it }
        }
    }
}
