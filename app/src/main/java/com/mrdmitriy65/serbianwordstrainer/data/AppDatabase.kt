package com.mrdmitriy65.serbianwordstrainer.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mrdmitriy65.serbianwordstrainer.data.entities.Category
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair

@Database(
    entities = [WordPair::class, Category::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordPairDao(): WordPairDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "application_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}