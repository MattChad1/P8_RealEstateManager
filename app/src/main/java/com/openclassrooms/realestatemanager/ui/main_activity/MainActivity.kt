package com.openclassrooms.realestatemanager.ui.main_activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.add_property.AddPropertyActivity
import com.openclassrooms.realestatemanager.ui.detail_property.DetailPropertyFragment
import com.openclassrooms.realestatemanager.ui.list_properties.ListPropertiesFragment
import com.openclassrooms.realestatemanager.ui.maps.MapsActivity
import com.openclassrooms.realestatemanager.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding;
    lateinit var toolbar: Toolbar

    val TAG = "MyLog MainActivity"

    // History of the adds consulted
    var lastProperty2: MutableList<Int> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        toolbar = binding.topAppBar
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main_fragment, ListPropertiesFragment::class.java, null)
                .commit()
        }

        // Check if the user come from a click in map, and then display details
        val idProperty = intent?.getIntExtra("idProperty", 0) ?: 0
        if (idProperty != 0) {
            sendNewDetails(idProperty)
        }
        

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.toolbar_add -> {
                val intent = Intent(this, AddPropertyActivity::class.java)
                startActivity(intent)
            }
            R.id.toolbar_edit -> {
                val intent = Intent(this, AddPropertyActivity::class.java)
                intent.putExtra(AddPropertyActivity.EDIT_ID, lastProperty2.last())
                startActivity(intent)
            }

            R.id.toolbar_search -> {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_fragment, SearchFragment(), "search_fragment")
                    .commit()
            }

            R.id.toolbar_map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    fun sendNewDetails(id: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        val newFragment = DetailPropertyFragment()
        val args = Bundle()
        if (lastProperty2.last()!=id) lastProperty2.add(id)
        args.putInt("idProperty", id)
        newFragment.arguments = args

        if (resources.getBoolean(R.bool.isTablet) == false) transaction.replace(R.id.main_fragment, newFragment, "detail_fragment")
        else transaction.replace(R.id.second_fragment, newFragment, "detail_fragment")

        transaction.disallowAddToBackStack()
        transaction.commit()
    }


    override fun onBackPressed() {
            if (supportFragmentManager.findFragmentByTag("detail_fragment") != null && (supportFragmentManager.findFragmentByTag("detail_fragment") as DetailPropertyFragment).isVisible) {
                if (!resources.getBoolean(R.bool.isTablet)) {
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.main_fragment, ListPropertiesFragment::class.java, null)
                        .commit()
                }
                else if (lastProperty2.size>1) {
                    lastProperty2.removeLast()
                    sendNewDetails(lastProperty2.last())
                }
                else super.onBackPressed()
                Log.i(TAG, "onBackPressed: 1")
            }

            else if (supportFragmentManager.findFragmentByTag("search_fragment") != null && (supportFragmentManager.findFragmentByTag("search_fragment") as SearchFragment).isVisible) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_fragment, ListPropertiesFragment::class.java, null)
                    .commit()
                Log.i(TAG, "onBackPressed: 2")
            } else super.onBackPressed()

    }



}


