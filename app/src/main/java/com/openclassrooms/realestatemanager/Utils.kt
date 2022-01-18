package com.openclassrooms.realestatemanager

import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Philippe on 21/02/2018.
 */
object Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return Math.round(dollars * 0.812) as Int
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param euros
     * @return
     */
    fun convertEuroToDollar(euros: Int): Int {
        return Math.round(euros / 0.812).toInt()
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    fun getTodayDate(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(Date())
    }

    fun formatPrice (price: Int?): String {
        return if (price==null) "" else "$" + NumberFormat.getNumberInstance(Locale.US).format(price)
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
//    fun isInternetAvailable(context: Context?): Boolean? {
//        val wifi = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        return wifi.isWifiEnabled
//    }
}