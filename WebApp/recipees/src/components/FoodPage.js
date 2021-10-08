import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import CloseIcon from '@material-ui/icons/Close';
import Slide from '@material-ui/core/Slide';
import Image from './Image'
import Grid from '@material-ui/core/Grid'
import FavoriteButton from './FavoriteButton'
import IngredientsTable from './IngredientsTable';
import TextField from './TextField';
import RateRecipes from './RateRecipes';

const styles = {
    appBar: {
        position: 'relative',
    },
    flex: {
        flex: 1,
    },
};

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

class FullScreenDialog extends React.Component {
    state = {
        open: false,
        IngredientsLoaded: false,
        foodID: this.props.food.id
    };
    componentDidMount() {
        //this.fetchIngredients()
    }
    handleClickOpen = () => {
        this.setState({ open: true });
    };

    handleClose = () => {
        this.setState({ open: false });
    };

    Ingredients = null


    /*fetchIngredients() {
         this.Ingredients = new Promise(resolve => {
             setTimeout(() => {
               resolve(firebase.firestore().collection("Foods").doc(this.props.food.FoodName).collection("Ingredients").get());
             }, 1000);
           })
           console.log(this.Ingredients)
     }
     /*firebase.firestore().collection("Foods").doc(this.props.food.FoodName).collection("Ingredients").get().then(this.setState(state => {
             console.log(this.Ingredients)    
             return { IngredientsLoaded: true }
             }))*/

    
    

    render() {
        const { classes, food } = this.props;
        return (
            <div maxwidth = "100%"  >
                <Button onClick={this.handleClickOpen}>Learn More</Button>
                <Dialog
                    fullScreen
                    open={this.state.open}
                    onClose={this.handleClose}
                    TransitionComponent={Transition}
                >
                    <AppBar className={classes.appBar}>
                        <Toolbar>
                            <IconButton color="inherit" onClick={this.handleClose} aria-label="Close">
                                <CloseIcon />
                            </IconButton>
                            <Typography variant="h6" color="inherit" className={classes.flex}>
                                {food.FoodName}
                            </Typography>
                            <FavoriteButton food = {food}/>
                        </Toolbar>
                    </AppBar>
                    <div flexgrow="2">
                        <Grid
                            container
                            direction="row"
                            justify="flex-start"
                            alignItems="flex-start"
                        >
                            <Grid
                                container
                                spacing={8}
                                direction="row"
                                justify="flex-start"
                                alignItems="flex-start" >
                                <Grid item xs =  {12} sm = {6}>
                                    <Image image={food.ImageUrl} />
                                </Grid>
                                <Grid item xs = {12} sm = {6}>
                                    <TextField text={food.Recipe} />
                                </Grid>
                            </Grid>
                            <Grid
                                container
                                spacing={8}
                                direction="row"
                                justify="flex-start"
                                alignItems="flex-start" >
                                <Grid item xs =  {12} sm = {6}>
                                    <IngredientsTable food={food} />
                                     <RateRecipes foodID = {this.state.foodID}/>
                                </Grid>
                                <Grid item xs =  {12} sm = {6}>
                                    <TextField text={food.Description} />
                                </Grid>
                            </Grid>
                        </Grid>
                    </div>
                </Dialog>
            </div>
        );


    }
}

FullScreenDialog.propTypes = {
    classes: PropTypes.object.isRequired,
};



export default withStyles(styles)(FullScreenDialog);