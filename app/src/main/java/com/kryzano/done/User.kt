package com.kryzano.done

class User {

    private var email: String = ""  // email
    private var username: String = "" // username as a display name (will be unique)
    private var friends: ArrayList<String> = ArrayList() // string list of friends emails
    private var quitList: ArrayList<Quit> = ArrayList() // quit list of users quit
    private var blockList: ArrayList<String> = ArrayList() // list of people blocked, handled all locally

    constructor(email: String, username: String) {
        this.email = email
        this.username = username
    }

    fun addQuit(quit: Quit){

        quitList.add(quit)

    }

    fun removeQuit(quit: Quit){

        quitList.remove(quit)

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

    fun getIdentifiers(): Pair<String, String> {
        return Pair(email, username)
    }

}