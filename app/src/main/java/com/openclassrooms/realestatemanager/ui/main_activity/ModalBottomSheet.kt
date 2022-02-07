package com.openclassrooms.realestatemanager.ui.main_activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ModalBottomSheetBinding

class ModalBottomSheet : BottomSheetDialogFragment() {


    private val viewModel: MainActivityViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.filterSearchRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding: ModalBottomSheetBinding = ModalBottomSheetBinding.inflate(inflater, container, false)

        binding.btnPrice.setOnClickListener { displayAlert() }


        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    fun displayAlert() {
        val builder = AlertDialog.Builder(MyApplication.instance)
        val view: View = layoutInflater.inflate(R.layout.form_range, null)

        with(builder)
        {
            setTitle("Your price range")
            setView(view)
            setPositiveButton(R.string.apply_button) { dialog, which ->
                Toast.makeText(
                    activity,
                    "OK", Toast.LENGTH_SHORT
                ).show()
            }
            setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(
                    activity,
                    "cancel", Toast.LENGTH_SHORT
                ).show()
            }
            show()

        }
    }
}
