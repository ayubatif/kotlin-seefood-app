import React, { Component } from 'react';
import StarIcon from '@material-ui/icons/Star';
import StarBorder from '@material-ui/icons/StarBorder';
import Typography from '@material-ui/core/Typography';


class DisplayAverage extends Component{
    // state = {
    //     avg: Math.round(this.props.avg)
    // }

    render(){
        if(Math.round(this.props.avg) == 1){
            return(
            <div><Typography variant={"title"}>Average rating: </Typography><StarIcon/><StarBorder/><StarBorder/><StarBorder/><StarBorder/></div>
            );
        } else if(Math.round(this.props.avg) == 2){
            return(
            <div><Typography variant={"title"}>Average rating: </Typography><StarIcon/><StarIcon/><StarBorder/><StarBorder/><StarBorder/></div>
            );
        } else if(Math.round(this.props.avg) == 3){
            return(
            <div><Typography variant={"title"}>Average rating: </Typography><StarIcon/><StarIcon/><StarIcon/><StarBorder/><StarBorder/></div>
            );
        } else if(Math.round(this.props.avg) == 4){
            return(
            <div><Typography variant={"title"}>Average rating: </Typography><StarIcon/><StarIcon/><StarIcon/><StarIcon/><StarBorder/></div>
            );
        } else if(Math.round(this.props.avg) == 5){
            return(
            <div><Typography variant={"title"}>Average rating: </Typography><StarIcon/><StarIcon/><StarIcon/><StarIcon/><StarIcon/></div>
            );
        } else{
            return(
            <div><Typography variant={"title"}>This recipe has no reviews. Be the first to review it!</Typography></div>
            );
        }
    }
}

export default DisplayAverage;