import React, { Component } from 'react';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import StarIcon from '@material-ui/icons/Star';
import IconButton from '@material-ui/core/IconButton';
import Card from '@material-ui/core/Card';
import StarBorder from '@material-ui/icons/StarBorder';
import Button from '@material-ui/core/Button';
import firebase from '../firebase.js';
import DisplayAverage from './DisplayAverage';

class RateRecipes extends Component {
    constructor(props){
        super(props);
        this.avgRating();
        this.state = {
            value: 0,
            rating: 0,
            avg: 0
        }
    }
    // avgRating();
    // state = {
    //     value: 0,
    //     rating: 0,
    //     avg: 0
    // }

    handleValue(v){
        this.setState({value: v});
    }

    handleRating(v){
        this.avgRating();
        console.log(this.state.value);
        this.setState({
            rating : this.state.value
        })
            firebase.auth().onAuthStateChanged(user => {
                if(user){
                    console.log(this.props.foodID);
                    console.log(user.uid);
                    firebase.firestore().collection("Foods").doc(this.props.foodID).collection("Ratings").doc(user.uid).set({Rating: this.state.value}, {merge: true})
                }
            })
    }

    avgRating(){
        var sum = 0;
        var n = 0;
        console.log(this.props.foodID);
        firebase.firestore().collection("Foods").doc(this.props.foodID).collection("Ratings").get().then(ratings => {
            ratings.forEach(rating => {
                if(rating.exists){
                    console.log(rating.id+", "+rating.data().Rating);//rating.data.Rating
                    sum += rating.data().Rating;
                    n++;
                } else{
                    console.log("Rating doesn't exist");
                }
                console.log("sum = "+sum);
                console.log("n = "+n);
                // console.log("rating = ");//rating.data.Rating
                // sum += rating.data();
                // n++;
            })
        }).then(() =>{
            if(n!=0){
                this.setState({
                    avg : sum/n
                })
            }
        });
    }

    render(){
        if(this.state.value==1 && !this.state.rating){
            return(
                <Grid item xs={12} sm={6}>
                    <Card>
                        <Typography variant="title">Rate this recipe:</Typography>
                        <IconButton className="button" onClick={() => this.handleValue(1)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(2)}>
                            <StarBorder />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(3)}>
                            <StarBorder />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(4)}>
                            <StarBorder />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(5)}>
                            <StarBorder />
                        </IconButton>
                        <Button onClick={() => this.handleRating()}>Submit</Button>
                    </Card>
                    <Card><DisplayAverage avg={this.state.avg}/></Card>
                </Grid>
            );
        } else if(this.state.value==2 && !this.state.rating){
            return(
            <Grid item xs={12} sm={6}>
                    <Card>
                        <Typography variant="title">Rate this recipe:</Typography>
                        <IconButton className="button" onClick={() => this.handleValue(1)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(2)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(3)}>
                            <StarBorder />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(4)}>
                            <StarBorder />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(5)}>
                            <StarBorder />
                        </IconButton>
                        <Button onClick={() => this.handleRating()}>Submit</Button>
                    </Card>
                    <Card><DisplayAverage avg={this.state.avg}/></Card>
                </Grid>
            );
        } else if(this.state.value==3 && !this.state.rating){
            return(
            <Grid item xs={12} sm={6}>
                    <Card>
                        <Typography variant="title">Rate this recipe:</Typography>
                        <IconButton className="button" onClick={() => this.handleValue(1)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(2)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(3)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(4)}>
                            <StarBorder />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(5)}>
                            <StarBorder />
                        </IconButton>
                        <Button onClick={() => this.handleRating()}>Submit</Button>
                    </Card>
                    <Card><DisplayAverage avg={this.state.avg}/></Card>
                </Grid>
            );
        } else if(this.state.value==4 && !this.state.rating){
            return(
            <Grid item xs={12} sm={6}>
                    <Card>
                        <Typography variant="title">Rate this recipe:</Typography>
                        <IconButton className="button" onClick={() => this.handleValue(1)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(2)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(3)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(4)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(5)}>
                            <StarBorder />
                        </IconButton>
                        <Button onClick={() => this.handleRating()}>Submit</Button>
                    </Card>
                    <Card><DisplayAverage avg={this.state.avg}/></Card>
                </Grid>
            );
        } else if(this.state.value == 5 && !this.state.rating){
            return(
            <Grid item xs={12} sm={6}>
                    <Card>
                        <Typography variant="title">Rate this recipe:</Typography>
                        <IconButton className="button" onClick={() => this.handleValue(1)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(2)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(3)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(4)}>
                            <StarIcon />
                        </IconButton>
                        <IconButton className="button" onClick={() => this.handleValue(5)}>
                            <StarIcon />
                        </IconButton>
                        <Button onClick={() => this.handleRating()}>Submit</Button>
                    </Card>
                    <Card><DisplayAverage avg={this.state.avg}/></Card>
                </Grid>
            );
        } else if(this.state.rating){
            return(
            <Grid item xs={12} sm={6}>
            <Card>
               <Typography  variant={"title"}><b>You rated this recipe {this.state.value}/5 stars!</b></Typography>
               {/* <Typography variant={"h5"}>Average rating: {Math.round(this.state.avg)} stars.</Typography> */}
            </Card>
            <Card><DisplayAverage avg={this.state.avg}/>
            <Button onClick={()=>{this.setState({rating : 0, value: 0})}}>Change Rating</Button>
            </Card>
            </Grid>
        );
        }

        return(
            
            <Grid item xs={12} sm={6}>
                <Card>
                    <Typography variant="title">Rate this recipe:</Typography>
                    <IconButton className="button" onClick={() => this.handleValue(1)}>
                        <StarBorder />
                    </IconButton>
                    <IconButton className="button" onClick={() => this.handleValue(2)}>
                        <StarBorder />
                    </IconButton>
                    <IconButton className="button" onClick={() => this.handleValue(3)}>
                        <StarBorder />
                    </IconButton>
                    <IconButton className="button" onClick={() => this.handleValue(4)}>
                        <StarBorder />
                    </IconButton>
                    <IconButton className="button" onClick={() => this.handleValue(5)}>
                        <StarBorder />
                    </IconButton>
                    {/* <Typography variant={"h5"}>Average rating: {Math.round(this.state.avg)} stars.</Typography> */}
                    <Card>
                    <DisplayAverage avg={this.state.avg}/>
                    </Card>
                </Card>
            </Grid>
        );
        
        
        
        
        
    }
}

export default RateRecipes;