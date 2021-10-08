import React, { Component } from 'react';
import '../styles/userpanel.scss';
import { Link } from 'react-router-dom';
import IconButton from '@material-ui/core/IconButton';

import AccountBoxIcon from '@material-ui/icons/AccountBox';
import KitchenIcon from '@material-ui/icons/Kitchen';
import HomeIcon from '@material-ui/icons/Home';
import FavoriteIcon from '@material-ui/icons/Favorite';
import PlayForWorkIcon from '@material-ui/icons/PlayForWork'
import Grid from '@material-ui/core/Grid';
import User from '../database_modules/User'
import CreateRecipe from './CreateRecipe';
import AccountSettings from './AccountSettings';
import Tooltip from '@material-ui/core/Tooltip';

class UserPanel extends Component {
	user = new User()
	signOut = () => {
		this.user.signOutUser()
		this.props.userChange()
	}

	render() {

		return (
			<Grid id="IconContainer"
				container spacing={8} direction="row"
				justify="center"
				alignItems="flex-end">
				<Grid item xs>
					<Tooltip disableFocusListener disableTouchListener title="Home">
						<IconButton component={Link} to='/' className="button" aria-label="Home">
							<HomeIcon style={{ fontSize: 20 }} />
						</IconButton>
					</Tooltip>	
				</Grid>
				<Grid item xs>
					<CreateRecipe/>
				</Grid>
				<Grid item xs>
					<Tooltip disableFocusListener disableTouchListener title="MyRecipes">
						<IconButton component={Link} to='/my_recipes' className="button" aria-label="My Recipes">
							<KitchenIcon style={{ fontSize: 20 }} />
						</IconButton>
					</Tooltip>	
				</Grid>
				<Grid item xs>
					<Tooltip disableFocusListener disableTouchListener title="MyFavourites">					
						<IconButton component={Link} to='/my_favourites' className="button" aria-label="My Favourites">
							<FavoriteIcon style={{ fontSize: 20 }} />
						</IconButton>
					</Tooltip>	
				</Grid>
				<Grid item xs >
					<AccountSettings />
				</Grid>
				<Grid item xs >
					<Tooltip disableFocusListener disableTouchListener title="Logout">
						<IconButton component={Link} to='/' onClick={this.signOut} className="button" aria-label="Login">
							<PlayForWorkIcon style={{ fontSize: 20 }} />
						</IconButton>
					</Tooltip>
				</Grid>
			</Grid>
		);
	}
}

export default UserPanel;
