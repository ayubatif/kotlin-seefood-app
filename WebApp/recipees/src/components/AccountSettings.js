import React, { Component } from 'react';
import Grid from '@material-ui/core/Grid';
import Dialog from '@material-ui/core/Dialog';
import Account from './Account'
import IconButton from '@material-ui/core/IconButton';
import AccountBoxIcon from '@material-ui/icons/AccountBox';
import Tooltip from '@material-ui/core/Tooltip';

class AccountSettings extends Component {
    state = {
        open: false,
    };

    handleClickOpen = () => {
        this.setState({ open: true });
    };

    handleClose = () => {
        this.setState({ open: false });
    };
    
    render() {

        return (
            <Grid item xs>
                <Tooltip disableFocusListener disableTouchListener title="Account">
					<IconButton className="button" aria-label="Account Settings"
					    onClick={this.handleClickOpen}
					>
						<AccountBoxIcon style={{ fontSize: 20 }} />
					</IconButton>
                </Tooltip>
					<Dialog
					    open={this.state.open}
						onClose={this.handleClose}
                        aria-labelledby="form-dialog-title"
                        fullScreen
					>
					<Account  handleClose = {this.handleClose} />
                    
					</Dialog>
			</Grid>
        )
    }
}

export default AccountSettings;