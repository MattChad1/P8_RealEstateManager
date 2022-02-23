package com.openclassrooms.realestatemanager.ui.list_properties

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.ItemClickListener
import com.openclassrooms.realestatemanager.utils.Utils
import java.io.File
import java.io.IOException

class PropertiesAdapter(
    private val context: Context,
    private val properties: MutableList<PropertyViewStateItem>,
    private val clickListener: ItemClickListener
) :
    RecyclerView.Adapter<PropertiesAdapter.ViewHolder>() {

    var itemSelected: Int? = null
    var viewSelected: View? = null


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val type: TextView = view.findViewById(R.id.item_property_tv_type)
        val neighborhood: TextView = view.findViewById(R.id.item_property_tv_neighborhood)
        val price: TextView = view.findViewById(R.id.item_property_tv_price)
        val image: ImageView = view.findViewById(R.id.item_property_image)
        val tvSold: TextView = view.findViewById(R.id.item_property_tv_sold)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_property, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from the dataset at this position and replace the
        // contents of the view with that element
//        if (properties[position].id == itemSelected) {
//            viewHolder.itemView.setBackgroundColor(
//                ContextCompat.getColor(
//                    context,
//                    R.color.colorAccent
//                )
//            )
//        } else viewHolder.itemView.setBackgroundColor(
//            ContextCompat.getColor(
//                context,
//                R.color.white
//            )
//        )
        if (MyApplication.instance.resources.getBoolean(R.bool.isTablet)) {
            if (properties[position].selected == true) highlightItem(viewHolder.itemView)
            else noSelectItem(viewHolder.itemView)
        }

        try {
            viewHolder.image.setImageURI(Uri.fromFile(File(MyApplication.instance.filesDir, properties[position].photo.nameFile + ".jpg")))
        }
        catch (e: IOException) {
            e.printStackTrace()

        }
        viewHolder.type.text = properties[position].type
//        viewHolder.neighborhood.text = properties[position].description
        //TODO : A changer pour le vrai quartier
        viewHolder.neighborhood.text = "Manhattan"
        viewHolder.price.text = Utils.formatPrice(properties[position].price)
        if (properties[position].dateSold != null) {
            viewHolder.tvSold.visibility = View.VISIBLE
        }
        viewHolder.itemView.setOnClickListener {
            clickListener.onItemAdapterClickListener(position)
//            noSelectItem(viewSelected)
//            viewSelected = viewHolder.itemView
//            highlightItem(viewSelected)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = properties.size

    fun highlightItem(v: View?) {
        v?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
        v?.findViewById<TextView>(R.id.item_property_tv_price)?.setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    fun noSelectItem(v: View?) {
        v?.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        v?.findViewById<TextView>(R.id.item_property_tv_price)?.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
    }


}