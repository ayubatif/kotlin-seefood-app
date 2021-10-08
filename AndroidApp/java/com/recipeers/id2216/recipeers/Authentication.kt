package com.recipeers.id2216.recipeers

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class Authentication : AppCompatActivity(), View.OnClickListener, SignUp.OnFragmentInteractionListener, Login.OnFragmentInteractionListener
    ,   Terms.OnFragmentInteractionListener{
    private lateinit var auth: FirebaseAuth
    private var isSignUpstate =  false
    private var dbUser = FirebaseFirestore.getInstance().collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        // ...
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        if (savedInstanceState == null) {
                var transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentRoot,   Login.newInstance(), "LoginFragment")
                .commit()
        }
        privacy_policy.setOnClickListener(this)
    }



    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.




        /**/



    }


    override fun onClick(v: View?) {
        when (v) {
            privacy_policy -> {
                var transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.abc_popup_enter,R.anim.abc_popup_exit)
                transaction.replace(R.id.fragmentRoot, Terms.newInstance())
                transaction.addToBackStack(null);
                transaction.commit()
            }
        }
    }

    override fun changeToSignUp() {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_slide_right,R.anim.exit_slide_right)
        transaction.replace(R.id.fragmentRoot, SignUp.newInstance())

        transaction.addToBackStack(null);
        transaction.commit()
    }

    override fun changeToLogin(){
        var transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_left)
        transaction.replace(R.id.fragmentRoot, Login.newInstance())
        transaction.addToBackStack(null);
        transaction.commit()
    }





    override fun tryLogin(){

        EmailInput.error = null
        PasswordInput.error = null


        EmailLayOut.error = null
        PasswordLayOut.error = null

        var cancel = false
        var focusView: View? = null

        val emailStr = EmailInput.text.toString()
        val passwordStr = PasswordInput.text.toString()

        if(TextUtils.isEmpty(emailStr) || !isEmailValid(emailStr)){
            EmailLayOut.error = getString(R.string.error_invalid_email)
            EmailInput.error = ""
            focusView = EmailInput
            cancel = true
        }

        if(TextUtils.isEmpty(passwordStr) || !isPasswordValid(passwordStr)){
            PasswordLayOut.error = getString(R.string.error_invalid_password)
            PasswordInput.error = null
            focusView = PasswordInput
            cancel = true
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        }else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
                userSignIn(emailStr, passwordStr)
        }
    }

    override fun trySignUp(){

        DisplayNameInputSignUp.error = null
        EmailInputSignUp.error = null
        PasswordInputSignUp.error = null
        ConfirmPasswordInput.error = null

        DisplayNameLayout.error = null
        EmailLayOutSignUp.error = null
        PasswordLayOutSignUp.error = null
        ConfirmPasswordLayOut.error = null

        var cancel = false
        var focusView: View? = null

        val DisplayNameStr = DisplayNameInputSignUp.text.toString()
        val emailStr = EmailInputSignUp.text.toString()
        val passwordStr = PasswordInputSignUp.text.toString()
        val passwordStr2 = ConfirmPasswordInput.text.toString()

        if(TextUtils.isEmpty(emailStr) || !isEmailValid(emailStr)){
            EmailLayOutSignUp.error = getString(R.string.error_invalid_email)
            EmailInputSignUp.error = ""
            focusView = EmailInputSignUp
            cancel = true
        }

        if(TextUtils.isEmpty(DisplayNameStr)){
            DisplayNameLayout.error = getString(R.string.display_name_error)
            DisplayNameInputSignUp.error = ""
            focusView = DisplayNameInputSignUp
            cancel = true
        }

        if(TextUtils.isEmpty(passwordStr) || !isPasswordValid(passwordStr)){
            PasswordLayOutSignUp.error = getString(R.string.error_invalid_password)
            PasswordInputSignUp.error = null
            focusView = PasswordInputSignUp
            cancel = true
        }

        if(TextUtils.isEmpty(passwordStr) || !isPasswordMatch(passwordStr, passwordStr2)){
            ConfirmPasswordLayOut.error = getString(R.string.error_incorrect_password_confirm)
            ConfirmPasswordInput.error = null
            focusView = PasswordInputSignUp
            cancel = true
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        }else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            userSignUp(DisplayNameStr, emailStr, passwordStr)
        }
    }

    fun userSignIn( email: String, password: String){
            if(isEmailValid(email) && isPasswordValid(password)){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("UserLogin", "signInWithEmail:success")
                            val user = auth.currentUser
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("CURRENT_USER", user)
                            startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Wrong Password / Email",
                                Toast.LENGTH_SHORT).show()
                        }

                        // ...
                    }

            }
    }


    fun userSignUp(displayName: String, email: String, password: String){
        if(isEmailValid(email) && isPasswordValid(password)){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        Log.d("UserSignUp", "signInWithEmail:success")
                        val user = auth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build()
                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("updateStatus", "User Display Name is Set.")
                                    var userData: MutableMap<String, Any?> = mutableMapOf(
                                        "DisplayName" to displayName,
                                        "UserID" to auth.currentUser!!.uid
                                    )
                                    dbUser.document(auth.currentUser!!.uid).set(userData)
                                }
                            }
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("CURRENT_USER", user)
                        startActivity(intent)

                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Unknown Error, Try again?",
                            Toast.LENGTH_SHORT).show()
                    }

                    // ...
                }

        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 6
    }

    private fun isPasswordMatch(password1: String, password2: String): Boolean {
        //TODO: Replace this with your own logic
        return (password1 == password2)
    }

    override fun onBackPressed() {

    }

}
