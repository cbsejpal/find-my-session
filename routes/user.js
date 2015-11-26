var user = require('./schema/userSchema');

var Users = user.Users;

exports.registerUser = function(req, res){

    var fullName = req.param('fullName');
    var email = req.param('email');
    var password =req.param('password') ;
    var linkedInURL = req.param('linkedInURL');
    var contactNumber =  req.param('contactNumber');


    var  newUser = new Users({
        fullName: fullName,
        email: email,
        password: password,
        linkedInURL: linkedInURL,
        contactNumber: contactNumber
    });

    var json_response;

    newUser.save(function(err){

        if(err){
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

exports.loginUser = function(req, res){

    var email = req.param('email');
    var password=  req.param('password');

    Users.findOne({email: email, password:password}, function(err,doc){
        if(err){
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
                    status: 200,
                    data: "No User Found"
                };
            }

        }

        res.send(json_response);
    });
};