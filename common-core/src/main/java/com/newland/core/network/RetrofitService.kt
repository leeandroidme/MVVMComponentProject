package com.newland.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

object RetrofitService {
    private val logger = Logger.getLogger(RetrofitService::class.java.simpleName)
    private val BASE_URL = "https://www.wanandroid.com"

    public fun <T> createApiService(clazz: Class<T>, url: String = BASE_URL): T =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(
            url
        ).client(createOkClient()).build().create(clazz)

    private fun createOkClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor {
            logger.info(it)
        }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .connectTimeout(5_000L, TimeUnit.MILLISECONDS)
            .readTimeout(10_000L, TimeUnit.MILLISECONDS)
            .writeTimeout(30_000, TimeUnit.MILLISECONDS)
            .build()
    }
}