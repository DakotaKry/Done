package com.kryzano.done.ui.quit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.forEach
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kryzano.done.MainActivity
import com.kryzano.done.MainViewModel
import com.kryzano.done.Quit
import com.example.done.R
import com.example.done.databinding.FragmentQuitBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class QuitFragment : Fragment() {

    private var _binding: FragmentQuitBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // mainViewModel for communicating with MainActivity
        val mainViewModel: MainViewModel by activityViewModels()

        /**
         *  Testing quitList
         *  TODO: Implement a User Object that will hold our users quitList
         */

        var quitList: ArrayList<Quit> = ArrayList()
        val cal = Calendar.getInstance()
        cal.set(2022,3,4, 6, 23, 32)
        quitList.add(Quit("test1", Calendar.getInstance()))  // Note, since this is init in the frag and also always current. Switching frags will reset
        quitList.add(Quit("test2",cal)) // this one will not be reset as a hard date is given

        // Ends Testing Code //


        _binding = FragmentQuitBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Recycler View Code //
        val recyclerView = binding.recyclerviewQuit
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = QuitRecyclerViewAdapter(quitList)

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