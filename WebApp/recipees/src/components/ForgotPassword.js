import React, { Component } from 'react';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';

class ForgotPassword extends Component {

    render() {
        return (
            <div>
                <DialogTitle>Forgot Password</DialogTitle>
                <DialogContent>
                    <TextField
                        fullWidth
                        id="standard-with-placeholder"
                        label="E-mail address"
                        margin="normal"
                    />
                    <Grid container justify="space-between" spacing={0}>
                        <Grid item>
                            <IconButton color="inherit" onClick={this.props.handleClose} aria-label="Close"
                                size="small"
                            >
                                <CloseIcon />
                            </IconButton>
                        </Grid>
                        <Grid item>
                            <Button onClick={this.handleClick}>Reset Password</Button>
                        </Grid>
                    </Grid>
                </DialogContent>
            </div>
        );
    }
}

export default ForgotPassword;

/*
password text frield from:
https://material-ui.com/demos/text-fields/
*/