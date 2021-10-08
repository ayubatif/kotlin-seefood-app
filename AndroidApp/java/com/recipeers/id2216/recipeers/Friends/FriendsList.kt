package com.recipeers.id2216.recipeers.Friends

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.recipeers.id2216.recipeers.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Messages.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Messages.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FriendsList : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var adapter: FriendsListAdapter
    private var dbUser = FirebaseFirestore.getInstance().collection("Users")
    private var auth = FirebaseAuth.getInstance()
    private var userID: String = ""
    private var friend: MutableMap<String, Any>? = null
    private var profileIDs: ArrayList<String> = ArrayList()
    private var profileNames : ArrayList<String> = ArrayList()
    private var profilePictures : ArrayList<String> = ArrayList()
    private lateinit var friends_list_context: Context
    private lateinit var friendsRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        userID = auth.currentUser!!.uid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val friendsListView = inflater.inflate(R.layout.fragment_friends_list, container, false)
        friendsRecycler = friendsListView.findViewById(R.id.friends_list_recycler)
        friendsRecycler.layoutManager = LinearLayoutManager(context)
        var i=0

        dbUser.document(userID).collection("Friends").get().addOnSuccessListener { friends ->
            profileIDs.clear()
            profileNames.clear()
            profilePictures.clear()
            for (friend in friends.documents) {
                i++
                dbUser.document(friend.id).get().addOnSuccessListener { user ->
                    profileIDs.add(friend.id)
                    profileNames.add(user.get("DisplayName").toString())
                    profilePictures.add((user.get("Image").toString()))
                    if(i == friends.size()){
                        adapter = FriendsListAdapter(profileIDs, profileNames, profilePictures, friends_list_context, object : FriendsListAdapter.AdapterItemClickListener {
                            override fun sendMessage(Box: DocumentReference)  {
                                listener?.goToMessageBox(Box)
                            }
                            override fun updateAdapter() {
                                adapter.notifyDataSetChanged()
                            }
                            override fun openProfile(userID : String){
                                dbUser.document(userID).get().addOnSuccessListener {
                                    listener?.changeToProfile(it.data, userID)
                                }
                            }
                        })
                        friendsRecycler.adapter = adapter
                    }
                }
            }
        }
        return friendsListView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            friends_list_context = context
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
        fun goToMessageBox(Box: DocumentReference)
        fun changeToProfile(profile: MutableMap<String, Any>?, authorID: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Messages.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FriendsList().apply {
                arguments = Bundle().apply {

                }
            }
    }
}