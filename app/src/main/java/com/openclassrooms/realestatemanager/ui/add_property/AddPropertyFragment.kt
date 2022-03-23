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

    private lateinit var binding: FragmentAddPropertyBinding
    private lateinit var navController: NavController
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
    ): View {
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
            val adapter = CustomDropDownAdapter(ctx, agents)
            spinnerAgents.adapter = adapter
        }


        /* 2 DatePickerDialog */
        binding.editDateStartSaleInput.setOnClickListener {
            val dpd = OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                binding.editDateStartSaleInput.setText(
                    getString(R.string.date_for_datePicker, dayOfMonth, monthOfYear + 1, year)
                )
            }
            val dateStartSaleDatePicker = Calendar.getInstance()
            if (!binding.editDateStartSaleInput.text.isNullOrEmpty()) {
                val dateSplit = binding.editDateStartSaleInput.text!!.split("/")
                dateStartSaleDatePicker[Calendar.YEAR] = dateSplit[2].toInt()
                dateStartSaleDatePicker[Calendar.MONTH] = dateSplit[1].toInt() - 1
                dateStartSaleDatePicker[Calendar.DAY_OF_MONTH] = dateSplit[0].toInt()
            }

            val d = DatePickerDialog(
                ctx,
                dpd,
                dateStartSaleDatePicker[Calendar.YEAR],
                dateStartSaleDatePicker[Calendar.MONTH],
                dateStartSaleDatePicker[Calendar.DAY_OF_MONTH]
            )
            d.show()
        }

        binding.editDateSoldInput.setOnClickListener {
            val dpd = OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                binding.editDateSoldInput.setText(
                    getString(R.string.date_for_datePicker, dayOfMonth, monthOfYear + 1, year)
                )
            }
            val dateSoldDatePicker = Calendar.getInstance()
            if (!binding.editDateSoldInput.text.isNullOrEmpty()) {
                val dateSplit = binding.editDateSoldInput.text!!.split("/")
                dateSoldDatePicker[Calendar.YEAR] = dateSplit[2].toInt()
                dateSoldDatePicker[Calendar.MONTH] = dateSplit[1].toInt() - 1
                dateSoldDatePicker[Calendar.DAY_OF_MONTH] = dateSplit[0].toInt()
            }

            val d = DatePickerDialog(
                ctx,
                dpd,
                dateSoldDatePicker[Calendar.YEAR],
                dateSoldDatePicker[Calendar.MONTH],
                dateSoldDatePicker[Calendar.DAY_OF_MONTH]
            )
            d.show()
        }


        // To deal with error messages
        viewModel.validAdress.observe(ctx) { msg ->
            binding.editAdress.error = msg
            binding.editAdress.requestFocus()
        }
        viewModel.validPrice.observe(ctx) { msg -> binding.editPrice.error = msg }
        viewModel.validImage.observe(ctx) { msg -> binding.tvErreurPhotos.text = msg }
        viewModel.validDateStartSell.observe(ctx) { msg -> binding.editDateStartSale.error = msg }


        // To add images to the form
        internalStoragePhotoAdapter = InternalStoragePhotoAdapter {
            val isDeletionSuccessful = deletePhotoFromInternalStorage(it.name)
            if (isDeletionSuccessful) {
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
                builder.setTitle(getString(R.string.title_which_room))
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
            viewModel.getPropertyById(viewModel.idEdit).observe(ctx) { property ->
                binding.editAdressInput.setText(property?.adress)
                property?.description?.let { binding.editDescriptionInput.setText(it) }
                property?.bathrooms?.let { binding.editNumBathroomsInput.setText(it.toString()) }
                property?.bedrooms?.let { binding.editNumBedroomsInput.setText(it.toString()) }
                property?.rooms?.let { binding.editNumRoomsInput.setText(it.toString()) }
                property?.price?.let { binding.editPriceInput.setText(it.toString()) }
                property?.squareFeet?.let { binding.editSurfaceInput.setText(it.toString()) }
                property?.dateStartSell?.let { binding.editDateStartSaleInput.setText(it) }
                spinnerTypes.setSelection(viewModel.allTypes.value!!.indexOf(property?.type!!))

                for (c in proximityCheckboxes) {
                    if (property.proximities?.contains(c.tag) == true) c.isChecked = true
                }

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

                val destination = AddPropertyFragmentDirections.actionAddFragmentBackToMain()
                viewModel.formFinished.observe(ctx) {
                    if (it) navController.navigate(destination)
                    Toast.makeText(ctx, R.string.add_property_conf, Toast.LENGTH_LONG).show()
                }
            }
        }
        return binding.root
    }




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