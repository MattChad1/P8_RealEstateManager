package com.openclassrooms.realestatemanager.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.openclassrooms.realestatemanager.ui.main_activity.MainActivity
import com.openclassrooms.realestatemanager.utils.PermissionUtils
import com.openclassrooms.realestatemanager.utils.PermissionUtils.requestPermission
import com.openclassrooms.realestatemanager.utils.Utils
import java.io.File

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnMyLocationButtonClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationClickListener {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var allProperties: List<MapsViewStateItem>
    private lateinit var navController: NavController

    private lateinit var googleMap: GoogleMap
    private var permissionDenied = false

    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.navigationRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        this.googleMap = googleMap
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.712784, -74.005941), 10f))
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        enableMyLocation()



        googleMap.setOnInfoWindowClickListener(this)
        googleMap.setInfoWindowAdapter(this)

        viewModel.allPropertiesLiveData.observe(this) { properties ->
            googleMap.clear()
            allProperties = properties
            for (p in properties) {
                val location = getLocationByAddress(requireActivity(), p.adress)
                if (location != null) {
                    val marker = googleMap.addMarker(MarkerOptions().position(location))
                    marker?.tag = properties.indexOf(p)
                }
            }
        }
    }


    private fun getLocationByAddress(context: Context, strAddress: String?): LatLng? {
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

    override fun getInfoContents(marker: Marker): View {
        val bindingWindow: InfoWindowBinding = InfoWindowBinding.inflate(layoutInflater)
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

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::googleMap.isInitialized) return
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            googleMap.setOnMyLocationButtonClickListener(this)
            googleMap.setOnMyLocationClickListener(this)
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(
                requireActivity() as MainActivity, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(requireActivity(), "Current location:\n$location", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        Toast.makeText(requireActivity(), R.string.permission_required_toast, Toast.LENGTH_LONG).show()
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}