import React from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import User from '../database_modules/User';
import Grid from '@material-ui/core/Grid';
import ForgotPassword from './ForgotPassword';
import Dialog from '@material-ui/core/Dialog';

class Login extends React.Component {

    constructor(props) {
        super(props);
    }
    state = {
        email: '',
        password: '',
        open: false,
    }


    user = new User()
    userLogin = () => {
        this.props.handleClose();
        this.user.signInUser(this.email, this.password)
    }

    handleEmail = (event) => {
        this.email = event.target.value
    }

    handlePassword = (event) => {
        this.password = event.target.value
    }

    handleClickOpen = () => {
        this.setState({ open: true });
    };

    handleClose = () => {
        this.setState({ open: false });
    };

    render() {
        return (
            <div>
                <DialogTitle id="form-dialog-title">Login</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Welcome Back! Please type in your Account information to continue :)
              </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="email"
                        label="Email Address"
                        type="email"
                        fullWidth
                        onChange={this.handleEmail}
                    />

                    <TextField
                        margin="dense"
                        id="password"
                        label="Password"
                        type="password"
                        fullWidth
                        onChange={this.handlePassword}
                    />
                    <Grid container justify="space-between" spacing={24}>
                    <Grid item>
                            <Button onClick={this.userLogin} color="primary">
                                Continue
              </Button>
                        </Grid>
                        <Grid item>
                        <Button onClick={this.handleClickOpen} onClose={this.handleClose}>Forgot password</Button>
                        <Dialog open={this.state.open}><ForgotPassword handleClose = {this.handleClose}/></Dialog>
                        </Grid>
                    </Grid>
                </DialogContent>
            </div>
        );
    }
}



export default Login;