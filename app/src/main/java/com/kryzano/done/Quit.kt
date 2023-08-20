package com.kryzano.done

import java.util.Calendar


/**
 * Quit Object that keeps a Date of when one has quit, and a title
 */
class Quit(private var title: String, private var calendar: Calendar) {

    /**
     * gets the current calendar when one has quit
     *
     * Args: None
     * Returns: Calendar
     */
    fun getCalendar(): Calendar{
        return calendar
    }

    /**
     * gets the current title of what one has quit
     *
     * Args: None
     * Returns: String
     */
    fun getTitle(): String{
        return title
    }

    /**
     * sets the date of when one has quit
     *
     * Args: newCalendar:Calendar
     * Returns: None
     */
    fun setCalendar(newCalendar: Calendar){
        calendar = newCalendar
    }

    /**
     * sets the title of what one has quit
     *
     * Args: newTitle:String
     * Returns: None
     */
    fun setTitle(newTitle: String){
        title = newTitle
    }

    /**
     * sets the calendar to the current time when one relapse
     *
     * Args: None
     * Returns: None
     */
    fun relapse(){
        calendar = Calendar.getInstance()
    }

}