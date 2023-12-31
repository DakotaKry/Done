package com.kryzano.done.ui.quit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kryzano.done.Quit
import com.example.done.R
import com.kryzano.done.MainViewModel
import com.kryzano.done.User
import java.lang.IndexOutOfBoundsException
import java.util.Calendar

class FriendsQuitRecyclerViewAdapter(private val quitList: ArrayList<Quit>):
    RecyclerView.Adapter<FriendsQuitRecyclerViewAdapter.ViewHolder>(){


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        // gets our views
        val timerTextView: TextView
        val titleTextView: TextView

        init {
            timerTextView = itemView.findViewById(R.id.recycler_quit_timer_text)
            titleTextView = itemView.findViewById(R.id.recycler_quit_title_text)
        }



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        Log.d("FriendsQuitRecyclerViewAdapter", "onCreateViewHolder")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_quit_row, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: FriendsQuitRecyclerViewAdapter.ViewHolder, position: Int) {
        // gets and updates with appropriate title
        viewHolder.titleTextView.text = quitList[position].getTitle()

        // sets up a Long Click Listener on each row, that then displays a drop down menu
        viewHolder.itemView.setOnLongClickListener {
            Log.d("RecyclerView", "Long clicked")

            true


        }


        /**
         * gets and updates and continues to update every second the timer text
         * The solution to updating the views every second was found here:
         *      https://stackoverflow.com/questions/4776514/updating-textview-every-n-seconds
         * The function uses View.postDelayed to executes a Runnable every 1000ms which recursively calls itself
         *
         * Args: None
         * Returns: None
         */
        fun updateTimerText(){
            viewHolder.timerTextView.postDelayed({

                try {

                    val quitStartTime = quitList[position].getCalendar()
                    val elapsed = timeElapsed(quitStartTime) // Array of time elapsed
                    val elapsedText = getDisplayFormat(elapsed)



                    viewHolder.timerTextView.text = elapsedText

                    updateTimerText()
                } catch(e: IndexOutOfBoundsException){
                    Log.d("RecyclerView", "updateTimerText: Item removed")
                }


            }, 1000)
        }

        updateTimerText()



    }



    /**
     * Formats the string for the timer text from an array of ints
     *
     * Args: elapsed: Array(6)<Int>[years, months, days, hours, minutes, seconds]
     * Return: String
     */
    private fun getDisplayFormat(elapsed: Array<Int>): String {
        val elapsedText: String = if ((elapsed[0] != 0)) {
            ("${elapsed[0]} yrs," +
                    " ${elapsed[1]} mos, ${elapsed[2]} days" +
                    " ${elapsed[3]}:${elapsed[4]}:${elapsed[5].toString().padStart(2,'0')}")
        } else if ((elapsed[1] != 0)) {
            ("${elapsed[1]} mos, ${elapsed[2]} days," +
                    " ${elapsed[3]}:${elapsed[4]}:${elapsed[5].toString().padStart(2,'0')}")
        } else if ((elapsed[2] != 0)) {
            ("${elapsed[2]} days," +
                    " ${elapsed[3]}:${elapsed[4].toString().padStart(2, '0')}:${elapsed[5].toString().padStart(2,'0')}")
        } else if ((elapsed[3] != 0)){
            ("${elapsed[3]}:${elapsed[4].toString().padStart(2, '0')}:${elapsed[5].toString().padStart(2,'0')}")
        } else if ((elapsed[4] != 0)){
            ("${elapsed[4]}:${elapsed[5].toString().padStart(2,'0')}")
        } else {
            ("${elapsed[5]}")
        }

        return elapsedText
    }


    override fun getItemCount(): Int {
        return quitList.size
    }

    /**
     * Calculates time elapsed since given Calendar
     *
     * Args: since: Calendar
     * Return: Array(6)<Int>[years, months, days, hours, minutes, seconds]
     */
    private fun timeElapsed(since: Calendar) : Array<Int> {

        // Current time converted from ms to s and as an Int
        val currentTime = (Calendar.getInstance().timeInMillis/1000).toInt()
        val sinceTime = (since.timeInMillis/1000).toInt()
        var delta:Int = currentTime - sinceTime

        Log.v("UpdateTime", "Delta: $delta")


        var remainder:Int = delta % (365*24*60*60) // gets the remainder of seconds mod a year (365days)
        val years:Int = (delta - remainder) / (365*24*60*60) // calculates total years (should always be whole)
        delta = remainder // updates delta


        // This just coverts the difference in seconds into years, months, days, hours, minutes, seconds
        // usable for formatting

        remainder = delta % (30*24*60*60) // gets remainder of seconds mod a month (30days)
        val months:Int = (delta - remainder) / (30*24*60*60) // calculates total month
        delta = remainder

        remainder = delta % (24*60*60)
        val days:Int = (delta - remainder) / (24*60*60)
        delta = remainder

        remainder = delta % (60*60)
        val hours:Int = (delta - remainder) / (60*60)
        delta = remainder

        remainder = delta % 60
        val minutes:Int = (delta - remainder) / 60
        delta = remainder

        val seconds:Int = delta

        return arrayOf(years, months, days, hours, minutes, seconds)

    }


}