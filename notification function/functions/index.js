'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.notifications = functions.database.ref('/Messages/{pushId}')
.onWrite((change, context) =>
{

	
      // Grab the current value of what was written to the Realtime Database.
      const original = change.after.val();
		
	const payload = {
        notification: {
            title: original.title ,
            body:  original.body ,//valueObject.body || valueObject.photoUrl,
            sound: "default"
	},
	};


	//Create an options object that contains the time to live for the notification and the priority
        const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24,
	sheduling:"now" 
        };
 

        return admin.messaging().sendToTopic("pushNotifications", payload, options);
});
