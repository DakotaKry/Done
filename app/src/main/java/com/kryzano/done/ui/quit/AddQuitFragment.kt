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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.done.databinding.FragmentAddQuitBinding
import com.google.android.material.textfield.TextInputEditText
import com.kryzano.done.MainViewModel
import com.kryzano.done.Quit
import com.kryzano.done.User
import java.util.Calendar


/**
 * A simple [Fragment] subclass.
 *
 */
class AddQuitFragment : DialogFragment(){

    private var _binding: FragmentAddQuitBinding? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var user: User
    private var quitEdit: Quit? = null
    private var editingQuit = false


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // mainViewModel for communicating with MainActivity
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        user = mainViewModel.getUser()
        quitEdit = mainViewModel.getQuitEdit()

        if (quitEdit != null){
            this.editingQuit = true
        }


        // Inflate the layout for this fragment

        _binding = FragmentAddQuitBinding.inflate(inflater, container, false)
        val root = binding.root

        // All of our view attributes
        val quiteTextTitle: TextInputEditText = binding.newQuitTitleEditText
        val cancelBtn: Button = binding.newQuitCancelButton
        val doneBtn: Button = binding.newQuitDoneButton
        val dateBtn: Button = binding.newQuitStartDateButton
        val timeBtn: Button = binding.newQuitStartTimeButton
        val startDateText: TextView = binding.newQuitStartDateText
        val startTimeText: TextView = binding.newQuitStartTimeText

        // This will be the Calendar used to construct the newQuit
        var startCalendar: Calendar = Calendar.getInstance()

        // If we are actually editing a quit, we want to populate old quit data
        if (editingQuit){

            // Gets Calendar info
            val prevCalendar = quitEdit!!.getCalendar()
            val prevYear = prevCalendar.get(Calendar.YEAR)
            val prevMonth = prevCalendar.get(Calendar.MONTH)
            val prevDay = prevCalendar.get(Calendar.DAY_OF_MONTH)
            val prevHour = prevCalendar.get(Calendar.HOUR_OF_DAY)
            val prevMinute = prevCalendar.get(Calendar.MINUTE)
            val prevStartDateText = "$prevDay/$prevMonth/$prevYear"
            val prevStartTimeText = "$prevHour:$prevMinute"

            // Sets title
            quiteTextTitle.setText(quitEdit!!.getTitle())

            // Sets Date and Time text
            startDateText.text = prevStartDateText
            startTimeText.text = prevStartTimeText

            // Sets current calendar
            startCalendar = prevCalendar
        }


        // This will be the String used to construct the newQuit

        // sets up the Date Selector Button
        setDateBtn(dateBtn, startDateText, startCalendar)

        // sets up the Time Selector Button
        setTimeBtn(timeBtn, startTimeText, startCalendar)

        doneBtn.setOnClickListener {
            val newQuit = Quit(quiteTextTitle.text.toString(), startCalendar)

            if(editingQuit){
                // If we are editing a quit, remove the old one
                user.removeQuit(quitEdit!!)
                // nullify quitEdit so we don't launch again in edit mode
                mainViewModel.removeQuitEdit()
            }

            user.addQuit(newQuit)
            this.dismiss()
        }

        cancelBtn.setOnClickListener {
            if(editingQuit){
                // nullify quitEdit so we don't launch again in edit mode
                mainViewModel.removeQuitEdit()
            }
            this.dismiss()
        }


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
            val year = startCalendar.get(Calendar.YEAR)
            val month = startCalendar.get(Calendar.MONTH)
            val day = startCalendar.get(Calendar.DAY_OF_MONTH)



            val datePickerDialog = DatePickerDialog(requireActivity(),
                { _, yearSelected, monthSelected, daySelected ->

                    // updates text in fragment to represent selected Date
                    val startDayDisplayText = "$daySelected/${monthSelected + 1}/$yearSelected"
                    startDateText.text = startDayDisplayText

                    // updates startCalendar to selected Date, time will be set elsewhere
                    // Only updates the date and keeps the time the same
                    startCalendar.set(yearSelected,
                        monthSelected,
                        daySelected,
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
            val hour = startCalendar.get(Calendar.HOUR_OF_DAY)
            val minute = startCalendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireActivity(),
                { _, hourSelected, minuteSelected ->

                    // updates text in fragment to represent new time
                    val startTimeDisplayText = "$hourSelected:$minuteSelected"
                    startTimeText.text = startTimeDisplayText

                    // updates startCalendar to selected Time, date will be set elsewhere
                    // Only updates the time and keeps the day the same
                    startCalendar.set(startCalendar.get(Calendar.YEAR),
                        startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH),
                        hourSelected,
                        minuteSelected)


                }, hour, minute, true)

            timePickerDialog.show()

        }

    }


}