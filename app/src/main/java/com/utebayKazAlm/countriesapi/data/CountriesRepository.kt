package com.utebayKazAlm.countriesapi.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.utebayKazAlm.countriesapi.models.Country
import com.utebayKazAlm.countriesapi.util.ResultOf

//Не стал создавать DataSource, так как проект маленький, и оно без надобности усложнило бы код
class CountriesRepository constructor(private val api: CountriesApi) {
    private val REPO = "CountriesRepository"

    //Решил использовать LiveData вместо Flow в этом проекте.
    private val _countries: MutableLiveData<ResultOf<List<Country>>> = MutableLiveData()
    val countries: LiveData<ResultOf<List<Country>>> = _countries

    //Нужен чтобы не загружать все страны снова, если они уже были загружены.
    private var allCountriesLoaded = false

    //Нужно чтобы не загружало страны, если текст не изменился.
    private var countryName = ""

    suspend fun getAllCountries() {
        //Если true то все страны уже загружены и снова загружать не надо
        if (allCountriesLoaded) return

        //Устанавливаем значение что оно в процессе загрузки.
        _countries.value = ResultOf.Loading()
        try {
            val response = api.getAllCountries()
            val responseCountries = response.body()
            if (response.isSuccessful && responseCountries != null) {
                _countries.value = ResultOf.Success(responseCountries)
                //Все страны загружены.
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
        //Если новый текст и этот текст равны, то ничего делать не надо,
        //так как поиск по этому названию уже был сделан.
        if (countryName == name) return
        //А если новый текст отличается, то передаем его в countryName.
        else countryName = name

        _countries.value = ResultOf.Loading()
        try {
            val response = api.getCountriesByName(name)
            val responseCountries = response.body()
            if (response.isSuccessful && responseCountries != null) {
                _countries.value = ResultOf.Success(responseCountries)
                //Так как был совершен поиск по названию, больше не загружены все страны.
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