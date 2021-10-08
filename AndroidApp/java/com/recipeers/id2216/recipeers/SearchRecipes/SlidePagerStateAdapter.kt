package com.recipeers.id2216.recipeers.SearchRecipes

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.recipeers.id2216.recipeers.FoodCard
import java.util.ArrayList

class SlidePagerStateAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var foods: MutableList<MutableMap<String, Any>?> = mutableListOf <MutableMap<String, Any>?>()
    private var foodIds: ArrayList<String> = ArrayList(1)

    override fun getCount(): Int {
        return foods.size
    }

    override fun getItem(position: Int): Fragment {
        Log.e("position", position.toString())
        Log.e("foodName", foods[position]!!["FoodName"].toString())
        return FoodCard.newInstance(foods[position], foodIds[position])
    }

    fun setFoods(list: MutableList<MutableMap<String, Any>?>){
        foods = list
    }
    fun setFoodIds(list: ArrayList<String>){
        foodIds = list
    }

}
