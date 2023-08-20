package com.example.done.ui.quit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.done.MainActivity
import com.example.done.R
import com.example.done.databinding.FragmentQuitBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        val quitViewModel =
            ViewModelProvider(this).get(QuitViewModel::class.java)

        _binding = FragmentQuitBinding.inflate(inflater, container, false)
        val root: View = binding.root




        // On click listener for the add new quit button
        val addQuitButton: FloatingActionButton = binding.newButtonQuit
        addQuitButton.setOnClickListener {
            Log.d("QuitFrag", "addQuitButton Clicked!")


            val addQuitFragment: Fragment = AddQuitFragment()
            val addQuitTransaction: FragmentTransaction = childFragmentManager.beginTransaction().apply {
                add(R.id.fragment_fragment_quit, addQuitFragment)
                commit()
            }



        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}