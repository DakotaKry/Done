package com.example.done

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.done.databinding.ActivityMainBinding
import com.example.done.ui.quit.QuitViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Boolean to see if we need to freeze Nav Bar init to false
        var freezeNav: MutableLiveData<Boolean> = MutableLiveData(false)


        val navView: BottomNavigationView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        freezeNav = mainViewModel.getFreezeNavLive()
        // Observes when the live data changes
        freezeNav.observe(this, Observer<Boolean>(){
            if (freezeNav.value == true) {
                Log.d("LiveData", "freezeNav is True!")
                navView.menu.forEach { it.isEnabled = false }


            } else {
                Log.d("LiveData","freezeNav is False!")
                navView.menu.forEach { it.isEnabled = true }
            }
            Log.d("LiveData","freezeNav observed!")
        })

        // Change the value of the live data To be implemented with ViewModel to change across fragments



    }
}