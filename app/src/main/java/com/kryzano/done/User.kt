package com.kryzano.done

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.UserProfileChangeRequest.Builder
import java.lang.Exception

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
    }

    fun getUid(): String {
        return this.uid
    }

    /**
     * Gets and overrides User with everything from Firestore
     *
     * Args: None
     * Returns: None
     */
    fun initialize() {

        for ( friend in friends ){
            db.addFriend(this.uid, friend)
        }

        for ( quit in quitList ){
            db.addQuit(this.uid, quit)
        }

        for ( block in blockList ){
            db.addBlock(this.uid, block)
        }


        db.fetchUsername(this.uid){ result ->
            this.username = result
        }

        db.fetchFriends(this.uid){ result ->
            this.friends = result
        }
        db.fetchQuits(this.uid){ result ->
            this.quitList = result
        }
        db.fetchBlocks(this.uid){ result ->
            this.blockList = result
        }

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