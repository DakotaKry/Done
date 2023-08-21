package com.kryzano.done

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.done.R
import com.example.done.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Authentication Attributes //


    lateinit var user: User
    private lateinit var mainViewModel: MainViewModel
    private lateinit var auth:Auth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Main","Activity start")

        // Must Be Ran First AND In Order //

        // Sets up mainViewModel
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Sets reference to mainViewModel
        auth = Auth(mainViewModel)
        auth.initAuth() // Logs in user or signs in anon. Returns a User class
        user = mainViewModel.getUser()

        user.initialize()

        // binding must be called after user in MainViewModel has been initialized!!
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // You can get the User class anywhere from the mainViewModel

        // End Run First //


        // TODO: Some of this might be redundant
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_quit, R.id.navigation_friends, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }



}