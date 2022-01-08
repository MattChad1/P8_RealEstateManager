package com.openclassrooms.realestatemanager.ui.list_properties

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.databinding.FragmentListPropertiesBinding
import com.openclassrooms.realestatemanager.datas.model.Property

class ListPropertiesFragment : Fragment() {


    var properties: MutableList<Property> = emptyList<Property>().toMutableList()
    private val viewModel: ListPropertiesViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        var binding: FragmentListPropertiesBinding = FragmentListPropertiesBinding.inflate(inflater, container, false)


        val rv: RecyclerView = binding.rvListProperties
        rv.layoutManager = LinearLayoutManager(requireActivity())
        rv.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        val adapter = PropertiesAdapter(requireActivity(), properties)
        rv.adapter = adapter

        viewModel.allProperties.observe(this) { newProperties ->
            // Update the cached copy of the words in the adapter.
            properties.let {
                properties.clear()
                if (newProperties != null) {
                    for (p in newProperties) {
                        if (p!=null) properties.add(p)
                    }
                }
                adapter.notifyDataSetChanged() }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }


}