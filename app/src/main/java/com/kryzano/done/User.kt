package com.kryzano.done

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.UserProfileChangeRequest.Builder
import java.lang.Exception
import javax.security.auth.callback.Callback

class User(fuser: FirebaseUser) {

    private var fuser = fuser // Only ever used for changing displayname/username
    private var uid = fuser.uid
    private val db = Database()

    private var username = "Dummy"
    private var friends: ArrayList<String> = ArrayList() // string list of friends emails
    private var quitList: ArrayList<Quit> = ArrayList() // quit list of users quit
    private var blockList: ArrayList<String> = ArrayList() // list of people blocked, handled all locally

    fun refreshFuser(fuser: FirebaseUser){
        this.uid = fuser.uid
        this.fuser = fuser
        this.initialize()
    }

    fun getUid(): String {
        return this.uid
    }

    /**
     * Gets and overrides User with everything from Firestore
     * This will suspend code execution until all data has been fetched
     *
     * Args: None
     * Returns: None
     */
    fun initialize() {
        Log.d("User", "initalize")

        Log.d("Database", "Fetching for: ${this.uid}")


        for ( friend in friends ){
            db.addFriend(this.uid, friend)
        }

        for ( quit in quitList ){
            db.addQuit(this.uid, quit)
        }

        for ( block in blockList ){
            db.addBlock(this.uid, block)
        }


        // Uses code execution halting versions of methods, to insure we have all data before we continue
        // since this method is ran at startup, we want to wait until we have all the data before continuing
        // else wise we would have to worry about notifying the QuitRecyclerViewAdapter that we now have data from here.
        this.username = db.getUsername(this.uid)

        this.friends = db.getFriends(this.uid)

        this.blockList = db.getBlocks(this.uid)

        this.quitList = db.getQuits(this.uid)
        // Note, db.getWhatever() halts code execution. And db.fetchWhatever() provides a callback for asyn


    }

    fun getUsername(): String {
        return this.username
    }

    fun setUsername(newUsername: String){

        val request = UserProfileChangeRequest.Builder().setDisplayName(newUsername).build()
        try {
            fuser.updateProfile(request)
            this.username = newUsername
            db.pushUsername(this.uid, newUsername)

        } catch (e: Exception){

        }

    }


    fun addQuit(quit: Quit){

        quitList.add(quit)
        db.addQuit(this.uid, quit)

    }

    fun removeQuit(quit: Quit){

        quitList.remove(quit)
        db.removeQuit(this.uid, quit)

    }

    fun addFriend(friend: String){

        friends.add(friend)
        db.addFriend(this.uid, friend)

    }

    fun removeFriend(friend: String){

        friends.remove(friend)
        db.removeFriend(this.uid, friend)

    }

    fun addBlock(block: String){

        blockList.add(block)
        db.addBlock(this.uid, block)

    }

    fun removeBlock(block: String){

        blockList.remove(block)
        db.removeBlock(this.uid, block)

    }

    fun getQuitList(): ArrayList<Quit> {
        return quitList
    }

    fun getFriends(): ArrayList<String> {
        return friends
    }

    fun getBlockList(): ArrayList<String> {
        return blockList
    }

}