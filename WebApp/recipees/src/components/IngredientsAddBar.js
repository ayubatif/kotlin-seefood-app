import React, { Component } from 'react';
import AsyncSelect from 'react-select/lib/Async';
import firebase from '../firebase.js';
import { Link, Redirect } from 'react-router-dom';
import '../styles/ingredientsAddBar.scss';
import Grid from '@material-ui/core/Grid';
import IconButton from '@material-ui/core/IconButton';

import AddBoxIcon from '@material-ui/icons/AddBox';
import SnackBar from './SnackBar';
import Tooltip from '@material-ui/core/Tooltip';

class IngredientsAddBar extends Component {
  state = {
    query: [],
    IngredientsAdded: false
  };
  componentDidMount() {
    db.get().then((snap) => {
      snap.forEach(ingredient =>
        Ingredients.push({ label: ingredient.id, value: ingredient.get("value") }))
    });
  }
  handleInputChange = (event) => {
    const query = event;
    this.setState({ query: event, IngredientsAdded: false });
    return query;
  };

  handleAddClick = () => {
    this.setState({IngredientsAdded: true})
    this.props.handleAdd(this.state.query);
  };

  render() {
    if(this.state.IngredientsAdded){
      return (

        <div className="nonFixedSearchBar">
          <Grid className="searchContainer"
            container spacing={8} direction="row"
            justify="center"
            alignItems="flex-start">
            <Grid item xs={11}>
              <AsyncSelect
                isMulti
                cacheOptions
                defaultOptions
                onChange={this.handleInputChange}
                loadOptions={promiseOptions}
              />
            </Grid>
  
  
            <Grid item xs={1}>
              <Tooltip disableTouchListener disableFocusListener title="Confirm Ingredients">
                <IconButton onClick={this.handleAddClick} className="button" aria-label="My Recipes">
                  <AddBoxIcon style={{ fontSize: 20 }} />
                </IconButton>
              </Tooltip>  
            </Grid>
          </Grid>
          <SnackBar message = {"Ingredients are Added"} />
        </div>
  
      );
    }
    return (

      <div className="nonFixedSearchBar">
        <Grid className="searchContainer"
          container spacing={8} direction="row"
          justify="center"
          alignItems="flex-start">
          <Grid item xs={11}>
            <AsyncSelect
              isMulti
              cacheOptions
              defaultOptions
              onChange={this.handleInputChange}
              loadOptions={promiseOptions}
            />
          </Grid>


          <Grid item xs={1}>
            <Tooltip disableTouchListener disableFocusListener title="Confirm Ingredients">
             <IconButton onClick={this.handleAddClick} className="button" aria-label="My Recipes">
                <AddBoxIcon style={{ fontSize: 20 }} />
              </IconButton>
            </Tooltip>  
          </Grid>
        </Grid>

      </div>

    );
  }

}

const db = firebase.firestore().collection("Ingredients");

const Ingredients = [];

const filter = (inputValue) =>
  Ingredients.filter(i =>
    i.label.toLowerCase().includes(inputValue.toLowerCase())
  );

const promiseOptions = inputValue =>
  new Promise(resolve => {
    setTimeout(() => {
      resolve(filter(inputValue));
    }, 1000);
  });

export default IngredientsAddBar; 