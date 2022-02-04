package com.openclassrooms.realestatemanager.ui.add_property

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        viewModel.idEdit = intent.getIntExtra(EDIT_ID, 0)


        val toolbar = binding.topAppBar
        toolbar.title = "Add new property"
        // TODO: code doesn't work to remove item from menu
        toolbar.menu.removeItem(0)
        toolbar.menu.removeItem(1)
        toolbar.menu.removeItem(2)


        // Permissions to use internal storage (read only)
        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted

            if (!readPermissionGranted) {
                Toast.makeText(this, "Can't read files without permission.", Toast.LENGTH_LONG).show()
            }
        }
        updateOrRequestPermissions()

        /*DatePickerDialog */





        // Spinner type of properties
        val spinner: Spinner = binding.formTypeProperty
        viewModel.allTypes.observe(this) { types ->
            val adapter = CustomDropDownAdapter(this, types)
            spinner.adapter = adapter
        }


//        loadPhotosFromInternalStorageIntoRecyclerView()

        /* 2 DatePickerDialog */
        val currentDate = Calendar.getInstance()
        binding.editDateStartSaleInput.setOnClickListener {
            val dpd = OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                binding.editDateStartSaleInput.setText(
                    getString(R.string.date_for_datePicker, dayOfMonth, monthOfYear + 1, year)
                )
            }
            val d = DatePickerDialog(
                this,
                dpd,
                currentDate[Calendar.YEAR],
                currentDate[Calendar.MONTH],
                currentDate[Calendar.DAY_OF_MONTH]
            )
            d.show()
        }

        binding.editDateSoldInput.setOnClickListener {
            val dpd = OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                binding.editDateSoldInput.setText(
                    getString(R.string.date_for_datePicker, dayOfMonth, monthOfYear + 1, year)
                )
            }

            val d = DatePickerDialog(
                this,
                dpd,
                currentDate[Calendar.YEAR],
                currentDate[Calendar.MONTH],
                currentDate[Calendar.DAY_OF_MONTH]
            )
            d.show()
        }


        // To deal with error messages
        viewModel.validAdress.observe(this) { msg -> binding.editAdress.error = if (msg != null) msg else null }
        viewModel.validPrice.observe(this) { msg -> binding.editPrice.error = if (msg != null) msg else null }
        viewModel.validImage.observe(this) { msg -> binding.tvErreurPhotos.text = msg }
        viewModel.validDateStartSell.observe(this) { msg -> binding.editDateStartSale.error = msg }


        // To add images to the form
        internalStoragePhotoAdapter = InternalStoragePhotoAdapter {
            val isDeletionSuccessful = deletePhotoFromInternalStorage(it.name)
            if (isDeletionSuccessful) {
//                loadPhotosFromInternalStorageIntoRecyclerView()
                Toast.makeText(this, "Photo successfully deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete photo", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.imagesPrevLiveData.observe(this) { images ->
            internalStoragePhotoAdapter.submitList(images)
            internalStoragePhotoAdapter.notifyDataSetChanged()
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
//                            loadPhotosFromInternalStorageIntoRecyclerView()
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
//                        loadPhotosFromInternalStorageIntoRecyclerView()
                        Toast.makeText(this, "Photo saved successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.show()
            }
        }

        binding.btnTakePhoto.setOnClickListener { takePhoto.launch() }
        binding.btnTakePhotoGallery.setOnClickListener { openGallery.launch("image/*") }


        // To get datas if user wants to edit an add
        if (viewModel.idEdit != 0) {
            viewModel.getPropertyById(viewModel.idEdit.toInt()).observe(this) { property ->
                binding.editAdressInput.setText(property?.adress)
                binding.editDescriptionInput.setText(property?.description)
                property?.bathrooms?.let { binding.editNumBathroomsInput.setText(it.toString()) }
                property?.bedrooms?.let { binding.editNumBedroomsInput.setText(it.toString()) }
                property?.rooms?.let { binding.editNumRoomsInput.setText(it.toString()) }
                property?.price?.let { binding.editPriceInput.setText(it.toString()) }
                binding.editSurfaceInput.setText(property?.squareFeet.toString())
                binding.editDateStartSaleInput.setText(property?.dateStartSell)
                spinner.setSelection(viewModel.allTypes.value!!.indexOf(property?.type!!))
//                loadPhotosFromInternalStorageIntoRecyclerView(property.id)



                // In case the sale is done, set visibility of the fields where to put the date of the sale
                binding.titlePropertySold.visibility = View.VISIBLE
                binding.editDateSold.visibility = View.VISIBLE
            }
        }


        // if form is submited
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
                binding.editAdressInput.getInput(),
                binding.editDateStartSaleInput.getInput(),
                binding.editDateSoldInput.getInput()
            )

            if (viewModel.validAdress.value == null && viewModel.validImage.value == null && viewModel.validPrice.value == null) {
                Toast.makeText(this, R.string.add_property_conf, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }


    private fun TextInputEditText.getInput(): String? = if (this.text.isNullOrEmpty()) null else this.text.toString()

    fun loadPhotosFromInternalStorageIntoRecyclerView() {
        lifecycleScope.launch {
            val photos = viewModel.imagesPrevLiveData


        }

    }

//    private fun loadPhotosFromInternalStorageIntoRecyclerView() {
//        lifecycleScope.launch {
//            val photos = viewModel.imagesPrevLiveData.value
////            var prefix = id ?: viewModel.getMaxId()
//            val photos = loadPhotosFromInternalStorage("${prefix}-")
//            viewModel.checkLiveDataPhotos(photos)
//        }
//    }

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