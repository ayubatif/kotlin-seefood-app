import firebase from '../firebase.js';
import React, { Component } from 'react';
import Button from '@material-ui/core/Button';




class FavoriteButton extends Component {
    state = {
        food: this.props.food,
        isFavorite: false,
        isChecked: false
    }

    checkFavorite = () => {
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                firebase.firestore().collection("Users").doc(user.uid).collection("Favorites").where("ID", "==", this.state.food.id).get().then(check => {
                    if (check.size > 0) {
                        this.setState(state => {
                            return { isFavorite: true, isChecked: true }
                        })
                        return false
                    } else {
                        this.setState(state => {
                            return { isFavorite: false, isChecked: true }
                        })
                        return true
                    }

                })
            }
        })
    }


    handleFavorite = () => {

        var currentFood = this.state.food
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                firebase.firestore().collection("Users").doc(user.uid).collection("Favorites").doc(currentFood.id).set({ ID: currentFood.id }, { merge: true })
                this.checkFavorite()
            }
        })
    }

    handleUnFavorite = () => {

        var currentFood = this.state.food
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                firebase.firestore().collection("Users").doc(user.uid).collection("Favorites").doc(currentFood.id).delete()
                this.checkFavorite()
            }
        })
    }

    render() {
        if (this.state.isChecked == false) {
            this.checkFavorite()
        }
        if (this.state.isFavorite) {
            return ((<Button color="inherit" onClick={this.handleUnFavorite}>
                Unfavourite
            </Button>))
        }
        return (<Button color="inherit" onClick={this.handleFavorite}>
            Favourite
        </Button>)
    }
}

export default FavoriteButton