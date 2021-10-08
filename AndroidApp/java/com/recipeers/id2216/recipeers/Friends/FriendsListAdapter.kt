package com.recipeers.id2216.recipeers.Friends

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.recipeers.id2216.recipeers.MainActivity
import com.recipeers.id2216.recipeers.R
import com.recipeers.id2216.recipeers.Utils.GlideApp
import kotlinx.android.synthetic.main.layout_friend_item.view.*

class FriendsListAdapter(
    val profileIDs: ArrayList<String>,
    val profileNames: ArrayList<String>,
    val profilePictures: ArrayList<String>,
    val context: Context, val listen: AdapterItemClickListener) : RecyclerView.Adapter<FriendViewHolder>() {

    private var xprofileIDs: ArrayList<String> = ArrayList()
    private var xprofileNames: ArrayList<String> = ArrayList()
    private var xprofilePictures: ArrayList<String> = ArrayList()

    override fun getItemCount(): Int {
        return profileIDs.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        var view = FriendViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_friend_item, parent, false))
        return view
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder?.profileName.text = profileNames[position]
        GlideApp.with(context)
            .load(Uri.parse(profilePictures[position]))
            .placeholder(R.color.GreyTransparent)
            .into(holder?.profilePic)
        holder?.viewProfile.setOnClickListener {
            //TODO: Open user profile with property profileIDs[position]
            listen.openProfile(profileIDs[position])
        }
        holder?.removeFriend.setOnClickListener {
            //TODO: Remove friend with property profileIDs[position]
            FriendsHelper().removeFriend(profileIDs[position])
            listen.updateAdapter()
            profileIDs.removeAt(position)
            profileNames.removeAt(position)
            profilePictures.removeAt(position)
        }
        holder?.messageFriend.setOnClickListener {
            //TODO: Message friend with property profileIDs[position]
            listen.sendMessage(FriendsHelper().getFriendMessageBox(profileIDs[position]))
        }
    }

    interface AdapterItemClickListener {
        // TODO: Update argument type and name
        fun sendMessage(Box: DocumentReference)
        fun openProfile(userID: String)
        fun updateAdapter()
    }
}

class FriendViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val profileName = view.friend_list_profile_name!!
    val profilePic = view.friend_list_profile_pic!!
    val viewProfile = view.viewProfileButton!!
    val removeFriend = view.removeFriendButton!!
    val messageFriend = view.friend_list_message!!
}