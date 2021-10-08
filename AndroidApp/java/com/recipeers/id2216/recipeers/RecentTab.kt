package com.recipeers.id2216.recipeers

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.GridView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.recipeers.id2216.recipeers.Utils.GlideApp
import kotlinx.android.synthetic.main.food_grid_item.view.*
import kotlinx.android.synthetic.main.fragment_item_grid.*
import kotlinx.android.synthetic.main.fragment_recent_tab.*
import java.net.URL


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var gridCont: Context

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ItemGrid.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ItemGrid.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RecentTab : Fragment() {
    // TODO: Rename and change types of parameters
    private var db = FirebaseFirestore.getInstance()
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var adapter: FoodGridAdapter? = null
    private var foodGridList: ArrayList<FoodGrid> = ArrayList()
    private var foods: MutableList<MutableMap<String, Any>?> = mutableListOf <MutableMap<String, Any>?>()
    private var foodIds: ArrayList<String> = ArrayList(1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var foodGridView = inflater.inflate(R.layout.fragment_recent_tab, container, false)
        var foodsRef = db.collection("Foods")
        foodsRef.orderBy("FoodName").limit(9).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    foodGridList.clear()
                    foods.clear()
                    foodIds.clear()
                    var i = 0
                    for (document in task.result!!) {
                        i++
                        var foodGridName = document.data.get("FoodName").toString()
                        var foodGridImageUrlString = document.data.get("ImageUrl").toString()
                        //var foodGridImageUrlBitmap = BitmapFactory.decodeStream(foodGridImageUrlString.openConnection().getInputStream())
                        var foodGridItem: FoodGrid = FoodGrid(foodGridName, foodGridImageUrlString)
                        foodGridList.add(foodGridItem)
                        foods.add(document.data)
                        foodIds.add(document.id)
                        if(i == task.result!!.size() ){
                            adapter = FoodGridAdapter(gridCont, foodGridList)
                            foodGridView.findViewById<GridView>(R.id.food_grid).adapter = adapter
                            foodGridView.findViewById<GridView>(R.id.food_grid).setOnItemClickListener{
                                parent, view, position, id ->
                                Toast.makeText(gridCont,"Clicked Item: "+ foodGridList[position].foodName,Toast.LENGTH_SHORT).show()
                                listener?.onFoodItemClicked(foods[position], foodIds[position])
                            }
                        }
                    }
                } else {
                    Log.e("lool", "error")
                }
            }
        return foodGridView
    }
    // TODO: Rename method, update argument and hook method into UI event

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            gridCont = context
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
        fun onFoodItemClicked(food: MutableMap<String, Any>?, foodID: String)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ItemGrid.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            RecentTab().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
class FoodGridAdapter : BaseAdapter {
    var foodsList = ArrayList<FoodGrid>()
    var context: Context? = null
    constructor(context: Context, foodsList: ArrayList<FoodGrid>) : super() {
        this.context = context
        this.foodsList = foodsList
    }
    override fun getCount(): Int {
        return foodsList.size
    }
    override fun getItem(position: Int): Any {
        return foodsList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val food = this.foodsList[position]
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var foodView = inflator.inflate(R.layout.food_grid_item, null)
        GlideApp.with(gridCont)
            .load(Uri.parse(food.foodImage))
            .placeholder(R.color.GreyTransparent)
            .into(foodView.findViewById(R.id.food_grid_image))
        foodView.food_grid_name.text = food.foodName!!
        return foodView
    }



}




