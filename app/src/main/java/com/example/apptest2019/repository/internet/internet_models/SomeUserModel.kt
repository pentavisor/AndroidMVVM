package com.example.apptest2019.repository.internet.internet_models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SomeUserModel(
    @Json(name = "info")
    val info: Info,
    @Json(name = "results")
    val results: List<Result>
) {
    @JsonClass(generateAdapter = true)
    data class Info(
        @Json(name = "page")
        val page: Int,
        @Json(name = "results")
        val results: Int,
        @Json(name = "seed")
        val seed: String,
        @Json(name = "version")
        val version: String
    )

    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "name")
        val name: Name,
        @Json(name = "picture")
        val picture: Picture
    ) {
        @JsonClass(generateAdapter = true)
        data class Name(
            @Json(name = "first")
            val first: String,
            @Json(name = "last")
            val last: String,
            @Json(name = "title")
            val title: String
        )

        @JsonClass(generateAdapter = true)
        data class Picture(
            @Json(name = "large")
            val large: String,
            @Json(name = "medium")
            val medium: String,
            @Json(name = "thumbnail")
            val thumbnail: String
        )
    }
}