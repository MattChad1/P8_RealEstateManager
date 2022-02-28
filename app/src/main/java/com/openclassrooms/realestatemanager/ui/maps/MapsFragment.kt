package com.openclassrooms.realestatemanager.ui.maps

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentMapsBinding
import com.openclassrooms.realestatemanager.databinding.InfoWindowBinding
import com.openclassrooms.realestatemanager.utils.Utils
import java.io.File

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var allProperties: List<MapsViewStateItem>
    private lateinit var navController: NavController

    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.navigationRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(layoutInflater)
        if (requireActivity().resources.getBoolean(R.bool.isTablet)) {
            requireActivity().findViewById<FragmentContainerView>(R.id.fragment_left_column).visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        navController = findNavController()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }


    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.712784, -74.005941), 10f))
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.setOnInfoWindowClickListener(this)
        googleMap.setInfoWindowAdapter(this)

        viewModel.allPropertiesLiveData.observe(this) { properties ->
            googleMap.clear()
            allProperties = properties
            for (p in properties) {
                if (p.adress != null) {
                    val location = getLocationByAddress(requireActivity(), p.adress)
                    if (location != null) {
                        val marker = googleMap.addMarker(MarkerOptions().position(location))
                        marker?.tag = properties.indexOf(p)
                    }
                }
            }
        }
    }


    fun getLocationByAddress(context: Context, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        try {
            val address = coder.getFromLocationName(strAddress, 5) ?: return null
            val location = address.first()
            return LatLng(location.latitude, location.longitude)
        }
        catch (e: Exception) {
            Log.w("Maps Fragment", "getLocationByAddress: $e")
        }
        return null
    }

    override fun onInfoWindowClick(marker: Marker) {
        viewModel.changeSelection(allProperties[marker.tag as Int].id)
        val sendData = MapsFragmentDirections.actionMapsFragmentToDetailPropertyFragment()
        navController.navigate(sendData)
    }

    override fun getInfoContents(marker: Marker): View? {
        var bindingWindow: InfoWindowBinding = InfoWindowBinding.inflate(layoutInflater)
        val position = marker.tag
        val property = allProperties[position as Int]
        property.photo?.let {
            bindingWindow.ivMapwindow.setImageURI(
                Uri.fromFile(File(MyApplication.instance.filesDir, "${property.photo!!.nameFile}.jpg"))
            )
        }
            bindingWindow.tvMapwindowTitle.text = property.type
            bindingWindow.tvMapwindowPrice.text = Utils.formatPrice(property.price)
            return bindingWindow.root
        }


    override fun getInfoWindow(p0: Marker): View? {
        return null
    }

}