package com.openclassrooms.realestatemanager.ui.add_property

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding
import com.openclassrooms.realestatemanager.datas.model.Agent
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.utils.PhotoUtils.Companion.deletePhotoFromInternalStorage
import com.openclassrooms.realestatemanager.utils.PhotoUtils.Companion.savePhotoToInternalStorage
import com.openclassrooms.realestatemanager.utils.Utils.getInput
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.*


class AddPropertyFragment : Fragment() {

    var testAgents = listOf<Agent>()

    lateinit var binding: FragmentAddPropertyBinding
    lateinit var navController: NavController
    private lateinit var internalStoragePhotoAdapter: InternalStoragePhotoAdapter

    private var readPermissionGranted = false
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    private val viewModel: AddPropertyViewModel by viewModels {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.navigationRepository)
    }

    var proximityCheckboxes: MutableList<CheckBox> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        binding = FragmentAddPropertyBinding.inflate(inflater, container, false)
        navController = findNavController()
        if (requireActivity().resources.getBoolean(R.bool.isTablet)) {
            requireActivity().findViewById<FragmentContainerView>(R.id.fragment_left_column).visibility = View.GONE
        }


        val ctx = requireActivity()
        viewModel.idEdit = requireArguments().getInt("idProperty")


        // Permissions to use internal storage (read only)
        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted

            if (!readPermissionGranted) {
                Toast.makeText(ctx, "Can't read files without permission.", Toast.LENGTH_LONG).show()
            }
        }
        updateOrRequestPermissions()

        // Checkbox for proximities
        lifecycleScope.launch {
            val proximities = viewModel.getAllProximities()
            val layout = binding.layoutForProximities

            proximities.forEach { p ->
                val checkbox = CheckBox(ctx)
                checkbox.text = getString(ctx.resources.getIdentifier(p.refLegend, "string", ctx.packageName))
                checkbox.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                checkbox.tag = p.idProximity
                proximityCheckboxes.add(checkbox)
                layout.addView(checkbox)
            }

        }


        // Add datas to spinners
        val spinnerTypes: Spinner = binding.formTypeProperty
        viewModel.allTypes.observe(ctx) { types ->
            val adapter = CustomDropDownAdapter(ctx, types)
            spinnerTypes.adapter = adapter
        }

        val spinnerAgents: Spinner = binding.spinnerAgents
        viewModel.allAgents.observe(ctx) { agents ->
            testAgents = agents
            val adapter = CustomDropDownAdapter(ctx, agents)
            spinnerAgents.adapter = adapter
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
                ctx,
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
                ctx,
                dpd,
                currentDate[Calendar.YEAR],
                currentDate[Calendar.MONTH],
                currentDate[Calendar.DAY_OF_MONTH]
            )
            d.show()
        }


        // To deal with error messages
        viewModel.validAdress.observe(ctx) { msg -> binding.editAdress.error = if (msg != null) msg else null }
        viewModel.validPrice.observe(ctx) { msg -> binding.editPrice.error = if (msg != null) msg else null }
        viewModel.validImage.observe(ctx) { msg -> binding.tvErreurPhotos.text = msg }
        viewModel.validDateStartSell.observe(ctx) { msg -> binding.editDateStartSale.error = msg }


        // To add images to the form
        internalStoragePhotoAdapter = InternalStoragePhotoAdapter {
            val isDeletionSuccessful = deletePhotoFromInternalStorage(it.name)
            if (isDeletionSuccessful) {
//                loadPhotosFromInternalStorageIntoRecyclerView()
                Toast.makeText(ctx, "Photo successfully deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(ctx, "Failed to delete photo", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.imagesPrevLiveData.observe(ctx) { images ->
            internalStoragePhotoAdapter.submitList(images)
            internalStoragePhotoAdapter.notifyDataSetChanged()
        }
        setupInternalStorageRecyclerView()


        val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                val builder = AlertDialog.Builder(ctx)
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
                            Toast.makeText(ctx, "Photo saved successfully", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(ctx, "Failed to save photo", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.show()
            }
        }

        val openGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { it ->
                val builder = AlertDialog.Builder(ctx)
                val mView: View = layoutInflater.inflate(R.layout.dialog_name_file, null)
                builder.setTitle("Which room is it?")
                builder.setView(mView)
                builder.setPositiveButton(
                    "OK"
                ) { dialog, id ->
                    val legend = mView.findViewById<TextInputEditText>(R.id.edit_name_file_input).getInput()
                    if (!legend.isNullOrEmpty()) {
                        val nameFile = viewModel.maxId.toString() + "-" + UUID.randomUUID().toString()
                        val inputStream: InputStream? = ctx.contentResolver.openInputStream(it)
                        inputStream?.copyTo(MyApplication.instance.openFileOutput("$nameFile.jpg", MODE_PRIVATE))
                        viewModel.addPhoto(nameFile, MediaStore.Images.Media.getBitmap(ctx.contentResolver, it), legend)
//                        loadPhotosFromInternalStorageIntoRecyclerView()
                        Toast.makeText(ctx, "Photo saved successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(ctx, "Failed to save photo", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.show()
            }
        }

        binding.btnTakePhoto.setOnClickListener { takePhoto.launch() }
        binding.btnTakePhotoGallery.setOnClickListener { openGallery.launch("image/*") }


        // To get datas if user wants to edit an add
        if (viewModel.idEdit != 0) {
            viewModel.getPropertyById(viewModel.idEdit.toInt()).observe(ctx) { property ->
                binding.editAdressInput.setText(property?.adress)
                binding.editDescriptionInput.setText(property?.description)
                property?.bathrooms?.let { binding.editNumBathroomsInput.setText(it.toString()) }
                property?.bedrooms?.let { binding.editNumBedroomsInput.setText(it.toString()) }
                property?.rooms?.let { binding.editNumRoomsInput.setText(it.toString()) }
                property?.price?.let { binding.editPriceInput.setText(it.toString()) }
                binding.editSurfaceInput.setText(property?.squareFeet.toString())
                binding.editDateStartSaleInput.setText(property?.dateStartSell)
                spinnerTypes.setSelection(viewModel.allTypes.value!!.indexOf(property?.type!!))

                for (c in proximityCheckboxes) {
                    if (property.proximities?.contains(c.tag) == true) c.isChecked = true
                }
//                loadPhotosFromInternalStorageIntoRecyclerView(property.id)


                // In case the sale is done, set visibility of the fields where to put the date of the sale
                binding.titlePropertySold.visibility = View.VISIBLE
                binding.editDateSold.visibility = View.VISIBLE
            }
        }


        // if form is submited
        binding.btnSubmit.setOnClickListener {

            val proximitiesSelected: MutableList<Int> = mutableListOf()
            proximityCheckboxes.forEach {
                if (it.isChecked) proximitiesSelected.add(it.tag as Int)
            }


            viewModel.addNewProperty(
                spinnerTypes.selectedItem as TypeOfProperty,
                spinnerAgents.selectedItem as Agent,
                binding.editPriceInput.getInput()?.toLong(),
                binding.editSurfaceInput.getInput()?.toDouble(),
                binding.editNumRoomsInput.getInput()?.toInt(),
                binding.editNumBedroomsInput.getInput()?.toInt(),
                binding.editNumBathroomsInput.getInput()?.toInt(),
                binding.editDescriptionInput.getInput(),
                binding.editAdressInput.getInput(),
                proximitiesSelected,
                binding.editDateStartSaleInput.getInput(),
                binding.editDateSoldInput.getInput()
            )

            if (viewModel.validAdress.value == null && viewModel.validImage.value == null && viewModel.validPrice.value == null) {
                Toast.makeText(ctx, R.string.add_property_conf, Toast.LENGTH_LONG).show()

                val destination = AddPropertyFragmentDirections.actionAddFragmentBackToMain()
                viewModel.formFinished.observe(ctx) {
                    if (it) navController.navigate(destination)
                }

            }
        }
        return binding.root
    }


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
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            requireActivity(),
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