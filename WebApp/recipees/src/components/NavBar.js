import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import '../styles/header.scss';
import firebase from '../firebase.js';
import UserPanel from './UserPanel'
import DefaultPanel from './DefaultPanel'
import AsyncSearchBar from './AsyncSearchBar'
import Grid from '@material-ui/core/Grid'
class NavBar extends React.Component {
	state = {
		anchorEl: null,
		mobileMoreAnchorEl: null,
		ingredients: [],
		userLoggedIn: false
	};

	componentDidMount() {
		this.isUserLoggedIn();
	}


	isUserLoggedIn() {
		firebase.auth().onAuthStateChanged((user) => {
			if (user) {
				this.setState({
					userLoggedIn: true
				})
			} else {
				this.setState({
					userLoggedIn: false
				})
			}
		})
	}

	userChange = () => {
		this.isUserLoggedIn()
	}




	handleSearchClick = (query) => {
		this.props.handleSearch(query);
	  };






	render() {
		const userLoggedIn = this.state.userLoggedIn;

		return (
			
				<AppBar position='fixed' >
					<Toolbar >
						<Grid container spacing = {8}
						direction="row"
						justify="flex-start"
						alignItems="flex-start">
						<Grid item xs = {12} sm = {6}>
						
							{(userLoggedIn === true) ? <UserPanel userChange={this.userChange} /> : <DefaultPanel />}
						
						</Grid>
						<Grid item xs = {12} sm = {6}>
						<AsyncSearchBar onSearchClick={this.handleSearchClick} />
						</Grid>
						</Grid>
					</Toolbar>
				</AppBar>


			
		);
	}
}



export default NavBar;