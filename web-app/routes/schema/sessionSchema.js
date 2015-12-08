var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var autoIncrement = require('mongoose-auto-increment');

//mongoDB URL
var mongoConnectURL = "mongodb://admin:admin123@ds059644.mongolab.com:59644/find-my-session";
var connection = mongoose.createConnection(mongoConnectURL);
autoIncrement.initialize(connection);
var sessionSchema = new Schema({
    sessionId: {type: Number, required: true},
    sessionName: {type: String, required: true},
    sessionDesc: {type: String, required: true},
    sessionTimings: {type: String, required: true},
    sessionLocation: {type:String, required: true},
    speakerName: {type: String, required: true},
    speakerDetails: {type:String, required: true},
    attendees: {type: Number, default: 0},
    currentlyAttending: {type: Number, default: 0}
},{
    versionKey: false
});

var Sessions = mongoose.model('Sessions', sessionSchema);

sessionSchema.plugin(autoIncrement.plugin, {
    model: 'Sessions',
    field: 'sessionId',
    startAt: 1,
    incrementBy: 1
});

exports.Sessions = Sessions;