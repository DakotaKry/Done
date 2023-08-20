package com.example.done.ui.quit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.done.R
import com.example.done.databinding.FragmentAddQuitBinding
import com.example.done.databinding.FragmentQuitBinding


/**
 * A simple [Fragment] subclass.
 * Use the [AddQuitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddQuitFragment : Fragment() {

    private var _binding: FragmentAddQuitBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_quit, container, false)
    }


}