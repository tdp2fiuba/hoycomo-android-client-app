package com.ar.tdp2fiuba.hoycomo.service.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.activity.MyOrdersActivity;
import com.ar.tdp2fiuba.hoycomo.activity.RateStoreActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class HoyComoFirebaseMessagingService extends FirebaseMessagingService {

    public static final String ARG_NOTIFICATION_ID = "notification_id";

    private static final String CHANNEL_ID = "HoyComo";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().get("topic") != null && remoteMessage.getData().get("topic").equals("DELIVERED")){
            sendDeliveredNotification(remoteMessage);
        } else {
            sendBasicNotification(remoteMessage);
        }
    }

    public void sendBasicNotification(RemoteMessage remoteMessage){
        int notificationId = new Random().nextInt(60000);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)  //a resource for your custom small icon
                .setContentTitle(remoteMessage.getData().get("title")) //the "title" value you sent in your notification
                .setContentText(remoteMessage.getData().get("message")) //ditto
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

    public void sendDeliveredNotification(RemoteMessage remoteMessage){
        final int notificationId = new Random().nextInt(60000);

        Intent rateIntent = new Intent(this, RateStoreActivity.class);
        rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        rateIntent.putExtra(ARG_NOTIFICATION_ID, notificationId);
        rateIntent.putExtra(RateStoreActivity.ARG_STORE_ID_TO_RATE, remoteMessage.getData().get("storeId"));
        PendingIntent pendingRateIntent = PendingIntent.getActivity(this, 0, rateIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent rejectIntent = new Intent(this, MyOrdersActivity.class);
        rejectIntent.putExtra(ARG_NOTIFICATION_ID, notificationId);
        rejectIntent.putExtra(MyOrdersActivity.ARG_ORDER_ID_TO_REJECT, remoteMessage.getData().get("orderId"));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(rejectIntent);
        PendingIntent pendingRejectIntent = PendingIntent.getActivity(this, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)  //a resource for your custom small icon
                .setContentTitle(remoteMessage.getData().get("title")) //the "title" value you sent in your notification
                .setContentText(remoteMessage.getData().get("message")) //ditto
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(android.R.drawable.star_off, getString(R.string.rate), pendingRateIntent)
                .addAction(R.drawable.ic_cancel_black_24dp, getString(R.string.reject), pendingRejectIntent)
                .setContentIntent(pendingRateIntent)
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }
}
