var mongoose = require('mongoose');
var Schema = mongoose.Schema;

//mongoDB URL
var mongoConnectURL = "mongodb://admin:admin123@ds059644.mongolab.com:59644/find-my-session";
mongoose.createConnection(mongoConnectURL);

var sessionSchema = new Schema({
    sessionName: {type: String, required: true},
    sessionDesc: {type: String, required: true},
    sessionTimings: {type: String, required: true},
    sessionLocation: {type:String, required: true},
    speakerName: {type: String, required: true},
    speakerDetails: {type:String, required: true}

},{
    versionKey: false
});

var Sessions = mongoose.model('Sessions', sessionSchema);

exports.Sessions = Sessions;