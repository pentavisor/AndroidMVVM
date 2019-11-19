package com.example.apptest2019.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.apptest2019.repository.database.UserDatabase
import com.example.apptest2019.repository.database.database_models.UserDataModel
import com.example.apptest2019.repository.internet.RandomUserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(context: Context) {

    private val userDatabaseInstance: UserDatabase = UserDatabase.getInstance(context)
    private val randomUserApi:RandomUserApi = RandomUserApi.getInstance()

    val userLiveData:LiveData<List<UserDataModel>> = userDatabaseInstance.userDatabaseDao.getAllUsers()

    suspend fun addNewUser(){
        withContext(Dispatchers.IO){
            val addUser = randomUserApi.getSomeUserAsync().await()
            userDatabaseInstance.userDatabaseDao.insert(UserDataModel(firstName = addUser.results.get(0).name.first, lastName = addUser.results.get(0).name.last))
        }
    }
    fun deleteAllUsers(){
        userDatabaseInstance.userDatabaseDao.deleteAll()
    }


    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(context: Context): Repository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Repository(context)
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}