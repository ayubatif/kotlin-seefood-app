import React from 'react';
import TextField from '@material-ui/core/TextField';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import User from '../database_modules/User';
import SignUpTos from './SignUpTos'

class Login extends React.Component {

    constructor(props) {
        super(props);
    }
    state = {
        email: '',
        password: ''
    }


    user = new User()


    userSignUp = () => {
        this.props.handleClose();
        this.user.createUser(this.email, this.password)
    }

    handleEmail = (event) => {
        this.email = event.target.value
    }

    handlePassword = (event) => {
        this.password = event.target.value
    }

    render() {
        return (
            <div><DialogTitle id="form-dialog-title">Sign Up</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Welcome! Please type in your desired Account information to sign up :)
              </DialogContentText>
                    <TextField
                        key="email"
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

                    <SignUpTos SignUp = {this.userSignUp}/>
                </DialogContent>
            </div>



        );
    }
}



export default Login;