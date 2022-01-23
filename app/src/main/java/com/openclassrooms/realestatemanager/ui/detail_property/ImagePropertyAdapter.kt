package com.openclassrooms.realestatemanager.ui.detail_property

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.openclassrooms.realestatemanager.datas.model.ImageRoom

class ImagePropertyAdapter(activity: AppCompatActivity, val images: MutableList<ImageRoom>) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return images.size
    }

    override fun createFragment(position: Int): Fragment {
        return ImagesFragment.getInstance(position, images[position].nameFile, images[position].legende)
    }

}