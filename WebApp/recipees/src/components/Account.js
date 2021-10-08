import React, { Component } from 'react';
import '../styles/account.scss';
import firebase from '../firebase.js'
import Grid from '@material-ui/core/Grid'
import Button from '@material-ui/core/Button'
import UpdatePassword from './UpdatePassword';
import Dialog from '@material-ui/core/Dialog';
import AllergenTable from './AllergenTable';
import { Typography } from '@material-ui/core';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import FireUploader from "react-firebase-file-uploader";
import Avatar from './Avatar';
import User from '../database_modules/User'

class Account extends Component {
	
    state = {
		username: "",
		avatar: "",
        isUploading: false,
        passwordDialog: false,
		progress: 0,
        avatarURL: 'testurl.html',
		open: false,
		invalidPassword: false
	};

	user = new User();
    handleShowDialog = () => {
		this.setState({ open: true });
    };
    handleHideDialog = () => {
        this.setState({ open: false });
	};
	handlePasswordError = () => {

	}
	handleClose = () => {
        this.props.handleClose();
    }

    handleUploadStart = () => this.setState({ isUploading: true, progress: 0 });
	handleProgress = progress => this.setState({ progress });
	handleUploadError = error => {
		this.setState({ isUploading: false });
		console.error(error);
	};
	handleUploadSuccess = filename => {
		this.setState({ avatar: filename, progress: 100, isUploading: false });
		firebase
			.storage()
			.ref("Users")
			.child(filename)
			.getDownloadURL()
            .then(url => 
				this.setState({ avatarURL: url },
					this.user.setImageUrl(url)
					));
            
	};
	
	componentDidMount() {
		firebase.auth().onAuthStateChanged(user => {
            if (user) {
				// default image is successGirl one
				var imageUrl = 'https://firebasestorage.googleapis.com/v0/b/id2216-project.appspot.com/o/Users%2Fsuccess-symbol.jpg?alt=media&token=3cea8163-076c-462d-8699-7624eff5b76a';
				var db = firebase.firestore().collection('Users');
                db.doc(user.uid).get().then(snap => {
                    if (snap.exists) {
                        console.log("User object:", snap.data());
						if(snap.data().Image){
							imageUrl = snap.data().Image;
						}
						this.setState({avatarURL : imageUrl});
                    } else {
                        // doc.data() will be undefined in this case
						this.setState({avatarURL : imageUrl});
					}
                });
            } else {
		}});
	}

	componentWillUnmount() {
		
	}

	render () {
		return (
            <div>
				<AppBar>
					<Toolbar>
						<Grid justify="space-between" spacing={24} container>
							<Button onClick={this.handleClose}>Done</Button>
						</Grid>
					</Toolbar>
				</AppBar>

				<DialogTitle>User Settings</DialogTitle>
				<DialogContent>
					<Grid container spacing={16} direction="row" justify="center" alignItems="center">
                        <Grid xs={12} item>
                            <Typography variant="h3">User Settings</Typography>
                        </Grid>
						<Grid xs={12} item>
                            <Typography variant="h6">Adjust your profile settings and edit your NotMyFood list</Typography>
						</Grid>
						<Grid xs={12} item>
							<Avatar image={this.state.avatarURL} />
						</Grid>
                        <Grid xs={12} item>
							<label className="button-change-password">
								Change Profile Picture
							<FireUploader
									hidden
									accept="image/*"
									storageRef={firebase.storage().ref('Users')}
									onUploadStart={this.handleUploadStart}
									onUploadError={this.handleUploadError}
									onUploadSuccess={this.handleUploadSuccess}
									onProgress={this.handleProgress}
								/>
							</label>
						</Grid>
						<Grid xs={12} item>
							<div><Button className="button-change-password" onClick={this.handleShowDialog} variant="contained">Change your Password</Button></div>
						</Grid>
                        <Grid xs={12} item>
                        <Dialog
                            open={this.state.open}
                            onClose={this.handleHideDialog}
                            aria-labelledby="form-dialog-title"
                        >
                            <UpdatePassword handleClose={this.handleHideDialog} />
                        
                        </Dialog>
                        </Grid>
					</Grid>

                    <Typography variant="h5">Manage your NotMyFood list</Typography>
                    <Typography variant="subtitle1">Ingredients in your list (Allergens/Food you dislike) are not included in recommended recipes</Typography>
                    <AllergenTable/>

				</DialogContent>
			</div>
		);
	}
}

export default Account;
