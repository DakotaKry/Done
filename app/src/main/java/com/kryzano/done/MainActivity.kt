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
import com.kryzano.done.ui.quit.QuitViewModel
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Authentication Attributes //
    //lateinit var auth: FirebaseAuth // This is the auth user
    lateinit var fuser: FirebaseUser
    lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAuth()





        // Boolean to see if we need to freeze Nav Bar init to false
        var freezeNav: MutableLiveData<Boolean>
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
        // Observes when there is a change to freezeNav property
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


        // Database testing TODO: Remove
        var testUser: User = User("test@test.com", "dummyTestUser")
        testUser.addQuit(Quit("testQuit", Calendar.getInstance()))
        val db: Database = Database()
        db.createNewUser(testUser)
        val quitList = db.getQuits("test@test.com")
        Log.d("Test","$quitList")
        val username = db.getUsername("test@test.com")
        Log.d("Test","$username")





    }

    private fun checkAuth(){

        val user = FirebaseAuth.getInstance().currentUser

        Log.d("Auth", "User: ${user.toString()}")


        if (user == null){
            // Anon auth
            Log.d("Auth", "signInAnon")
            FirebaseAuth.getInstance().signInAnonymously()

        }

        if (user != null) {
            this.fuser = user
        }

    }


}