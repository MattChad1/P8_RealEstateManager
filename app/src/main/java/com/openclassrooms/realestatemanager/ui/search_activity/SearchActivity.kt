package com.openclassrooms.realestatemanager.ui.search_activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)

        binding.btnPrice.setOnClickListener { displayAlert() }








        setContentView(binding.root)
    }

    fun displayAlert() {
        val builder = AlertDialog.Builder(this)
        val view: View = layoutInflater.inflate(R.layout.form_range, null)

        with(builder)
        {
            setTitle("Your price range")
            setView(view)
            setPositiveButton(R.string.apply_button) { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    "OK", Toast.LENGTH_SHORT
                ).show()
            }
            setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    "cancel", Toast.LENGTH_SHORT
                ).show()
            }
            show()

        }
    }

}