import React, { Component } from 'react';
import Loading from './Loading';
import FoodCard from './FoodCard.js';
import Grid from '@material-ui/core/Grid'
import firebase from '../firebase.js';

class MyRecipes extends Component {
	state = {
		isLoaded: false,
		PropsLoaded: false,
		FoodsLoaded: false
	}
	Foods = []
	Props = []

	componentDidMount(){
		new Promise(resolve => {
			setTimeout(() => {
			  resolve(this.loadFoods());
			}, 1000);
		  })
	}

	loadFoods(){
		firebase.auth().onAuthStateChanged(user =>{
			if(user){
				firebase.firestore().collection("Users").doc(user.uid).collection("Foods").get().then(AllFoods => {
					AllFoods.docs.forEach(food =>{
						this.Foods.push(food)	
						if(this.Foods.length == AllFoods.docs.length){
							this.setState(state => {return {FoodsLoaded: true}})
						}
					})
				})
			}
		})
	}

	loadProps() {
    this.Foods.forEach(food => {
      dbFood.doc(food.id).get().then(snap => {
        this.Props.push({
          id: snap.id,
          FoodName: snap.get("FoodName"),
          Recipe: snap.get("Recipe"),
          ImageUrl: snap.get("ImageUrl"),
          Description: snap.get("Description")
		})
		if(this.Props.length == this.Foods.length){
			this.setState((state) => {
				return { isLoaded: true, PropsLoaded: true}
			  })
		}
      })
    })
  }


	render () {
		if(this.state.FoodsLoaded && this.state.PropsLoaded == false){
			this.loadProps()
		}

		if (this.state.isLoaded) {
			return (
			  <div>
				<Grid id="IconContainer"
				  container spacing={8}
				  direction="row"
				  justify="space-evenly"
				  alignItems="flex-start">
				  {this.Props.map(prop => {
					return <FoodCard key={prop.id} food={prop} />
				  })}
	  
				</Grid>
	  
			  </div>
	  
	  
			);
		  }
		return (
			<Loading/>
		);
	}
}
var dbFood = firebase.firestore().collection("Foods");

export default MyRecipes;