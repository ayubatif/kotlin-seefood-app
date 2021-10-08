import React, { Component } from 'react';
import '../styles/header.scss';
import { Link } from 'react-router-dom';

class Header extends Component {

	render () {
		return (
			<nav>
				<Link to='/'>Recipeers</Link>
				<div className="nav-right">
				    <Link to='/create_recipe'>Create Recipe</Link>
				    <Link to='/my_recipes'>My Recipes</Link>
				    <Link to='/account'>Account</Link>
				</div>
				{/*TODO: add log in/log out*/}
			</nav>
		);
	}
}

export default Header;
