import firebase from 'firebase';

// Initalize and export Firebase.
const config = {
    apiKey: "AIzaSyAmuhXHsvNnppr7BPiRreA1v7ZxfEqq_zI",
    authDomain: "id2216-project.firebaseapp.com",
    databaseURL: "https://id2216-project.firebaseio.com",
    projectId: "id2216-project",
    storageBucket: "id2216-project.appspot.com",
    messagingSenderId: "439948695812"
};

export default firebase.initializeApp(config);
