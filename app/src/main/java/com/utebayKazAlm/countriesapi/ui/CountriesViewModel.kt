package com.utebayKazAlm.countriesapi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utebayKazAlm.countriesapi.data.CountriesRepository
import com.utebayKazAlm.countriesapi.models.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(private val repository: CountriesRepository) : ViewModel() {

    val countries = repository.countries

    //Decided to use LiveData instead of Flow.
    private val _country: MutableLiveData<Country> = MutableLiveData()
    val country: LiveData<Country> = _country

    init {
        getAllCountries()
    }

    fun setCountry(country: Country) {
        _country.value = country
    }

    fun getAllCountries() {
        viewModelScope.launch {
            repository.getAllCountries()
        }
    }

    fun getCountriesByName(name: String) {
        viewModelScope.launch {
            repository.getCountriesByName(name)
        }
    }
}