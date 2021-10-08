package com.recipeers.id2216.recipeers.Friends

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recipeers.id2216.recipeers.R
import com.recipeers.id2216.recipeers.Utils.GlideApp
import kotlinx.android.synthetic.main.layout_action_feed_item.view.*

class ActionFeedAdapter(
    val context: Context,
    val actionList: ArrayList<UserActionItem>,
    val listen: AdapterItemClickListener?
) : RecyclerView.Adapter<ActionViewHolder>() {

    private lateinit var view: ActionViewHolder

    override fun getItemCount(): Int {
        return actionList.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        view = ActionViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_action_feed_item, parent, false))
        return view
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        holder?.profileName?.text = actionList[position].displayName
        GlideApp.with(context)
            .load(Uri.parse(actionList[position].userImage))
            .placeholder(R.color.GreyTransparent)
            .into(holder?.profilePic)
        GlideApp.with(context)
            .load(Uri.parse(actionList[position].foodImage))
            .placeholder(R.color.GreyTransparent)
            .into(holder?.foodImage)
        holder?.actionContext?.text = actionList[position].context

        holder?.profilePic.setOnClickListener {
            listen?.openProfile(actionList[position].userID!!)
        }
        holder.foodImage.setOnClickListener {
            listen?.openFood(actionList[position].foodID!!)
        }
    }

    interface AdapterItemClickListener {
        fun openProfile(userID: String)
        fun openFood(foodID: String)
    }
}

class ActionViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val profileName = view.action_feed_profile_name!!
    val profilePic = view.action_feed_profile_pic!!
    val foodImage = view.action_feed_target_pic!!
    val actionContext = view.action_feed_context!!
}