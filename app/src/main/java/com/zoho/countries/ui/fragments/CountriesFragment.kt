package com.zoho.countries.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.zoho.countries.CountriesAdapter
import com.zoho.countries.R
import com.zoho.countries.databinding.FragmentCountriesBinding
import com.zoho.countries.datasource.local.entities.country.Countries
import com.zoho.countries.utils.Resource
import com.zoho.countries.utils.autoCleared
import com.zoho.countries.viewmodels.CountriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CountriesFragment : Fragment(), CountriesAdapter.CountryItemListener {

    private var countriesBinding: FragmentCountriesBinding by autoCleared()
    private val viewModel: CountriesViewModel by viewModels()
    private lateinit var countriesAdapter: CountriesAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var checkPermission: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        countriesBinding = FragmentCountriesBinding.inflate(inflater, container, false)
        return countriesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countriesBinding.animationView.repeatCount = LottieDrawable.INFINITE
        setupRecyclerView()
        setupObservers()
        setSearchData()
    }

    private fun setupRecyclerView() {
        countriesAdapter = CountriesAdapter(this)
        countriesBinding.countriesRecyclerView.layoutManager = StaggeredGridLayoutManager(
            2,
            LinearLayoutManager.VERTICAL
        )
        countriesBinding.countriesRecyclerView.adapter = countriesAdapter
    }

    private fun setupObservers() {

        viewModel.countryList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {

                    if (!it.data.isNullOrEmpty()) {
                        countriesBinding.mShimmerViewContainer?.stopShimmerAnimation()
                        countriesBinding.mShimmerViewContainer?.visibility = View.GONE
                        countriesBinding.countriesRecyclerView?.visibility = View.VISIBLE
                        countriesAdapter.submitList(it.data)
                        checkLocationPermission()
                    }
                }
                Resource.Status.ERROR -> Toast.makeText(
                    requireContext(),
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()

                Resource.Status.LOADING -> countriesBinding.mShimmerViewContainer?.startShimmerAnimation()
            }
        })
    }

    private fun setSearchData() {

        countriesBinding.searchCountries.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                countriesBinding.searchCountries.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchDataDatabase(newText.toString())
                return true
            }
        })
    }

    private fun searchDataDatabase(searchText: String) {
        viewModel.countries.observe(viewLifecycleOwner, object : Observer<List<Countries>> {

            override fun onChanged(items: List<Countries>?) {
                if (items == null) {
                    return
                }
                countriesAdapter.submitList(items)
            }
        })
        viewModel.searchIt(searchText)
    }

    private fun checkLocationPermission() {

        if (!checkPermission) {
            return
        }
        checkPermission = false

        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), LOCATION_PERMISSION_REQUEST
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {

        if (!checkGPSEnabled()) {
            return
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->

                if (location == null) {
                    getCurrentLocation()
                }

                location?.also {
                    val latitude = location!!.latitude
                    val longitude = location!!.longitude
                    getWeatherData(latitude, longitude)
                }
            }
    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled()) {
            showAlert()
        }
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Location permission needed")
            .setMessage("This app requires GPS to be enabled to get the weather information. Do you want to enable now?")
            .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                checkPermission = true
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }

    fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

    private fun getWeatherData(latitude: Double, longitude: Double) {

        viewModel.getWeatherData(latitude, longitude).observe(viewLifecycleOwner, Observer {

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let {
                        Timber.v(""+it)
                        val degree = it.main?.temp.toInt()
                        val description = it.weather[0].description.capitalizeWords()
                        val city = it.name
                        val degreesymbol = 0x00B0.toChar()

                        countriesBinding.weatherDegree.text = "$degree $degreesymbol in $city"
                        countriesBinding.weatherStatus.text = "$description"
                    }
                }
                Resource.Status.ERROR ->
                    Timber.v(it.message, "ErrorLog")

            }
        })
    }

    override fun onClickedCountry(countryName: String, latlng: List<Double>) {
        countriesBinding.searchCountries.clearFocus()

        Timber.v("latlngList : " + latlng)

        findNavController().navigate(
            R.id.action_countriesFragment_to_countryDetailFragment,
            bundleOf("countryName" to countryName, "latlng" to latlng)
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission) {
            checkLocationPermission()
        }
        checkPermission = true
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST = 1000
    }
}