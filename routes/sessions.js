var session = require('./schema/sessionSchema');

var Sessions = session.Sessions;

exports.sessionDetails = function (req, res){
	var sessionId = req.param("sessionId");
	res.render('displayUserSessions',{sessionId : sessionId});
}


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

    console.log(newSession);
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

    	console.log(json_response);
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
            if(docs){
                json_response = {
                    status: 200,
                    data: docs
                };
            }
            else{
                json_response = {
                    status: 200,
                    data: "No Sessions found"
                };
            }

        }
        res.send(json_response);
    });
};

exports.getSession = function(req, res){

    var sessionId = req.param('sessionId');

    Sessions.findOne({sessionId: sessionId}, function(err,doc){

    	console.log("outside doc");
        if(err){

        	console.log("error");
            json_response = {
                status: 500,
                data: 'error'
            };
        }
        else{
            if(doc){
                json_response = {
                        status: 200,
                        data: doc
                    };
            }
            else{
                json_response = {
                    status: 300,
                    data: "No Session found"
                };
            }
        }
      res.send(json_response);
    });
};