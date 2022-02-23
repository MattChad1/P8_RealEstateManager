package com.openclassrooms.realestatemanager.ui.detail_property

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.databinding.FragmentImagesBinding
import java.io.File


class ImagesFragment : Fragment() {

    var TAG = "MyLog ImagesFragment"

    lateinit var binding: FragmentImagesBinding

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_SRC = "srcImage"
        const val ARG_LEGENDE = "legendeImage"

        fun getInstance(position: Int, srcImage: String, legendeImage: String?): Fragment {
            val vpFragment = ImagesFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            bundle.putString(ARG_SRC, srcImage)
            bundle.putString(ARG_LEGENDE, legendeImage)
            vpFragment.arguments = bundle
            return vpFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(ARG_POSITION)
        val imageFilePath = requireArguments().getString(ARG_SRC)
        val legendeImage = requireArguments().getString(ARG_LEGENDE)
        imageFilePath?.let { binding.imageRoom.setImageURI(Uri.fromFile(File(MyApplication.instance.filesDir, imageFilePath + ".jpg"))) }

        if (legendeImage.isNullOrEmpty()) binding.legendeViewpager.visibility = View.GONE
        else {
            binding.legendeViewpager.visibility = View.VISIBLE
            binding.legendeViewpager.text = legendeImage
        }
    }


}