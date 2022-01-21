package com.openclassrooms.realestatemanager.ui.add_property

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import java.util.ArrayList

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


        viewModel.allTypes.observe(this) { types ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
            spinner.adapter = adapter
        }

        binding.btnSubmit.setOnClickListener {

            viewModel.addNewProperty(
                spinner.selectedItem as TypeOfProperty,
                null,
                binding.editPriceInput.getInput()?.toDouble(),
                binding.editSurfaceInput.getInput()?.toDouble(),
                binding.editNumRoomsInput.getInput()?.toInt(),
                binding.editNumBedroomsInput.getInput()?.toInt(),
                binding.editNumBathroomsInput.getInput()?.toInt(),
                binding.editDescriptionInput.getInput(),
                null,
                binding.editAdressInput.getInput(),
                true
            )

        }


    }

    fun TextInputEditText.getInput(): String? = if (this.text.isNullOrEmpty()) null else this.text.toString()


}