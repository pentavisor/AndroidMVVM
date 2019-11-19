package com.example.apptest2019.repository.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.apptest2019.repository.database.database_models.UserDataModel

@Dao
interface MyDatabaseDao {
    @Insert
    fun insert(night: UserDataModel)

    @Update
    fun update(night: UserDataModel)

    @Delete
    fun delete(night: UserDataModel)

    @Query("SELECT * from user_list WHERE id = :key")
    fun get(key: Long): UserDataModel?

    @Query("DELETE FROM user_list")
    fun clear()

    @Query("SELECT * FROM user_list ORDER BY id DESC")
    fun getAllUsers(): LiveData<List<UserDataModel>>

    @Query("SELECT * FROM user_list ORDER BY id DESC LIMIT 1")
    fun getFirstUser(): UserDataModel?

    @Query("DELETE FROM user_list")
    fun deleteAll()


}