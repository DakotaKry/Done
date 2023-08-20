package com.example.done.ui.quit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.done.Quit
import com.example.done.R
import java.lang.Exception
import java.time.Year
import java.util.Calendar
import kotlin.concurrent.thread

class QuitRecyclerViewAdapter(private val quitList: ArrayList<Quit>):
    RecyclerView.Adapter<QuitRecyclerViewAdapter.ViewHolder>(){


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        // gets our views
        val timerTextView: TextView
        val titleTextView: TextView

        init {
            timerTextView = itemView.findViewById(R.id.recycler_quit_timer_text)
            titleTextView = itemView.findViewById(R.id.recycler_quit_title_text)
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        Log.d("QuitRecyclerViewAdapter", "onCreateViewHolder")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_quit_row, parent, false)

        return ViewHolder(view)

    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // gets and updates with appropriate title
        viewHolder.titleTextView.text = quitList[position].getTitle()


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
            viewHolder.timerTextView.postDelayed(Runnable {
                val quitStartTime = quitList[position].getCalendar()
                val elapsed = timeElapsed(quitStartTime) // Array of time elapsed
                val elapsedText = "${elapsed[0]} y, ${elapsed[1]} m, ${elapsed[2]} d, ${elapsed[3]}:${elapsed[4]}:${elapsed[5]}"

                viewHolder.timerTextView.text = elapsedText

                updateTimerText()


            }, 1000)
        }

        updateTimerText()



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

        Log.d("UpdateTime", "Delta: $delta")
        Log.d("UpdateTime", "Current Time: $currentTime")
        Log.d("UpdateTime", "Quit Time: $sinceTime")

        var remainder:Int = delta % (365*24*60*60) // gets the remainder of seconds mod a year (365days)
        val years:Int = (delta - remainder) / (365*24*60*60) // calcs total years (should always be whole)
        delta = remainder // updates delta

        Log.d("UpdateTime", "Year Delta: $remainder")
        Log.d("UpdateTime", "Year Remainder: $remainder")
        Log.d("UpdateTime", "Years: $years")


        // This just coverts the difference in seconds into years, months, days, hours, minutes, seconds
        // usable for formatting

        remainder = delta % (30*24*60*60) // gets remainder of seconds mod a month (30days)
        val months:Int = (delta - remainder) / (30*24*60*60) // calcs total month
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