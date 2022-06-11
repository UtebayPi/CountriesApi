package com.utebayKazAlm.countriesapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.utebayKazAlm.countriesapi.databinding.CountryItemBinding
import com.utebayKazAlm.countriesapi.models.Country
import kotlin.math.round

class CountryListAdapter(
    private val clickListener: (Country) -> Unit
) : ListAdapter<Country, CountryListAdapter.CountryViewHolder>(DiffCallBack) {

    inner class CountryViewHolder(private val binding: CountryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(country: Country) {
            country.flags?.png?.let { url ->
                Glide.with(this.itemView).load(url).into(binding.ivFlag)
            }
            binding.tvName.text = "Country: " + country.name?.common.toString()
            binding.tvCapital.text = "Capital: " + (country.capital?.get(0) ?: "none")
            binding.tvRegion.text = "Region: " + (country.subregion ?: "none")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CountryViewHolder(
            CountryItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = getItem(position)
        holder.bind(country)
        holder.itemView.setOnClickListener {
            clickListener(country)
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem.fifa == newItem.fifa
        }

        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem == newItem
        }
    }
}