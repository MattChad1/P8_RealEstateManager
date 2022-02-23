package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.utils.Utils

class OldMainActivity : AppCompatActivity() {
    private lateinit var textViewMain: TextView
    private lateinit var textViewQuantity: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.old_activity_main)

        // Bug 1 : Correction de l'id pour ce TextView
        textViewMain = findViewById(R.id.activity_main_activity_text_view_main)
        textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity)
        configureTextViewMain()
        configureTextViewQuantity()
    }

    private fun configureTextViewMain() {
        textViewMain.textSize = 15f
        textViewMain.text = "Le premier bien immobilier enregistré vaut "
    }

    private fun configureTextViewQuantity() {
        val quantity = Utils.convertDollarToEuro(100)
        textViewQuantity.textSize = 20f
        // Bug 2 : setText avec int. Ajout de String.valueOf
        textViewQuantity.text = quantity.toString()
    }
}