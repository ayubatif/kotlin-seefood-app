import React, { Component } from 'react';
import {  Route, Redirect } from 'react-router-dom';


class LoadSearch extends Component {
    render(){
        return(
            <Redirect to = '/search_recipes'/>
        )
    }
}

export default LoadSearch