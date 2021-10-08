import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid'
import FoodPage from './FoodPage';

const styles = {
  card: {
    maxWidth: 345,
  },
  media: {
    objectFit: 'cover',
  },
};


function FoodCard(props) {
  const { classes, food } = props;
  return (
    <Grid item xs={12} sm = {4}>
      <Card className={classes.card}>
        <CardActionArea>
          <CardMedia
            component="img"
            alt={food.id}
            className={classes.media}
            height="100"
            image={food.ImageUrl}
            title={food.FoodName}
          />
          <CardContent>
            <Typography gutterBottom variant="h5" component="h2">
              {food.FoodName}
            </Typography>
            <Typography component="p">
              {food.Description}
            </Typography>
          </CardContent>
        </CardActionArea>
        <CardActions>
          <Button size="small" color="primary">
            Share
          </Button>
          <FoodPage food={food} />
        </CardActions>
      </Card>
    </Grid>
  );
}


FoodCard.propTypes = {
  classes: PropTypes.object.isRequired,
};


export default withStyles(styles)(FoodCard);