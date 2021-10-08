import firebase from '../firebase.js';

class User {
    db = firebase.firestore().collection('Users');

    constructor(){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                // User is not null
                this.userID = user.uid;
            } else {
                // User is null
            }
          });
    }


    testUser(){
        firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
                // User is not null
                console.log("User is signed in")
            } else {
                // User is null
                console.log("User is signed out")
            }
          });
    }

    createUser(email, password){
        firebase.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            // ...
            
            console.log(errorCode);
            console.log(errorMessage);
          });
    firebase.auth().useDeviceLanguage();
    this.dataBaseConstruct(); 
    this.testUser();
    this.sendVeritificationEmail();     
    }

    signInUser(email, password){
        firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            // Log the Errors
            console.log(errorCode);
            console.log(errorMessage);
          });
          this.testUser();
    }

    signOutUser(){
        firebase.auth().signOut();
    }

    sendVeritificationEmail(){
        firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
            user.sendEmailVerification().then(function() {
                // Email sent.
                }).catch(function(error) {
                // An error happened.
            });
        } else {
        }});
    }

    dataBaseConstruct(){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                var uid = user.uid;
        
                this.db.doc(uid).set({
                    Email : user.email,
                    DisplayName : user.displayName,
                    EmailVerified : user.emailVerified,
                    Image : user.photoURL,
                  });
                this.db.doc(uid).collection('Favorites').doc('0').set({
                    0 : null
                });
                this.db.doc(uid).collection('Inbox').doc('0').set({
                    Content : null,
                    Sender : null
                });
                this.db.doc(uid).collection('Sent').doc('0').set({
                    Content : null,
                    Sender : null
                });
            } else { 
            }
          });
            
    }

    sendMessage(to, content, messageID){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {

            this.db.doc(to).collection('Inbox').doc(messageID).set({
                //from: user.displayName, // userID enough?
                senderID: user.uid,
                content: content
            })

            this.db.doc(user.uid).collection('Sent').doc(messageID).set({
                //to: User.getDisplayName(to), // function returns undefined
                receiverID: to,
                content: content
            })

        } else {
        }});
    }

    getImage(){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                // default image is batman one
                var imageUrl = 'https://firebasestorage.googleapis.com/v0/b/id2216-project.appspot.com/o/images%2Fimages.jpeg?alt=media&token=a19bcec2-f7ca-4778-9850-c19381eec14d';
                this.db.doc(user.uid).get().then(snap => {
                    if (snap.exists) {
                        imageUrl = snap.data().Image;
                        return imageUrl;
                    } else {
                        // doc.data() will be undefined in this case
                        return imageUrl;
                    }
                });
            } else {
        }});
        
    }

    getInbox(){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                return this.db.doc(user.uid).collection('Inbox');
        } else {
        }});
    }

    getOutbox(){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                return this.db.doc(user.uid).collection('Sent');
        } else {
        }});
    }

    addFavorite(foodID){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                this.db.doc(user.uid).collection('Favorites').doc(foodID).set({});
            } else { 
        }});
    }

    addHistory(foodID){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                this.db.doc(user.uid).collection('History').doc(foodID).set({});
            } else { 
        }});
    }

    addHistoryLimited(foodID){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                // check for recycling
                this.db.doc(user.uid).collection('History').get().then(snap =>{
                    this.db.doc(user.uid).collection('History').doc(foodID).set({});
                    if(snap.size > 20){
                        snap.forEach(historyFood =>{
                            historyFood.ref.delete();
                            return;
                        });
                    }
                    else {
                }});
            }
            else {
        }});       
    }

    addAllergy(ingredient){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                this.db.doc(user.uid).collection('Allergens').doc(ingredient).set({});
            } else { 
        }});
    }

    setImageUrl(url){
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                this.db.doc(user.uid).set({Image : url}, {merge: true});
            } else { 
        }});
    }

    setImage(file){
        firebase.auth().onAuthStateChanged(user => {
            var storage = firebase.storage();
            var storageRef = storage.ref();
            var metadata = {
                contentType: 'image/jpeg',
              };    

            if (user) {
                var task = storageRef.child("Users/" + user.uid).put(file, metadata);
                task.snapshot.ref.getDownloadURL().then(url => { 
                    this.db.doc(user.uid).set({
                        Image: url
                    }, {merge: true});
                });
        } else {
        }});
    }

    updatePassword(newPassword){
        firebase.auth().onAuthStateChanged(user => {  
            if (user) {
                user.updatePassword(newPassword).then(function() {
                    // Update successful.
                  }).catch(function(error) {
                    // An error happened.
                    console.log(error);
                  });
        } else {
        }});
    }

    updateEmail(newEmail){
        firebase.auth().onAuthStateChanged(user => {  
            if (user) {
                user.updateEmail(newEmail).then(function() {
                    // Update successful.
                  }).catch(function(error) {
                    // An error happened.
                    console.log(error);
                  });

                  this.db.doc(user.uid).set({
                      Email : newEmail
                  }, {merge: true});
        } else {
        }});
    }
    
 
}

export default User;