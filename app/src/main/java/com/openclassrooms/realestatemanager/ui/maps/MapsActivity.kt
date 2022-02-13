package com.openclassrooms.realestatemanager.ui.maps

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import com.openclassrooms.realestatemanager.databinding.ActivityMapsBinding
import com.openclassrooms.realestatemanager.databinding.InfoWindowBinding
import com.openclassrooms.realestatemanager.ui.main_activity.MainActivity
import com.openclassrooms.realestatemanager.utils.Utils
import java.io.File

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    lateinit var allProperties: List<MapsViewStateItem>

    private val viewModel: MapsViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.filterSearchRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }



    override fun onMapReady(googleMap: GoogleMap) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.712784, -74.005941), 10f))
//        googleMap.uiSettings.isMapToolbarEnabled = true;
        googleMap.uiSettings.isZoomControlsEnabled = true;
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.setOnInfoWindowClickListener(this)
        googleMap.setInfoWindowAdapter(this)

        viewModel.allPropertiesLiveData.observe(this) {properties ->
            googleMap.clear()
            allProperties = properties
            for (p in properties) {
                if (p.adress != null) {
                    val location = getLocationByAddress(this, p.adress);
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
            val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("idProperty", allProperties[marker.tag as Int].id)
        startActivity(intent)
    }

    override fun getInfoContents(marker: Marker): View? {
        var bindingWindow: InfoWindowBinding = InfoWindowBinding.inflate(getLayoutInflater())
        val position = marker.tag
        val property = allProperties[position as Int]
        bindingWindow.ivMapwindow.setImageURI(
            Uri.fromFile(File(MyApplication.instance.filesDir, "${property.photo.nameFile}.jpg")))
        bindingWindow.tvMapwindowTitle.text = property.type
        bindingWindow.tvMapwindowPrice.text = Utils.formatPrice(property.price)
        return bindingWindow.root
    }

    override fun getInfoWindow(p0: Marker): View? {
        return null
    }

}