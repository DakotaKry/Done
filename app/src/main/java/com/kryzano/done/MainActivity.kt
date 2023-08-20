package com.kryzano.done

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.done.R
import com.example.done.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kryzano.done.ui.quit.QuitFragment
import com.kryzano.done.ui.quit.QuitViewModel
import java.util.Calendar
import java.util.concurrent.TimeoutException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Authentication Attributes //
    //lateinit var auth: FirebaseAuth // This is the auth user
    var fuser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    lateinit var user: User
    lateinit var mainViewModel: MainViewModel
    lateinit var auth:Auth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Main","Activity start")

        // Must Be Ran First AND In Order //

        // TODO: Remove testing line below
        //if (this.fuser != null){ FirebaseAuth.getInstance().signOut()} // Makes sure we are signed out for testing

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
        // Boolean to see if we need to freeze Nav Bar init to false
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


    }



}