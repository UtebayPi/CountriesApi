package com.utebayKazAlm.countriesapi.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.utebayKazAlm.countriesapi.models.Country
import com.utebayKazAlm.countriesapi.util.ResultOf

//Didn't use DataSource, because the project is small, and it would have created a code smell.
class CountriesRepository constructor(private val api: CountriesApi) {
    private val REPO = "CountriesRepository"

    //Desided to try LiveData instead of Flows.
    private val _countries: MutableLiveData<ResultOf<List<Country>>> = MutableLiveData()
    val countries: LiveData<ResultOf<List<Country>>> = _countries

    //Use it to not load all the countries, if they have already been loaded.
    private var allCountriesLoaded = false

    //Use it to not load countries again, if text didn't change.
    private var countryName = ""

    suspend fun getAllCountries() {
        //If then all countries are already loaded, i don't need to do that again.
        if (allCountriesLoaded) return

        //Showing that it is loading.
        _countries.value = ResultOf.Loading()
        try {
            val response = api.getAllCountries()
            val responseCountries = response.body()
            if (response.isSuccessful && responseCountries != null) {
                _countries.value = ResultOf.Success(responseCountries)
                //All countries are loaded.
                allCountriesLoaded = true
            } else {
                _countries.value = ResultOf.Error(response.message())
            }
        } catch (e: Exception) {
            _countries.value = ResultOf.Error("Error occurred: ${e.message}")
            Log.e(REPO, "Error occurred: ${e.message}")
        }
    }

    suspend fun getCountriesByName(name: String) {
        //if new and old country name is the same, no need to load again.
        if (countryName == name) return
        //Else set the new country name
        else countryName = name

        _countries.value = ResultOf.Loading()
        try {
            val response = api.getCountriesByName(name)
            val responseCountries = response.body()
            if (response.isSuccessful && responseCountries != null) {
                _countries.value = ResultOf.Success(responseCountries)
                //Because countries were loaded by name, not all countries are loaded.
                allCountriesLoaded = false
            } else {
                _countries.value = ResultOf.Error(response.message())
            }
        } catch (e: Exception) {
            _countries.value = ResultOf.Error("Error occurred: ${e.message}")
            Log.e(REPO, "Error occurred: ${e.message}")
        }
    }
}