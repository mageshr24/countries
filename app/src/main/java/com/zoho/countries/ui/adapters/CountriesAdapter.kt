package com.zoho.countries

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zoho.countries.databinding.ItemCountryBinding
import com.zoho.countries.datasource.local.entities.country.Countries
import com.zoho.countries.utils.loadSvg

class CountriesAdapter(private val listener: CountryItemListener) :
    ListAdapter<Countries, CountriesAdapter.CountryViewHolder>(DiffCallback()) {

    interface CountryItemListener {
        fun onClickedCountry(countryName: String, latlng: List<Double>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding: ItemCountryBinding =
            ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CountryViewHolder(
            binding, listener
        )
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val fixtureItem = getItem(position)

        holder.apply {
            bind(fixtureItem)
        }
    }

    class CountryViewHolder(
        private val itemBinding: ItemCountryBinding,
        private val listener: CountryItemListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

        private lateinit var country: Countries

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: Countries) {
            this.country = item
            itemBinding.countryName.text = item.name
            itemBinding.image.loadSvg(item.flag.toString())
        }

        override fun onClick(v: View?) {
            listener.onClickedCountry(country.name, country.latlng)
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Countries>() {

    override fun areItemsTheSame(oldItem: Countries, newItem: Countries): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Countries, newItem: Countries): Boolean {
        return oldItem == newItem
    }
}
