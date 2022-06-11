package com.utebayKazAlm.countriesapi.di

import com.utebayKazAlm.countriesapi.data.CountriesApi
import com.utebayKazAlm.countriesapi.util.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.utebayKazAlm.countriesapi.data.CountriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CountriesModule {

    @Provides
    @Singleton
    fun provideCountriesApi(): CountriesApi {
        // позволяет логировать запросы в retrofit,
        //полезно для дебаггинга
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL) //BASE_URL находится в util/Constants.kt
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(CountriesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCountriesRepository(api: CountriesApi): CountriesRepository = CountriesRepository(api)
}