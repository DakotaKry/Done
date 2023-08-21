package com.kryzano.done.ui.quit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.done.R
import com.kryzano.done.MainActivity
import com.kryzano.done.MainViewModel
import com.kryzano.done.User
import com.kryzano.done.ui.friends.FriendViewFragment
import com.kryzano.done.ui.friends.FriendsFragment

class FriendsRecyclerViewAdapter(private val friendList: ArrayList<String>, private val user: User):
    RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder>(){


    private lateinit var mainViewModel: MainViewModel



    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        // gets our view
        val titleTextView: TextView

        init {
            titleTextView = itemView.findViewById(R.id.recycler_friend_title_text)
        }



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        Log.d("QuitRecyclerViewAdapter", "onCreateViewHolder")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_friend_row, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // gets and updates with appropriate title
        viewHolder.titleTextView.text = friendList[position]

        // sets up a Long Click Listener on each row,
        viewHolder.itemView.setOnLongClickListener {
            Log.d("RecyclerView", "Long clicked")

            // TODO: launch friend view fragment

            val context = (viewHolder.itemView.context as MainActivity)
            val fcontext = viewHolder.itemView.findFragment<FriendsFragment>()
            fcontext.hideFAB()

            mainViewModel = ViewModelProvider(context)[MainViewModel::class.java]
            mainViewModel.setFriendView(user.getFriends()[position])


            context.supportFragmentManager.beginTransaction()
                .add(R.id.fragment_fragment_friends, FriendViewFragment())
                .addToBackStack("additional_frag").commit()

            true

        }
    }




    override fun getItemCount(): Int {
        return friendList.size
    }



}