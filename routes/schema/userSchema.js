var mongoose = require('mongoose');
var Schema = mongoose.Schema;
//mongoDB URL
var mongoConnectURL = "mongodb://admin:admin123@ds059644.mongolab.com:59644/find-my-session";
mongoose.createConnection(mongoConnectURL);

var userSchema = new Schema({

    fullName: {type:String, required: true},
    email: {type:String, required: true},
    password: {type:String, required: true},
    linkedInURL: {type:String, required: true},
    contactNumber: {type:String, required: true}
});

var Users = mongoose.model('Users', userSchema);