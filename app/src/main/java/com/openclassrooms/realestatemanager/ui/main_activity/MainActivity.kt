package com.openclassrooms.realestatemanager.ui.main_activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding


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

    @ExperimentalBadgeUtils
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val badge = BadgeDrawable.create(this)
        viewModel.countFilterLiveData.observe(this) {
            badge.number = it
            if (it>0) {
                BadgeUtils.attachBadgeDrawable(
                    badge,
                    toolbar,
                    R.id.searchFragment
                )
            }
            else BadgeUtils.detachBadgeDrawable(
                badge,
                toolbar,
                R.id.searchFragment
            )
        }
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



    override fun onBackPressed() {
        // If we are on DetailFragment && on tablet, use add history
        if (navController.currentDestination?.id==R.id.detailPropertyFragment && resources.getBoolean(R.bool.isTablet) && viewModel.getPreviousAdd()!=null) {
            navController.navigate(R.id.detailPropertyFragment)
        }
        else super.onBackPressed()

    }



}


