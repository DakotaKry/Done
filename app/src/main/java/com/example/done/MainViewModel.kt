package com.example.done

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * MainViewModel
 * Used to observe variable changes in MainActivity from other fragments
 */
class MainViewModel : ViewModel() {

    private val _freezeNav = MutableLiveData<Boolean>().apply {
        value = false
    }

    /**
     * getFreezeNavLive
     * gets the MutableLiveData<Boolean> so one can observe when the data has changed
     */
    fun getFreezeNavLive(): MutableLiveData<Boolean> {
        return _freezeNav
    }

    /**
     * setFreezeNavLive
     * sets the boolean value for _freezeNav
     */
    fun setFreezeNavLive(boolean: Boolean){
        _freezeNav.postValue(boolean)
    }

}