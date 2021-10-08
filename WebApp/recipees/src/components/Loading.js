import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import LinearProgress from '@material-ui/core/LinearProgress';

const styles = {
  root: {
    flexGrow: 2,
    position: "fixed",
    width: "100%",
    height: "100%",
    
    top: "50%",
  },
};

function LinearQuery(props) {
  const { classes } = props;
  return (
    <div className={classes.root}>
      <LinearProgress variant="query" />
    </div>
  );
}

LinearQuery.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(LinearQuery);