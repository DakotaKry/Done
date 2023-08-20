package com.kryzano.done.ui.quit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kryzano.done.MainViewModel
import com.example.done.databinding.FragmentQuitBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kryzano.done.User
import java.util.Calendar

class QuitFragment : Fragment(){

    private var _binding: FragmentQuitBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var user: User
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: QuitRecyclerViewAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // mainViewModel for communicating with MainActivity
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        user = mainViewModel.getUser()



        _binding = FragmentQuitBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Recycler View Code //
        val recyclerView = binding.recyclerviewQuit
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = QuitRecyclerViewAdapter(user.getQuitList(), user)

        recyclerView.adapter = adapter





        // On click listener for the add new quit button
        val addQuitButton: FloatingActionButton = binding.newButtonQuit
        addQuitButton.setOnClickListener {
            Log.d("QuitFrag", "addQuitButton Clicked!")


            // Show Add Quit popup fragment
            AddQuitFragment().show(childFragmentManager,null)
            adapter.notifyDataSetChanged() // Need to notify data set change in case they added a new quit
            // Live data might be more effective at this



        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}