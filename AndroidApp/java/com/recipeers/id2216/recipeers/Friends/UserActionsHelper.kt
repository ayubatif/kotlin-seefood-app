package com.recipeers.id2216.recipeers.Friends

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.sql.Timestamp
import java.util.ArrayList

class UserActionsHelper {
    private var dbUser = FirebaseFirestore.getInstance().collection("Users")
    private var dbFood = FirebaseFirestore.getInstance().collection("Foods")
    private var auth = FirebaseAuth.getInstance()
    private var userID: String = ""
    private lateinit var foodName: String
    private lateinit var foodImage: String
    private var userFoodRating: Any? = null
    private lateinit var userDisplayName: String
    private lateinit var userProfileUrl: String
    private var actionString: String = "null action"
    private var friendsList: ArrayList<String> = ArrayList()
    private var userActionList : ArrayList<Any?> = ArrayList()

    fun addCreateRecipeAction(foodID: String){
        addUserAction(foodID, "CreateRecipeAction")
    }

    fun addFaveRecipeAction(foodID: String){
        addUserAction(foodID, "FaveRecipeAction")
    }

    fun addRateRecipeAction(foodID: String){
        addUserAction(foodID, "RateRecipeAction")
    }

    private fun addUserAction(foodID: String, collection: String) {
        userID = auth.currentUser!!.uid
        dbUser.document(userID).get().addOnSuccessListener {
            userDisplayName = it.get("DisplayName").toString()
            userProfileUrl = it.get("Image").toString()

            if (!foodID.isEmpty()) {
                dbFood.document(foodID).get().addOnSuccessListener { food ->
                    foodName = food.get("FoodName").toString()
                    foodImage = food.get("ImageUrl").toString()

                    dbFood.document(foodID).collection("Ratings").document(userID).get().addOnSuccessListener { rating ->
                        userFoodRating = rating.get("Rating")

                        when (collection) {
                            "CreateRecipeAction" -> actionString = "Created $foodName"
                            "FaveRecipeAction" -> actionString = "Faved $foodName"
                            "RateRecipeAction" -> actionString =
                                    "Rated $foodName $userFoodRating/5.0"
                        }

                        val time = Timestamp(System.currentTimeMillis())

                        val userActionData: MutableMap<String, Any?> = mutableMapOf(
                            "Context" to actionString,
                            "DisplayName" to userDisplayName,
                            "TimeStamp" to time,
                            "UserProfilePic" to userProfileUrl,
                            "foodID" to foodID,
                            "FoodImage" to foodImage,
                            "Type" to collection
                        )

                        Log.e("timeAction", userID)
                        dbUser.document(userID).collection("Actions").add(userActionData)
                    }
                }
            }
        }
    }

    fun getUserActions(ACTION_COUNT: Int): ArrayList<Any?> {
        userID = auth.currentUser!!.uid
        
        friendsList = FriendsHelper().getFriends()

        for(friendID in friendsList) {
            dbUser.document(friendID).collection("Actions").addSnapshotListener { snapshot, e ->
                snapshot!!.query.orderBy("TimeStamp", Query.Direction.DESCENDING).limit(ACTION_COUNT.toLong()).get().addOnSuccessListener {
                    for (each in it) {
                        userActionList.add(each)
                    }
                }
            }
        }
        return userActionList
    }

}