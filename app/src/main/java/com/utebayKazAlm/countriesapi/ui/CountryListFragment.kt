package com.utebayKazAlm.countriesapi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.utebayKazAlm.countriesapi.R
import com.utebayKazAlm.countriesapi.adapter.CountryListAdapter
import com.utebayKazAlm.countriesapi.databinding.FragmentCountryListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountryListFragment : Fragment(R.layout.fragment_country_list) {

    private var _binding: FragmentCountryListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountriesViewModel by activityViewModels()
    private lateinit var countryListAdapter: CountryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCountryListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        //Создал Job чтобы его можно было отменять,
        //чтобы suspend функция не запускалась каждый раз при изменений каждой буквы в поле ввода,
        //а ожидало пока пользователь полностью напишет слово.
        var searchJob: Job? = null
        binding.etCountryName.addTextChangedListener { editable ->
            searchJob?.cancel()
            searchJob = MainScope().launch {
                delay(500)
                if (editable == null) return@launch
                if (editable.toString().isEmpty()) {
                    //Если поле пустое. то снова берем все страны.
                    viewModel.getAllCountries()
                    return@launch
                }
                viewModel.getCountriesByName(editable.toString())
            }
        }

        viewModel.countries.observe(viewLifecycleOwner) { result ->
            //Удобнее чем писать when.
            result.onSuccess { countries ->
                //pbLoading это ProgressBar. Мы его скрываем, показываем.
                binding.pbLoading.visibility = View.INVISIBLE
                countryListAdapter.submitList(countries)
            }
            result.onError { message, data ->
                binding.pbLoading.visibility = View.INVISIBLE
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
            }
            result.onLoading {
                binding.pbLoading.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRecyclerView() {
        countryListAdapter = CountryListAdapter { country ->
            //Устанавливаем страну, данные о которой будут показаны на втором фрагменте.
            viewModel.setCountry(country)
            findNavController().navigate(
                CountryListFragmentDirections.actionCountryListFragmentToCountryDetailFragment()
            )
        }
        binding.rvCountries.adapter = countryListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}