package com.example.done

class User {

    private var quitList: ArrayList<Quit> = ArrayList()


    fun addQuit(quit: Quit){

        quitList.add(quit)

    }

    fun removeQuit(quit: Quit){

        quitList.remove(quit)

    }

}