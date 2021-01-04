package com.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonParser;
import com.models.NotificationModel;
import com.tracking.smarttrack.MainActivity;
import com.tracking.smarttrack.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService ";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String msg =  remoteMessage.getData().toString();
        String[] msgDetail;
        NotificationModel msgModel;
        List<NotificationModel> msgList = new ArrayList<NotificationModel>();

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            msg = msg.substring(1, msg.length()-1);
            msgDetail = msg.split(",");


            for(int i = 0 ; i < msgDetail.length ; i++){
                String[] msgData = msgDetail[i].split("=");
                msgModel = new NotificationModel(msgData[0].trim(), msgData[1]);
                Log.e(TAG, "---msgData: " + msgData[0].trim());

                msgList.add(msgModel);

            }


            Log.e(TAG, "---Data Payload: " + remoteMessage.getData().toString());
            String jsonStr = remoteMessage.getData().toString();
            jsonStr = jsonStr.replaceAll("=", "\":\"");
            jsonStr = jsonStr.replaceAll(", ", "\",\"");
            jsonStr = jsonStr.replaceAll("[{]", "{\"");
            jsonStr = jsonStr.replaceAll("[}]", "\"}");


            Log.e(TAG, "----Notification JSON After" + jsonStr);


            try {
                JSONObject json = new JSONObject(jsonStr);
                sendPushNotification(json);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }else{
            Log.e(TAG, "Remote Message size is 0" );
        }


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject jsonPre) {
        //optionally we can display the json into log
        Log.e(TAG, "----Notification JSON " + jsonPre.toString());


        try {
            //getting the json data
            JSONObject json = new JSONObject(jsonPre.toString());

            //parsing json data
            String title = json.getString("title");
            String message = json.getString("message");
            String imageUrl = json.get("image").toString();
           // Log.d("", "" + imageUrl);
            //creating MyNotificationManager object
            NotificationManagerSmart mNotificationManager = new NotificationManagerSmart(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            //if there is no image
            if(imageUrl.equals("") || imageUrl == null){
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void sendPushNotificationString(List<NotificationModel> messageList) {
        //optionally we can display the json into log
        try {

            String title = "";
            String message = "";
            String imageUrl = "";
            Log.d("msgList", "msgList: " + messageList.size());


            for(int i = 0 ; i <messageList.size() ; i++){
                if(messageList.get(i).getKeyName().equals("title"))    {
                    title = messageList.get(i).getValue();
                    Log.e(TAG, "message: " + title );
                }else if(messageList.get(i).getKeyName().equals("message")){
                    message = messageList.get(i).getValue();
                    Log.e(TAG, "message: " + message );
                }else if(messageList.get(i).getKeyName().equals("image")){
                    imageUrl = messageList.get(i).getValue();
                }
            }




            //creating MyNotificationManager object
            NotificationManagerSmart mNotificationManager = new NotificationManagerSmart(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            //if there is no image
            if(imageUrl.equals("") || imageUrl == null){
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    void setPendingIntent(){
        Intent intent  = new Intent(this, MainActivity.class);
        intent.putExtra("key_1","value");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //also tried with  PendingIntent.FLAG_CANCEL_CURRENT


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("title")
                .setContentText("messageBody")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());


    }


    // Format

/*    {
        "to" : "fnr7Uszzl0g:APA91bF-ICU1nzj700Rz6C4qAk3hY5CPf-Z8aGtUWdHYPHO8Nh1Y5TPkknuCfDMACOxUspj6UAYkwEfqXMIdydL-RfHA-pqAm1eI4ogs-uepJXj2pRUxQrdjV9AyszJVuF60JkGZnOQD",
            "notification": {
                "body": "Cool offers. Get them before expiring!",
                "title": "Flat 80% discount",
                "icon": "https://sportsroundtablerak.files.wordpress.com/2011/04/katiedrake1.jpg"
            },
        "data" : {
                "title" : "Mario",
                "message" : "great match",
                "image" : "https://sportsroundtablerak.files.wordpress.com/2011/04/katiedrake1.jpg"



        }
    }
 */

}
