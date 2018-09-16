const express = require('express');
const fs = require('fs');
var app = express();
const {authorize,listMessages} = require('./index.js');

let port = 5000;
app.get('/addEmail/:company',(req, res) => {
    var comapnyName = req.params.company;
    fs.readFile('credentials.json', (err, content) => {
        if (err) return console.log('Error loading client secret file:', err);
        // Authorize a client with credentials, then call the Gmail API.
        //console.log("in read");
      //   authorize(JSON.parse(content),(err,res) => {
      //       console.log("in read"+ JSON.stringify(res));
      //       listLabels(res);
      //     listMessages("me,aldo",res);
      //   });
          authorize(JSON.parse(content),comapnyName,listMessages);
      });
});

app.listen(port, () => {
    console.log(`Started up at port ${port}`);
  });
  
  module.exports = {app};