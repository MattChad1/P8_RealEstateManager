package com.openclassrooms.realestatemanager.ui.main_activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.add_property.AddPropertyFragment
import com.openclassrooms.realestatemanager.ui.detail_property.DetailPropertyFragment
import com.openclassrooms.realestatemanager.ui.list_properties.ListPropertiesFragment
import com.openclassrooms.realestatemanager.ui.list_properties.ListPropertiesFragmentDirections
import com.openclassrooms.realestatemanager.ui.list_properties.ListPropertiesViewModel
import com.openclassrooms.realestatemanager.ui.maps.MapsFragment
import com.openclassrooms.realestatemanager.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding;
    lateinit var toolbar: Toolbar

    val TAG = "MyLog MainActivity"

    // History of the adds consulted
    var lastProperty2: MutableList<Int> = mutableListOf()

    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: MainActivityViewModel by viewModels() {
        ViewModelFactory(MyApplication.instance.propertyRepository, MyApplication.instance.navigationRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        toolbar = binding.topAppBar
        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(navController,appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_sync -> {
                viewModel.synchroniseWithFirestore()
            }
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        super.onOptionsItemSelected(item)
//        when (item.itemId) {
//            R.id.toolbar_add -> {
//                val intent = Intent(this, AddPropertyFragment::class.java)
//                startActivity(intent)
//            }
//            R.id.toolbar_edit -> {
//                val intent = Intent(this, AddPropertyFragment::class.java)
//                intent.putExtra(AddPropertyFragment.EDIT_ID, lastProperty2.last())
//                startActivity(intent)
//            }
//
//            R.id.toolbar_search -> {
//                supportFragmentManager.beginTransaction()
//                    .setReorderingAllowed(true)
//                    .replace(R.id.main_fragment, SearchFragment(), "search_fragment")
//                    .commit()
//            }
//
//            R.id.toolbar_map -> {
//                val intent = Intent(this, MapsFragment::class.java)
//                startActivity(intent)
//            }
//        }
//        return true
//    }

//    fun sendNewDetails(id: Int) {
//        val transaction = supportFragmentManager.beginTransaction()
//        val newFragment = DetailPropertyFragment()
//        val args = Bundle()
//        if (lastProperty2.last()!=id) lastProperty2.add(id)
//        args.putInt("idProperty", id)
//        newFragment.arguments = args
//
//        if (resources.getBoolean(R.bool.isTablet) == false) transaction.replace(R.id.main_fragment, newFragment, "detail_fragment")
//        else transaction.replace(R.id.second_fragment, newFragment, "detail_fragment")
//
//        transaction.disallowAddToBackStack()
//        transaction.commit()
//    }
//
//    fun sendNewDetails(id: Int, navigationComponent: Boolean) {
//
//        if (lastProperty2.lastOrNull()!=id) lastProperty2.add(id)
//
//        if (resources.getBoolean(R.bool.isTablet) == false) {
//            val sendData = ListPropertiesFragmentDirections.actionListPropertiesFragmentToDetailPropertyFragment()
//            binding.navHostFragment?.findNavController()?.navigate(sendData)
//        }
//
//
//    }




    override fun onBackPressed() {
        // If we are on DetailFragment && on tablet, use add history
        if (navController.currentDestination?.id==R.id.detailPropertyFragment && resources.getBoolean(R.bool.isTablet) && viewModel.getPreviousAdd()!=null) {
            navController.navigate(R.id.detailPropertyFragment)
        }
        else super.onBackPressed()

    }



}


