/* eslint-disable eqeqeq */
import React, { Component } from 'react';
import firebase from '../firebase.js';
import '../styles/search.scss';
import FoodCard from './FoodCard.js';
import Grid from '@material-ui/core/Grid'
import Loading from './Loading'
import SnackBar from './SnackBar'

class Search extends Component {

  state = {
    isLoaded: false,
    foodsLoaded: false,
    avoidedFoodIDsLoaded: false,
    foodIDsLoaded: false,
    totalFoodIDsLoaded: false,
    allDone: false,
    Foods: [],
    Props: []
  }


  FoodIDs = [];
  AvoidedFoodIDs = [];
  Queries = [];
  queryCount = [];
  queryTime = [];
  totalFoodIds = 0;

  async componentDidMount() {
    firebase.auth().onAuthStateChanged(user => {
      if (user) {
        firebase.firestore().collection("Users").doc(user.uid).collection('Allergens').get().then(snap => {
          snap.forEach(allergen => {
            db.doc(allergen.data().label).collection('Foods').get().then(foodIds => {
              foodIds.docs.forEach(food => {
                if (!this.AvoidedFoodIDs.includes(food.data().ID)) {
                  this.AvoidedFoodIDs.push(food.data().ID);
                }
              });
              this.setState(() => {
                return { avoidedFoodIDsLoaded: true };
              })
            })
          })
        })
      }
    })

    new Promise(resolve => {
      setTimeout(() => {
        resolve(this.fetchTotalFoodIds());
      }, 1000);
    })

  }

  fetchTotalFoodIds() {
    this.queryCount = 0
    this.props.queries.forEach(query => {
      db.doc(query.label).collection("Foods").get().then(foodIds => {
        if (!(this.queryTime.includes(query))) {
          this.queryTime.push(query)
        }
        this.totalFoodIds = this.totalFoodIds + foodIds.docs.length
        if (this.queryTime.length == this.props.queries.length) {
          this.setState(() => {
            return { totalFoodIDsLoaded: true };
          })
        }
      })
    })

  }

  fetchData() {
    this.queryCount = 0
    var foodCounter = 0
    this.props.queries.forEach(query => {
      this.queryCount = this.queryCount + 1
      db.doc(query.label).collection("Foods").get().then(foodIds => {
        foodIds.docs.forEach(food => {

          if (!this.FoodIDs.includes(food.data().ID)) {
            foodCounter = foodCounter + 1
            if(!this.AvoidedFoodIDs.includes(food.data().ID)) {
              this.FoodIDs.push(food.data().ID)
            }
          }
          if (foodCounter == this.totalFoodIds && this.queryCount == this.props.queries.length) {
            this.setState(() => {
              return { foodIDsLoaded: true };
            })
          }
        })
      })
    })
  }

  loadFoods() {
    this.FoodIDs.forEach(food => {
      this.state.Foods.push(food)
    })

    this.setState(() => {
      return { foodsLoaded: true };
    })

  }

  loadProps() {
    this.state.Foods.forEach(food => {
      dbFood.doc(food).get().then(snap => {
        this.state.Props.push({
          id: snap.id,
          FoodName: snap.get("FoodName"),
          Recipe: snap.get("Recipe"),
          ImageUrl: snap.get("ImageUrl"),
          Description: snap.get("Description")
        })
        this.setState(() => {
          return { isLoaded: true, allDone: true };
        })
      })
    })

    this.setState(() => {
      return { isLoaded: true, allDone: true };
    })
  }



  render() {
    if (this.props.queries.length == 0) {
      return (<SnackBar message={"Please Search Something to get Results"} />)
    }
    if (this.state.avoidedFoodIDsLoaded && this.state.totalFoodIDsLoaded && this.state.foodIDsLoaded == false && this.state.foodsLoaded == false && this.state.allDone == false) {
      this.fetchData()
    }

    if (this.state.avoidedFoodIDsLoaded && this.state.totalFoodIDsLoaded && this.state.foodIDsLoaded && this.state.foodsLoaded == false && this.state.allDone == false) {
      this.loadFoods()
    }

    if (this.state.avoidedFoodIDsLoaded && this.state.totalFoodIDsLoaded && this.state.foodIDsLoaded && this.state.foodsLoaded && this.state.allDone == false) {
      this.loadProps()
    }




    if (this.state.isLoaded) {
      return (
        <div className = "Search">
          <Grid id="IconContainer"
            container spacing={8}
            direction="row"
            justify="space-evenly"
            alignItems="flex-start">
            {this.state.Props.map(prop => {
              return <FoodCard key={prop.id} food={prop} />
            })}

          </Grid>

        </div>


      );
    } else {
      return (<div className = "SearchLoading"><Loading /> </div>)
    }

  }
}






var db = firebase.firestore().collection("Ingredients");
var dbFood = firebase.firestore().collection("Foods");


export default Search;