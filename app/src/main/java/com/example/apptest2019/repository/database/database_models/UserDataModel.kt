package com.example.apptest2019.repository.database.database_models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_list", indices = [Index(value = ["id"], unique = true)])
data class UserDataModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "first_name")
    val firstName: String = "",

    @ColumnInfo(name = "last_name")
    val lastName: String = "",

    @ColumnInfo(name = "google_map_link")
    val googleMapLink: String = "",

    @ColumnInfo(name = "birthday")
    val birthday: String = "",

    @ColumnInfo(name = "sex")
    val sex: Boolean = false,

    @ColumnInfo(name = "sensor_number")
    val sensorNumber: String = "",

    @ColumnInfo(name = "sensor_secret_code")
    val sensorSecretCode: String = "",
    //Image from resource
    @ColumnInfo(name = "imageLink")
    val imageLink: Int = 0,
    // Image from Internet
    @ColumnInfo(name = "imageName")
    val imageName: String = "",

    @ColumnInfo(name = "future_id")
    val future_id: Int = 0
)