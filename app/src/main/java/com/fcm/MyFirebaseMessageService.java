package com.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tracking.constants.Constants;
import com.tracking.smarttrack.LoginActivity;
import com.tracking.smarttrack.NewFeedActivity;
import com.tracking.smarttrack.R;
import com.tracking.smarttrack.fragment.NewFeedFragment;


// By Dhillon on 14/4/19.

public class MyFirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Constants.ShowLocalNotification(getApplicationContext(), remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(), 101);
    }




}


