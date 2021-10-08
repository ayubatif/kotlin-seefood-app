import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import Login from './Login';
import SignUp from './SignUp';
import IconButton from '@material-ui/core/IconButton';
import InputIcon from '@material-ui/icons/Input';
import Tooltip from '@material-ui/core/Tooltip';

class Authentication extends React.Component {
  state = {
    open: false,
    status: true,
  };

  handleClickOpen = () => {
    this.setState({ open: true });
  };

  handleClose = () => {
    this.setState({ open: false });
  };

  handleStatus = () => {
    this.setState({ status: !this.state.status })
  }



  render() {
    const status = this.state.status;
    return (
      <div>
        <Tooltip disableFocusListener disableTouchListener title="Login">
          <IconButton onClick={this.handleClickOpen} aria-label="Home">
            <InputIcon style={{ fontSize: 20 }} />
          </IconButton>
        </Tooltip>  
        <Dialog
          open={this.state.open}
          onClose={this.handleClose}
          aria-labelledby="form-dialog-title"
        >

          {(status === true) ? <Button onClick={this.handleStatus}>Click Here To Sign Up</Button>
            : <Button onClick={this.handleStatus}>Click Here To Login</Button>}


          {(status === true) ? <Login handleClose={this.handleClose} />
            : <SignUp handleClose={this.handleClose} />}



        </Dialog>
      </div>
    );
  }
}

export default Authentication;