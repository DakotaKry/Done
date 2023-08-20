package com.kryzano.done.ui.quit

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
import androidx.fragment.app.DialogFragment
import com.example.done.databinding.FragmentAddQuitBinding
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar


/**
 * A simple [Fragment] subclass.
 * Use the [AddQuitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddQuitFragment : DialogFragment(){

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

        // All of our view attributes
        var quiteTextTitle: TextInputEditText = binding.newQuitTitleEditText
        val cancelBtn: Button = binding.newQuitCancelButton
        val doneBtn: Button = binding.newQuitDoneButton
        val dateBtn: Button = binding.newQuitStartDateButton
        val timeBtn: Button = binding.newQuitStartTimeButton
        var startDateText: TextView = binding.newQuitStartDateText
        var startTimeText: TextView = binding.newQuitStartTimeText

        // This will be the Calendar used to construct the newQuit
        var startCalendar: Calendar = Calendar.getInstance()
        // This will be the String used to construct the newQuit
        var quitTitle: String = "Untitled"


        // sets up the Date Selector Button
        setDateBtn(dateBtn, startDateText, startCalendar)

        // sets up the Time Selector Button
        setTimeBtn(timeBtn, startTimeText, startCalendar)







        return root


    }

    /**
     * sets up the Date Selector Btn
     *
     * Args: dateBtn: Button; startDateText: TextView; startCalendar: Calendar
     * Return: None
     */
    private fun setDateBtn(dateBtn: Button, startDateText: TextView, startCalendar: Calendar){
        // Date Set Button
        dateBtn.setOnClickListener{

            Log.d("AddQuitFrag", "dateBtn clicked!")

            // Date picker variables
            var year = startCalendar.get(Calendar.YEAR)
            var month = startCalendar.get(Calendar.MONTH)
            var day = startCalendar.get(Calendar.DAY_OF_MONTH)



            var datePickerDialog: DatePickerDialog = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // updates text in fragment to represent selected Date
                startDateText.text = "$dayOfMonth/${monthOfYear + 1}/$year"

                // updates startCalendar to selected Date, time will be set elsewhere
                // Only updates the date and keeps the time the same
                startCalendar.set(year,
                    monthOfYear,
                    dayOfMonth,
                    startCalendar.get(Calendar.HOUR_OF_DAY),
                    startCalendar.get(Calendar.MINUTE))

                Log.d("AddQuitFrag", "datePickerDialog Date: ${startCalendar.get(Calendar.DAY_OF_MONTH)}/${startCalendar.get(Calendar.MONTH)+1}/${startCalendar.get(Calendar.YEAR)}")


            }, year, month, day)

            datePickerDialog.show()


        }
    }

    /**
     * sets up the Time Selector Btn
     *
     * Args: timeBtn: Button; startTimeText: TextView; startCalendar: Calendar
     * Return: None
     */
    private fun setTimeBtn(timeBtn: Button, startTimeText: TextView, startCalendar: Calendar){
        // Time Set Button
        timeBtn.setOnClickListener {

            Log.d("AddQuitFrag", "timeBtn clicked!")

            // time picker variables
            var hour = startCalendar.get(Calendar.HOUR_OF_DAY)
            var minute = startCalendar.get(Calendar.MINUTE)

            var timePickerDialog: TimePickerDialog = TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                // updates text in fragment to represent new time
                startTimeText.text = "$hourOfDay:$minute"

                // updates startCalendar to selected Time, date will be set elsewhere
                // Only updates the time and keeps the day the same
                startCalendar.set(startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH),
                    startCalendar.get(Calendar.DAY_OF_MONTH),
                    hourOfDay,
                    minute)


            }, hour, minute, true)

            timePickerDialog.show()

        }

    }


}