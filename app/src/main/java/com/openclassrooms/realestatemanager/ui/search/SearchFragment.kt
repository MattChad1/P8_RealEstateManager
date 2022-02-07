package com.openclassrooms.realestatemanager.ui.search

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding
import com.openclassrooms.realestatemanager.datas.model.Filter
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.Utils.getInput
import java.util.*


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.filterSearchRepository)
    }
    lateinit var filter: Filter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(layoutInflater)

        viewModel.filterLiveData.observe(viewLifecycleOwner) { filter = it }



        binding.btnPrice.setOnClickListener { displayAlertRange(SearchViewModel.PRICE, filter.price) }
        binding.btnRooms.setOnClickListener { displayAlertRange(SearchViewModel.NUMROOMS, filter.numRooms) }
        binding.btnBedrooms.setOnClickListener { displayAlertRange(SearchViewModel.NUMBEDROOMS, filter.numBedrooms) }
        binding.btnBathrooms.setOnClickListener { displayAlertRange(SearchViewModel.NUMBATHROOMS, filter.numBathrooms) }
        binding.btnSurface.setOnClickListener { displayAlertRange(SearchViewModel.SURFACE, filter.surface) }
        binding.btnDateStartSale.setOnClickListener { displayAlertRange(SearchViewModel.DATE_START_SALE, filter.dateStartSale) }


        viewModel.filterLiveData.observe(viewLifecycleOwner) {
            setBtnColor(it)
        }

        viewModel.mediatorLiveData.observe(viewLifecycleOwner) { num -> binding.tvNumMatch.text = getString(R.string.number_match, num)
        }


        return binding.root
    }

    fun displayAlertRange(field: String, range: Pair<Any?, Any?>, dates: Boolean = false) {
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = if (!dates) layoutInflater.inflate(R.layout.form_range, null) else layoutInflater.inflate(R.layout.form_range_dates, null)
        val inputLow: TextInputEditText = view.findViewById(R.id.edit_low_range_input)
        val inputHigh: TextInputEditText = view.findViewById(R.id.edit_high_range_input)

        if (!dates) {
            range.first?.let { inputLow.setText(range.first.toString()) }
            range.second?.let { inputHigh.setText(range.second.toString()) }
        }

        else {
            var dateToShow = Calendar.getInstance()
            if (range.first != null) {
                var dateTab = range.first.toString().split("-")
                dateToShow.set(dateTab[0].toInt(), dateTab[1].toInt(), dateTab[2].toInt())
            }
                inputLow.setOnClickListener {
                    val dpd = DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        inputLow.setText(
                            getString(R.string.date_for_datePicker, dayOfMonth, monthOfYear + 1, year)
                        )
                    }
                    val d = DatePickerDialog(
                        requireActivity(),
                        dpd,
                        dateToShow[Calendar.YEAR],
                        dateToShow[Calendar.MONTH],
                        dateToShow[Calendar.DAY_OF_MONTH]
                    )
                    d.show()
                }


            range.second?.let { inputHigh.setText(Utils.formatDateDayBefore(range.second.toString())) }
        }

        with(builder)
        {
            setTitle("Your price range")
            setView(view)
            setPositiveButton(R.string.apply_button) { dialog, which ->
                viewModel.updateFilter(field, inputLow.getInput(), inputHigh.getInput())
                Toast.makeText(
                    requireActivity(),
                    "OK", Toast.LENGTH_SHORT
                ).show()
            }
            setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(
                    requireActivity(),
                    "cancel", Toast.LENGTH_SHORT
                ).show()
            }
            show()

        }
    }

    fun setBtnColor(filter: Filter) {
        val colorActive = ContextCompat.getColor(requireActivity(), R.color.colorPrimaryDark)
        val colorNotActive = ContextCompat.getColor(requireActivity(), R.color.colorSecondaryDark)

        val btnList = listOf<Button>(binding.btnPrice, binding.btnRooms, binding.btnBedrooms, binding.btnBathrooms, binding.btnSurface)
        val values = listOf<Pair<Number?, Number?>>(filter.price, filter.numRooms, filter.numBedrooms, filter.numBathrooms, filter.surface)

        for (i in 0 until values.size) {
            if (values[i].first==null && values[i].second==null) btnList[i].setBackgroundColor(colorNotActive)
            else btnList[i].setBackgroundColor(colorActive)

        }

    }

}