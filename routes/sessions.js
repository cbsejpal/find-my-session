var session = require('./schema/sessionSchema');

var Sessions = session.Sessions;

exports.addSession = function(req, res){

    var sessionName = req.param('sessionName');
    var sessionDesc = req.param('sessionDesc');
    var sessionTimings =req.param('sessionTimings') ;
    var sessionLocation = req.param('sessionLocation');
    var speakerName =  req.param('speakerName');
    var speakerDetails = req.param('speakerDetails');

    var  newSession = new Sessions({
        sessionName: sessionName,
        sessionDesc: sessionDesc,
        sessionTimings: sessionTimings,
        sessionLocation: sessionLocation,
        speakerName: speakerName,
        speakerDetails: speakerDetails
    });

    var json_response;

    newSession.save(function(err){
        if(err){
            json_response = {
                status: 500,
                data: 'error'
            };
        }
        else{
            json_response = {
                status: 200,
                data: 'New Session Created'
            };
        }

        res.send(json_response);
    });
};

exports.getAllSessions = function(req, res){

    Sessions.find({}, function(err,docs){

        if(err){
            json_response = {
                status: 500,
                data: 'error'
            };
        }
        else{
            json_response = {
                status: 200,
                data: docs
            };
        }
        res.send(json_response);
    });
};

exports.getSession = function(req, res){

    var sessionId = req.param('sessionId');

    Sessions.findOne({sessionId: sessionId}, function(err,doc){

        if(err){
            json_response = {
                status: 500,
                data: 'error'
            };
        }
        else{
            json_response = {
                status: 200,
                data: doc
            };
        }
        res.send(json_response);
    });
};