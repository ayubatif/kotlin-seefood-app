import React, { Component } from 'react';
import '../styles/create.scss';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import FireUploader from "react-firebase-file-uploader";
import firebase from '../firebase.js';
import Image from './Image';
import Food from '../database_modules/Food';
import IngredientsAddBar from './IngredientsAddBar'
import SnackBar from './SnackBar'
import template from '../template.jpg'
import Transfer from '../database_modules/Transfer';
import { Popover } from '@material-ui/core';

//check this:
//https://www.npmjs.com/
//package/react-firebase-file-uploader?fbclid=IwAR1zT8IuW7Ojfs0cSAo-Fst80NiYoL9teoLXIbw33EYA8DDemy4r0aTpZ_w 

class Create extends Component {
	IngredientsDb = firebase.firestore().collection("Ingredients")
	state = {
		foodName: "",
		ImageURL: "",
		Recipe: "",
		Description: "",
		avatar: "",
		isUploading: false,
		progress: 0,
		ingredients: [],
		TemplatePictureUrl: "",
		check: false
	};

	handleUploadStart = () => this.setState({ isUploading: true, progress: 0 });
	handleProgress = progress => this.setState({ progress });
	handleUploadError = error => {
		this.setState({ isUploading: false });
		console.error(error);
	};

	handleUploadSuccess = filename => {
		this.setState({ avatar: filename, progress: 100, isUploading: false });
		firebase
			.storage()
			.ref("FoodImages")
			.child(filename)
			.getDownloadURL()
			.then(url => {
				this.setState({ ImageURL: url , TemplatePictureUrl: url})});	
	};

	handleAdd = (ingredients) => {
		this.state.ingredients = ingredients
		this.setState({check : 1})
		return (<SnackBar message={"Items are Added! :)"}/>)
	}

	confirm = () => {
		this.setState({check : true});
		console.log("missing");
		//return (<SnackBar message={"Confirm ingredients before sumbitting!"}/>)
	}

	handleClick = () => {
		firebase.auth().onAuthStateChanged(user => {
			if (user) {
				var food = new Food(this.state.foodName, this.state.ImageURL, this.state.Recipe, this.state.Description)
				food.dataBaseConstruct()
				food.addFood()
				this.props.handleClose()
				this.state.ingredients.forEach(ingredient => {
					firebase.firestore().collection("Foods").doc(user.uid + this.state.foodName).collection("Ingredients").doc(ingredient.label).set({ Amount: "unspecified" }, {merge: true})
					this.IngredientsDb.doc(ingredient.label).collection("Foods").doc(user.uid + this.state.foodName).set({ ID: user.uid + this.state.foodName }, {merge: true})
					firebase.firestore().collection("Users").doc(user.uid).collection("Foods").doc(user.uid + this.state.foodName).set({ ID: user.uid + this.state.foodName }, {merge: true})
				})
			}
		})
	};

	handleFoodName = (event) => {
		this.setState({foodName : event.target.value})
	}
	handleDescription = (event) => {
		this.setState({Description : event.target.value})
	}
	handleRecipe = (event) => {
		this.setState({Recipe : event.target.value})
	}



	render() {
		if(this.state.check){
			return(
			<div>
				<AppBar>
					<Toolbar>
						<Grid justify="space-between" spacing={24} container>
							<IconButton color="inherit" onClick={this.props.handleClose} aria-label="Close"
								size="small"
							>
								<CloseIcon />
							</IconButton>
						</Grid>
						<Grid>
							<Button onClick={() => 
								{	
									if(!this.state.check){
										this.confirm();
									} else{
										this.handleClick();
									}
									
								}
							}>
							Submit</Button>
						</Grid>
					</Toolbar>
				</AppBar>

				<DialogTitle>Create a recipee</DialogTitle>
				<DialogContent>
					<Grid container spacing={8} direction="row" justify="space-between" alignItems="center">
						<Grid item xs={5} container>
							<TextField onChange={this.handleFoodName} placeholder="Recipe Name" variant="outlined" fullWidth ></TextField>

						</Grid>
						<Grid item xs={5} container>
							<Image  image = {this.state.TemplatePictureUrl} />
						</Grid>
						<Grid item xs={5} container>
							<IngredientsAddBar handleAdd={this.handleAdd} />
						</Grid>
						<Grid item xs={5} container>
							<label className="upload">
								UPLOAD IMAGE
							<FireUploader

									hidden
									accept="image/*"
									storageRef={firebase.storage().ref('FoodImages')}
									onUploadStart={this.handleUploadStart}
									onUploadError={this.handleUploadError}
									onUploadSuccess={this.handleUploadSuccess}
									onProgress={this.handleProgress}

								/>
							</label>
						</Grid>
					</Grid>
					<p>Cooking instructions</p>
					<TextField onChange={this.handleRecipe} multiline variant="outlined" fullWidth></TextField>
					<p>Description and Tips</p>
					<TextField onChange={this.handleDescription} multiline variant="outlined" fullWidth></TextField>
				</DialogContent>
				<SnackBar message={"Confirm ingredients before sumbitting!"}/>
			</div>
			);
		}else{
		return (
			<div>
				<AppBar>
					<Toolbar>
						<Grid justify="space-between" spacing={24} container>
							<IconButton color="inherit" onClick={this.props.handleClose} aria-label="Close"
								size="small"
							>
								<CloseIcon />
							</IconButton>
						</Grid>
						<Grid>
							<Button onClick={() => 
								{	
									if(!this.state.check){
										this.confirm()
									} else{
										this.handleClick()
									}
									
								}
							}>
							Submit</Button>
						</Grid>
					</Toolbar>
				</AppBar>

				<DialogTitle>Create a recipee</DialogTitle>
				<DialogContent>
					<Grid container spacing={8} direction="row" justify="space-between" alignItems="center">
						<Grid item xs={5} container>
							<TextField onChange={this.handleFoodName} placeholder="Recipe Name" variant="outlined" fullWidth ></TextField>

						</Grid>
						<Grid item xs={5} container>
							<Image  image = {this.state.TemplatePictureUrl} />
						</Grid>
						<Grid item xs={5} container>
							<IngredientsAddBar handleAdd={this.handleAdd} />
						</Grid>
						<Grid item xs={5} container>
							<label className="upload">
								UPLOAD IMAGE
							<FireUploader

									hidden
									accept="image/*"
									storageRef={firebase.storage().ref('FoodImages')}
									onUploadStart={this.handleUploadStart}
									onUploadError={this.handleUploadError}
									onUploadSuccess={this.handleUploadSuccess}
									onProgress={this.handleProgress}

								/>
							</label>
						</Grid>
					</Grid>
					<p>Cooking instructions</p>
					<TextField onChange={this.handleRecipe} multiline variant="outlined" fullWidth></TextField>
					<p>Description and Tips</p>
					<TextField onChange={this.handleDescription} multiline variant="outlined" fullWidth></TextField>
				</DialogContent>
			</div>
		);
		}
	}
}

export default Create;

/*spacing taken from:
https://stackoverflow.com/questions/47686456/
whats-the-right-way-to-float-right-or-left-using-the-material-ui-appbar-with-ma
*/
