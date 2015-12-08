var express = require('express');
var http = require('http');
var path = require('path');
var favicon = require('static-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var mongo = require('./routes/mongo');
var routes = require('./routes');
var sessions = require('./routes/sessions');
var user = require('./routes/user');
var admin = require('./routes/admin');
var expressSession = require("express-session");
var mongoStore = require("connect-mongo")(expressSession);



var mongoose = require('mongoose');
//mongoDB URL
var mongoConnectURL = "mongodb://myAdmin123:myAdmin123@c280.alcatraz.0.mongolayer.com:10280/find-my-session";
var mongoSessionConnectURL = "mongodb://myAdmin123:myAdmin123@c280.alcatraz.0.mongolayer.com:10280/sessions";
mongoose.connect(mongoConnectURL);

console.log("connected");

var app = express();


// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');
app.set('port', process.env.PORT || 4000);
app.use(favicon());
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded());
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use(expressSession({
    secret : 'mySECRETMongoDBString',
    resave : false, // don't save session if unmodified
    saveUninitialized : false, // don't create session until something stored
    duration : 30 * 60 * 1000,
    activeDuration : 5 * 60 * 1000,
    store : new mongoStore({
        url : mongoSessionConnectURL
    })
}));
app.use(app.router);

app.get('/', routes.index);
app.post('/addSession', sessions.addSession);
app.get('/getAllSessions', sessions.getAllSessions);

app.get('/getSession', sessions.getSession);

app.get('/sessionDetails',sessions.sessionDetails);


app.get('/registerUser',user.register);
app.post('/registerUser', user.registerUser);
app.get('/loginUser',user.login);
app.post('/loginUser', user.loginUser);
app.get('/userProfile',user.userProfile);
app.get('/addAttendees', user.addAttendees);
app.get('/currentAttendees', user.currentAttendees);
app.get('/adminDashboard',admin.adminDashboard);
app.get('/logout',user.logout);
app.get('/about',user.about);


app.get('/adminLogin',admin.login);
app.post('/adminLogin',admin.adminLogin);
app.get('/displayAdminSessions',admin.displayAdminSessions);

/// catch 404 and forwarding to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

/// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
    app.use(function(err, req, res, next) {
        res.render('error', {
            message: err.message,
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.render('error', {
        message: err.message,
        error: {}
    });
});


module.exports = app;
mongo.connect(mongoSessionConnectURL, function() {

   /* http.createServer(app).listen(app.get('port'), function() {
        console.log('Express server listening on port ' + app.get('port'));
    });*/
});