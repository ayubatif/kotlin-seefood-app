package com.recipeers.id2216.recipeers

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.fragment_comment_page.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private var ARG_PARAM1 : MutableMap<String, Any>? = null
private var ARG_PARAM2 = "param2"
private var db = FirebaseFirestore.getInstance()

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CommentPage.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CommentPage.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CommentPage : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: MutableMap<String, Any>? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var comments = db.collection("Foods").document(ARG_PARAM2).collection("Comments")

    private val auth = FirebaseAuth.getInstance()

    private var counter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = ARG_PARAM1;
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        comments.orderBy("Priority").limit(6).get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                var list = task.result!!.reversed()
                for(document in list){
                    var user = document.data.get("DisplayName").toString()
                    var date = document.data.get("Date").toString()
                    var message = document.data.get("Message").toString()

                    var userView = createTextView(user, 18F)
                    var dateView = createTextView(date, 14F)
                    var messageView = createTextView(message+"\n", 24F)

//                    var userView = TextView(context)
//                    userView.text = user
//                    userView.setTextSize(0, 18F)
//
//                    var dateView = TextView(context)
//                    dateView.text = date
//                    dateView.setTextSize(0, 14F)
//
//
//                    var messageView = TextView(context)
//                    messageView.text = message
//                    dateView.setTextSize(0, 24F)

                    scroll_layout.addView(userView)
                    scroll_layout.addView(dateView)
                    scroll_layout.addView(messageView)
                }
            }
        }


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_comment_page, container, false)
        view.findViewById<Button>(R.id.button).setOnClickListener(this)

//        button.setOnClickListener(this)
        return view
    }
    //db.collection("Foods").document(ARG_PARAM2).get().addOnSuccessListener { documentSnapshot ->
//                    val name = documentSnapshot?.data?.get("FoodName").toString()
//                    println("FoodName: $name")
//                }
    // TODO: Rename method, update argument and hook method into UI event
//    fun sendComment(view : View) {
//        val message = editText.text.toString()
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
//    }

    private fun createTextView(content : String, textSize : Float) : TextView{
        var view = TextView(context)
        view.text = content
        view.setTextSize(0, textSize)
        return view
    }

    override fun onClick(v : View){
        when(v){
            button -> {
                v.hideKeyboard()

                val date = Date()
                //println("Date: $date")
                //println("Date(long): ${date.time}")
                val message = editText.text.toString()
                editText.setText("")
                //Log.e("Comment Sent", message)

                var username: String? = auth.currentUser!!.displayName
                println("Current UserID: ${auth.currentUser!!.uid}")
                println("Raw Username: $username")
                if (username === null || username.isEmpty()) {
                    username = "Anonymous"
                }
                val userView = createTextView(username, 18F)

                val dateView = createTextView(date.toString(), 14F)

                val comment = createTextView(message+"\n", 24F)

                var fields: MutableMap<String, Any?> = HashMap()
                fields.put("DisplayName", username)
                fields.put("Date", date.toString())
                fields.put("Priority", (0-date.time))
                fields.put("Message", message)

                scroll_layout.addView(userView)
                scroll_layout.addView(dateView)
                scroll_layout.addView(comment)

                val commentID = "${auth.currentUser!!.uid}${date.time}"
                //println("FoodID: $ARG_PARAM2")
                //println("CommentID: $commentID")
                db.collection("Foods").document(ARG_PARAM2)
                    .collection("Comments").document(commentID).set(fields)
            }
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
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
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommentPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: MutableMap<String, Any>?, param2: String) =
            CommentPage().apply {
                arguments = Bundle().apply {
                    ARG_PARAM1 = param1
                    ARG_PARAM2 = param2
                }
            }
    }
}
