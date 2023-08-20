package com.kryzano.done

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * MainViewModel
 * Used to observe variable changes in MainActivity from other fragments
 */
class MainViewModel : ViewModel() {

    private lateinit var user: User

    private val _freezeNav = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun setUser(user: User){
        this.user = user
    }

    fun getUser(): User{
        return this.user
    }



}