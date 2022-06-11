package com.utebayKazAlm.countriesapi.models

data class Country(
    val altSpellings: List<String>?,
    val area: Double?,
    val borders: List<String>?,
    val capital: List<String>?,
    val capitalInfo: CapitalInfo?,
    val coatOfArms: CoatOfArms?,
    val continents: List<String>?,
    val currencies: Map<String, Currency>?,
    val fifa: String?,
    val flag: String?,
    val flags: Flags?,
    val languages: Map<String, String>?,
    val latlng: List<Double>?,
    val maps: Maps?,
    val name: Name?,
    val population: Int?,
    val postalCode: PostalCode?,
    val region: String?,
    val startOfWeek: String?,
    val status: String?,
    val subregion: String?,
    val timezones: List<String>?,
    val translations: Translations?,
    val unMember: Boolean?
)