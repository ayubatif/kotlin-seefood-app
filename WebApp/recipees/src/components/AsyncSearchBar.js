import React, { Component } from 'react';

import AsyncSelect from 'react-select/lib/Async';
import firebase from '../firebase.js';
import { Link, Redirect } from 'react-router-dom';
import '../styles/searchbar.scss';
import Grid from '@material-ui/core/Grid';
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';

class AsyncSearchBar extends Component {
  state = {
    query: []
  };
  componentDidMount() {
    db.get().then((snap) => {
      var i = 1;
      snap.forEach(ingredient =>
        Ingredients.push({ label: ingredient.id, value: ingredient.get("value") }))
    });
  }
  handleInputChange = (event) => {
    const query = event;
    this.setState({ query: event });
    return query;
  };

  handleSearchClick = () => {
    this.props.onSearchClick(this.state.query);
  };

  

  render() {
    return (

      <div className="searchBar">
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

            <IconButton onClick={this.handleSearchClick} component={Link} to='/load_search' className="button" aria-label="My Recipes">
              <SearchIcon style={{ fontSize: 20 }} />
            </IconButton>
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

export default AsyncSearchBar; 