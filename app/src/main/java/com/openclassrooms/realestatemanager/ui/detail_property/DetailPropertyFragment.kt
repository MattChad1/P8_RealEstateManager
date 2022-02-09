package com.openclassrooms.realestatemanager.ui.detail_property

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentDetailPropertyBinding
import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.utils.Utils


class DetailPropertyFragment : Fragment() {

    var TAG = "MyLog DetailProperty"
    val allImages = mutableListOf<ImageRoom>()
    lateinit var binding: FragmentDetailPropertyBinding
    var property: DetailPropertyViewState? = null

    private var readPermissionGranted = false
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    private val viewModel: DetailPropertyViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.filterSearchRepository)
    }

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

        val idProperty = requireArguments().getInt("idProperty")

        viewModel.getPropertyById(idProperty).observe(viewLifecycleOwner) { property ->
            if (property != null) {
                property.photos?.let {
                    allImages.addAll(property.photos!!)
                    val imagePropertyAdapter = ImagePropertyAdapter(requireActivity() as AppCompatActivity, allImages)
                    binding.viewpagerRooms.adapter = imagePropertyAdapter

                    if (allImages.size <= 1) binding.tabLayout.visibility = View.GONE
                    else {
                        TabLayoutMediator(binding.tabLayout, binding.viewpagerRooms) { tab, position ->
                            tab.text = ""
                        }.attach()
                    }
                    Log.d(TAG, "Images présentation : " + allImages.joinToString())
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
                var i = 0
                property.proximities?.forEach { proximity ->
                    var root: LinearLayout = layoutInflater.inflate(R.layout.item_proximity, layoutProximity, false) as LinearLayout
                    var imageView = root.findViewById<ImageView>(R.id.iv_item_proximity)
                    imageView.setImageResource(this.resources.getIdentifier(proximity.icon, "drawable", activity?.packageName))
                    var textView = root.findViewById<TextView>(R.id.tv_item_proximity)
                    textView.text = activity?.getString(this.resources.getIdentifier(proximity.refLegend, "string", activity?.packageName))

                    layoutProximity.addView(root)
                }

                val fragmentManager: FragmentManager = childFragmentManager
                val bundle = Bundle()
                bundle.putString("adress", property.adress)

                fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.map, MapsFragment::class.java, bundle)
                    .commit()

                // Contact infos
                if (property.dateSold == null) binding.tvContactInfo.text =
                    this.getString(R.string.contact_info, property.agent.name, Utils.formatDateDayBefore(property.dateStartSell))
                else binding.tvContactInfo.text = this.getString(R.string.contact_info_sold, property.agent.name, Utils.formatDateDayBefore(property.dateSold))

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


}