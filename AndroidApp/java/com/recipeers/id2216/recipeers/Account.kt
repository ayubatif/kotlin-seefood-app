package com.recipeers.id2216.recipeers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.recipeers.id2216.recipeers.Utils.GlideApp
import kotlinx.android.synthetic.main.activity_account.*

class Account : AppCompatActivity(), View.OnClickListener {

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private var db = FirebaseFirestore.getInstance()
    private val userID = auth.currentUser!!.uid
    private val storage = FirebaseStorage.getInstance()
    
    val storageRef = storage.reference
    val PHOTO_CODE = 1046

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val userEmail = user!!.email
        val userName = user!!.displayName
        accountEmail.text = userEmail
        accountUsername.text = userName
        accountButtonChangePicture.setOnClickListener(this)
        db.collection("Users").document(userID).get().addOnSuccessListener {
            GlideApp.with(this)
                .load(Uri.parse(it.get("Image").toString()))
                .placeholder(R.drawable.loading_circle)
                .into(accountProfilePicture)
        }
    }

    //sends the user a reset password email
    fun changePassword(v: View?) {
        val userEmail = user!!.email
        auth.sendPasswordResetEmail(userEmail.toString())
        Toast.makeText(baseContext, "Reset Email Sent", Toast.LENGTH_LONG).show()
        startActivity(MainActivity.logOutIntent(this))
    }

    override fun onClick(v: View?) {
        when (v) {
            accountButtonChangePicture -> {
                selectPhoto()
            }
        }
    }

    fun selectPhoto() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        if (intent.resolveActivity(packageManager) != null)
            startActivityForResult(intent, PHOTO_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            val ImageUri = data.data
            // Do something with the photo based on Uri
            val selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri)

            var file = ImageUri

            val imageRef = storageRef.child("Users/" + auth.currentUser!!.uid)
            // Get the data from an ImageView as bytes
            val uploadTask = imageRef.putFile(file)

            val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation imageRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    db.collection("Users").document(userID)
                        .update("Image", downloadUri.toString())
                        .addOnSuccessListener {
                            Log.d("Database", "DocumentSnapshot successfully updated!")
                            Toast.makeText(this, "Profile Picture Updated :)", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e -> Log.w("Database", "Error writing document", e) }
                } else {
                    // Handle failures
                    // ...
                }
            }
            // Load the selected image into a preview
            accountProfilePicture.setImageBitmap(selectedImage)
        }
    }
}
