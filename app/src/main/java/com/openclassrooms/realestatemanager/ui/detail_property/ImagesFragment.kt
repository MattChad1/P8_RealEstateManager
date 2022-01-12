package com.openclassrooms.realestatemanager.ui.detail_property

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.databinding.FragmentImagesBinding


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
        setImageFromAssetsFile(requireContext(), imageFilePath, legendeImage)
    }
    /**
     * Gets the file from assets, converts it into a bitmap and sets it on the ImageView
     * @param context a Context instance
     * @param filePath relative path of the file
     */
    private fun setImageFromAssetsFile(context: Context, filePath: String?, legendeImage: String?) {

        if (filePath !=null) {
//            val assets = context.resources.assets
//            val srcImg = if (filePath.indexOf(".") > 0) filePath else filePath + ".jpg"
//            try {
//                val ims: InputStream = assets.open("images/" + srcImg)
//                binding.imageRoom.setImageDrawable(Drawable.createFromStream(ims, null))
//
//                if (legendeImage.isNullOrEmpty()) binding.legendeViewpager.visibility=View.GONE
//                else {
//                    binding.legendeViewpager.visibility=View.VISIBLE
//                    binding.legendeViewpager.text = legendeImage
//                }
//            } catch (ex: IOException) {
//                Log.d(TAG, "Erreur image : " + "images/" + srcImg)
//            }


            val resourceId: Int = resources.getIdentifier(
                filePath, "drawable",
                context.packageName
            )
            binding.imageRoom.setImageResource(resourceId)
            if (legendeImage.isNullOrEmpty()) binding.legendeViewpager.visibility=View.GONE
                else {
                    binding.legendeViewpager.visibility=View.VISIBLE
                    binding.legendeViewpager.text = legendeImage
                }
        }
    }
}