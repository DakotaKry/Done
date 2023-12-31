package com.kryzano.done.ui.friends

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.done.databinding.FragmentFriendsBinding
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.kryzano.done.Auth
import com.kryzano.done.Database
import com.kryzano.done.MainViewModel
import com.kryzano.done.User
import com.kryzano.done.ui.quit.FriendsRecyclerViewAdapter

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null

    // Authentication Attributes //

    // Sign In Launcher
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { result ->
        this.onSignInResult(result)
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var user: User
    private lateinit var adapter: FriendsRecyclerViewAdapter
    private lateinit var addFriendButton: FloatingActionButton
    private val db = Database()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Set Up // Order Matters
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        user = mainViewModel.getUser()

        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Auth(mainViewModel).checkAuth(signInLauncher)

        // End Setup //


        // Recycler View Code //

        val friendsDisplayName = ArrayList<String>()
        for (friendEmail in user.getFriends()) {
            val friendUid = db.getUidFromEmail(friendEmail)
            val friend = db.getUsername(friendUid)
            friendsDisplayName.add(friend)
            Log.v("FriendsFragment","friend: $friend")
        }

        val recyclerView = binding.recyclerviewFriends
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FriendsRecyclerViewAdapter(friendsDisplayName, user)
        recyclerView.adapter = adapter

        // End Recycler View Code //

        // Floating Action Button Code //



        // On click listener for the add new quit button
        addFriendButton = binding.newButtonFriends

        // Shows Floating Action Button if Fragment is not inflated onto
        if (!FriendViewFragment().isVisible){
            addFriendButton.show()
        }

        addFriendButton.setOnClickListener {
            // TODO
        }

        // End Floating Action Button Code //


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /**
     * Callback for on SignInResults from signIn Intent
     * https://firebase.google.com/docs/auth/android/firebaseui
     *
     * Args: result: FirebaseAuthUIAuthenticationResult
     * Return: None
     */
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

        if (result.resultCode == RESULT_OK ) {
            Auth(mainViewModel).onSignInResultGood(requireActivity())
        } else {
            Log.d("Auth","Failed: ${result.resultCode}")
            val oldUid = FirebaseAuth.getInstance().currentUser?.uid
            FirebaseAuth.getInstance().currentUser?.delete()
            if (oldUid != null){
                Database().removeUser(oldUid)
            }
            Auth(mainViewModel).checkAuth(signInLauncher)
            // failed
        }

    }

    /**
     * Hides the Floating Action Button in FriendsFragment
     *
     * Args: None
     * Returns: None
     */
    fun hideFAB() {
        this.addFriendButton.hide()
    }
}