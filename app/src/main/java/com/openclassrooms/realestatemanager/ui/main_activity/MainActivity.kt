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


//        val badgeDrawable = BadgeDrawable.create(this)
//        badgeDrawable.isVisible = true
//        badgeDrawable.number = 3
//        BadgeUtils.attachBadgeDrawable(badgeDrawable, menu.getItem(1) as View)
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


