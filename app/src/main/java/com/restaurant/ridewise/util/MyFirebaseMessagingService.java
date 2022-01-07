package com.restaurant.ridewise.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.activities.HomeActivity;
import com.restaurant.ridewise.util.Config;
import com.restaurant.ridewise.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "fcm";

    private NotificationUtils notificationUtils;


    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG,"token >>> "+token);
        super.onNewToken(token);
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getData().size());
        Log.d(TAG, "From: " + remoteMessage.getNotification().getTitle());

       if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Body: " + remoteMessage.getNotification());
            handleNotification(remoteMessage,remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            Log.d("fcm", "Data Payload: " + remoteMessage.getData().toString());
            try {
                handleDataMessage(new JSONObject(remoteMessage.getData().toString()));
            } catch (Exception e) {
                Log.d("fcm", "Exception: " + e.getMessage());
            }
        }

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.app_logo1)
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
        ;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }


    private void handleNotification(RemoteMessage remoteMessage,String message) {
        Log.d(TAG, "handleNotification: "+message);
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message

            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, pushNotification, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Default";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.app_logo1)
                    .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                    .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
            ;
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.d(TAG, "push json: " + json.toString());

        try {
            //    JSONObject data = json.getJSONObject("data");

            String title = json.getString("title");
            String body = json.getString("body");
            /* String message = json.getString("message");
             boolean isBackground = json.getBoolean("is_background");
             String imageUrl = json.getString("image");
             String timestamp = json.getString("timestamp");
             JSONObject payload = json.getJSONObject("payload");*/

            Log.d(TAG, "title: " + title);
            Log.d(TAG, "body: " + body);



            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Default";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.app_logo1)
                    .setContentTitle(title)
                    .setContentText(body).setAutoCancel(true).setContentIntent(pendingIntent);
            ;
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());

            grp_chat_broadcast(MyFirebaseMessagingService.this,body);
            
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();


        } catch (JSONException e) {
            Log.d(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage());
        }
    }

    /*
    Showing notification with text only
     */

    static void grp_chat_broadcast(Context context, String message) {

        Intent intent = new Intent("order_broadcast");

        //put whatever data you want to send, if any
        intent.putExtra("message", message);
     //   intent.putExtra("message_body", msg_body);


        //send broadcast
        context.sendBroadcast(intent);
    }



    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /*
    Showing notification with text and image
     */

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
