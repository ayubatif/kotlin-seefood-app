package com.recipeers.id2216.recipeers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_create_recipe.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.recipeers.id2216.recipeers.Friends.UserActionsHelper


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private var ARG_PARAM1: ArrayList<String> = ArrayList()
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CreateRecipe.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CreateRecipe.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CreateRecipe : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private val storage = FirebaseStorage.getInstance();
    private var param1: String? = null
    private var param2: String? = null
    private val auth = FirebaseAuth.getInstance()
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var ingredientsView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dialogBuilder: AlertDialog.Builder
    private val selectedIngredients: ArrayList<String> = ArrayList()
    private lateinit var imagePreview: ImageView
    private lateinit var fraqmentContext: Context
    private lateinit var ImageUri: Uri
    val storageRef = storage.reference
    private lateinit var selectedImage: Bitmap
    val PHOTO_CODE = 1046


    private lateinit var foodId: String
    private lateinit var foodName: String
    private lateinit var foodDescription: String
    private lateinit var foodRecipe: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //ingredients = ARG_PARAM1
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val recipeView = inflater.inflate(R.layout.fragment_create_recipe, container, false)
        imagePreview = recipeView.findViewById<ImageView>(R.id.food_image)
        ingredientsView = recipeView.findViewById<ListView>(R.id.food_list)
        imagePreview.setOnClickListener(this)
        recipeView.findViewById<Button>(R.id.CancelButton).setOnClickListener(this)
        recipeView.findViewById<Button>(R.id.addIngredients).setOnClickListener(this)
        recipeView.findViewById<Button>(R.id.SubmitButton).setOnClickListener(this)
        return recipeView
    }


    override fun onClick(v: View?) { //To change body of created functions use File | Settings | File Templates.
        when(v){
            food_image -> {
                selectPhoto()
            }
            CancelButton -> {
                listener?.changeToGrid()
            }
            SubmitButton -> {
                trySubmit()
            }
            addIngredients -> {
                selectIngredients()
            }
        }
    }

    

    fun selectPhoto() {

        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        if (intent.resolveActivity(activity!!.packageManager) != null)
            startActivityForResult(intent, PHOTO_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            ImageUri = data.data
            // Do something with the photo based on Uri
            selectedImage = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), ImageUri)
            // Load the selected image into a preview
            imagePreview.setImageBitmap(selectedImage)
        }
    }

    fun selectIngredients(){
        selectedIngredients.clear()
        val selectedList = ArrayList<Int>()
        dialogBuilder.setTitle("Ingredients")
        dialogBuilder.setMultiChoiceItems(ARG_PARAM1.toTypedArray(), null
        ) { dialog, which, isChecked ->
            if (isChecked) {
                selectedList.add(which)
            } else if (selectedList.contains(which)) {
                selectedList.remove(Integer.valueOf(which))
            }
        }

        dialogBuilder.setPositiveButton("DONE") { dialogInterface, inter ->

            for (i in selectedList.indices) {
                selectedIngredients.add(ARG_PARAM1[selectedList[i]])
            }

            ingredientsView.adapter = null
            adapter = ArrayAdapter<String>(fraqmentContext, R.layout.ingredient_item, R.id.ingredient_string, selectedIngredients)
            ingredientsView.adapter = adapter
        }

        dialogBuilder.show()
    }

    fun trySubmit(){
        FoodName.error = null
        FoodDescription.error = null
        FoodRecipe.error = null


        FoodNameLayout.error = null
        FoodDescriptionLayout.error = null
        FoodRecipeLayout.error = null


        var cancel = false
        var focusView: View? = null

        foodName = FoodName.text.toString()
        foodDescription = FoodDescription.text.toString()
        foodRecipe = FoodRecipe.text.toString()


        if(TextUtils.isEmpty(foodName)){
            FoodNameLayout.error = getString(R.string.error_invalid_email)
            FoodName.error = ""
            focusView = FoodName
            cancel = true
        }

        if(TextUtils.isEmpty(foodDescription)){
            FoodDescriptionLayout.error = getString(R.string.display_name_error)
            FoodDescription.error = ""
            focusView = FoodDescription
            cancel = true
        }

        if(TextUtils.isEmpty(foodRecipe) ){
            FoodRecipeLayout.error = getString(R.string.error_invalid_password)
            FoodRecipe.error = null
            focusView = FoodRecipe
            cancel = true
        }

        if(selectedIngredients.isEmpty()){
            Toast.makeText(fraqmentContext, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            cancel = true
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        }else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            submitRecipe()
        }
    }

    fun submitRecipe(){
        var file = ImageUri
        foodId = auth.currentUser!!.uid + "${file.lastPathSegment}"
        val imageRef = storageRef.child("Foods/" + auth.currentUser!!.uid + "${file.lastPathSegment}")
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
                val actions: UserActionsHelper = UserActionsHelper()
                actions.addCreateRecipeAction(foodId)
                databaseConstruct(downloadUri)
            } else {
                // Handle failures
                // ...
            }
        }
    }

    fun databaseConstruct(url: Uri?){
        val db = FirebaseFirestore.getInstance()
        val food: MutableMap<String, Any> = HashMap()
        food.put("AuthorId", auth.currentUser!!.uid)
        food.put("AuthorName", auth.currentUser!!.displayName.toString())
        food.put("FoodName",foodName)
        food.put("ImageUrl", url.toString())
        food.put("Recipe",foodRecipe)
        food.put("Description",foodDescription)
        db.collection("Foods").document(foodId)
            .set(food)
            .addOnSuccessListener {
                for (ingredient in selectedIngredients){
                    val ingredientMap: MutableMap<String, Any> = HashMap()
                    val foodMap: MutableMap<String, Any> = HashMap()
                    ingredientMap.put("Amount", "unspecified")
                    foodMap.put("ID", foodId)
                    db.collection("Foods").document(foodId).collection("Ingredients").document(ingredient).set(ingredientMap, SetOptions.merge())
                    db.collection("Ingredients").document(ingredient).collection("Foods").document(foodId).set(foodMap, SetOptions.merge())
                    db.collection("Users").document(auth.currentUser!!.uid).collection("Foods").document(foodId).set(foodMap, SetOptions.merge())
                    Toast.makeText(fraqmentContext, getString(R.string.create_succeed), Toast.LENGTH_SHORT).show()
                    listener?.changeToGrid()
                }
                Log.d("Database", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Database", "Error writing document", e) }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            fraqmentContext = context
            listener = context
            dialogBuilder = AlertDialog.Builder(context)
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
        fun changeToGrid()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateRecipe.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: ArrayList<String>) =
            CreateRecipe().apply {
                arguments = Bundle().apply {
                    ARG_PARAM1 = param1
                }
            }
    }
}
