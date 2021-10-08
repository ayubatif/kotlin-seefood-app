import React, { Component } from 'react';
import Grid from '@material-ui/core/Grid';
import Dialog from '@material-ui/core/Dialog';
import Create from './Create'
import IconButton from '@material-ui/core/IconButton';
import AddBoxIcon from '@material-ui/icons/AddBox';
import Tooltip from '@material-ui/core/Tooltip';

class CreateRecipe extends Component {
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
                <Tooltip disableFocusListener disableTouchListener title="CreateRecipe">
					<IconButton className="button" aria-label="Create Recipe"
					    onClick={this.handleClickOpen}
					>
						<AddBoxIcon style={{ fontSize: 20 }} />
					</IconButton>
                </Tooltip>    
					<Dialog
					    open={this.state.open}
						onClose={this.handleClose}
                        aria-labelledby="form-dialog-title"
                        fullScreen
					>
					<Create  handleClose = {this.handleClose} />
                    
					</Dialog>
			</Grid>
        )
    }
}

export default CreateRecipe;