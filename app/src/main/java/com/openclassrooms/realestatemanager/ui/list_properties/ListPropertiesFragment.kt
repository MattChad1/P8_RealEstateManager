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
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentListPropertiesBinding
import com.openclassrooms.realestatemanager.ui.ItemClickListener
import com.openclassrooms.realestatemanager.ui.detail_property.DetailPropertyFragment

class ListPropertiesFragment : Fragment(), ItemClickListener {


    var properties: MutableList<PropertyViewStateItem> = mutableListOf()
    private val viewModel: ListPropertiesViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository,MyApplication.instance.typeOfPropertyRepository)
    }
    var itemSelected: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        var binding: FragmentListPropertiesBinding = FragmentListPropertiesBinding.inflate(inflater, container, false)


        val rv: RecyclerView = binding.rvListProperties
        rv.layoutManager = LinearLayoutManager(requireActivity())
        rv.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        val adapter = PropertiesAdapter(requireActivity(), properties, this)
        rv.adapter = adapter

        viewModel.allProperties.observe(this) { newProperties ->

            // Check if list is launched for the 1st time, then display details of the 1st item
//            if (properties.isEmpty() && newProperties!= null && newProperties.isNotEmpty()) {
//                newProperties[0]?.let {
//                    sendNewDetails (it)
//                }
//
//            }
            properties.clear()
            properties.addAll(newProperties)
                adapter.notifyDataSetChanged() }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onItemAdapterClickListener(position: Int) {
        sendNewDetails(properties[position].id)

    }

    fun sendNewDetails (id: Int) {

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        val newFragment = DetailPropertyFragment()
        val args = Bundle()
//        val json = Json.encodeToString(property)
        args.putInt("idProperty", id)
        newFragment.arguments = args

        if (resources.getBoolean(R.bool.isTablet)==false) transaction?.replace(R.id.main_fragment, newFragment)
        else transaction?.replace(R.id.second_fragment, newFragment)

        transaction?.disallowAddToBackStack()
        transaction?.commit()

    }


}