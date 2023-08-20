package com.kryzano.done.ui.quit

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kryzano.done.Quit
import com.example.done.R
import com.kryzano.done.MainActivity
import com.kryzano.done.MainViewModel
import com.kryzano.done.User
import java.lang.IndexOutOfBoundsException
import java.util.Calendar

class QuitRecyclerViewAdapter(private val quitList: ArrayList<Quit>, private val user: User):
    RecyclerView.Adapter<QuitRecyclerViewAdapter.ViewHolder>(){

    private lateinit var mainViewModel: MainViewModel



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

        Log.d("QuitRecyclerViewAdapter", "onCreateViewHolder")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_quit_row, parent, false)

        return ViewHolder(view)

    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // gets and updates with appropriate title
        viewHolder.titleTextView.text = quitList[position].getTitle()

        // sets up a Long Click Listener on each row, that then displays a drop down menu
        viewHolder.itemView.setOnLongClickListener {
            Log.d("RecyclerView", "Long clicked")

            // Popup Menu //
            handlePopupMenu(viewHolder, position)


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
     * Handles the popup menu displayed when long clicking on a quit in the RecyclerView
     *
     * Args: viewHolder: ViewHolder, position: Int
     * Return: Boolean
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun handlePopupMenu(viewHolder: ViewHolder, position: Int): Boolean {
        // creates the popup menu
        val popup = PopupMenu(viewHolder.itemView.context, viewHolder.itemView)
        popup.inflate(R.menu.quit_drawer_menu)

        popup.setOnMenuItemClickListener { item -> // sets the listener for each menu item
            Log.d("RecyclerView", "Menu Item Clicked!")

            when (item.itemId) { // gets the specific button pressed

                // On clicking the delete button
                R.id.quit_drawer_delete -> {
                    Log.d("RecyclerView", "Delete!")

                    // Builds an alert dialog confirming
                    val alertBuilder = AlertDialog.Builder(viewHolder.itemView.context)
                    alertBuilder.setMessage("Are you sure you want to delete?")
                    alertBuilder.setPositiveButton("Yes") { dialog, _ ->

                        // Handles deleting the item from the user and recycler view
                        val itemToRemove = user.getQuitList()[position]
                        user.removeQuit(itemToRemove)
                        this.notifyItemRemoved(position)
                        this.notifyItemRangeChanged(position, user.getQuitList().size - position)

                        dialog.dismiss()
                    }
                    alertBuilder.setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }

                    alertBuilder.create().show() // shows the alert dialog

                }

                R.id.quit_drawer_relapsed -> {

                    // Builds an alert dialog confirming
                    val alertBuilder = AlertDialog.Builder(viewHolder.itemView.context)
                    alertBuilder.setMessage("Are you sure you want to reset the timer?")
                    alertBuilder.setPositiveButton("Yes") { dialog, _ ->

                        // handles relapsing the quit
                        val itemToRelapse = user.getQuitList()[position]
                        itemToRelapse.relapse()

                        dialog.dismiss()
                    }
                    alertBuilder.setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }

                    alertBuilder.create().show() // shows the alert dialog



                }

                R.id.quit_drawer_edit -> {

                    val context = (viewHolder.itemView.context as MainActivity)
                    mainViewModel = ViewModelProvider(context)[MainViewModel::class.java]
                    mainViewModel.setQuitEdit(user.getQuitList()[position])
                    // Show Add Quit popup fragment
                    AddQuitFragment().show(context.supportFragmentManager, null)
                    // Item may have been deleted and it may not have
                    this.notifyDataSetChanged()
                    // Need to notify data set change in case they added a new quit




                }

            }


            true
        }
        popup.gravity = Gravity.END // Makes the popup menu appear on the right of the screen
        popup.show()
        return true // for the lambda expression
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