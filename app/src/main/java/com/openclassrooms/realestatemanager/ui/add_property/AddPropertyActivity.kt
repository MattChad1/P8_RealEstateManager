package com.openclassrooms.realestatemanager.ui.add_property

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding

class AddPropertyActivity : AppCompatActivity() {

lateinit var binding: ActivityAddPropertyBinding
    private val viewModel: AddPropertyViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.typeOfPropertyRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val spinner: Spinner = binding.formTypeProperty


            val valuesForSpinner = viewModel.getTypes()

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, valuesForSpinner)
            spinner.adapter = adapter


        binding.btnSubmit.setOnClickListener {






        }




    }
}