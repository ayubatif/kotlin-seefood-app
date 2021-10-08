package com.recipeers.id2216.recipeers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sign_up.*


// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SignUp.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SignUp.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SignUp : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val lay = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val btn1 = lay.findViewById<Button>(R.id.SignUpButtonSignUp)
        val btn2 = lay.findViewById<Button>(R.id.LogInButtonSignUp)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
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
    override fun onClick(v: View?) {
        when (v) {
            LogInButtonSignUp -> { listener?.changeToLogin()}
            SignUpButtonSignUp ->{listener?.trySignUp()}
        }
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun changeToLogin()
        fun trySignUp()

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignUp.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SignUp().apply {

            }
    }
}
