package com.example.done.ui.quit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.done.R
import com.example.done.databinding.FragmentAddQuitBinding
import com.example.done.databinding.FragmentQuitBinding
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Date


/**
 * A simple [Fragment] subclass.
 * Use the [AddQuitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddQuitFragment : DialogFragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

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

        _binding = FragmentAddQuitBinding.inflate(inflater, container, false)
        val root = binding.root


        var quiteTextTitle: TextInputEditText = binding.newQuitTitleEditText
        val cancelBtn: Button = binding.newQuitCancelButton
        val doneBtn: Button = binding.newQuitDoneButton
        val dateTimeBtn: Button = binding.newQuitStartDateButton
        var startDateText: TextView = binding.newQuitStartDateText

        var calendar: Calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)




        dateTimeBtn.setOnClickListener{

            Log.d("AddQuitFrag", "dateTimeBtn clicked!")


            var datePickerDialog: DatePickerDialog = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                Toast.makeText(requireActivity(), """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()

                startDateText.text = "$dayOfMonth/${monthOfYear + 1}/$year"

            }, year, month, day)

            datePickerDialog.show()


        }

        Log.d("AddQuitFrag", "datePickerDialog Date: $year$month$day")




        return root


    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        TODO("Not yet implemented")
    }


}