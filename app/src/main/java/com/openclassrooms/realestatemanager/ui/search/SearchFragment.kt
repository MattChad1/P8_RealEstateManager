package com.openclassrooms.realestatemanager.ui.search

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding
import com.openclassrooms.realestatemanager.datas.model.Filter
import com.openclassrooms.realestatemanager.ui.list_properties.ListPropertiesFragment
import com.openclassrooms.realestatemanager.utils.Utils.getInput
import kotlinx.coroutines.launch
import java.util.*


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.filterSearchRepository)
    }
    var filter = Filter()
    var proximityCheckboxes = mutableListOf<CheckBox>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(layoutInflater)


        binding.btnPrice.setOnClickListener { displayAlertRange(SearchViewModel.PRICE, filter.price) }
        binding.btnRooms.setOnClickListener { displayAlertRange(SearchViewModel.NUMROOMS, filter.numRooms) }
        binding.btnBedrooms.setOnClickListener { displayAlertRange(SearchViewModel.NUMBEDROOMS, filter.numBedrooms) }
        binding.btnBathrooms.setOnClickListener { displayAlertRange(SearchViewModel.NUMBATHROOMS, filter.numBathrooms) }
        binding.btnSurface.setOnClickListener { displayAlertRange(SearchViewModel.SURFACE, filter.surface) }
        binding.btnDateStartSale.setOnClickListener { displayAlertCalendar(SearchViewModel.DATE_START_SALE, filter.dateStartSale) }
        binding.btnDateStartSale.setOnClickListener { displayAlertCalendar(SearchViewModel.DATE_END_SALE, filter.dateSoldMax) }
        binding.btnProximity.setOnClickListener { displayAlertCheckbox(filter.proximity) }

        binding.tvLinkToListFragment.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val newFragment = ListPropertiesFragment()
            transaction?.replace(R.id.main_fragment, newFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }

        viewModel.filterLiveData.observe(viewLifecycleOwner) {
            setBtnColor(it)
        }

        viewModel.mediatorLiveData.observe(viewLifecycleOwner) { num ->
            viewModel.filterLiveData.value?.let{filter = it}
            if (filter != Filter()) {
                binding.tvNumMatch.visibility = View.VISIBLE
                if (!resources.getBoolean(R.bool.isTablet) && num!=0) binding.tvLinkToListFragment.visibility = View.VISIBLE
                else binding.tvLinkToListFragment.visibility = View.GONE
            }
            binding.tvNumMatch.text = getString(R.string.number_match, num)
        }

        return binding.root
    }

    fun displayAlertCalendar(field: String, value: String?) {
        val dpd = DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            viewModel.updateFilter(field, "$year-$monthOfYear-$dayOfMonth", null)
        }
        var dateToShow = Calendar.getInstance()
        if (value != null) {
            var dateTab = value.toString().split("-")
            dateToShow.set(dateTab[0].toInt(), dateTab[1].toInt(), dateTab[2].toInt())
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

    fun displayAlertRange(field: String, range: Pair<Any?, Any?>, dates: Boolean = false) {
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = layoutInflater.inflate(R.layout.form_range, null)
        val inputLow: TextInputEditText = view.findViewById(R.id.edit_low_range_input)
        val inputHigh: TextInputEditText = view.findViewById(R.id.edit_high_range_input)

        range.first?.let { inputLow.setText(range.first.toString()) }
        range.second?.let { inputHigh.setText(range.second.toString()) }

        with(builder)
        {
            setTitle(field)
            setView(view)
            setPositiveButton(R.string.apply_button) { dialog, which ->
                viewModel.updateFilter(field, inputLow.getInput(), inputHigh.getInput())
                Toast.makeText(
                    requireActivity(),
                    "Filter added", Toast.LENGTH_SHORT
                ).show()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            }
            show()

        }
    }

    fun displayAlertCheckbox(selected: List<Int>) {
        lifecycleScope.launch() {
            val builder = AlertDialog.Builder(requireActivity())
            val layout = LinearLayout(requireActivity(), null, 0)
            val proximities = viewModel.getAllProximities()

            proximities.forEach { p ->
                val checkbox = CheckBox(requireActivity())
                checkbox.text = getString(requireActivity().resources.getIdentifier(p.refLegend, "string", requireActivity().packageName))
                checkbox.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                if (p.idProximity in selected) checkbox.isChecked = true
                checkbox.tag = p.idProximity
                proximityCheckboxes.add(checkbox)
                layout.addView(checkbox)
            }

            with(builder)
            {
                setTitle("What do you want nearby")
                setView(layout)
                setPositiveButton(R.string.apply_button) { dialog, which ->
                    val proximitiesSelected: MutableList<Int> = mutableListOf()
                    proximityCheckboxes.forEach {
                        if (it.isChecked) proximitiesSelected.add(it.tag as Int)
                    }
                    viewModel.updateFilterProximity(proximitiesSelected)
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
    }

    fun setBtnColor(filter: Filter) {
        val colorActive = ContextCompat.getColor(requireActivity(), R.color.colorPrimaryDark)
        val colorNotActive = ContextCompat.getColor(requireActivity(), R.color.colorSecondaryDark)

        val btnListTwoValues = listOf<Button>(binding.btnPrice, binding.btnRooms, binding.btnBedrooms, binding.btnBathrooms, binding.btnSurface)
        val values2 = listOf<Pair<Number?, Number?>>(filter.price, filter.numRooms, filter.numBedrooms, filter.numBathrooms, filter.surface)

        for (i in 0 until values2.size) {
            if (values2[i].first == null && values2[i].second == null) btnListTwoValues[i].setBackgroundColor(colorNotActive)
            else btnListTwoValues[i].setBackgroundColor(colorActive)
        }

        val btnListOneValue = listOf<Button>(binding.btnDateStartSale, binding.btnDateEndSale)
        val values1 = listOf<String?>(filter.dateStartSale, filter.dateSoldMax)
        for (i in 0 until values1.size) {
            if (values1[i] == null) btnListOneValue[i].setBackgroundColor(colorNotActive)
            else btnListOneValue[i].setBackgroundColor(colorActive)
        }

        if (filter.proximity.isEmpty()) binding.btnProximity.setBackgroundColor(colorNotActive)
        else binding.btnProximity.setBackgroundColor(colorActive)

    }

}