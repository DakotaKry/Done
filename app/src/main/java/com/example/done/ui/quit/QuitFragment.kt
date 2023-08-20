package com.example.done.ui.quit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.done.databinding.FragmentQuitBinding
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

        val textView: TextView = binding.textHome
        quitViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val addQuitButton: FloatingActionButton = binding.newButtonQuit
        addQuitButton.setOnClickListener {
            Log.d("QuitFrag", "addQuitButton Clicked!")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}