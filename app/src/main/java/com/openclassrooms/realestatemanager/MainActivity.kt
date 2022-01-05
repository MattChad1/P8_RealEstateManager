package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit private var textViewMain: TextView;
    lateinit private var textViewQuantity: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bug 1 : Correction de l'id pour ce TextView
        textViewMain = findViewById(R.id.activity_main_activity_text_view_main)
        textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity)
        configureTextViewMain()
        configureTextViewQuantity()
    }

    private fun configureTextViewMain() {
        textViewMain.setTextSize(15f)
        textViewMain.setText("Le premier bien immobilier enregistré vaut ")
    }

    private fun configureTextViewQuantity() {
        val quantity = Utils.convertDollarToEuro(100)
        textViewQuantity.setTextSize(20f)
        // Bug 2 : setText avec int. Ajout de String.valueOf
        textViewQuantity.setText(quantity.toString())
    }
}