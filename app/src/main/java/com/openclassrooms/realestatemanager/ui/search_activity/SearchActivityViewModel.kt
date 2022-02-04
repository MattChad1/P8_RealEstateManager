package com.openclassrooms.realestatemanager.ui.search_activity

import androidx.lifecycle.ViewModel

class SearchActivityViewModel: ViewModel() {

    fun checkFormRange (value1: Int, value2: Int): String? {
        if (value1 > value2) return "Les valeurs doivent être inversées"
        else return null
    }



}