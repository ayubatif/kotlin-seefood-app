import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom';
import '../styles/main.scss';
import Account from './Account';
import Create from './Create';
import MyRecipes from './MyRecipes';
import Search from './Search';
import Login from './Login';
import LoadSearch from './LoadSearch'
import Favourites from './Favourites';
import NavBar from './NavBar';

import Grid from '@material-ui/core/Grid'
class Main extends Component {
	state = {
		ingredients: [],
	}

	handleSearch = (ingredients) => {
		
		this.setState({ ingredients: ingredients })
		
	}

	render() {
		return (
			<div >
				<Grid 
				container spacing={16}
				direction="row"
				justify="center"
				alignItems="center">
				<Grid item xs = {12}>
				<NavBar handleSearch = {this.handleSearch}/>
				</Grid>
				<Grid item xs = {12}>
				<div className = "wrapperMain">
					<Switch>
						<Route path='/account' component={Account} />
						<Route path='/create_recipe' component={Create} />
						<Route path='/my_recipes' component={MyRecipes} />
						<Route path='/my_favourites' component={Favourites} />
						<Route path='/search_recipes' render={() => <Search queries={this.state.ingredients} />} />
						<Route path='/login' component={Login} />
						<Route path='/load_search' component = {LoadSearch}/>
						
					</Switch>
				</div>
				
				</Grid>
				</Grid>
			</div>
		);
	}
}

export default Main;
