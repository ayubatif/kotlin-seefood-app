import React, { Component } from 'react';
import '../styles/account.scss';
import { Link } from 'react-router-dom';
import IconButton from '@material-ui/core/IconButton';
import HomeIcon from '@material-ui/icons/Home';
import Grid from '@material-ui/core/Grid'
import Authentication from './Authentication';
import Tooltip from '@material-ui/core/Tooltip';

class User extends Component {


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
                <Grid item xs><Authentication/></Grid>
                
            </Grid>
        );
    }
}

export default User;