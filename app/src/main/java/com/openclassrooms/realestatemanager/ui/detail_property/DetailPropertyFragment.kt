package com.openclassrooms.realestatemanager.ui.detail_property

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.datas.model.Property
import com.openclassrooms.realestatemanager.ui.ItemClickListener
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class DetailPropertyFragment : Fragment() {

    var TAG = "MyLog DetailProperty"

lateinit var property: Property

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        property = Json.decodeFromString<Property>(arguments!!.getString("property", ""))
        Log.i(TAG, "onCreateView: " + property.id)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_property_, container, false)
    }




}