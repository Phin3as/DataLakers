// app/routes.js

var multiparty = require('connect-multiparty'),
    multipartyMiddleware = multiparty();

var fs = require('fs'),
    S3FS = require('s3fs'),
    s3fsImpl = new S3FS('lakersbucket', {
        accessKeyId: "",
        secretAccessKey: ""
    });

//Create our bucket if it doesn't exist
s3fsImpl.create();

var User = require('../app/models/user');
module.exports = function(app, passport) {
    app.use(multipartyMiddleware);	

    // =====================================
    // HOME PAGE (with login links) ========
    // =====================================
    app.get('/', function(req, res) {
        res.render('index.ejs'); // load the index.ejs file
    });
    
    app.get('/searchWords', function(req, res){
        //Load the request module
        var request = require('request');
        request('http://localhost:8080/search?query=' + req.query.query, function (error, response, body) {
            console.log(body);
            if (!error && response.statusCode == 200) {
                console.log(body) // Show the HTML for the Google homepage. 
            }
        })
    });

    app.get('/search', function(req, res){
        var request = require('request');

        //Lets configure and request
        request({
            url: 'http://ec2-54-186-223-83.us-west-2.compute.amazonaws.com:8080/search', //URL to hit
            qs: req.query, //Query string data
            method: 'GET'
        }, function(error, response, body){
            if(error) {
                console.log(error);
            } else {

                console.log(JSON.parse(body));
            }
            res.render('graph.ejs', {message: body});    
        });
    })
    //########### Making changes here for AWS S3
    
    app.post('/upload', function (req, res) {
	var file = req.files.file;
        var stream = fs.createReadStream(file.path);
        return s3fsImpl.writeFile(file.originalFilename, stream).then(function () {
            fs.unlink(file.path, function (err) {
                if (err) {
                    console.error(err);
                }
            });
            res.redirect(307, 'http://ec2-54-186-223-83.us-west-2.compute.amazonaws.com:8080' + req.path);
            console.log(res); 
        });
    });

    app.get('/assign', function(req, res){
        var request = require('request');

        //Lets configure and request
        request({
            url: 'http://ec2-54-186-223-83.us-west-2.compute.amazonaws.com:8080/assign', //URL to hit
            qs: req.query, //Query string data
            method: 'GET'
        }, function(error, response){
            if(error) {
                console.log(error);
            }  
	    res.render('back.ejs', {user : req.query.user});    
        });
    })
    // =====================================
    // LOGIN ===============================
    // =====================================
    // show the login form
    app.get('/login', function(req, res) {

        // render the page and pass in any flash data if it exists
        res.render('login.ejs', { message: req.flash('loginMessage') }); 
    });
    
    app.get('/allUsers', function(req, res){
        
        User.find({}, function(err, users) {
            if (err) throw err;

                // object of all the users
                console.log(users);
                res.render('allUsers.ejs',
                       { results: users }
                );              
        });        
    });

    app.post('/login', passport.authenticate('local-login', {
        successRedirect : '/profile', // redirect to the secure profile section
        failureRedirect : '/login', // redirect back to the signup page if there is an error
        failureFlash : true // allow flash messages
    }));


    // process the login form
    // app.post('/login', do all our passport stuff here);

    // =====================================
    // SIGNUP ==============================
    // =====================================
    // show the signup form
    app.get('/signup', function(req, res) {

        // render the page and pass in any flash data if it exists
        res.render('signup.ejs', { message: req.flash('signupMessage') });
    });

    // process the signup form
    // app.post('/signup', do all our passport stuff here);
    
  //process the signup form
    app.post('/signup', passport.authenticate('local-signup', {
        successRedirect : '/profile', // redirect to the secure profile section
        failureRedirect : '/signup', // redirect back to the signup page if there is an error
        failureFlash : true // allow flash messages
    }));


    // =====================================
    // PROFILE SECTION =====================
    // =====================================
    // we will want this protected so you have to be logged in to visit
    // we will use route middleware to verify this (the isLoggedIn function)
    app.get('/profile', isLoggedIn, function(req, res) {
        res.render('profile.ejs', {
            user : req.user // get the user out of session and pass to template
        });
    });

    app.get('/listDocs', function(req, res){
        var request = require('request');

        //Lets configure and request
        request({
            url: 'http://ec2-54-186-223-83.us-west-2.compute.amazonaws.com:8080/listDoc', //URL to hit
            qs: req.query, //Query string data
            method: 'GET'
        }, function(error, response, body){
            if(error) {
                console.log(error);
            } else {

                console.log(JSON.parse(body));
            }
            res.render('allDocs.ejs', {message: body});    
        });
    })
    // =====================================
    // LOGOUT ==============================
    // =====================================
    app.get('/logout', function(req, res) {
        req.logout();
        res.redirect('/');
    });
};

// route middleware to make sure a user is logged in
function isLoggedIn(req, res, next) {

    // if user is authenticated in the session, carry on 
    if (req.isAuthenticated())
        return next();

    // if they aren't redirect them to the home page
    res.redirect('/');
}




