import React, { Component } from 'react';

import firebase from '../firebase.js';
import User from '../database_modules/User.js';
import Food from '../database_modules/Food.js';
import Utility from '../database_modules/Utility.js';
import Query from '../database_modules/Query.js';
import Transfer from '../database_modules/Transfer.js';
import AsyncSearchBar from '../components/AsyncSearchBar'
import NavBar from '../components/NavBar';
import { Link } from 'react-router-dom';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import AddIcon from '@material-ui/icons/Add';
import BottomNav from '../components/BottomNav.js';
import Authentication from '../components/Authentication.js';
import Card from '../components/FoodCard.js';
import FoodPage from '../components/FoodPage.js';

class Test extends Component {



  state = {
    displayName: "name",
    foodName: "food",
    ingredients: []
  }


  handleSearch = (ingredient) => {
    this.setState(prevState => ({ ingredients: [...prevState.ingredients, ingredient] }))
  }

  componentDidMount() {
    var user = new User();
    
    /*

    // Get a reference to the storage service, which is used to create references in storage bucket
    var storage = firebase.storage();
      
    // Create a storage reference from Firebase storage service
    var storageRef = storage.ref();
      
    var file = new File([""] ,"test-image.jpeg");
    var transfer = new Transfer();
    transfer.uploadFile("test/", file);
    transfer.downloadFile("test/", file, "ye");

    */

    /*
    Authentication 
    */
    //user.createUser("messagingtester9999@gmail.com", "ayubtest");
    user.signInUser("messagingtester666@gmail.com", "ayubtestX");
    //user.signInUser("plzplsdontdontchangethisemailesplelicy@gmail.com", "ayubtest");
    //user.signOutUser(); // auth times may lead to desync?
    //user.updateEmail("messagingtester666@gmail.com");
    //user.updatePassword("ayubtestX");
     /*
    User
    */
   /*
    user.setImage("test-image.jpeg");
    */
    
    /*
    Messages
    */
   /*
    user.sendMessage("osaTg5YzgDgt8HvxaOmBPjvbjHr2","helo","njnn");
    */
     /*
    Query
    */
    var foodQuery = new Query("Foods");
    var a = foodQuery.getDocument("1541774852942hleZze4oBhRzRtjubdN5F30EqXg2942315.3125");
    //var collection = foodQuery.filterCollection("FoodName", "==", "kebab");
    //console.log(collection);
     /*
    Comments
    */
   /*
    Food.addComment(a.id, "too spicy");
    console.log(Food.getComments(a.id));
    */
     /*
    Favorites
    */
   /*
    user.addFavorite(a.id);
    */
     /*
    History
    */
   /*
    user.addHistory(a.id);
    for(var i=26;i<99;i++){
      user.addHistory("1541728960849osaTg5YzgDgt8HvxaOmBPjvbjHr2849252."+i);
    }
    */
    
     /*
    Food
    */
   /*
    var FoodName = "kebab";
    var ImageUrl = "aaa.jpg";
    var Recipe = "make wrap";
    var SrcUrl = "kekbnf.html";
     var food =  new Food(FoodName, ImageUrl, Recipe, SrcUrl);
    console.log(food);
    food.addFood();
     var ingredient1 = {
      Ingredient : "wrap",
      Count: 1
    }
    var ingredient2 = {
      Ingredient : "spicy kebab",
      Count: 1824
    }
    
    food.addIngredient(ingredient1.Ingredient, ingredient1.Count);
    food.addIngredient(ingredient2.Ingredient, ingredient2.Count);
    console.log(Food.getIngredients(a.id));
     Food.editRecipe(a.id, "just amke pizza instead");
    
    // works perfectly and uses O(N)
    Food.addRating(a.id, 2);
    Food.addRating(a.id, 3);
     // works but need test more cases uses O(1)
    Food.addRatingOptimized(a.id, 4);
    Food.addRatingOptimized(a.id, 5);
    */
     /*
    Storage
    */
   /*
    var transfer = new Transfer();
    transfer.uploadFile("Test/", "test-image.jpeg");
    */

  }




  prop = {
    FoodName: "Shawarma",
    ImageUrl: "https://ichef.bbci.co.uk/food/ic/food_16x9_832/recipes/one_pot_chorizo_and_15611_16x9.jpg",
    Description: "A quick, simple and speedy dinner that cooks in just one pan. A true family favourite that the kids can help to cook."
  }

  render() {
    firebase.firestore().collection("Foods").doc("Pizza").collection("Ingredients").get().then(ingredient => 
      console.log(ingredient.docs.map(each => {
        return {name: each.id, Amount: each.data().Amount }
      })))
    return (
      
      <div><FoodPage food = {this.prop}/></div>
    );
  }
}

export default Test;