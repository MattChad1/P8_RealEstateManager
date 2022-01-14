package com.openclassrooms.realestatemanager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.add_property.AddProperty
import com.openclassrooms.realestatemanager.ui.detail_property.DetailPropertyFragment
import com.openclassrooms.realestatemanager.ui.list_properties.ListPropertiesFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding;
    lateinit var toolbar: Toolbar

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

//        var secondFragment = supportFragmentManager.findFragmentById(R.id.second_fragment);
//
//            //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
//            if (secondFragment == null && binding.secondFragment != null) {
//                secondFragment = DetailPropertyFragment()
//                supportFragmentManager.beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.second_fragment, DetailPropertyFragment::class.java, null)
//                    .commit()
//            }



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.toolbar_add -> {
                startActivity(Intent (this,AddProperty::class.java ))
            }


        }


        return true
    }




}