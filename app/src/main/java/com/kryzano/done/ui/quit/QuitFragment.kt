package com.kryzano.done.ui.quit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.view.forEach
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kryzano.done.MainActivity
import com.kryzano.done.MainViewModel
import com.kryzano.done.Quit
import com.example.done.R
import com.example.done.databinding.FragmentQuitBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kryzano.done.User
import java.util.Calendar

class QuitFragment : Fragment(){

    private var _binding: FragmentQuitBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var user: User
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: QuitRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // mainViewModel for communicating with MainActivity
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]


        /**
         *  Testing quitList
         *  TODO: Implement a User Object that will hold our users quitList
         */

        user = mainViewModel.getUser()
        //Log.d("ViewModel", "User in Quit Frag: ${user.getUsername()}")


        var quitList: ArrayList<Quit> = ArrayList()
        val cal = Calendar.getInstance()
        cal.set(2019,4,29, 0, 0, 0)
        //user.addQuit(Quit("smoking",cal)) // this one will not be reset as a hard date is given

        // Ends Testing Code //


        _binding = FragmentQuitBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Recycler View Code //
        val recyclerView = binding.recyclerviewQuit
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = QuitRecyclerViewAdapter(user.getQuitList(), user)//, this)

        recyclerView.adapter = adapter




        // On click listener for the add new quit button
        val addQuitButton: FloatingActionButton = binding.newButtonQuit
        addQuitButton.setOnClickListener {
            Log.d("QuitFrag", "addQuitButton Clicked!")

            val frameLayout: FrameLayout = binding.fragmentFragmentQuit
            // Freeze nav menu
            //mainViewModel.setFreezeNavLive(true)

            // Show Add Quit popup fragment
            AddQuitFragment().show(childFragmentManager,null)
            adapter.notifyDataSetChanged() // Need to notify data set change in case they added a new quit
            // Live data might be more effective at this

            //val addQuitFragment: DialogFragment = AddQuitFragment()
            //childFragmentManager.beginTransaction().apply {
            //    add(R.id.fragment_fragment_quit, addQuitFragment)
            //    commit()
            //}



        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}