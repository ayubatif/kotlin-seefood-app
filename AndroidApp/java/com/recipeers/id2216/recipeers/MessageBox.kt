package com.recipeers.id2216.recipeers

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.fragment_message_box.*
import java.sql.Timestamp
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MessageBox.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MessageBox.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MessageBox : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var messageBoxRef: DocumentReference
    private var dbUser = FirebaseFirestore.getInstance().collection("Users")
    private var auth = FirebaseAuth.getInstance()
    private var userID: String = ""
    private var profileNames : ArrayList<String> = ArrayList()
    private var messageBoxContext : ArrayList<String> = ArrayList()
    private lateinit var adapter: MessageAdapter
    private lateinit var message_box_context: Context
    private lateinit var recipentUserID: String
    private lateinit var userProfileUrl: String
    private lateinit var recipentUserDisplayName: String
    private lateinit var recipentUserProfileUrl: String
    private lateinit var database: DatabaseReference
    val db = FirebaseFirestore.getInstance()
    var listenerNotification: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        userID = auth.currentUser!!.uid
        messageBoxRef.get().addOnSuccessListener {
            recipentUserID = it.get("UserID").toString()
            dbUser.document(recipentUserID).get().addOnSuccessListener {
                recipentUserDisplayName = it.get("DisplayName").toString()
                recipentUserProfileUrl = it.get("Image").toString()
            }
            //subscribeToMessage(recipentUserID)
        }

        dbUser.document(userID).get().addOnSuccessListener {
            userProfileUrl = it.get("Image").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var messageBoxView = inflater.inflate(R.layout.fragment_message_box, container, false)

        messageBoxRef.collection("MessageBox").addSnapshotListener({ snapshot, e ->
            snapshot!!.query.orderBy("TimeStampt").get().addOnSuccessListener {
                profileNames.clear()
                messageBoxContext.clear()
                for(each in it.documents){
                    Log.e("lolMess" , "" + it.documents.size)
                    profileNames.add(each.get("DisplayName").toString())
                    messageBoxContext.add(each.get("MessageContext").toString())
                }
                adapter = MessageAdapter(profileNames,messageBoxContext, message_box_context)
                var messageRecycler = messageBoxView.findViewById<RecyclerView>(R.id.message_recycler)
                messageRecycler.adapter = adapter
                messageRecycler.layoutManager = LinearLayoutManager(context)
                messageRecycler.smoothScrollToPosition(messageBoxContext.size)
                messageRecycler.addOnItemTouchListener(MessageBoxClickListener(message_box_context, messageRecycler!!, object: MessageBoxClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemLongClick(view: View?, position: Int) {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                }))
            }
        })

        messageBoxView.findViewById<Button>(R.id.send_button).setOnClickListener(this)
        return messageBoxView
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onClick(v: View?) {
        when(v){
            send_button -> {
                if(!message_input.text.isNullOrEmpty()){
                    sendMessage()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            message_box_context = context
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
         * @param param2 Parameter 2.
         * @return A new instance of fragment MessageBox.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(box: DocumentReference) =
            MessageBox().apply {
                arguments = Bundle().apply {
                    messageBoxRef = box
                }
            }
    }


    fun sendMessage(){

        val time = Timestamp(System.currentTimeMillis())

        var message = message_input.text
        message_input.text = null
        val displayName = auth.currentUser!!.displayName
        var messageData: MutableMap<String, Any?> = mutableMapOf(
            "DisplayName" to displayName,
            "MessageContext" to message.toString(),
            "TimeStampt" to time,
            "UserID" to userID
        )
        var messageBoxDataRecipent: MutableMap<String, Any?> = mutableMapOf(
            "Context" to message.toString(),
            "DisplayName" to auth.currentUser!!.displayName,
            "UserID" to auth.currentUser!!.uid,
            "TimeStampt" to time,
            "UserProfilePic" to userProfileUrl
        )

        var messageBoxDataUser: MutableMap<String, Any?> = mutableMapOf(
            "Context" to message.toString(),
            "DisplayName" to recipentUserDisplayName,
            "UserID" to recipentUserID,
            "TimeStampt" to time,
            "UserProfilePic" to recipentUserProfileUrl
        )
        Log.e("timeMessage", recipentUserDisplayName)
        dbUser.document(recipentUserID).collection("Messages").document(auth.currentUser!!.uid).set(messageBoxDataRecipent)
        dbUser.document(recipentUserID).collection("Messages").document(auth.currentUser!!.uid).collection("MessageBox").add(messageData)


        dbUser.document(userID).collection("Messages").document(messageBoxRef.id).set(messageBoxDataUser)
        dbUser.document(userID).collection("Messages").document(messageBoxRef.id).collection("MessageBox").add(messageData)
        database = FirebaseDatabase.getInstance().reference
        var notification: MutableMap<String, Any> = HashMap()
        notification.put("Sender", displayName!!)
        notification.put("Message", message.toString())
        notification.put("Time", time)
        db.collection("Users").document(recipentUserID).collection("Notification").document("Message").set(notification, SetOptions.merge())
        Log.e("test", "attempting to send message")
    }
}

/*
notification database idea of using realtime database by Pontus Asp
 */