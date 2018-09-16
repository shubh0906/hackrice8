const fs = require('fs');
const readline = require('readline');
const {google} = require('googleapis');
const base64url = require('base64url');
var striptags = require('striptags');

const axios = require('./axios.js');

// If modifying these scopes, delete token.json.
const SCOPES = ['https://www.googleapis.com/auth/gmail.readonly'];
const TOKEN_PATH = 'token.json';
let tokenData;

// Load client secrets from a local file.


/**
 * Create an OAuth2 client with the given credentials, and then execute the
 * given callback function.
 * @param {Object} credentials The authorization client credentials.
 * @param {function} callback The callback to call with the authorized client.
 */
function authorize (credentials,company ,callback) {
  const {client_secret, client_id, redirect_uris} = credentials.installed;
  const oAuth2Client = new google.auth.OAuth2(
      client_id, client_secret, redirect_uris[0]);

  // Check if we have previously stored a token.
  fs.readFile(TOKEN_PATH, (err, token) => {
    if (err) return getNewToken(oAuth2Client, callback);
    oAuth2Client.setCredentials(JSON.parse(token));
    tokenData = JSON.parse(token);
    callback ("me",company,oAuth2Client);
    //callback(oAuth2Client);
  });
}

/**
 * Get and store new token after prompting for user authorization, and then
 * execute the given callback with the authorized OAuth2 client.
 * @param {google.auth.OAuth2} oAuth2Client The OAuth2 client to get token for.
 * @param {getEventsCallback} callback The callback for the authorized client.
 */
function getNewToken(oAuth2Client, callback) {
  const authUrl = oAuth2Client.generateAuthUrl({
    access_type: 'offline',
    scope: SCOPES,
  });
  console.log('Authorize this app by visiting this url:', authUrl);
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });
  rl.question('Enter the code from that page here: ', (code) => {
    rl.close();
    oAuth2Client.getToken(code, (err, token) => {
      if (err) return console.error('Error retrieving access token', err);
      oAuth2Client.setCredentials(token);
      // Store the token to disk for later program executions
      fs.writeFile(TOKEN_PATH, JSON.stringify(token), (err) => {
        if (err) return console.error(err);
        console.log('Token stored to', TOKEN_PATH);
      });
      callback("me","Aldo",oAuth2Client);
    });
  });
}

/**
 * Lists the labels in the user's account.
 *
 * @param {google.auth.OAuth2} auth An authorized OAuth2 client.
 */

function listMessages(userId, query,auth) {
    const gmail = google.gmail({version: 'v1', auth});
    let userData;
    let data ={};
    gmail.users.getProfile({
        'userId' : userId
    },(err,res) => {
        if (err) return console.log('The API returned an error getProfile: ' + err);
        userData = {
            'user': res.data.emailAddress,
            'updatedTime': new Date().getTime(),
            'token':tokenData
        };
        //data.push(user);
        console.log(userData);
    });


    //console.log("in mess"+JSON.stringify(gmail));
    gmail.users.messages.list({
        'userId': userId,
        'q': query
      },(err,res) =>{
        if (err) return console.log('The API returned an error listMessages: ' + err);
        console.log("\n\n\n\n\n\n"+JSON.stringify(res.data));
        const emails = [];
        const messages = res.data.messages;
        let  counter =0;
        if(messages.length){
            console.log(messages.length);
            messages.forEach ((message) =>{
                //console.log(messages.length+" "+ counter);
                //console.log(` - ${message.id}`);
                gmail.users.messages.get({
                    'userId': userId,
                    'id': message.id
                    
                },(err,res) => {
                    if (err) return console.log('The API returned an error singleMessage: ' + err);
                    let subject;
                    //console.log(striptags(base64url.decode( res.data.raw)));
                    let body ="";
                    if(res.data.payload.parts){
                        res.data.payload.parts.forEach(part =>{
                            if(part.body.size != 0)
                            body+= part.body.data;
                            //console.log(part.body.size);
                        });
                    }
                    //console.log("hello"+striptags(base64url.decode (body)));
                    res.data.payload.headers.forEach ( header => {
                        if(header.name === "Subject")
                        subject=header.value;
                    });
                    emails.push({
                        'emailTime': res.data.internalDate,
                        'emailBody':striptags( base64url.decode(body)),
                        'emailSubject': subject
                    });
                    console.log(counter);    
                    counter ++;
                    if(counter == messages.length){
                        //console.log(striptags( base64url.decode(res.data.payload.parts[0].body.data)));
                        // res.data.payload.headers.forEach ( header => {
                        //     if(header.name === "Subject")
                        //     console.log(header.value);
                        // });
                        //let temp = res.data.payload.parts[0].body.data + res.data.payload.parts[1].body.data;
                        // /console.log(striptags(base64url.decode(temp)));
                        console.log(emails[0]);
                        data= {
                            ...userData,
                            'emails':emails
                        };
                        axios.post("/update/offers",data)
                            .then(res =>{
                                console.log(res.status);
                            })
                            .catch(err => console.log("eeeeeeeeeeeeeeeeeeeeee\n\n\n\n"+err));
                        //axios.get('/').then( res => console.log(res.status));
                        //console.log(data.emails.length);
                        console.log("---------\n"+JSON.stringify(data));
                    }
                });
            });
            
        }
      }
    );
  }
  module.exports.authorize = authorize;
  module.exports.listMessages = listMessages;