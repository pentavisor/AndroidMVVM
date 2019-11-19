package com.example.apptest2019.repository.internet

import android.content.Context
import com.example.apptest2019.BuildConfig
import com.example.apptest2019.repository.internet.internet_models.SomeUserModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface RandomUserApi {

    @GET("?inc=name,picture&nat=us")
    //@Headers("Connection:close", "Content-Type: application/json;charset=UTF-8", "Accept: application/json")//"Connection:close",
    fun getSomeUserAsync(
    ): Deferred<SomeUserModel>



    companion object {
        @Volatile
        private var INSTANCE: RandomUserApi? = null

        fun getInstance(): RandomUserApi {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = RandomUserApi()
                    INSTANCE = instance
                }
                return instance
            }
        }
        //operator fun invoke() позволяет вызывать фабрику RetrofitAuthenticationApi через переопределенный конструктор: RetrofitAuthenticationApi()
        operator fun invoke(): RandomUserApi {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
                .build()
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())//юзает ассинхронные корутины Kotlin
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl("https://randomuser.me/api/")//cервис генерации дефолтных данных о пользователях
                .client(okHttpClient)//юзает okHttp с интерсептером для отдадки
                .build()

            return retrofit.create(RandomUserApi::class.java)
        }
    }
}