'use strict';

var loopback = require('loopback');
var boot = require('loopback-boot');

var app = module.exports = loopback();





app.start = function() {
  // start the web server
  return app.listen(function() {
    app.emit('started');
    var baseUrl = app.get('url').replace(/\/$/, '');
    console.log('Web server listening at: %s', baseUrl);
    if (app.get('loopback-component-explorer')) {
      var explorerPath = app.get('loopback-component-explorer').mountPath;
      console.log('Browse your REST API at %s%s', baseUrl, explorerPath);
    }
  });
};

// Bootstrap the application, configure models, datasources and middleware.
// Sub-apps like REST API are mounted via boot scripts.
boot(app, __dirname, function(err) {
  if (err) throw err;

  // start the server if `$ node server.js`
  if (require.main === module)
    app.start();
});


/*var express = require('express');
var multer = require('multer'),
  bodyParser = require('body-parser'),
  path = require('path');

var exp = express();

exp.post('/',function(req,res){
  res.send("hello")
})

var upload = multer({ dest: 'uploads/' })

exp.post('/up', upload.single('avatar'), function (req, res, next) {
  // req.file is the `avatar` file 
  // req.body will hold the text fields, if there were any 
  console.log(req.file);
  console.log(req.body);
  res.send("666666");
})
 
exp.listen(8000,function(){
  console.log("listening pn port 8000")
})
*/


