package com.recipeers.id2216.recipeers

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_camera.*
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.recipeers.id2216.recipeers.SearchRecipes.SearchRecipesActivity


class CameraActivity : AppCompatActivity(), View.OnClickListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        capture.setOnClickListener(this)
        sendBitmap.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v) {
            capture -> {

            }
            sendBitmap -> {

            }
        }
    }




}