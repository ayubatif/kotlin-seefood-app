package com.recipeers.id2216.recipeers.Friends

import java.sql.Timestamp

class UserActionItem {
    var userID: String? = null
    var displayName: String? = null
    var userImage: String? = null
    var foodID: String? = null
    var foodImage: String? = null
    var context: String? = null
    var timeStamp: String? = null


    constructor(
        listUserID: String,
        listUserName: String,
        listUserImage: String,
        listFoodID: String,
        listFoodImage: String,
        listContext: String,
        listTimeStamp: String
    ){
        userID = listUserID
        displayName = listUserName
        userImage = listUserImage
        foodID = listFoodID
        foodImage = listFoodImage
        context = listContext
        timeStamp = listTimeStamp
    }

}