package com.example.done

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kryzano.done.Database
import com.kryzano.done.Quit
import com.kryzano.done.User
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import java.util.Calendar



/**
 * User Instrumented Test that used FirebaseAuth and Database.
 * Note that generally one should avoid using a production database to
 * conduct tests. However, seeing that this is a one person project, and
 * that the alternative is to setup a complex local testing server.
 * I decided to move forward with this setup, which is easier for
 * someone to download, setup their own Firestore Database, and use the tests.
 *
 */

@RunWith(AndroidJUnit4::class)
class UserInstrumentedTest {

     lateinit var testUid: String
     lateinit var testUser: User
     lateinit var fuser: FirebaseUser
     val db = Database()

    // Ran before each test, we setup an Anon user to conduct the test
     @Before
     fun setup(){
         FirebaseAuth.getInstance().signInAnonymously()
         fuser = FirebaseAuth.getInstance().currentUser!!
         testUser = User(fuser)
         testUid = testUser.getUid()
         Log.v("Test","UserInstrumentedTest.setup(): uid: ${testUid}")
     }

    // Ran after each test, we delete the user from auth, and then delete
    // their data from the Database
    @After
    fun teardown(){
        fuser.delete()
        db.removeUser(testUid)
        Log.v("Test","UserInstrumentedTest.Teardown(): uid: ${testUid}")

    }

    // Tests Adding a quit
    @Test
    fun addQuit() {
        // Adds Quits
        val testCal1 = Calendar.getInstance()
        val testCal2 = Calendar.getInstance()
        testCal2.timeInMillis = Calendar.getInstance().timeInMillis + 31536000000 // 1 year ahead
        val testQuit1 = Quit("test quit 1", testCal1 )
        val testQuit2 = Quit("test quit 2", testCal2 )
        testUser.addQuit(testQuit1)
        testUser.addQuit(testQuit2)

        // test that they have been added
        val testQuitList = testUser.getQuitList()
        assert(testQuit1 == testQuitList[0])
        assert(testQuit2 == testQuitList[1])
        assert(testQuitList.size == 2)
    }

    // Test removing a quit, will fail if addQuit fails
    @Test
    fun removeQuit() {
        // Adds Quits
        val testCal1 = Calendar.getInstance()
        val testCal2 = Calendar.getInstance()
        testCal2.timeInMillis = Calendar.getInstance().timeInMillis + 31536000000 // 1 year ahead
        val testQuit1 = Quit("test quit 1", testCal1 )
        val testQuit2 = Quit("test quit 2", testCal2 )
        testUser.addQuit(testQuit1)
        testUser.addQuit(testQuit2)

        // test that they have been added
        val testQuitList = testUser.getQuitList()
        assert(testQuit1 == testQuitList[0])
        assert(testQuit2 == testQuitList[1])
        assert(testQuitList.size == 2)


        testUser.removeQuit(testQuit2)
        assert(testQuitList[0] == testQuit1)
        assert(testQuitList.size == 1)

    }

    // similar to quit test
    @Test
    fun addFriend() {
        val testFriend1 = "testFriend1@test.test"
        val testFriend2 = "testFriend2@test.test"
        testUser.addFriend(testFriend1)
        testUser.addFriend(testFriend2)

        val testFriendList = testUser.getFriends()
        assert(testFriendList.size == 2)
        assert(testFriendList[0] == testFriend1)
        assert(testFriendList[1] == testFriend2)


    }

   


}