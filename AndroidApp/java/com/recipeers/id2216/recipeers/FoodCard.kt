package com.recipeers.id2216.recipeers
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.recipeers.id2216.recipeers.Friends.ActionFeed
import com.recipeers.id2216.recipeers.Friends.UserActionsHelper
import com.recipeers.id2216.recipeers.Utils.GlideApp
import kotlinx.android.synthetic.main.fragment_food_card.*
import java.math.RoundingMode
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private var ARG_PARAM1: MutableMap<String, Any>? = null
private var ARG_PARAM2: String = ""

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FoodCard.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FoodCard.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FoodCard : Fragment(), View.OnClickListener, RatingBar.OnRatingBarChangeListener {
    // TODO: Rename and change types of parameters
    private var db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var param1: MutableMap<String, Any>? = null
    private var listener: OnFragmentInteractionListener? = null
    private var status: Boolean = false
    private var favoriteStatus: Boolean = false
    private var foodRating: Double = 5.0
    private lateinit var ratingText: TextView
    private var author: MutableMap<String, Any>? = null
    private var ingredients: ArrayList<String> = ArrayList(1)
    private lateinit var authorID: String
    private lateinit var horizontalLayoutManager: LinearLayoutManager
    private lateinit var adapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = ARG_PARAM1


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val foodCardView = inflater.inflate(R.layout.fragment_food_card, container, false)
        if (ARG_PARAM1!!["Rating"] != null) {
            foodRating = if (ARG_PARAM1!!["Rating"] is Long) {
                (ARG_PARAM1!!["Rating"] as Long).toDouble()
            } else {
                ARG_PARAM1!!["Rating"] as Double
            }
        }

        ratingText = foodCardView.findViewById(R.id.food_total_rating)
        db.collection("Users").document(ARG_PARAM1!!.get("AuthorId").toString()).get().addOnSuccessListener {
            author = it.data
            authorID = it.id
            foodCardView.findViewById<Button>(R.id.author_button).visibility = View.VISIBLE
            foodCardView.findViewById<Button>(R.id.author_button).text = author?.get("DisplayName").toString()
        }
        GlideApp.with(this)
            .load(Uri.parse(ARG_PARAM1!!.get("ImageUrl").toString()))
            .placeholder(R.drawable.loading_circle)
            .into(foodCardView.findViewById(R.id.food_image))
        foodCardView.findViewById<TextView>(R.id.food_see_recipe_button).setOnClickListener(this)
        foodCardView.findViewById<Button>(R.id.food_favorite).setOnClickListener(this)
        foodCardView.findViewById<Button>(R.id.author_button).setOnClickListener(this)
        foodCardView.findViewById<Button>(R.id.food_comment).setOnClickListener(this)
        ratingText.text = foodRating.toString()
        foodCardView.findViewById<RatingBar>(R.id.food_rating).rating = foodRating.toFloat()
        foodCardView.findViewById<TextView>(R.id.food_description).text = ARG_PARAM1!!.get("Description").toString()
        foodCardView.findViewById<TextView>(R.id.food_recipe).text = ARG_PARAM1!!.get("Recipe").toString()
        foodCardView.findViewById<TextView>(R.id.food_name).text = ARG_PARAM1!!.get("FoodName").toString()
        db.collection("Foods/" + ARG_PARAM2 + "/Ingredients")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var i = 0
                    for (document in task.result!!) {
                        i++
                        ingredients.add(document.id)
                        Log.e("ingredient", document.id)

                        if (i == task.result!!.size()) {

                            var ingredientsView = foodCardView.findViewById<RecyclerView>(R.id.food_ingredients)
                            if (!status) {
                                status = true
                                ingredientsView.layoutManager = horizontalLayoutManager
                            }
                            ingredientsView.adapter = adapter
                        }
                    }

                } else {
                    Log.e("lool", "error")
                }
            }
        foodCardView.findViewById<RatingBar>(R.id.food_rating).onRatingBarChangeListener = this

        favoriteCheck()

        Log.e("OnCreateView", "view created")
        return foodCardView
    }

    override fun onClick(v: View?) {
        when (v) {
            food_see_recipe_button -> {
                Log.e("test", "see recipe clicked")
                food_recipe_view.visibility = View.VISIBLE
            }

            author_button -> {
                listener?.changeToProfile(author, authorID)
            }

            food_favorite -> {
                Log.e("test", "fav clicked")
                favoriteClick()
            }

            food_comment -> {
                Log.e("test", "comment clicked")

                var transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.enter_slide_left,R.anim.exit_slide_right)
                transaction.replace(R.id.fragment_view_main, CommentPage.newInstance(ARG_PARAM1, ARG_PARAM2))
                transaction.addToBackStack(null)
                transaction.commit()
            }

        }
    }

    // TODO: Rename method, update argument and hook method into UI event


    override fun onAttach(context: Context) {
        super.onAttach(context)
        horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = IngredientAdapter(ingredients, context)
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
        fun changeToProfile(profile: MutableMap<String, Any>?, authorID: String)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FoodCard.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: MutableMap<String, Any>?, param2: String) =
            FoodCard().apply {
                Log.e("FoodPage", param2)
                Log.e("FoodPage", param1!!.keys.toString())
                ARG_PARAM1 = param1
                ARG_PARAM2 = param2

            }
    }

    override fun onRatingChanged(bar: RatingBar?, newRating: Float, p2: Boolean) {
        val userRatingsRef = db.collection("Foods").document(ARG_PARAM2).collection("Ratings")
        val myRating: MutableMap<String, Any> = HashMap()
        myRating["Rating"] = newRating

        userRatingsRef.document(auth.currentUser!!.uid).set(myRating).addOnSuccessListener {
            userRatingsRef.get().addOnSuccessListener { userRatings ->
                foodRating = 0.0
                for (userRating in userRatings) foodRating += (userRating["Rating"] as Double)
                foodRating = ratingRound(foodRating / userRatings.size())
                db.collection("Foods").document(ARG_PARAM2).update("Rating", foodRating).addOnSuccessListener {
                    UserActionsHelper().addRateRecipeAction(ARG_PARAM2)
                    ratingText.text = foodRating.toString()
                }
            }
        }
    }

    private fun ratingRound(rating: Double): Double {
        val decimalFormat = DecimalFormat("#.#")
        decimalFormat.roundingMode = RoundingMode.CEILING
        return decimalFormat.format(rating).toDouble()
    }

    fun favoriteClick() {
        val favoritesLocation = db.collection("Users").document(auth.currentUser!!.uid).collection("Favorites")
        val recipe: MutableMap<String, Any> = HashMap()
        recipe.put("ID", ARG_PARAM2)

        if (favoriteStatus) {
            favoritesLocation.document(ARG_PARAM2).delete()
            favoriteStatus = false
            Toast.makeText(activity, "Unfavorited", Toast.LENGTH_SHORT).show()
        } else {
            favoritesLocation.document(ARG_PARAM2).set(recipe)
            favoriteStatus = true
            UserActionsHelper().addFaveRecipeAction(ARG_PARAM2)
            Toast.makeText(activity, "Favorited", Toast.LENGTH_SHORT).show()
        }
    }

    //to check if it has been favorited before. To make it work in conjunction with the webapp
    fun favoriteCheck() {
        val auth = FirebaseAuth.getInstance()
        val favoritesLocation = db.collection("Users").document(auth.currentUser!!.uid).collection("Favorites")
        favoritesLocation.document(ARG_PARAM2).get().addOnSuccessListener { documentSnapshot ->
            var check = documentSnapshot.toObject(DocumentTemplate::class.java)
            Log.e("favoriteCheck", documentSnapshot.toString())
            Log.e("favoriteCheck", check?.doc.toString())
            if (check?.doc !== null) {
                favoriteStatus = true
            }
        }
    }

    //helper object class to find specific field when getting the database item
    data class DocumentTemplate(var input: String = "") {
        var key = ""
        var metadata = ""
        var doc = ""
    }
    /* inspiration from:
    https://stackoverflow.com/questions/46995080/how-do-i-get-the-document-id-for-a-firestore-document-using-kotlin-data-classes
    https://stackoverflow.com/questions/46633412/firebase-firestore-toobject-with-kotlin
     */
}
