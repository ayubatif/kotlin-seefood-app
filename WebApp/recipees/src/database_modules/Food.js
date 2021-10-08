import firebase from '../firebase.js';
import Utility from './Utility.js';

class Food {

    db = firebase.firestore().collection('Foods');

    constructor(foodName, imageUrl, recipe, description){
        firebase.auth().onAuthStateChanged(user => {
            if(user){
                this.foodID = user.uid + foodName;
                this.AuthorId = user.uid;
                this.FoodName = foodName;
                this.ImageUrl = imageUrl;
                this.Rating = 0;
                this.Recipe = recipe;
                this.Description = description;
            } else {
                this.AuthorId = null;
                this.foodID = null;
                this.FoodName = null;
                this.ImageUrl = null;
                this.Rating = 0;
                this.Recipe = null;
                this.Description = null;
            }
        })
     }

    // create a new recipe
    addFood(){
        firebase.auth().onAuthStateChanged(user =>{
            if(user){
                this.dataBaseConstruct();  
            }
        });
        return this.foodID      
    }

    // setup database fields
    dataBaseConstruct(){
        if (this.foodID) {
            this.db.doc(this.foodID).set({
                AuthorId : this.AuthorId,
                FoodName : this.FoodName,
                ImageUrl : this.ImageUrl,
                Recipe : this.Recipe,
                Description : this.Description
                });
        }
    }

    static editRecipe(foodID, recipe){
        firebase.auth().onAuthStateChanged(user =>{
            if(user){
                var db = firebase.firestore().collection('Foods');
                if(foodID){
                    db.doc(foodID).set({Recipe: recipe}, { merge: true });
                }
            }
        }); 
    }

    static setImage(foodID, file){
        var db = firebase.firestore().collection('Foods');
        firebase.auth().onAuthStateChanged(user => {
            var storage = firebase.storage();
            var storageRef = storage.ref();
            var metadata = {
                contentType: 'image/jpeg',
              };

            if (user) {
                var task = storageRef.child("Foods/" + foodID).put(file, metadata);
                task.snapshot.ref.getDownloadURL().then(url => {
                    db.doc(foodID).set({
                        ImageUrl: url
                    }, {merge: true});
                });
        } else {
        }});
    }

    static getIngredients(foodID){
        var db = firebase.firestore().collection('Foods');
        return db.doc(foodID).collection("Ingredients");
    }

    addIngredient(ingredient, count){
        firebase.auth().onAuthStateChanged(user =>{
            if(user){
                if(this.foodID){
                    this.db.doc(this.foodID).collection("Ingredients").doc(ingredient).set({Ingredient: ingredient, Count: count});
                }
            }
        }); 
    }

    static getComments(foodID){
        var db = firebase.firestore().collection('Foods');
        return db.doc(foodID).collection("Comments");
    }

    static addComment(foodID, content){
        firebase.auth().onAuthStateChanged(user =>{
            if(user){
                var db = firebase.firestore().collection('Foods');
                var commentId = Utility.generateID(user);
                if(foodID){
                    db.doc(foodID).collection("Comments").doc(commentId).set({UserId: user.uid, Content: content}, { merge: true });
                }
            }
        }); 
    }

    static getRatings(foodID){
        var db = firebase.firestore().collection('Foods');
        return db.doc(foodID).collection("Ratings");
    }

    static addRating(foodID, rating){
        firebase.auth().onAuthStateChanged(user =>{
            if(user){
                var db = firebase.firestore().collection('Foods');
                if(foodID){
                    db.doc(foodID).collection("Ratings").doc(user.uid).set({Rating: rating}, { merge: true });
                }
            }
        }); 
        Food.updateRatingAverage(foodID);
    }
    
    static updateRatingAverage(foodID){
        var db = firebase.firestore().collection('Foods');
        var Ratings = Food.getRatings(foodID).get();
        Ratings.then(snap =>{
            var ratingCount = snap.size;
            var ratingTotal = 0;
            snap.forEach(rating =>{
                ratingTotal += rating.get("Rating");
            })
            db.doc(foodID).set({Rating: ratingTotal / ratingCount}, { merge: true });
        });
    }

    /**
     * adds a new user rating then updates the food rating
     *
     * @param {*} newRating new rating by the user
     */
    static addRatingOptimized(foodID, newRating){
        firebase.auth().onAuthStateChanged(user =>{
            if(user){
                var db = firebase.firestore().collection('Foods');
                if(foodID){
                    var ratingTotal = 0;
                    var ratingCount = 1;
                    // retrieve rating count
                    Food.getRatings(foodID).get().then(snap =>{
                        ratingCount = snap.size;
                    });
                    // retreive old average rating from its field
                    db.doc(foodID).get().then(snap =>{
                        ratingTotal = snap.data().Rating * ratingCount;
                    });
                    // retrieve old user rating
                    Food.getRatings(foodID).doc(user.uid).get().then(snap =>{
                        if (snap.exists) {
                            ratingTotal -= snap.data().Rating;
                        } else {
                            ratingCount += 1;
                        }
                        // set new user rating and calculate new average rating
                        Food.getRatings(foodID).doc(user.uid).set({Rating: newRating});
                        ratingTotal += newRating;
                        var averageRating = ratingTotal / ratingCount
                        db.doc(foodID).set({Rating: averageRating}, { merge: true });
                    });
                }
            }
        }); 
        
    }

}

export default Food;