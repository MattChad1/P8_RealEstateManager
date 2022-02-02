package com.openclassrooms.realestatemanager.ui.add_property

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.MainActivity
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.utils.PhotoUtils.Companion.deletePhotoFromInternalStorage
import com.openclassrooms.realestatemanager.utils.PhotoUtils.Companion.loadPhotosFromInternalStorage
import com.openclassrooms.realestatemanager.utils.PhotoUtils.Companion.savePhotoToInternalStorage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.*


class AddPropertyActivity : AppCompatActivity() {

    companion object {
        val EDIT_ID = "EDIT_ID"
    }

    lateinit var binding: ActivityAddPropertyBinding
    private lateinit var internalStoragePhotoAdapter: InternalStoragePhotoAdapter

    private var readPermissionGranted = false
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    private val viewModel: AddPropertyViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.typeOfPropertyRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idEdit = intent.getIntExtra(EDIT_ID, 0)



        val toolbar = binding.topAppBar
        toolbar.title = "Add new property"
        toolbar.menu.removeItem(0)
        toolbar.menu.removeItem(1)
        toolbar.menu.removeItem(2)

        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted

            if (!readPermissionGranted) {
                Toast.makeText(this, "Can't read files without permission.", Toast.LENGTH_LONG).show()
            }
        }
        updateOrRequestPermissions()


        val spinner: Spinner = binding.formTypeProperty

        viewModel.allTypes.observe(this) { types ->
            val adapter = CustomDropDownAdapter(this, types)
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

        viewModel.validAdress.observe(this) { msg ->
            binding.editAdress.error = if (msg != null) msg else null
        }

        viewModel.validPrice.observe(this) { msg ->
            binding.editPrice.error = if (msg != null) msg else null
        }

        viewModel.validImage.observe(this) { msg -> binding.tvErreurPhotos.text = msg }

        internalStoragePhotoAdapter = InternalStoragePhotoAdapter {
            val isDeletionSuccessful = deletePhotoFromInternalStorage(it.name)
            if (isDeletionSuccessful) {
                //loadPhotosFromInternalStorageIntoRecyclerView()
                Toast.makeText(this, "Photo successfully deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete photo", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.imagesPrevLiveData.observe(this) { images ->
            internalStoragePhotoAdapter.submitList(images)

            if (viewModel.validAdress==null && viewModel.validImage==null && viewModel.validPrice==null) {
                Toast.makeText(this, R.string.add_property_conf, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }

        }

        setupInternalStorageRecyclerView()

        val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                val builder = AlertDialog.Builder(this)
                val mView: View = layoutInflater.inflate(R.layout.dialog_name_file, null)
                builder.setTitle("Which room is it?")
                builder.setView(mView)
                builder.setPositiveButton(
                    "OK"
                ) { dialog, id ->
                    val legend = mView.findViewById<TextInputEditText>(R.id.edit_name_file_input).getInput()
                    if (!legend.isNullOrEmpty()) {
                        val nameFile = viewModel.maxId.toString() + "-" + UUID.randomUUID().toString()
                        val isSavedSuccessfully = savePhotoToInternalStorage(nameFile, it)

                        if (isSavedSuccessfully) {
                            viewModel.addPhoto(nameFile, it, legend)
                            //loadPhotosFromInternalStorageIntoRecyclerView()
                            Toast.makeText(this, "Photo saved successfully", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.show()
            }
        }

        val openGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { it ->

                val builder = AlertDialog.Builder(this)
                val mView: View = layoutInflater.inflate(R.layout.dialog_name_file, null)
                builder.setTitle("Which room is it?")
                builder.setView(mView)
                builder.setPositiveButton(
                    "OK"
                ) { dialog, id ->
                    val legend = mView.findViewById<TextInputEditText>(R.id.edit_name_file_input).getInput()
                    if (!legend.isNullOrEmpty()) {
                        val nameFile = viewModel.maxId.toString() + "-" + UUID.randomUUID().toString()
                        val inputStream: InputStream? = contentResolver.openInputStream(it)
                        inputStream?.copyTo(MyApplication.instance.openFileOutput("$nameFile.jpg", MODE_PRIVATE))
                        viewModel.addPhoto(nameFile, MediaStore.Images.Media.getBitmap(this.getContentResolver(), it), legend)
                        //loadPhotosFromInternalStorageIntoRecyclerView()
                        Toast.makeText(this, "Photo saved successfully", Toast.LENGTH_SHORT).show()
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

        binding.btnTakePhotoGallery.setOnClickListener { openGallery.launch("image/*") }


        if (idEdit!=0) {
            viewModel.getPropertyById(idEdit.toInt()).observe(this) {property ->
                binding.editAdressInput.setText(property?.adress)
                binding.editDescriptionInput.setText(property?.description)
                property?.bathrooms?.let { binding.editNumBathroomsInput.setText(it.toString()) }
                property?.bedrooms?.let { binding.editNumBedroomsInput.setText(it.toString()) }
                property?.rooms?.let { binding.editNumRoomsInput.setText(it.toString()) }
                property?.price?.let { binding.editPriceInput.setText(it.toString()) }
                binding.editSurfaceInput.setText(property?.squareFeet.toString())
                spinner.setSelection(property?.type!!.idType)
            }
        }
    }

    private fun TextInputEditText.getInput(): String? = if (this.text.isNullOrEmpty()) null else this.text.toString()


    private fun loadPhotosFromInternalStorageIntoRecyclerView() {
        lifecycleScope.launch {
            val photos = loadPhotosFromInternalStorage("${viewModel.maxId}-")
            internalStoragePhotoAdapter.submitList(photos)
        }
    }


    private fun setupInternalStorageRecyclerView() = binding.rvPrivatePhotos.apply {
        adapter = internalStoragePhotoAdapter
        layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
    }

    private fun updateOrRequestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
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