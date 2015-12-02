var user = require('./schema/userSchema');
var Users = user.Users;

var session = require('./schema/sessionSchema');
var Sessions = session.Sessions;


exports.register = function (req, res)
{

	res.render('signup');

}


exports.about = function (req, res){
	
	res.render('about');
}
exports.login = function (req, res)
{

	res.render('login');

}


exports.registerUser = function(req, res){
	
	

    var fullName = req.param('fullName');
    var email = req.param('email');
    var password =req.param('password') ;
    var linkedInURL= req.param('linkedInUrl');
    var contactNumber =  req.param('contactNumber');


    var  newUser = new Users({
        fullName: fullName,
        email: email,
        password: password,
        linkedInURL: linkedInURL,
        contactNumber: contactNumber
    });

    console.log("new User : "+newUser.linkedInUrl);
    var json_response;

    newUser.save(function(err){

        if(err){
        	console.log(err);
            json_response = {
                status: 500,
                data: 'error'
            };
        }
        else{
            json_response = {
                status: 200,
                data: 'New User Added'
            };
        }

        res.send(json_response);
    });

};

exports.userProfile = function (req, res){
	console.log(req.session.userId);
	  if(req.session.userId){
	        res.header('Cache-Control','no-cache, private, no-store, must-revalidate, max-stale=0, post-check=0, pre-check=0');
	        res.render('userProfile');
	    }
	    else{
	        res.redirect('/');
	    }
	//res.render('userProfile');
	}

exports.loginUser = function(req, res){

    var email = req.param('email');
    var password=  req.param('password');
    	

    Users.findOne({email: email, password:password}, function(err,doc){
        if(err){
        	console.log(err);
            json_response = {
                status: 500,
                data: 'error'
            };
        }
        else{
            if(doc){
            	console.log(doc.email);
            	req.session.userId = doc.email;
                json_response = {
                    status: 200,
                    data: doc
                };
                
            }
            else{
                json_response = {
                		
                    status: 300,
                    data: "No User Found"
                };
            }

        }

        res.send(json_response);
    });
};

exports.currentAttendees = function(req, res){

    var sessionId = req.param('sessionId');

    var json_response;

    Sessions.findOne({sessionId: sessionId}, function(err, doc){

        if(err){
            json_response = {
                status: 500,
                data: 'Error in Session Finding'
            };
            res.send(json_response);
        }

        else{
            if(doc){
                doc.currentlyAttending = doc.currentlyAttending + 1;

                console.log(doc.currentlyAttending);
                doc.save(function(err) {

                    if (err) {
                        json_response = {
                            status: 500,
                            data: 'Error in doc saving'
                        };
                        res.send(json_response);
                    } else {
                        json_response = {
                            status: 200,
                            data: 'Success'
                        };
                        res.send(json_response);
                    }
                });
            }
            else{
                json_response = {
                    status: 200,
                    data: 'No Session Found'
                };
                res.send(json_response);
            }
        }
    });
};

exports.addAttendees = function(req, res){

    var sessionId = req.param('sessionId');

    var userId = req.session.userId;

    var json_response;

    var sessionName;

    Sessions.findOne({sessionId: sessionId}, function(err, session) {
        if(err){
            json_response = {
                status: 500,
                data: 'Error in Session Finding'
            };
            res.send(json_response);
        }
        else{
            if(session){

                session.attendees = session.attendees + 1;
                sessionName = session.sessionName;

                console.log("attendees " + session.attendees);
                console.log("session name " + sessionName);

                session.save(function(err){

                    if(err){
                        json_response = {
                            status: 500,
                            data: 'Error in Session doc saving'
                        };
                        res.send(json_response);
                    }
                    else{

                        Users.findOne({email: userId}, function (err, doc) {
                            if(err){
                                json_response = {
                                    status: 500,
                                    data: 'Error in User Finding'
                                };
                                res.send(json_response);
                            }
                            else{
                                if(doc){
                                    doc.sessionsAttending.push({
                                        sessionId: sessionId,
                                        sessionName: sessionName
                                    });
                                    doc.save(function(err){
                                        if(err){
                                            json_response = {
                                                status: 500,
                                                data: 'Error in User doc saving'
                                            };
                                            res.send(json_response);
                                        }
                                        else{
                                            json_response = {
                                                status: 200,
                                                data: 'Success'
                                            };
                                            res.send(json_response);
                                        }
                                    });
                                }
                                else{
                                    json_response = {
                                        status: 200,
                                        data: 'No User Found'
                                    };
                                    res.send(json_response);
                                }
                            }
                        });
                    }
                });
            }
            else{
                json_response = {
                    status: 200,
                    data: 'No Session Found'
                };
                res.send(json_response);
            }
        }

    });
};


exports.logout = function(req, res)
{
	req.session.destroy();
	res.redirect('/');

}