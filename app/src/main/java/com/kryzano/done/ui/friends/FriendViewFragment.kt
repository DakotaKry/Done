package com.kryzano.done.ui.friends

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.done.R
import com.example.done.databinding.FragmentViewFriendBinding
import com.kryzano.done.Database
import com.kryzano.done.MainViewModel
import com.kryzano.done.Quit
import com.kryzano.done.User
import com.kryzano.done.ui.quit.FriendsQuitRecyclerViewAdapter


/**
 * A simple [Fragment] subclass.
 *
 *
 */
class FriendViewFragment : Fragment() {

    private var _binding: FragmentViewFriendBinding? = null
    private val binding get() = _binding!!
    private val db = Database()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var friendView: String
    private lateinit var friendQuitList: ArrayList<Quit>
    private lateinit var adapter: FriendsQuitRecyclerViewAdapter
    private lateinit var user: User



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        try {
            friendView = mainViewModel.getFriendView()!!
        } catch (e: NullPointerException){
            Log.wtf("FriendViewFragment", "friendView is null?!")
            throw e
        }

        _binding = FragmentViewFriendBinding.inflate(inflater, container, false)
       // val root: View = binding.root

        friendQuitList = db.getQuits(friendView)
        user = mainViewModel.getUser()

        // Recycler View Code //
        val recyclerView = binding.recyclerviewFriendsQuit
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FriendsQuitRecyclerViewAdapter(friendQuitList)

        recyclerView.adapter = adapter




        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_friend, container, false)

    }






}