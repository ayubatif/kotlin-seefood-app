

class Utility{

    static generateID(user){
        const date = new Date();
        return(date.getTime() + user.uid + date.getMilliseconds() + (date.getUTCDay() * (date.getUTCFullYear()) / date.getSeconds()) );
    }

    
}

export default Utility;