package com.kryzano.done

import androidx.lifecycle.ViewModel

/**
 * MainViewModel
 * Used to observe variable changes in MainActivity from other fragments
 */
class MainViewModel : ViewModel() {

    private lateinit var user: User
    private var quitEdit: Quit? = null

    fun setUser(user: User){
        this.user = user
    }

    fun getUser(): User{
        return this.user
    }

    fun setQuitEdit(quit: Quit){
        this.quitEdit = quit
    }

    fun getQuitEdit(): Quit?{
        return this.quitEdit
    }

    fun removeQuitEdit(){
        this.quitEdit = null
    }


}