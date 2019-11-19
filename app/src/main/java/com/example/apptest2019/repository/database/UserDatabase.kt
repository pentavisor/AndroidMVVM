package com.example.apptest2019.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.apptest2019.repository.database.daos.MyDatabaseDao
import com.example.apptest2019.repository.database.database_models.UserDataModel

@Database(entities = [UserDataModel::class], version = 2, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDatabaseDao: MyDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            UserDatabase::class.java,
                            "user_history_database"
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}