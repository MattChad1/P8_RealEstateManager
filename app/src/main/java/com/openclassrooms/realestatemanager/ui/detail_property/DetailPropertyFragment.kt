package com.openclassrooms.realestatemanager.ui.detail_property

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentDetailPropertyBinding
import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.datas.model.Property
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class DetailPropertyFragment : Fragment() {

    var TAG = "MyLog DetailProperty"
    val allImages = mutableListOf<ImageRoom>()
    lateinit var binding: FragmentDetailPropertyBinding

lateinit var property: Property

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailPropertyBinding.inflate(inflater, container, false)

        property = Json.decodeFromString<Property>(arguments!!.getString("property", ""))

        property.photos?.let {
            allImages.addAll(property.photos!!)
            val imagePropertyAdapter = ImagePropertyAdapter(requireActivity() as AppCompatActivity, allImages)

            binding.viewpagerRooms.adapter = imagePropertyAdapter
            if (allImages.size <= 1) binding.tabLayout.visibility= View.GONE
            else {
                //tabLayout.width= allImages.size * 40 problème val cannot be reassigned
                TabLayoutMediator(binding.tabLayout, binding.viewpagerRooms) { tab, position ->
                    tab.text = ""
                }.attach()
            }
            Log.d(TAG, "Images présentation : "+allImages.joinToString())
        }

        binding.tvDetailDescription.text = property.description

        // Inflate the layout for this fragment
        return binding.root
    }




}