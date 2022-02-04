package com.openclassrooms.realestatemanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.add_property.AddPropertyActivity
import com.openclassrooms.realestatemanager.ui.detail_property.DetailPropertyFragment
import com.openclassrooms.realestatemanager.ui.list_properties.ListPropertiesFragment
import com.openclassrooms.realestatemanager.ui.search_activity.SearchActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding;
    lateinit var toolbar: Toolbar

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
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (!resources.getBoolean(R.bool.isTablet) && supportFragmentManager.findFragmentByTag("fragment_detail") != null && (supportFragmentManager.findFragmentByTag("fragment_detail") as DetailPropertyFragment).isVisible) {
            Log.i(TAG, "onBackPressed: ")

        } else super.onBackPressed()

    }


}