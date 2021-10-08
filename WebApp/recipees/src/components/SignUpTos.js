import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

class SignUpTos extends React.Component {
    state = {
        open: false,
        scroll: 'paper',
    };

    handleClickOpen = scroll => () => {
        this.setState({ open: true, scroll });
    };

    handleClose = () => {
        this.setState({ open: false });
    };

    render() {
        return (
            <div>
                <Button onClick={this.handleClickOpen('paper')}>Continue</Button>
                <Dialog
                    open={this.state.open}
                    onClose={this.handleClose}
                    scroll={this.state.scroll}
                    aria-labelledby="scroll-dialog-title"
                >
                    <DialogTitle id="scroll-dialog-title">Privacy Policy</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            Your privacy is important to us. It is Recipeers AB's policy to respect your privacy regarding any information we may collect from you across our website, http://www.Recipeers.com, and other sites we own and operate.
                            We only ask for personal information when we truly need it to provide a service to you. We collect it by fair and lawful means, with your knowledge and consent. We also let you know why we’re collecting it and how it will be used.
                            We only retain collected information for as long as necessary to provide you with your requested service. What data we store, we’ll protect within commercially acceptable means to prevent loss and theft, as well as unauthorised access, disclosure, copying, use or modification.
                            We don’t share any personally identifying information publicly or with third-parties, except when required to by law.
                            Our website may link to external sites that are not operated by us. Please be aware that we have no control over the content and practices of these sites, and cannot accept responsibility or liability for their respective privacy policies.
                            You are free to refuse our request for your personal information, with the understanding that we may be unable to provide you with some of your desired services.
                            Your continued use of our website will be regarded as acceptance of our practices around privacy and personal information. If you have any questions about how we handle user data and personal information, feel free to contact us.
                            This policy is effective as of 12 November 2018.
            </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose} color="primary">
                            Cancel
            </Button>
                        <Button onClick={this.props.SignUp} color="primary">
                            Agree
            </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

export default SignUpTos;