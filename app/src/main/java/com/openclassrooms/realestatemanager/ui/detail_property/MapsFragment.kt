package com.openclassrooms.realestatemanager.ui.detail_property

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R


class MapsFragment : Fragment() {

    var adress: String? = null

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        googleMap.uiSettings.isMapToolbarEnabled = true;
        googleMap.uiSettings.isZoomControlsEnabled = false;
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = false

        if (adress != null) {
            val marker = getLocationByAddress(requireActivity(), adress);
            if (marker!=null) {
                googleMap.addMarker(MarkerOptions().position(marker))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15f))
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            adress = bundle.getString("adress", "")
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun getLocationByAddress(context: Context, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        try {
            val address = coder.getFromLocationName(strAddress, 5) ?: return null
            val location = address.first()
            return LatLng(location.latitude, location.longitude)
        } catch (e: Exception) {
            Log.w("Maps Fragment", "getLocationByAddress: $e")
        }
        return null
    }
}