import firebase from '../firebase.js';

class Query{

    constructor(collectionID){
        this.db = firebase.firestore().collection(collectionID);
        this.document = null;
        this.collection = null;
    }

    getDocument(documentID){
        this.document = this.db.doc(documentID);
        if(this.document){
                return this.document;
        }    
    }

    filterCollection(field, condition, comparable){
        this.collection = this.db.where(field, condition, comparable);
        if(this.collection){
            return this.collection;
        }
    }
}

export default Query;