package com.recipeers.id2216.recipeers

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.recipeers.id2216.recipeers.Friends.FriendsHelper
import com.recipeers.id2216.recipeers.Utils.GlideApp
import kotlinx.android.synthetic.main.fragment_other_user_profile.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private var ARG_PARAM1 : MutableMap<String, Any>? = null
private var ARG_PARAM2 : String = ""

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OtherUserProfile.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OtherUserProfile.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class OtherUserProfile : Fragment(), View.OnClickListener {
    private var db = FirebaseFirestore.getInstance()
    // TODO: Rename and change types of parameters
    private var param1: MutableMap<String, Any>? = null
    private var listener: OnFragmentInteractionListener? = null
    private val mUser = FirebaseAuth.getInstance().currentUser
    private var isFriend: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = ARG_PARAM1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isFriend = false
        // Inflate the layout for this fragment
        val oUserProfileView = inflater.inflate(R.layout.fragment_other_user_profile, container, false)
        val txtAddFriend = oUserProfileView.findViewById<TextView>(R.id.add_friend_btn)
        //db.collection("Users").document()
        Log.d("ARG_PARAM1",ARG_PARAM1.toString())
        GlideApp.with(this)
            .load(Uri.parse(ARG_PARAM1!!.get("Image").toString()))
            .placeholder(R.drawable.loading_circle)
            .into(oUserProfileView.findViewById(R.id.user_avatar))

        oUserProfileView.findViewById<TextView>(R.id.username).text = ARG_PARAM1!!.get("DisplayName").toString()
        db.collection("Users").document(ARG_PARAM2).collection("Friends").document(mUser!!.uid).get().addOnSuccessListener {
            /*it.forEach {
                if(it.id == mUser!!.uid){
                    isFriend = true
                }
            }*/

            isFriend = it.exists()

            Log.e("friend", it.exists().toString())
            //isFriend = it.exists()
            oUserProfileView.findViewById<Button>(R.id.add_friend_btn).setOnClickListener(this)
            if(isFriend){
                txtAddFriend.text = "Remove Friend"
            }
            else{
                txtAddFriend.text = "Add Friend"
            }
        }.addOnFailureListener { exception ->
            Log.e("friend", "get failed with ", exception)
        }





        return oUserProfileView
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onClick(v: View?) {
        when(v){
            add_friend_btn -> {
                if(isFriend){
                    isFriend = false
                    add_friend_btn.text = "Add Friend"
                    FriendsHelper().removeFriend(ARG_PARAM2)
                } else {
                    isFriend = true
                    add_friend_btn.text = "Remove Friend"
                    FriendsHelper().addFriend(ARG_PARAM2)
                }
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment OtherUserProfile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1:  MutableMap<String, Any>?, param2: String) =
            OtherUserProfile().apply {
                ARG_PARAM1 = param1
                ARG_PARAM2 = param2
            }
    }
}