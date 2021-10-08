package com.recipeers.id2216.recipeers.SearchRecipes

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.firestore.FirebaseFirestore
import com.recipeers.id2216.recipeers.FoodCard
import com.recipeers.id2216.recipeers.R


class SearchRecipesActivity : AppCompatActivity(), FoodCard.OnFragmentInteractionListener {

    private var db = FirebaseFirestore.getInstance()
    private var foods: MutableList<MutableMap<String, Any>?> = mutableListOf <MutableMap<String, Any>?>()
    private var foodIds: ArrayList<String> = ArrayList(1)
    private var foodCardList: ArrayList<Fragment> = ArrayList(1)
    private var size = 0
    private var documentCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipes)
        runML(intent.getParcelableExtra("CAPTURED_BITMAP"))
    }
    /**
     * Sets up the foodCard viewpager
     * Handles empty and non empty foodList
     */
    private fun setupViewPager() {
        Log.e("SetupViewPager", "loaded")
        val viewPager = findViewById<ViewPager>(R.id.search_pager)
        val adapter = SlidePagerStateAdapter(supportFragmentManager)
        if(!foodIds.isEmpty()) {
            //val viewpagerContainer = findViewById<FrameLayout>(R.id.viewpager_contatiner)
            //viewpagerContainer.visibility = View.INVISIBLE

            adapter.setFoods(foods)
            adapter.setFoodIds(foodIds)
            viewPager.setPageTransformer(true, ZoomOutPageTransformer())
        } else {
            //val noFoodPage = findViewById<TextView>(R.id.no_food_page)
            //noFoodPage.visibility = View.VISIBLE
        }
        viewPager.offscreenPageLimit = 0
        viewPager.adapter = adapter
    }

    override fun changeToProfile(profile: MutableMap<String, Any>?, authorID: String) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    fun getSearchResults(id: String){
        val docRef = db.collection("Foods").document(id)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                documentCount++
                val document = task.result
                if (document!!.exists()) {
                    foods.add(document.data)
                    foodIds.add(document.id)
                    Log.e("Food Name", document.data!!.get("FoodName").toString())
                    Log.e("documentCount", documentCount.toString())
                    Log.e("size", size.toString())
                    if(documentCount == size){
                        setFoodCardList()
                    }
                } else {
                    Log.d("getSearchResults", "No such document")
                }
            } else {
                Log.d("lol", "get failed with ", task.exception)
            }
        }
    }
    fun setFoodCardList(){
        var i = 0
        for(food in foods){
            Log.e("setFoodCard", food!!.get("FoodName").toString())
            foodCardList.add(FoodCard.newInstance(food, foodIds[i]))
            i++
            if(i == foods.size){
                setupViewPager()
            }
        }
    }

    fun runML(bitmap: Bitmap) {
        val options = FirebaseVisionCloudDetectorOptions.Builder()
            .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
            .setMaxResults(15)
            .build()
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance()
            .visionCloudLabelDetector
        val result = detector.detectInImage(image)
            .addOnSuccessListener {
                // Task completed successfully
                // ...
                for(label in it){
                    //var foodIDList: MutableList<String> = mutableListOf<String>()
                    Log.e("ML", label.label)
                    val  id:String = label.label.get(0).toUpperCase() + label.label.subSequence(1, label.label.length).toString()
                    db.collection("Ingredients/" + id + "/Foods")
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (document in task.result!!) {
                                    size++
                                    getSearchResults(document.id)
                                    Log.e("ID", document.id)
                                    Log.e("size", task.result!!.size().toString())
                                }
                            } else {
                                Log.e("lool", "error")
                            }
                        }
                }
                val loadingCircle = findViewById<ProgressBar>(R.id.progressBar)
                loadingCircle.visibility = View.INVISIBLE
            }
            .addOnFailureListener(
                object : OnFailureListener {
                    override fun onFailure(e: Exception) {
                        // Task failed with an exception
                        // ...
                        Log.e("Label", "fail to label")
                    }
                })
    }
}
