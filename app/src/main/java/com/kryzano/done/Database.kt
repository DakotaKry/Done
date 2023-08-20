package com.kryzano.done
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Database {

    // Gets our Firebase Instance
    private val fdb = Firebase.firestore
    private val fdbUsers = fdb.collection("users") // users collection

    /**
     * creates the user in the Firestore db, initializing the email,
     *      username, and any quits already created
     *
     * Args: user: User
     * returns: None
     */
    fun createNewUser(user: User){

        // get what we need from the User object
        val identifiers = user.getIdentifiers()
        val quitList = user.getQuitList()

        // creates a hashmap for the users field
        val usernameData = hashMapOf(
            "username" to identifiers.second
        )


        Log.d("Database", identifiers.first)
        Log.d("Database", identifiers.second)

        // sets the usernameData
        fdbUsers.document("/${identifiers.first}").set(usernameData)

        // uses indexing for unique identifiers
        for (i in 0 until quitList.size) {

            // creates a hashmap for every quit field
            val quitData = hashMapOf<String, Any>(
                "title" to quitList[i].getTitle(), // title stored as a string
                "time" to quitList[i].getCalendar().timeInMillis // calendar stored as a long
            )

            // sets
            fdbUsers.document("/${identifiers.first}")
                .collection("/quits").document("/${i}").set(quitData)

        }


    }

    fun getUsername(email: String): String{

        var username = ""

        try {

            val docRef = fdb.collection("users").document("/$email")
            val task = docRef.get()
            while (!task.isComplete){
                Thread.sleep(100)
            }
            val result = task.result
            username = result.data!!["username"] as String

        } catch (exception: Exception) {
            Log.e("Database", exception.toString())
        }


        return username

    }

    /**
     * gets the quits given a users email address.
     * method waits for results and then returns
     *
     * Args: email: String
     * Return: quitList: ArrayList<Quit>
     */
    fun getQuits(email: String): ArrayList<Quit>{

        var quitList: ArrayList<Quit> = ArrayList()

        try {

            val docRef = fdb.collection("users").document("/$email")
            val task = docRef.collection("/quits").get()
            while (!task.isComplete){
                Thread.sleep(100)
                // Does nothing as we wait for the task to complete
                // TODO: Consider Async design using Livedata so not to freeze thread
                // https://stackoverflow.com/questions/57330766/why-does-my-function-that-calls-an-api-or-launches-a-coroutine-return-an-empty-o/57330767#57330767
            }
            val result = task.result

            // Goes through all quits and populates empty quitList for return
            for (document in result) {
                Log.d("DatabaseFetch","ID: ${document.id}, Data: ${document.data}")
                val title = document.data["title"].toString() // get the title
                // get the calendar
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = document.data["time"].toString().toLong()
                val quit = Quit(title, calendar)
                Log.d("DatabaseFetch","quit: ${quit.getTitle()}")
                quitList.add(quit) // add the Quit to list

            }


        } catch (exception: Exception) {
            Log.e("Database", exception.toString())
        }

        return quitList

    }

}