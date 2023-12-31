package com.kryzano.done
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar


// TODO: Could make a private attribute uid, instead of always passing it to the method

// Note, Database().getWhatever() halts code execution.
// And Database().fetchWhatever() provides a callback for asynchronous execution
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
    fun pushUsername(uid: String, username: String){
        Log.d("DatabasePush","pushed username $username to $uid")

        fdbUsers.document("/${uid}").update("username", username)

    }

    /**
     * Pushed a new email to the Firestore given a uid
     *
     * Args: uid:String, email:String
     */
    fun pushEmail(uid: String, email: String){
        Log.d("DatabasePush","pushed email")

        fdbUsers.document("/${uid}").update("email", email)

    }

    /**
     * Fetches the username from a given uid (might be redundant since
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

    /**
     * Gets username given a uid
     *
     * Args: uid:String
     * Returns: String
     */
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

        Log.d("DatabaseFetch","returned: $username")
        return username

    }



    /**
     * Fetches the quits given a users uid (usually email).
     * method gives a call back for asynchronous execution
     * see: https://stackoverflow.com/questions/57330766/why-does-my-function-that-calls-an-api-or-launches-a-coroutine-return-an-empty-o/57330767#57330767
     *
     * Args: uid: String
     * Return: None
     * Result: ArrayList<Quit>
     */
    fun fetchQuits(uid: String, callback:(ArrayList<Quit>) -> Unit){

        val docRef = fdb.collection("users").document("/$uid")
        docRef.collection("/quits").get().addOnSuccessListener {
            val quitList: ArrayList<Quit> = ArrayList()
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
     * Gets a uid given an email
     *
     * Args: email:String
     * Returns: String
     */
    fun getUidFromEmail(email: String): String{
        var uid = ""

        try {

            val query = fdb.collection("users").whereEqualTo("email", email)
            val task = query.get()

            while (!task.isComplete){
                Thread.sleep(10)
            }

            val result = task.result

            uid = result.documents[0].id

            Log.d("Database", "uid $uid")


        } catch (exception: Exception) {
            Log.e("Database", exception.toString())
        }

        return uid
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

        val quitList: ArrayList<Quit> = ArrayList()

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
     * method gives a call back for asynchronous execution
     * see: https://stackoverflow.com/questions/57330766/why-does-my-function-that-calls-an-api-or-launches-a-coroutine-return-an-empty-o/57330767#57330767
     *
     * Args: uid: String
     * Return: None
     * Result: ArrayList<String>
     */
    fun fetchFriends(uid: String, callback:(ArrayList<String>) -> Unit){

        val docRef = fdb.collection("users").document("/$uid")
        docRef.collection("/friends").get().addOnSuccessListener {
            val friendList: ArrayList<String> = ArrayList()
            val result = it
            for (document in result) {
                Log.d("DatabaseFetch","ID: ${document.id}, Data: ${document.data}")
                val friendUid = document.data["uid"].toString() // get the title

                Log.d("DatabaseFetch","friend: $friendUid")
                friendList.add(friendUid) // add the Quit to list

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

        val friendList: ArrayList<String> = ArrayList()

        try {
            val docRef = fdb.collection("users").document("/$uid").collection("/friends")
            val task = docRef.get()
            while (!task.isComplete) {
                Thread.sleep(100)
            }
            val result = task.result

            for (document in result) {
                Log.d("DatabaseFetch", "ID: ${document.id}, Data: ${document.data}")
                val friendUid = document.data["uid"].toString() // get the title

                Log.d("DatabaseFetch", "friend: $friendUid")
                friendList.add(friendUid) // add the Quit to list

            }

        } catch (e: Exception) {
            throw e
        }
        return friendList
    }

    /**
     * Fetches the blocks given a users uid (usually email).
     * method gives a call back for asynchronous execution
     * see: https://stackoverflow.com/questions/57330766/why-does-my-function-that-calls-an-api-or-launches-a-coroutine-return-an-empty-o/57330767#57330767
     *
     * Args: uid: String
     * Return: None
     * Result: ArrayList<String>
     */
    fun fetchBlocks(uid: String, callback:(ArrayList<String>) -> Unit){

        val docRef = fdb.collection("users").document("/$uid")
        docRef.collection("/blocks").get().addOnSuccessListener {
            val blockList: ArrayList<String> = ArrayList()
            val result = it
            for (document in result) {
                Log.d("DatabaseFetch","ID: ${document.id}, Data: ${document.data}")
                val blockUid = document.data["uid"].toString() // get the title

                Log.d("DatabaseFetch","block: $blockUid")
                blockList.add(blockUid) // add the Quit to list

            }

            callback(blockList)
        }

    }

    /**
     * Gets blocks given a uid
     *
     * Args: uid:String
     * Returns: ArrayList<String>
     */
    fun getBlocks(uid: String):ArrayList<String> {

        val blockList: ArrayList<String> = ArrayList()

        try {
            val docRef = fdb.collection("users").document("/$uid").collection("/blocks")
            val task = docRef.get()
            while (!task.isComplete) {
                Thread.sleep(100)
            }
            val result = task.result

            for (document in result) {
                Log.d("DatabaseFetch", "ID: ${document.id}, Data: ${document.data}")
                val blockUid = document.data["uid"].toString() // get the title

                Log.d("DatabaseFetch", "friend: $blockUid")
                blockList.add(blockUid) // add the Quit to list

            }

        } catch (e: Exception) {
            throw e
        }
        return blockList
    }

    /**
     * Adds a quit to Firestore given a uid
     *
     * Args: uid:String, quit:Quit
     * Returns: None
     */
    fun addQuit(uid: String, quit: Quit){

        val quitData = hashMapOf(
            "title" to quit.getTitle(),
            "time" to quit.getCalendar().timeInMillis
        )


        fdbUsers.document("/${uid}").collection("/quits")
            .document(quit.getTitle()).set(quitData)
        Log.d("DatabasePush","addQuit: $quitData")

    }

    /**
     * Removes a quit from Firestore given a uid
     *
     * Args: uid:String, quit:Quit
     * Returns: None
     */
    fun removeQuit(uid: String, quit: Quit){

        fdbUsers.document("/$uid").collection("/quits")
            .document(quit.getTitle()).delete()


    }

    /**
     * Adds a friend to Firestore given a uid
     *
     * Args: uid:String, friend:String
     * Returns: None
     */
    fun addFriend(uid: String, friend: String){

        // TODO: Add checking that they exist

        val friendData = hashMapOf(
            "uid" to friend
        )

        fdbUsers.document("/${uid}").collection("/friends")
            .document(friend).set(friendData)

    }

    /**
     * Removes a friend from Firestore given a uid
     *
     * Args: uid:String, friend:String
     * Returns: None
     */
    fun removeFriend(uid: String, friend: String){

        fdbUsers.document("/$uid").collection("/friends")
            .document(friend).delete()


    }

    /**
     * Adds a block to Firestore given a uid
     *
     * Args: uid:String, block:String
     * Returns: None
     */
    fun addBlock(uid: String, block: String){

        // TODO: Add checking that they exist

        val blockData = hashMapOf(
            "uid" to block
        )

        fdbUsers.document("/${uid}").collection("/blocks")
            .document(block).set(blockData)

    }

    /**
     * removes a block from Firestore given a uid
     *
     * Args: uid:String, block:String
     * Returns: None
     */
    fun removeBlock(uid: String, block: String){

        fdbUsers.document("/$uid").collection("/blocks")
            .document(block).delete()


    }

    /** removes a user from Firestore given their uid
     *
     * Args: uid:String
     * Returns: None
     */
    fun removeUser(uid: String){
        fdbUsers.document("/$uid").delete()
    }

}