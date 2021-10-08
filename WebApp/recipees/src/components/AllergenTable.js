import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import { Checkbox, Tooltip, Typography, IconButton } from '@material-ui/core';
import classNames from 'classnames';
import Toolbar from '@material-ui/core/Toolbar';
import DeleteIcon from '@material-ui/icons/Delete';
import firebase from '../firebase.js'
import IngredientsAddBar from './IngredientsAddBar.js';

let counter = 0;
function createData(label) {
  counter += 1;
  return { id: counter, label};
}

const rows = [
  { id: 'label', numeric: false, disablePadding: true, label: 'Ingredient' },
];

class AllergenTableHead extends React.Component {
  render() {
    const { onSelectAllClick, numSelected, rowCount } = this.props;

    return (
      <TableHead>
        <TableRow>
          <TableCell padding="checkbox">
            <Checkbox
              indeterminate={numSelected > 0 && numSelected < rowCount}
              checked={numSelected === rowCount}
              onChange={onSelectAllClick}
            />
          </TableCell>
          {rows.map(row => {
            return (
              <TableCell
                key={row.id}
                numeric={row.numeric}
                padding={'none'} // default or none
              >
              <Typography variant='h6'>Ingredient</Typography>
              </TableCell>
            );
          }, this)}
        </TableRow>
      </TableHead>
    );
  }
}

AllergenTableHead.propTypes = {
  numSelected: PropTypes.number.isRequired,
  onSelectAllClick: PropTypes.func.isRequired,
  rowCount: PropTypes.number.isRequired,
};

const styles = theme => ({
  root: {
    width: '95%',
    marginTop: theme.spacing.unit * 3,
  },
  table: {
    
  },
  tableWrapper: {
    overflowY: 'auto',
  },
});

class AllergenTable extends React.Component {
  state = {
    selected: [],
    data: [],
    newAllergy: [],
    page: 0,
    rowsPerPage: 5
  };

  componentDidMount() {
    firebase.auth().onAuthStateChanged(user => {
      if (user) {
        var db = firebase.firestore().collection('Users');
        var allergenList = [];
        db.doc(user.uid).collection('Allergens').get().then(snap => {
          snap.forEach(allergen => {
            allergenList.push( createData(allergen.get('label')));
          })
          this.setState({data : allergenList});
          this.setState({rowsPerPage : this.state.data.length});
        });
      } else {
    }});
  }

  componentDidUpdate() {
    if(this.state.rowsPerPage !== this.state.data.length){
      this.setState({rowsPerPage : this.state.data.length});
    }
  }
  
  handleSelectAllClick = event => {
    if (event.target.checked) {
      this.setState(state => ({ selected: state.data.map(n => n.id) }));
      return;
    }
    this.setState({ selected: [] });
  };

  handleClick = (event, id) => {
    const { selected } = this.state;
    const selectedIndex = selected.indexOf(id);
    let newSelected = [];

    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, id);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selected.slice(1));
    } else if (selectedIndex === selected.length - 1) {
      newSelected = newSelected.concat(selected.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(
        selected.slice(0, selectedIndex),
        selected.slice(selectedIndex + 1),
      );
    }
    this.setState({ selected: newSelected });
  };

  handleAdd = (newAllergy) => {
    firebase.auth().onAuthStateChanged(user => {
      if (user) {
        this.setState({newAllergy : newAllergy});
        var db = firebase.firestore().collection('Users');
        newAllergy.forEach(allergen => {
          db.doc(user.uid).collection('Allergens').doc(allergen.label).set({
            label: allergen.label,
            value: allergen.value
          });
        });
        // rebuild list
        var allergenList = [];
        this.counter = 0;
        db.doc(user.uid).collection('Allergens').get().then(snap => {
          snap.forEach(allergen => {
            allergenList.push( createData(allergen.get('label')));
          })
          this.setState({data : allergenList});
        });
        this.setState({rowsPerPage : this.state.data.length});
        this.setState({newAllergy : []});
      } else {
    }});
	}

  handleDelete = () => {
    this.state.selected.forEach(x => {
      firebase.auth().onAuthStateChanged(user => {
        var ingredient =  '';
        // remove from selected
        for(var i = this.state.data.length - 1; i >= 0; i--) {
          if(this.state.data[i].id === x) {
            ingredient = this.state.data[i].label;
            this.state.data.splice(i, 1);
            i = 0;
          }
        } 
        this.setState({selected : []});
        // delete from db
        firebase.firestore().collection('Users').doc(user.uid).collection('Allergens').where("label", "==", ingredient)
        .get()
        .then(querySnapshot => {
          querySnapshot.forEach((doc) => {
            doc.ref.delete().then(() => {
            }).catch(function(error) {
            });
          });
        })  
      });
    });
  };

  isSelected = id => this.state.selected.indexOf(id) !== -1;

  render() {
    const { classes } = this.props;
    const { data, selected, page, rowsPerPage} = this.state;
    const emptyRows = rowsPerPage - Math.min(rowsPerPage, data.length - page * rowsPerPage);

    return (
      <div>
        <IngredientsAddBar handleAdd={this.handleAdd} />
        <Paper className={classes.root}>
            <Toolbar
              className={classNames(classes.root, {
                [classes.highlight]: selected.length > 0,
              })}
            >
            <div className={classes.title}>
              {selected.length > 0 ? (
                <Typography color="inherit" variant="subtitle1">
                  {selected.length} selected
                </Typography>
              ) : (
                <Typography variant="h6" id="tableTitle">
                  NotMyFood list
                </Typography>
              )}
            </div>
            <div className={classes.spacer} />
            <div className={classes.actions}>
              {selected.length > 0 ? (
                <Tooltip title="Delete">
                  <IconButton onClick={this.handleDelete} aria-label="Delete">
                    <DeleteIcon />
                  </IconButton>
                </Tooltip>
              ) : (
                <Tooltip title="">
                  <IconButton aria-label="">
                  
                  </IconButton>
                </Tooltip>
              )} 
            </div>
          </Toolbar>
          <div className={classes.tableWrapper}>
            <Table className={classes.table} aria-labelledby="tableTitle">
              <AllergenTableHead
                numSelected={selected.length}
                onSelectAllClick={this.handleSelectAllClick}
                rowCount={data.length}
              />
              <TableBody>
                {data.map(n => {
                    const isSelected = this.isSelected(n.id);
                    return (
                      <TableRow
                        hover
                        onClick={event => this.handleClick(event, n.id)}
                        role="checkbox"
                        aria-checked={isSelected}
                        tabIndex={-1}
                        key={n.id}
                        selected={isSelected}
                      >
                        <TableCell padding="checkbox">
                          <Checkbox checked={isSelected} />
                        </TableCell>
                        <TableCell component="th" scope="row" padding="none">
                          {n.label}
                        </TableCell>
                      </TableRow>
                    );
                  })}
                {emptyRows > 0 && (
                  <TableRow style={{ height: 49 * emptyRows }}>
                    <TableCell colSpan={6} />
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </div>
        </Paper>
      </div>
    );
  }
}

AllergenTable.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(AllergenTable);
