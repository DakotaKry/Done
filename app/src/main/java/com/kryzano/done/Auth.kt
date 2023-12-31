package com.kryzano.done

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeoutException

class Auth(private val mainViewModel: MainViewModel) {

    private val db = Database()


    fun onSignInResultGood(context: Context) {

        val fuser = FirebaseAuth.getInstance().currentUser
        if (!fuser!!.isEmailVerified){
            Log.d("Auth","onSignInResultGood: sending email verification")
            fuser.sendEmailVerification()
            AuthUI.getInstance().signOut(context)
            Toast.makeText(context, "Email is not Verified, Sending Verification Email",
                Toast.LENGTH_LONG).show()
            this.initAuth()
        }
        else {
            Log.d("Auth","onSignInResultGood: refreshing User")


            mainViewModel.getUser().refreshFuser(fuser)
            // This will sync the database username a Auth display name every login
            fuser.displayName?.let { mainViewModel.getUser().setUsername(it) }

            // Sets email in firestore so we can search by email
            db.pushEmail(fuser.uid, fuser.email!!)

            // syncs everything
            mainViewModel.getUser().initialize()

        }

    }

    fun initAuth(): User{

        var fuser = FirebaseAuth.getInstance().currentUser
        var user: User

        if (fuser == null){
            try{
                Log.d("Auth","initAuth: null fuser")
                val task = FirebaseAuth.getInstance().signInAnonymously()
                var timeOutCounter = 0

                while (!task.isComplete){
                    if (timeOutCounter > 1000) {
                        throw(TimeoutException("Getting fuser Timed Out"))
                    } // 100 second timeout
                    timeOutCounter++
                    // Wait until we get a fuser
                    Thread.sleep(100)
                }

                fuser = task.result.user
                assert(fuser != null)
                Log.d("Auth", "initAuth: Has fuser")

            } catch (e: Exception){ throw e }
        }

        try{
            user = mainViewModel.getUser()
            mainViewModel.getUser().refreshFuser(fuser!!)
        }
        catch (e: UninitializedPropertyAccessException){
            user = User(fuser!!)
            mainViewModel.setUser(user)
        }

        Log.d("Auth", "Ending init fuser ${fuser!!.uid}")

        return user

    }

    fun checkAuth(signInLauncher: ActivityResultLauncher<Intent>){
        var fuser = FirebaseAuth.getInstance().currentUser

        if (fuser != null) { // Debug Block
            // TODO: This block is pointless
            Log.d("Auth", "providerData: ${fuser.providerData.size}")
        } else { Log.d("Auth", "checkAuth: null fuser?!") }

        if (fuser == null){
            this.initAuth()
            fuser = FirebaseAuth.getInstance().currentUser
        }
        assert(fuser!=null)

        if(fuser!!.providerData.size<=1){

            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()
            )

            // Create and launch sign-in intent
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .enableAnonymousUsersAutoUpgrade()
                .build()
            signInLauncher.launch(signInIntent)

        }


    }

}