package com.recipeers.id2216.recipeers.Friends

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.recipeers.id2216.recipeers.R
import com.google.firebase.auth.FirebaseAuth
import com.recipeers.id2216.recipeers.MainActivity
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ItemGrid.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ItemGrid.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ActionFeed : Fragment() {
    // TODO: Rename and change types of parameters
    private var dbUser = FirebaseFirestore.getInstance().collection("Users")
    private var dbFood = FirebaseFirestore.getInstance().collection("Foods")
    private var auth = FirebaseAuth.getInstance()
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var adapter: ActionFeedAdapter? = null
    private var userID: String = ""
    private var actionsList: ArrayList<UserActionItem> = ArrayList()
    private lateinit var mContext: Context
    private lateinit var friendsRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        if(auth.currentUser != null) {
            userID = auth.currentUser!!.uid
        }
        adapter = ActionFeedAdapter(mContext, ArrayList(), null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val actionFeedView = inflater.inflate(R.layout.fragment_friends_action_feed, container, false)
        friendsRecycler = actionFeedView.findViewById(R.id.friends_actions_recycler)
        friendsRecycler.layoutManager = LinearLayoutManager(context)
        val friendsRef = dbUser.document(userID).collection("Friends")
        var friendID: String
        var i=0
        var j=0

        friendsRef.get().addOnSuccessListener { friends ->
            actionsList.clear()
            for (friend in friends) {
                i++
                friendID = friend.reference.path.split('/')[3]
                dbUser.document(friendID).collection("Actions").limit(5).get()
                    .addOnCompleteListener { actions ->
                        if (actions.isSuccessful) {
                            for (action in actions.result!!) {
                                j++
                                actionsList.add(
                                    UserActionItem(
                                        friendID,
                                        action.data.get("DisplayName").toString(),
                                        action.data.get("UserProfilePic").toString(),
                                        action.data.get("foodID").toString(),
                                        action.data.get("FoodImage").toString(),
                                        action.data.get("Context").toString(),
                                        action.data.get("TimeStamp").toString()
                                    )
                                )

                                if (i == friends.size() && j == actions.result!!.size()) {
                                    actionsList = ArrayList(
                                        actionsList.sortedWith(
                                            compareBy {
                                                it.timeStamp
                                            }
                                        ).asReversed()
                                    )
                                    adapter = ActionFeedAdapter(
                                        mContext,
                                        actionsList,
                                        object : ActionFeedAdapter.AdapterItemClickListener {
                                            override fun openFood(foodID: String) {
                                                dbFood.document(foodID).get().addOnSuccessListener {
                                                    listener?.onFoodItemClicked(it.data, foodID)
                                                }
                                            }

                                            override fun openProfile(userID: String) {
                                                dbUser.document(userID).get().addOnSuccessListener {
                                                    listener?.changeToProfile(it.data, userID)
                                                }
                                            }
                                        })
                                    friendsRecycler.adapter = adapter
                                }
                            }
                        } else {
                            Log.e("looool", "error")
                        }
                    }
            }

            if(friends.isEmpty){
                actionsList.add(UserActionItem("hibvkugv5647", "Inga Kompisar",
                    "https://i.kym-cdn.com/photos/images/original/001/047/724/785.png", "vjvyr65uvutvu686r65c",
                    "https://ih0.redbubble.net/image.494693388.6395/flat,1000x1000,075,f.u1.jpg", "Empty recycler view LUL",
                    "nov21"))

                adapter = ActionFeedAdapter(mContext, actionsList, null)
                val friendsRecycler = actionFeedView.findViewById<RecyclerView>(R.id.friends_actions_recycler)
                friendsRecycler.adapter = adapter
                friendsRecycler.layoutManager = LinearLayoutManager(context)
            }
        }
        return actionFeedView
    }
    // TODO: Rename method, update argument and hook method into UI event

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mContext = context
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFoodItemClicked(food: MutableMap<String, Any>?, foodID: String)
        fun changeToProfile(user: MutableMap<String, Any>?, userID: String)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ItemGrid.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ActionFeed().apply {
                arguments = Bundle().apply {
                }
            }
    }
}




