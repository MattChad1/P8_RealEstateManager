package com.openclassrooms.realestatemanager.ui.list_properties

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.FragmentListPropertiesBinding
import com.openclassrooms.realestatemanager.ui.ItemClickListener
import com.openclassrooms.realestatemanager.ui.main_activity.MainActivity

class ListPropertiesFragment : Fragment(), ItemClickListener {


    var properties: MutableList<PropertyViewStateItem> = mutableListOf()

    private val viewModel: ListPropertiesViewModel by viewModels {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.navigationRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val binding: FragmentListPropertiesBinding = FragmentListPropertiesBinding.inflate(inflater, container, false)
        val rv: RecyclerView = binding.rvListProperties
        rv.layoutManager = LinearLayoutManager(requireActivity())
        rv.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        val adapter = PropertiesAdapter(requireActivity(), properties, this)
        rv.adapter = adapter

        viewModel.mediatorLiveData.observe(viewLifecycleOwner) { newProperties ->
            properties.clear()
            properties.addAll(newProperties)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }


    override fun onItemAdapterClickListener(position: Int) {
        sendNewDetails(properties[position].id)
        viewModel.changeSelection(properties[position].id)
    }



    fun sendNewDetails(id: Int) {
        val activity: MainActivity = activity as MainActivity
        if (resources.getBoolean(R.bool.isTablet) == false) {
            val sendData = ListPropertiesFragmentDirections.actionListPropertiesFragmentToDetailPropertyFragment(id)
            activity.navController.navigate(sendData)
        } else {
            activity.navController.navigate(R.id.detailPropertyFragment)

        }

    }


}