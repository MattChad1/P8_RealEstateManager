package com.openclassrooms.realestatemanager.ui.detail_property

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentDetailPropertyBinding
import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.utils.Utils


class DetailPropertyFragment : Fragment(), OnMapReadyCallback {

    var TAG = "MyLog DetailProperty"
    val allImages = mutableListOf<ImageRoom>()
    lateinit var binding: FragmentDetailPropertyBinding
    var property: DetailPropertyViewState? = null

    private var readPermissionGranted = false
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    private val viewModel: DetailPropertyViewModel by viewModels {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.navigationRepository)
    }
    var adressForMap: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted

            if (!readPermissionGranted) {
                Toast.makeText(requireActivity(), "Can't read files without permission.", Toast.LENGTH_LONG).show()
            }
        }
        updateOrRequestPermissions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailPropertyBinding.inflate(inflater, container, false)
        var navController = findNavController()

        if (requireActivity().resources.getBoolean(R.bool.isTablet)) {
            requireActivity().findViewById<FragmentContainerView>(R.id.fragment_left_column).visibility = View.VISIBLE
        }

        viewModel.propertyLiveData.observe(viewLifecycleOwner) { property ->
            if (property != null) {
                property.photos?.let {
                    allImages.addAll(property.photos!!)
                    val imagePropertyAdapter = ImagePropertyAdapter(requireActivity() as AppCompatActivity, allImages)
                    binding.viewpagerRooms.adapter = imagePropertyAdapter

                    if (allImages.size <= 1) binding.tabLayout.visibility = View.GONE
                    else {
                        binding.tabLayout.visibility = View.VISIBLE
                        TabLayoutMediator(binding.tabLayout, binding.viewpagerRooms) { tab, position ->
                            tab.text = ""
                        }.attach()
                    }
                }

                binding.tvDetailDescription.text = property.description

                val cardviews = listOf(
                    binding.cardviewSquareMeters,
                    binding.cardviewNumberRooms,
                    binding.cardviewNumberBathrooms,
                    binding.cardviewNumberBedrooms,
                    binding.cardviewLocation
                )
                val icons = listOf(
                    R.drawable.ic_baseline_square_foot_24,
                    R.drawable.ic_baseline_house_24,
                    R.drawable.ic_outline_bathroom_24,
                    R.drawable.ic_baseline_bed_24,
                    R.drawable.ic_baseline_location_on_24
                )
                val titles = listOf(
                    com.openclassrooms.realestatemanager.R.string.surface,
                    com.openclassrooms.realestatemanager.R.string.num_rooms,
                    com.openclassrooms.realestatemanager.R.string.num_bathrooms,
                    com.openclassrooms.realestatemanager.R.string.num_bedrooms,
                    com.openclassrooms.realestatemanager.R.string.location
                )
                val datas = listOf(
                    this.getString(com.openclassrooms.realestatemanager.R.string.surface_data, property.squareFeet),
                    property.rooms.toString(),
                    property.bathrooms.toString(),
                    property.bedrooms.toString(),
                    property.adress
                )

                for (i in 0..4) {
                    cardviews[i].iconDetail.setImageResource(icons[i])
                    cardviews[i].tvDescriptionTitle.text = getString(titles[i])
                    cardviews[i].tvDetailData.text = datas[i]

                }

                val layoutProximity = binding.layoutProximityIcons
                property.proximities?.forEach { proximity ->
                    val root: LinearLayout = layoutInflater.inflate(R.layout.item_proximity, layoutProximity, false) as LinearLayout
                    val imageView = root.findViewById<ImageView>(R.id.iv_item_proximity)
                    imageView.setImageResource(this.resources.getIdentifier(proximity.icon, "drawable", activity?.packageName))
                    val textView = root.findViewById<TextView>(R.id.tv_item_proximity)
                    textView.text = activity?.getString(this.resources.getIdentifier(proximity.refLegend, "string", activity?.packageName))

                    layoutProximity.addView(root)
                }

                adressForMap = property.adress
                val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.map_in_detail) as SupportMapFragment
                mapFragment.getMapAsync(this)


                // Contact infos
                if (property.dateSold == null) binding.tvContactInfo.text =
                    this.getString(R.string.contact_info, property.agent.name, Utils.formatDateDayBefore(property.dateStartSell))
                else binding.tvContactInfo.text = this.getString(R.string.contact_info_sold, property.agent.name, Utils.formatDateDayBefore(property.dateSold))

                binding.btnEdit.setOnClickListener {
                    val destination = DetailPropertyFragmentDirections.actionDetailPropertyFragmentToAddFragment(property.id)
                    navController.navigate(destination)

                }
            }

        }

        return binding.root
    }

    private fun updateOrRequestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        readPermissionGranted = hasReadPermission


        val permissionsToRequest = mutableListOf<String>()
        if (!readPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionsToRequest.isNotEmpty()) {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = false

        if (adressForMap != null) {
            val marker = getLocationByAddress(requireActivity(), adressForMap)
            if (marker != null) {
                googleMap.addMarker(MarkerOptions().position(marker))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15f))
            }

        }
    }

    private fun getLocationByAddress(context: Context, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        try {
            val address = coder.getFromLocationName(strAddress, 1) ?: return null
            val location = address.first()
            return LatLng(location.latitude, location.longitude)
        }
        catch (e: Exception) {
            Log.w("Maps Fragment", "getLocationByAddress: $e")
        }
        return null
    }


}