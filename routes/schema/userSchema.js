var mongoose = require('mongoose');
var Schema = mongoose.Schema;
//mongoDB URL
var mongoConnectURL = "mongodb://admin:admin123@ds059644.mongolab.com:59644/find-my-session";
mongoose.createConnection(mongoConnectURL);


var sessions = new Schema({
    sessionId: {type: Number, required: true},
    sessionName: {type: String, required: true}
});

var userSchema = new Schema({
    fullName: {type:String, required: true},
    email: {type:String, required: true},
    password: {type:String, required: true},
    linkedInURL: {type:String, required: true},
    contactNumber: {type:String, required: true},
    sessionsAttending: [sessions]
},{
    versionKey: false
});

var Users = mongoose.model('Users', userSchema);

exports.Users = Users;