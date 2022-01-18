package com.openclassrooms.realestatemanager.ui.detail_property

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.CardDatasDetailsBinding
import com.openclassrooms.realestatemanager.databinding.FragmentDetailPropertyBinding
import com.openclassrooms.realestatemanager.datas.model.ImageRoom


class DetailPropertyFragment : Fragment() {

    var TAG = "MyLog DetailProperty"
    val allImages = mutableListOf<ImageRoom>()
    lateinit var binding: FragmentDetailPropertyBinding
    var property: DetailPropertyViewState? = null

    private val viewModel: DetailPropertyViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.typeOfPropertyRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailPropertyBinding.inflate(inflater, container, false)

        val idProperty = arguments!!.getLong("idProperty")

        viewModel.getPropertyById(idProperty).observe(this) { property ->
            if (property!=null) {
                property.photos?.let {
                    allImages.addAll(property.photos!!)
                    val imagePropertyAdapter = ImagePropertyAdapter(requireActivity() as AppCompatActivity, allImages)

                    binding.viewpagerRooms.adapter = imagePropertyAdapter
                    if (allImages.size <= 1) binding.tabLayout.visibility = View.GONE
                    else {
                        //tabLayout.width= allImages.size * 40 problème val cannot be reassigned
                        TabLayoutMediator(binding.tabLayout, binding.viewpagerRooms) { tab, position ->
                            tab.text = ""
                        }.attach()
                    }
                    Log.d(TAG, "Images présentation : " + allImages.joinToString())
                }

                binding.tvDetailDescription.text = property.description

                val cardviews = listOf<
                        CardDatasDetailsBinding>(
                    binding.cardviewSquareMeters,
                    binding.cardviewNumberRooms,
                    binding.cardviewNumberBathrooms,
                    binding.cardviewNumberBedrooms,
                    binding.cardviewLocation
                )
                val icons = listOf<Int>(
                    com.openclassrooms.realestatemanager.R.drawable.ic_baseline_square_foot_24,
                    com.openclassrooms.realestatemanager.R.drawable.ic_baseline_house_24,
                    com.openclassrooms.realestatemanager.R.drawable.ic_outline_bathroom_24,
                    com.openclassrooms.realestatemanager.R.drawable.ic_baseline_bed_24,
                    com.openclassrooms.realestatemanager.R.drawable.ic_baseline_location_on_24
                )
                val titles = listOf<Int>(
                    com.openclassrooms.realestatemanager.R.string.surface,
                    com.openclassrooms.realestatemanager.R.string.num_rooms,
                    com.openclassrooms.realestatemanager.R.string.num_bathrooms,
                    com.openclassrooms.realestatemanager.R.string.num_bedrooms,
                    com.openclassrooms.realestatemanager.R.string.location
                )
                val datas = listOf<String?>(
                    this.getString(com.openclassrooms.realestatemanager.R.string.surface_data, property.squareFeet),
                    property.rooms.toString(),
                    property.bathrooms.toString(),
                    property.bedrooms.toString(),
                    property.adress
                )

                for (i in 0..4) {
                    cardviews[i].iconDetail.setImageResource(icons[i])
                    cardviews[i].tvDetailTitle.text = getString(titles[i])
                    cardviews[i].tvDetailData.text = datas[i]

                }

                binding.testProximity.text = property.proximity.toString()

                val fragmentManager: FragmentManager = childFragmentManager
                val bundle = Bundle()
                bundle.putString("adress", property.adress)

                fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.map, MapsFragment::class.java, bundle)
                    .commit()
            }

        }

//        property = Json.decodeFromString<Property>(arguments!!.getString("property", ""))



        // Inflate the layout for this fragment
        return binding.root
    }




}