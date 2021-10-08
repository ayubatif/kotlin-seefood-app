import React from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import User from '../database_modules/User';

class UpdatePassword extends React.Component {

    constructor(props) {
        super(props);
    }
    state = {
        password: '',
    }
    
    user = new User()
    handlePassword = (event) => {
        this.setState({password: event.target.value});
    }
    updatePassword = () => {
        this.props.handleClose();
        if(this.state.password.length > 5){
            this.user.updatePassword(this.state.password);
            this.user.signOutUser();
		    this.props.userChange()
        }
    }
    
    handleClose = () => {
        this.props.handleClose();
    }

    render() {
        return (
            <div><DialogTitle id="form-dialog-title">Password Change</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Use a strong password with at least 6 characters
                    </DialogContentText>
                    
                    <TextField
                        margin="dense"
                        id="passwordNew"
                        label="New Password"
                        type="password"
                        fullWidth
                        onChange={this.handlePassword}
                    />

                    <Button onClick={this.updatePassword} color="primary">Submit</Button>
                    <Button onClick={this.handleClose} color="primary">Cancel</Button>
                </DialogContent>
            </div>



        );
    }
}

export default UpdatePassword;