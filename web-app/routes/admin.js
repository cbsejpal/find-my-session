
exports.login = function(req,res){
	
	res.render('adminLogin');
}

exports.displayAdminSessions = function(req, res){
	res.render('displayAdminSessions');
	
}

exports.adminDashboard = function(req, res){
	
	res.render('adminDashboard');
}

exports.adminLogin = function(req, res){

	
    var email = req.param('email');
    var password =  req.param('password');
    console.log(email+" "+password);
    var json_response;
 
    
    if(email == "admin@admin.com" && password == "admin@admin.com"){

    	req.session.adminId = email;
    	 json_response = {
                 status: 200,
                 data: 'successful'
             };
    	res.send(json_response);
    }
    else{
    	
    	 json_response = {
                 status: 500,
                 data: 'error'
             };
    	res.send(json_response);
 	
    }
        
}


