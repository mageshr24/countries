package com.zoho.countries.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.zoho.countries.R
import com.zoho.countries.databinding.FragmentCountryDetailBinding
import com.zoho.countries.datasource.local.entities.ItemData
import com.zoho.countries.datasource.local.entities.airPollution.AirPollution
import com.zoho.countries.datasource.local.entities.country.Countries
import com.zoho.countries.utils.Resource
import com.zoho.countries.utils.autoCleared
import com.zoho.countries.utils.loadSvg
import com.zoho.countries.viewmodels.CountryDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CountryDetailFragment : Fragment() {
    private var countryDetailBinding: FragmentCountryDetailBinding by autoCleared()
    private val countryDetailViewModel: CountryDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        countryDetailBinding = FragmentCountryDetailBinding.inflate(inflater, container, false)

        setupBackButton()

        return countryDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<ItemData>("itemData")?.let {

            it.countryName?.let { data -> countryDetailViewModel.start(data) }

            if (!it.latitude.equals(0.0) && !it.longitude.equals(0.0)) {
                getAirPollutionData(it.latitude, it.longitude)
            }
        }

        getCountryDetails()
    }

    private fun getCountryDetails() {
        countryDetailViewModel.countries.observe(viewLifecycleOwner, Observer {
            bindCountryData(it!!)
        })
    }

    private fun bindCountryData(countries: Countries) {

        countryDetailBinding.countryimage.loadSvg(countries.flag.toString())

        if (countries.name.isNullOrEmpty()) {
            countryDetailBinding.countryName.visibility = View.GONE
        } else {
            countryDetailBinding.countryName.text = countries.name
        }

        if (countries.capital.isNullOrEmpty()) {
            countryDetailBinding.capitalName.visibility = View.GONE
            countryDetailBinding.capitalNameData.visibility = View.GONE
        } else {
            countryDetailBinding.capitalNameData.text = countries.capital
        }

        if (countries.languages[0].name.isNullOrEmpty()) {
            countryDetailBinding.language.visibility = View.GONE
            countryDetailBinding.languageData.visibility = View.GONE
        } else {
            countryDetailBinding.languageData.text = countries.languages[0].name
        }

        if (countries.region.isNullOrEmpty()) {
            countryDetailBinding.region.visibility = View.GONE
            countryDetailBinding.regionData.visibility = View.GONE
        } else {
            countryDetailBinding.regionData.text = countries.region
        }

        if (countries.currencies[0].code.isNullOrEmpty()) {
            countryDetailBinding.currency.visibility = View.GONE
            countryDetailBinding.currencyData.visibility = View.GONE
        } else {
            countryDetailBinding.currencyData.text =
                countries.currencies[0].code + " (" + countries.currencies[0].symbol + ")"
        }
    }

    private fun setupBackButton() {
        val navController = findNavController()

        countryDetailBinding.countryDetailToolbar.setNavigationIcon(R.drawable.ic_back_button)
        countryDetailBinding.countryDetailToolbar.setTitle("Country Details")
        countryDetailBinding.countryDetailToolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun getAirPollutionData(latitude: Double, longitude: Double) {

        countryDetailViewModel.getAirPollution(latitude, longitude)
            .observe(viewLifecycleOwner, Observer {

                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        it.data?.let {
                            Timber.v("" + it)
                            countryDetailBinding.countryDetailsShimmerFL?.stopShimmerAnimation()
                            countryDetailBinding.countryDetailsShimmerFL.visibility = View.GONE
                            countryDetailBinding.countryDetailsCL.visibility = View.VISIBLE
                            bindAirpollutionData(it!!)
                        }
                    }
                    Resource.Status.ERROR ->
                        Timber.v(it.message, "ErrorLog")

                    Resource.Status.LOADING -> countryDetailBinding.countryDetailsShimmerFL?.startShimmerAnimation()
                }
            })
    }

    private fun bindAirpollutionData(airPollution: AirPollution) {

        if (airPollution.list.isNotEmpty()) {
            countryDetailBinding.airPollutionLinear.visibility = View.VISIBLE

            if (airPollution.list[0].components.co.equals(0.0)) {
                countryDetailBinding.carbonText.visibility = View.GONE
                countryDetailBinding.carbonData.visibility = View.GONE
            } else {
                countryDetailBinding.carbonData.text =
                    airPollution.list[0].components.co.toString() + " μg/m3"
            }

            if (airPollution.list[0].components.no2.equals(0.0)) {
                countryDetailBinding.nitrogenText.visibility = View.GONE
                countryDetailBinding.nitrogenData.visibility = View.GONE
            } else {
                countryDetailBinding.nitrogenData.text =
                    airPollution.list[0].components.no2.toString() + " μg/m3"
            }

            if (airPollution.list[0].components.o3.equals(0.0)) {
                countryDetailBinding.ozoneText.visibility = View.GONE
                countryDetailBinding.ozoneData.visibility = View.GONE
            } else {
                countryDetailBinding.ozoneData.text =
                    airPollution.list[0].components.o3.toString() + " μg/m3"
            }

            if (airPollution.list[0].components.so2.equals(0.0)) {
                countryDetailBinding.sulphurText.visibility = View.GONE
                countryDetailBinding.sulphurData.visibility = View.GONE
            } else {
                countryDetailBinding.sulphurData.text =
                    airPollution.list[0].components.so2.toString() + " μg/m3"
            }
        }
    }
}
