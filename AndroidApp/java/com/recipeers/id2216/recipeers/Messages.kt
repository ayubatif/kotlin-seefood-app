package com.recipeers.id2216.recipeers

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


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
class Messages : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var adapter: MessageBoxAdapter
    private var dbUser = FirebaseFirestore.getInstance().collection("Users")
    private var auth = FirebaseAuth.getInstance()
    private var userID: String = ""
    private var profileNames : ArrayList<String> = ArrayList()
    private var messageBoxContext : ArrayList<String> = ArrayList()
    private var profilePictures : ArrayList<String> = ArrayList()
    private var MessageBoxRefs : ArrayList<DocumentReference> = ArrayList()
    private lateinit var message_context: Context
    private var counter = -1
    private var messagePosition = -1

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
        var messageView = inflater.inflate(R.layout.fragment_messages, container, false)
        val bundleCheck = arguments?.getString("Sender")
        dbUser.document(userID).collection("Messages").addSnapshotListener({ snapshot, e ->
            snapshot!!.query.orderBy("TimeStampt", Query.Direction.DESCENDING).get().addOnSuccessListener {
                profileNames.clear()
                messageBoxContext.clear()
                profilePictures.clear()
                for(each in it.documents){
                    Log.e("lolMess" , "" + it.documents.size)
                    MessageBoxRefs.add( dbUser.document(userID).collection("Messages").document(each.id))
                    profileNames.add(each.get("DisplayName").toString())
                    messageBoxContext.add(each.get("Context").toString())
                    profilePictures.add(each.get("UserProfilePic").toString())
                    counter += 1
                    if (bundleCheck != null && bundleCheck === each.get("Displayname")) {
                        messagePosition = counter
                    }
                }
                adapter = MessageBoxAdapter(profileNames,messageBoxContext,profilePictures,message_context)
                var messageRecycler = messageView.findViewById<RecyclerView>(R.id.message_recycler)
                messageRecycler.adapter = adapter
                messageRecycler.layoutManager = LinearLayoutManager(context)
                if (messagePosition != -1) {
                    Log.e("test", "pos " + messagePosition)
                }
                messageRecycler.addOnItemTouchListener(MessageBoxClickListener(message_context, messageRecycler!!, object: MessageBoxClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        Log.e("test", position.toString())
                        listener?.goToMessageBox(MessageBoxRefs[position])
                    }

                    override fun onItemLongClick(view: View?, position: Int) {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                }))
            }
        })
        return messageView
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            message_context = context
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
        fun goToMessageBox(Box: DocumentReference)
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
            Messages().apply {
                arguments = Bundle().apply {

                }
            }
    }
}


class MessageBoxClickListener(
    context: Context,
    recyclerView: RecyclerView,
    private val mListener: OnItemClickListener?
) : RecyclerView.OnItemTouchListener {

    private val mGestureDetector: GestureDetector

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)

        fun onItemLongClick(view: View?, position: Int)
    }

    init {

        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)

                if (childView != null && mListener != null) {
                    mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)

        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
        }

        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}



