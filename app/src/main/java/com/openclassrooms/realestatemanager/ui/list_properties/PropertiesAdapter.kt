package com.openclassrooms.realestatemanager.ui.list_properties

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.datas.model.Property

class PropertiesAdapter(private val properties: MutableList<Property>) :
        RecyclerView.Adapter<PropertiesAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val type: TextView = view.findViewById(R.id.item_property_tv_type)
        val neighborhood: TextView = view.findViewById(R.id.item_property_tv_neighborhood)
        val price: TextView = view.findViewById(R.id.item_property_tv_price)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_property, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.type.text = properties[position].type?.str
        viewHolder.neighborhood.text = properties[position].description
        viewHolder.price.text = properties[position].price.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = properties.size

}