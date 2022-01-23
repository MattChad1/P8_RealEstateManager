package com.openclassrooms.realestatemanager.ui.add_property

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.utils.PhotoUtils.Companion.deletePhotoFromInternalStorage
import com.openclassrooms.realestatemanager.utils.PhotoUtils.Companion.loadPhotosFromInternalStorage
import com.openclassrooms.realestatemanager.utils.PhotoUtils.Companion.savePhotoToInternalStorage
import kotlinx.coroutines.launch
import java.util.*


class AddPropertyActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddPropertyBinding
    private lateinit var internalStoragePhotoAdapter: InternalStoragePhotoAdapter

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
                binding.editPriceInput.getInput()?.toInt(),
                binding.editSurfaceInput.getInput()?.toDouble(),
                binding.editNumRoomsInput.getInput()?.toInt(),
                binding.editNumBedroomsInput.getInput()?.toInt(),
                binding.editNumBathroomsInput.getInput()?.toInt(),
                binding.editDescriptionInput.getInput(),
                null,
                binding.editAdressInput.getInput()
            )

        }

        viewModel.validAdress.observe(this) {msg ->
            binding.editAdress.error = if (msg!=null) msg else null
        }

        viewModel.validPrice.observe(this) {msg ->
            binding.editPrice.error = if (msg!=null) msg else null
        }

        internalStoragePhotoAdapter = InternalStoragePhotoAdapter {
            val isDeletionSuccessful = deletePhotoFromInternalStorage(it.name)
            if(isDeletionSuccessful) {
                loadPhotosFromInternalStorageIntoRecyclerView()
                Toast.makeText(this, "Photo successfully deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete photo", Toast.LENGTH_SHORT).show()
            }
        }

        setupInternalStorageRecyclerView()
        loadPhotosFromInternalStorageIntoRecyclerView()

        val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it!=null) {
                val builder = AlertDialog.Builder(this)
                val mView: View = layoutInflater.inflate(R.layout.dialog_name_file, null)
                builder.setTitle("Which room is it?")
                builder.setView(mView)
                builder.setPositiveButton("OK"
                ) { dialog, id ->
                    val legende = mView.findViewById<TextInputEditText>(R.id.edit_name_file_input).getInput()
                    if (!legende.isNullOrEmpty()) {
                    val nameFile = UUID.randomUUID().toString()
                    val isSavedSuccessfully = savePhotoToInternalStorage(nameFile, it)

                    if (isSavedSuccessfully) {
                        viewModel.addPhoto(nameFile, legende)
                        loadPhotosFromInternalStorageIntoRecyclerView()
                        Toast.makeText(this, "Photo saved successfully", Toast.LENGTH_SHORT).show()
                    }
                    } else {
                        Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.show()
                }
        }

        binding.btnTakePhoto.setOnClickListener {
            takePhoto.launch()
        }

    }

    fun TextInputEditText.getInput(): String? = if (this.text.isNullOrEmpty()) null else this.text.toString()

//    private fun createDialogNameFile() {
//        val builder = AlertDialog.Builder(this)
//        builder.setView(com.openclassrooms.realestatemanager.R.layout.dialog_name_file)
//        builder.setTitle("Test")
//        builder.setMessage("Message")
//        builder.setPositiveButton("OK"
//        ) { dialog, id ->
//
//            Toast.makeText(this@AddPropertyActivity, "Photo saved successfully", Toast.LENGTH_SHORT).show()
//        }
//        builder.show()
//    }

    private fun loadPhotosFromInternalStorageIntoRecyclerView() {
        lifecycleScope.launch {
            val photos = loadPhotosFromInternalStorage()
            internalStoragePhotoAdapter.submitList(photos)
        }
    }

    private fun setupInternalStorageRecyclerView() = binding.rvPrivatePhotos.apply {
        adapter = internalStoragePhotoAdapter
        layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
    }



}