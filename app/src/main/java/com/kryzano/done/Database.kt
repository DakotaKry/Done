package com.kryzano.done
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO: Could make a private attribute uid, instead of always passing it to the method

// Note, Database().getWhatever() halts code execution.
// And Database().fetchWhatever() provides a callback for asyn calls
class Database() {

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
    fun pushUsername(uid: String, username: String){

        // Sets Username //

        // creates a hashmap for the users field
        val usernameData = hashMapOf(
            "username" to username
        )

        fdbUsers.document("/${uid}").set(usernameData)

    }

    /**
     * Fetchs the username from a given uid (might be redundant since
     *  one can also do FirebaseUser.displayName
     *
     *  Args: uid:String
     *  Return: None
     *  Result: String
     */
    fun fetchUsername(uid: String, callback:(String) -> Unit){

        val docRef = fdb.collection("users").document("/$uid")
        docRef.get().addOnSuccessListener {

            val result = it

            // try to get the username, if null then returns an empty string
            try{
                callback(result.data!!["username"] as String)

            } catch(e: NullPointerException){

                callback("")

            }

        }

    }
    fun getUsername(uid: String): String{

        var username = ""

        try {

            val docRef = fdb.collection("users").document("/$uid")
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
     * Fetches the quits given a users uid (usually email).
     * method gives a call back for asyn
     * see: https://stackoverflow.com/questions/57330766/why-does-my-function-that-calls-an-api-or-launches-a-coroutine-return-an-empty-o/57330767#57330767
     *
     * Args: uid: String
     * Return: None
     * Result: ArrayList<Quit>
     */
    fun fetchQuits(uid: String, callback:(ArrayList<Quit>) -> Unit){

        val docRef = fdb.collection("users").document("/$uid")
        docRef.collection("/quits").get().addOnSuccessListener {
            var quitList: ArrayList<Quit> = ArrayList()
            val result = it
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

            callback(quitList)
        }

    }

    /**
     * Gets the quits given a users uid (usually email).
     * Forces halt in code execution until a result is received
     *
     * Args: uid: String
     * Return: None
     * Result: ArrayList<Quit>
     */
    fun getQuits(uid: String):ArrayList<Quit>{

        var quitList: ArrayList<Quit> = ArrayList()

        try{
            val docRef = fdb.collection("users").document("/$uid").collection("/quits")
            val task = docRef.get()
            while (!task.isComplete){
                Thread.sleep(100)
            }
            val result = task.result

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

        } catch (e:Exception){
            throw e
        }
        return quitList

    }

    /**
     * Fetches the friends given a users uid (usually email).
     * method gives a call back for asyn
     * see: https://stackoverflow.com/questions/57330766/why-does-my-function-that-calls-an-api-or-launches-a-coroutine-return-an-empty-o/57330767#57330767
     *
     * Args: uid: String
     * Return: None
     * Result: ArrayList<String>
     */
    fun fetchFriends(uid: String, callback:(ArrayList<String>) -> Unit){

        val docRef = fdb.collection("users").document("/$uid")
        docRef.collection("/friends").get().addOnSuccessListener {
            var friendList: ArrayList<String> = ArrayList()
            val result = it
            for (document in result) {
                Log.d("DatabaseFetch","ID: ${document.id}, Data: ${document.data}")
                val fuid = document.data["uid"].toString() // get the title

                Log.d("DatabaseFetch","friend: ${fuid}")
                friendList.add(fuid) // add the Quit to list

            }

            callback(friendList)
        }

    }
    /**
     * Gets the quits given a users uid (usually email).
     * Forces halt in code execution until a result is received
     *
     * Args: uid: String
     * Return: None
     * Result: ArrayList<Quit>
     */
    fun getFriends(uid: String):ArrayList<String> {

        var friendList: ArrayList<String> = ArrayList()

        try {
            val docRef = fdb.collection("users").document("/$uid").collection("/friends")
            val task = docRef.get()
            while (!task.isComplete) {
                Thread.sleep(100)
            }
            val result = task.result

            for (document in result) {
                Log.d("DatabaseFetch", "ID: ${document.id}, Data: ${document.data}")
                val fuid = document.data["uid"].toString() // get the title

                Log.d("DatabaseFetch", "friend: $fuid")
                friendList.add(fuid) // add the Quit to list

            }

        } catch (e: Exception) {
            throw e
        }
        return friendList
    }

    /**
     * Fetches the blocks given a users uid (usually email).
     * method gives a call back for asyn
     * see: https://stackoverflow.com/questions/57330766/why-does-my-function-that-calls-an-api-or-launches-a-coroutine-return-an-empty-o/57330767#57330767
     *
     * Args: uid: String
     * Return: None
     * Result: ArrayList<String>
     */
    fun fetchBlocks(uid: String, callback:(ArrayList<String>) -> Unit){

        val docRef = fdb.collection("users").document("/$uid")
        docRef.collection("/blocks").get().addOnSuccessListener {
            var blockList: ArrayList<String> = ArrayList()
            val result = it
            for (document in result) {
                Log.d("DatabaseFetch","ID: ${document.id}, Data: ${document.data}")
                val buid = document.data["uid"].toString() // get the title

                Log.d("DatabaseFetch","block: ${buid}")
                blockList.add(buid) // add the Quit to list

            }

            callback(blockList)
        }

    }

    fun getBlocks(uid: String):ArrayList<String> {

        var blockList: ArrayList<String> = ArrayList()

        try {
            val docRef = fdb.collection("users").document("/$uid").collection("/blocks")
            val task = docRef.get()
            while (!task.isComplete) {
                Thread.sleep(100)
            }
            val result = task.result

            for (document in result) {
                Log.d("DatabaseFetch", "ID: ${document.id}, Data: ${document.data}")
                val buid = document.data["uid"].toString() // get the title

                Log.d("DatabaseFetch", "friend: $buid")
                blockList.add(buid) // add the Quit to list

            }

        } catch (e: Exception) {
            throw e
        }
        return blockList
    }

    fun addQuit(uid: String, quit: Quit){

        val quitData = hashMapOf(
            "title" to quit.getTitle(),
            "time" to quit.getCalendar().timeInMillis
        )


        fdbUsers.document("/${uid}").collection("/quits")
            .document(quit.getTitle()).set(quitData)
        Log.d("DatabasePush","addQuit: $quitData")

    }

    fun removeQuit(uid: String, quit: Quit){

        fdbUsers.document("/$uid").collection("/quits")
            .document(quit.getTitle()).delete()


    }

    fun addFriend(uid: String, friend: String){

        // TODO: Add checking that they exist

        val friendData = hashMapOf(
            "uid" to friend
        )

        fdbUsers.document("/${uid}").collection("/friends")
            .document(friend).set(friendData)

    }

    fun removeFriend(uid: String, friend: String){

        fdbUsers.document("/$uid").collection("/friends")
            .document(friend).delete()


    }

    fun addBlock(uid: String, block: String){

        // TODO: Add checking that they exist

        val blockData = hashMapOf(
            "uid" to block
        )

        fdbUsers.document("/${uid}").collection("/blocks")
            .document(block).set(blockData)

    }

    fun removeBlock(uid: String, block: String){

        fdbUsers.document("/$uid").collection("/blocks")
            .document(block).delete()


    }

    fun removeUser(uid: String){
        fdbUsers.document("/$uid").delete()
    }

}