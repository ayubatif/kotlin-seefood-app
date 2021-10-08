package com.recipeers.id2216.recipeers

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recipeers.id2216.recipeers.Utils.GlideApp
import kotlinx.android.synthetic.main.message_box.view.*

class MessageAdapter(val profileNames : ArrayList<String>,val messageBoxContext : ArrayList<String>,
                        val context: Context) : RecyclerView.Adapter<MessageBoxHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return profileNames.size
    }

    // Inflates the item views

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageBoxHolder {
        var  view = MessageBoxHolder(LayoutInflater.from(context).inflate(R.layout.message, parent, false))
        return view
    }

    override fun onBindViewHolder(holder: MessageBoxHolder, position: Int) {
        holder?.profileName?.text = profileNames.get(position)
        holder?.messageBoxContext?.text = messageBoxContext.get(position)
    }

}

class MessageBoxHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each ingredient to
    val profileName = view.messgae_box_profile_name
    val messageBoxContext = view.message_box_context

}