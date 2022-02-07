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
import com.openclassrooms.realestatemanager.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding;
    lateinit var toolbar: Toolbar
    lateinit var modalBottomSheet: ModalBottomSheet

    val TAG = "MyLog MainActivity"

    companion object {
        var lastProperty: Int? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.topAppBar
        setSupportActionBar(toolbar)

        modalBottomSheet = ModalBottomSheet()




        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main_fragment, ListPropertiesFragment::class.java, null)
                .commit()
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
                intent.putExtra(AddPropertyActivity.EDIT_ID, lastProperty)
                startActivity(intent)
            }

            R.id.toolbar_search -> {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_fragment, SearchFragment(), "search_fragment")
                    .commit()
//                modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
//                val intent = Intent(this, SearchActivity::class.java)
//                startActivity(intent)
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (!resources.getBoolean(R.bool.isTablet)) {
            if (supportFragmentManager.findFragmentByTag("fragment_detail") != null && (supportFragmentManager.findFragmentByTag("fragment_detail") as DetailPropertyFragment).isVisible) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_fragment, ListPropertiesFragment::class.java, null)
                    .commit()
                Log.i(TAG, "onBackPressed: 1")
            } else if (supportFragmentManager.findFragmentByTag("search_fragment") != null && (supportFragmentManager.findFragmentByTag("search_fragment") as SearchFragment).isVisible) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_fragment, ListPropertiesFragment::class.java, null)
                    .commit()
                Log.i(TAG, "onBackPressed: 2")
            } else super.onBackPressed()
        } else super.onBackPressed()
    }

}


