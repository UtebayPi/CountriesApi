package com.utebayKazAlm.countriesapi.data

import com.utebayKazAlm.countriesapi.models.Country
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesApi {
    @GET("all")
    suspend fun getAllCountries(): Response<List<Country>>

    @GET("name/{countryName}")
    suspend fun getCountriesByName(
        @Path("countryName")
        countryName: String
    ): Response<List<Country>>
}