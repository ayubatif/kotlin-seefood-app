package com.recipeers.id2216.recipeers.Friends

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.recipeers.id2216.recipeers.MainActivity
import java.util.ArrayList

class FriendsHelper {
    private var dbUser = FirebaseFirestore.getInstance().collection("Users")
    private var auth = FirebaseAuth.getInstance()
    private var userID: String = ""
    private var friendsListArray: ArrayList<String> = ArrayList()

    private fun initUser() {
        userID = auth.currentUser!!.uid
    }

    fun addFriend(friendID: String){
        initUser()
        var userProfile: MutableMap<String, Any?> = mutableMapOf(
            "ID" to friendID
        )
        var mainUserProfile: MutableMap<String, Any?> = mutableMapOf(
            "ID" to friendID
        )
        dbUser.document(userID).collection("Friends").document(friendID).set(userProfile)

        dbUser.document(friendID).collection("Friends").document(userID).set(mainUserProfile)
    }

    fun getFriends(): ArrayList<String> {
        initUser()
        dbUser.document(userID).collection("Friends").get().addOnSuccessListener {
            for(friend in it.documents){
                friendsListArray.add(friend.reference.path.split('/')[3])
            }
        }
        return friendsListArray
    }

    fun getFriendMessageBox(friendID: String): DocumentReference {
        initUser()
        Log.e("messageBox", friendID)
        Log.e("messageBox", userID)
        return dbUser.document(userID).collection("Messages").document(friendID)
    }



    fun removeFriend(friendID: String){
        initUser()
        dbUser.document(userID).collection("Friends").document(friendID).delete()
        dbUser.document(friendID).collection("Friends").document(userID).delete()

    }
}