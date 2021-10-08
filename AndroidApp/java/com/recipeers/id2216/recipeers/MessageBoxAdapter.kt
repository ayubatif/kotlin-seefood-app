package com.recipeers.id2216.recipeers

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recipeers.id2216.recipeers.Utils.GlideApp
import kotlinx.android.synthetic.main.message_box.view.*

class MessageBoxAdapter(val profileNames : ArrayList<String>,val messageBoxContext : ArrayList<String>,val profilePictures : ArrayList<String>,
                        val context: Context) : RecyclerView.Adapter<MessageViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return profileNames.size
    }

    // Inflates the item views

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var  view = MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.message_box, parent, false))
        return view
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder?.profileName?.text = profileNames.get(position)
        holder?.messageBoxContext?.text = messageBoxContext.get(position)
        GlideApp.with(gridCont)
            .load(Uri.parse(profilePictures.get(position)))
            .placeholder(R.color.GreyTransparent)
            .into(holder?.profilePic)
    }

}

class MessageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each ingredient to
    val profileName = view.messgae_box_profile_name
    val messageBoxContext = view.message_box_context
    val profilePic = view.message_box_profile_pic

}