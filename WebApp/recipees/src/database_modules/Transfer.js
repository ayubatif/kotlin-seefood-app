import firebase from '../firebase.js';

class Transfer{
    constructor(){
        // Get a reference to the storage service, which is used to create references in storage bucket
        this.storage = firebase.storage();

        // Create a storage reference from Firebase storage service
        this.storageRef = this.storage.ref();
    }


    uploadFile(path, file){
        var task = this.storageRef.child(path + file.name).put(file);
        this.getUrl(task);
    }

    getUrl(task){
        task.snapshot.ref.getDownloadURL().then(function(downloadURL) {
            });
    }


    downloadFile(path, fileName, elementId){
        this.storageRef.child(path + fileName).getDownloadURL().then(function(url) {
            // `url` is the download URL for 'images/stars.jpg'
          
          
            // Or inserted into an <img> element:
            var img = document.getElementById(elementId);
            img.src = url;
          }).catch(function(error) {
            // Handle any errors
          });
    }

    deleteFile(path, fileName){
        // Delete the file
        this.storageRef.child(path + fileName).delete().then(function() {
        // File deleted successfully
        }).catch(function(error) {
        // Uh-oh, an error occurred!
        console.log(error);
        });
    }
}


export default Transfer;
