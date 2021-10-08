import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import firebase from '../firebase.js';
import Loading from './Loading.js';
import Grid from '@material-ui/core/Grid'
const CustomTableCell = withStyles(theme => ({
  head: {
    backgroundColor: theme.palette.primary,
    color: theme.palette.secondary,
  },
  body: {
    fontSize: 14,
  },
}))(TableCell);

const styles = theme => ({
  root: {
    maxWidth: '100%',
    maxHeight: '100%',
    marginTop: theme.spacing.unit * 2,
    marginLeft: theme.spacing.unit * 2,
    marginBottom: theme.spacing.unit * 2,
    marginRight: theme.spacing.unit * 2,
    overflowY: 'scroll',
  },
  table: {
    
  },
  row: {
    '&:nth-of-type(odd)': {
      backgroundColor: theme.palette.background.default,
    },
  },
});





class CustomizedTable extends React.Component {
  componentDidMount() {
    this.getRows()
  }
  state = {

    classes: null,
    rowsLoaded: false
  }

  constructor(props) {
    super(props)
    this.state.classes = props.classes
  }
  rows = []
  count = 0
  getRows() {
    this.state.rows = firebase.firestore().collection("Foods").doc(this.props.food.id).collection("Ingredients").get().then(snap => {
      snap.forEach(ingredient => {
        this.count = this.count + 1
        this.rows.push(createData(ingredient.id, ingredient.data().Amount))

        if (this.count == snap.size) {
          this.setState(state => {
            return { rowsLoaded: true }
          })
        }
      })
    })
  }


  render() {

    if (this.state.rowsLoaded) {
      return (

        <Paper className={this.state.classes.root}>
          <Table className={this.state.classes.table}>
            <TableHead>
              <TableRow>
                <CustomTableCell>Ingredients</CustomTableCell>
                <CustomTableCell numeric>Amount</CustomTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {this.rows.map(row => {
                return (
                  <TableRow className={this.state.classes.row} key={row.id}>
                    <CustomTableCell component="th" scope="row">
                      {row.name}
                    </CustomTableCell>
                    <CustomTableCell numeric>{row.amount}</CustomTableCell>

                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </Paper>

      );
    }
    return (

      <Grid item xs> <Loading /> </Grid>)

  }

}

CustomizedTable.propTypes = {
  classes: PropTypes.object.isRequired,
};


let id = 0;
function createData(name, amount) {
  id += 1;
  return { id, name, amount };
}

export default withStyles(styles)(CustomizedTable);