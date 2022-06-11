package com.utebayKazAlm.countriesapi.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.utebayKazAlm.countriesapi.R
import com.utebayKazAlm.countriesapi.databinding.FragmentCountryDetailBinding
import com.utebayKazAlm.countriesapi.models.Country
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round

@AndroidEntryPoint
class CountryDetailFragment : Fragment(R.layout.fragment_country_detail) {

    private var _binding: FragmentCountryDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountriesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCountryDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.country.observe(viewLifecycleOwner) { country: Country? ->
            if (country == null) { // проверяем на null, если null то возвращаемся.
                findNavController().popBackStack()
                return@observe
            }
            country.flags?.png?.let { url ->
                Glide.with(this).load(url).into(binding.ivFlag)
            }
            country.coatOfArms?.png.let { url ->
                Glide.with(this).load(url).into(binding.ivEmblem)
            }
            //Здесь все данные крепим к TextView и ImageView.
            //Не использовал data binding так как оно делает нахождение ошибок в коде сложнее
            binding.apply {
                tvName.text = "Country: " + country.name?.common.toString()
                tvCapital.text = "Capital: " + (country.capital?.get(0) ?: "none")
                val population = round((country.population?.toDouble() ?: 0.0).div(1000)).div(1000)
                tvPopulation.text = "Population: $population million"
                tvRegion.text = "Region: " + (country.subregion ?: "none")
                tvArea.text = "Area: " + (country.area?.toInt() ?: "none") + " km2"
                tvCurrency.text = "Currency: " + (country.currencies?.values?.elementAtOrNull(0)?.name ?: "none")
                tvLanguages.text = "Language: " + (country.languages?.values?.elementAtOrNull(0) ?: "none")
            }
            //При нажатий на кнопку, перенаправляем на расположение этой страны в google maps.
            binding.btnOpenMap.setOnClickListener {
                val intentUri = Uri.parse(country.maps?.googleMaps)
                val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}