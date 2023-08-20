package com.kryzano.done.ui.friends

import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.done.databinding.FragmentFriendsBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.checkerframework.checker.index.qual.NonNegative

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null

    // Authentication Attributes //

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { result ->
        this.onSignInResult(result)
    }

    lateinit var user: FirebaseUser // This is the auth user



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val friendsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)

        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        friendsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        checkAuth()








        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Checks if a user is logged in, if not lauches the login intent
     *
     * Args: None
     * Return: None
     */
    private fun checkAuth(){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Log.d("Auth", "providerData: ${user.providerData.size}")
        }
        if (user == null || user.providerData.size <= 1) {
            // Need to login
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

        } else {

            if(user.isEmailVerified){
                this.user = user // All is good, log us in
            } else {
                user.sendEmailVerification()
                // Email is not verified
                AuthUI.getInstance().signOut(requireContext())
                    .addOnCompleteListener {

                        Toast.makeText(context, "Email not verified", Toast.LENGTH_LONG).show()
                        checkAuth()
                    }
            }


        }

    }

    /**
     * Callback for on SignInResults from signIn Intent
     * https://firebase.google.com/docs/auth/android/firebaseui
     *
     * Args: result: FirebaseAuthUIAuthenticationResult
     * Return: None
     */
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

        val response = result.idpResponse // for error handeling
        if (result.resultCode == RESULT_OK ) {
            checkAuth()
        } else {
            // failed
        }

    }
}