package com.utebayKazAlm.countriesapi.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utebayKazAlm.countriesapi.data.CountriesApi
import com.utebayKazAlm.countriesapi.models.Country
import com.utebayKazAlm.countriesapi.util.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(private val api: CountriesApi) : ViewModel() {

    private val TAG = "CountryViewModel"
    private val _countries: MutableLiveData<ResultOf<List<Country>>> = MutableLiveData()
    val countries: LiveData<ResultOf<List<Country>>> = _countries
    private val _country: MutableLiveData<Country> = MutableLiveData()
    val country: LiveData<Country> = _country

    //Нужен чтобы не загружать все страны снова, если они уже были загружены.
    private var allCountriesLoaded = false

    //Нужно чтобы не загружало страны, если текст не изменился
    private var searchText = ""

    init {
        getAllCountries()
    }

    fun selectCountry(country: Country) {
        _country.value = country
    }

    fun getAllCountries() {
        if (allCountriesLoaded) return
        viewModelScope.launch {
            _countries.value = ResultOf.Loading()
            try {
                val response = api.getAllCountries()
                val responseCountries = response.body()
                if (response.isSuccessful && responseCountries != null) {
                    _countries.value = ResultOf.Success(responseCountries)
                    allCountriesLoaded = true
                } else {
                    _countries.value = ResultOf.Error(response.message())
                }
            } catch (e: Exception) {
                _countries.value = ResultOf.Error("Error occurred: ${e.message}")
                Log.e(TAG, "Error occurred: ${e.message}")
            }

        }
    }

    fun getCountriesByName(name: String) {
        if (searchText == name) return
        searchText = name
        viewModelScope.launch {
            _countries.value = ResultOf.Loading()
            try {
                val response = api.getCountriesByName(name)
                val responseCountries = response.body()
                if (response.isSuccessful && responseCountries != null) {
                    _countries.value = ResultOf.Success(responseCountries)
                    allCountriesLoaded = false
                } else {
                    _countries.value = ResultOf.Error(response.message())
                }
            } catch (e: Exception) {
                _countries.value = ResultOf.Error("Error occurred: ${e.message}")
                Log.e(TAG, "Error occurred: ${e.message}")
            }

        }
    }
}