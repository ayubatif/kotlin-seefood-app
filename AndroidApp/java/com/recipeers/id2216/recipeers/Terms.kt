package com.recipeers.id2216.recipeers

import android.content.Context
import android.net.Uri
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_terms.*


// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Terms.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Terms.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Terms : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val lay = inflater.inflate(R.layout.fragment_terms, container, false)
        val btn1 = lay.findViewById<FloatingActionButton>(R.id.CloseButton)
        btn1.setOnClickListener(this)
        return lay
    }

    // TODO: Rename method, update argument and hook method into UI event


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
        fun changeToLogin()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Terms.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            Terms().apply {

            }
    }

    override fun onClick(v: View?) {
        when (v) {
            CloseButton -> {listener?.changeToLogin()}
        }
    }
}
