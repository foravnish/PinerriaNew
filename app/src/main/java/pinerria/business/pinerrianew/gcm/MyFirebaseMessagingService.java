package pinerria.business.pinerrianew.gcm;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pinerria.business.pinerrianew.Activites.Chat;
import pinerria.business.pinerrianew.Activites.ChatUSer;
import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.Activites.UserDetails;
import pinerria.business.pinerrianew.R;


/**
 * Created by Andriod Avnish on 06-Mar-18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    //  private NotificationUtils notificationUtils;

    Bitmap image;
    PendingIntent contentIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "Fromgdfhgdfghfgjf: " + remoteMessage.getMessageType());
        Log.e(TAG, "Fromgdfhgdfghfgjf: " + remoteMessage.getData());
        // Log.e(TAG, "Fromgdfhgdfghfgjf: " + remoteMessage.getNotification().getBody());


//        Map<String, String> data2 = remoteMessage.getData();
//        String myCustomKey = data2.get("my_custom_key");
//
        Log.d("gdfgdfgdfgdfgdfgd", remoteMessage.getData().toString());

        JSONObject jsonObject = new JSONObject(remoteMessage.getData());

        Log.d("dfgfdgdfgtrd", String.valueOf(jsonObject));


        //Todo notification



        if (!isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message

            Log.d("vgfvgdfgdfgdfgdfg","true");

        }else{
            // If the app is in background, firebase itself handles the notification
            Log.d("vgfvgdfgdfgdfgdfg","false");


            Log.d("gsdfgdfgrfdfgdgdf1",jsonObject.optString("mobile_no"));       //  mobile no
            Log.d("gsdfgdfgrfdfgdgdf2",jsonObject.optString("name"));            // name
            Log.d("gsdfgdfgrfdfgdgdf3",jsonObject.optString("receiver_id"));     // user id of receiver

            if (jsonObject.optString("status").equals("chat")) {
                UserDetails.chatWith = jsonObject.optString("mobile_no");

                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(jsonObject.optString("body"));
                Notification notification;
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        getApplicationContext());
                Intent notificationIntent = new Intent(getApplicationContext(), Chat.class);

                notificationIntent.putExtra("nameValue",jsonObject.optString("name"));
                notificationIntent.putExtra("id",jsonObject.optString("receiver_id"));

                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
                notification = mBuilder.setSmallIcon(R.mipmap.noti_icon).setTicker("Pinerria").setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(jsonObject.optString("title"))
                        .setTicker("Pinerria")
//                .setContentIntent(resultPendingIntent)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                        .setStyle(inboxStyle)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(jsonObject.optString("body")))
//                .setWhen(getTimeMilliSec(timeStamp))
                        .setSmallIcon(R.mipmap.noti_icon)
                        .setContentIntent(contentIntent)
                       //  .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(jsonObject.optString("body"))
                        .build();

                Random random = new Random();
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(random.nextInt(), notification);

            }

            else {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(jsonObject.optString("body"));
                Notification notification;
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        getApplicationContext());

                Intent notificationIntent = new Intent(getApplicationContext(), HomeAct.class);
                notificationIntent.putExtra("userType", "notification");
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
                notification = mBuilder.setSmallIcon(R.mipmap.noti_icon).setTicker("Pinerria").setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(jsonObject.optString("title"))
                        .setTicker("Pineria")
//                .setContentIntent(resultPendingIntent)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                        .setStyle(inboxStyle)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(jsonObject.optString("body")))
//                .setWhen(getTimeMilliSec(timeStamp))
                        .setSmallIcon(R.mipmap.noti_icon)
                        .setContentIntent(contentIntent)
                        // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(jsonObject.optString("body"))
                        .build();

                Random random = new Random();
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(random.nextInt(), notification);


            }

        }



        if (jsonObject.optString("status").equals("chat")) {
//            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//            inboxStyle.addLine(jsonObject.optString("body"));
//            Notification notification;
//            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                    getApplicationContext());
//            Intent notificationIntent = new Intent(getApplicationContext(), ChatUSer.class);
//            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
//            notification = mBuilder.setSmallIcon(R.mipmap.logo_noti).setTicker("Pinerria").setWhen(0)
//                    .setAutoCancel(true)
//                    .setContentTitle(jsonObject.optString("title"))
//                    .setTicker("Pineria")
////                .setContentIntent(resultPendingIntent)
//                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                    .setStyle(inboxStyle)
////                .setWhen(getTimeMilliSec(timeStamp))
//                    .setSmallIcon(R.mipmap.logo_noti)
//                    .setContentIntent(contentIntent)
//                    // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))
////                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                    .setContentText(jsonObject.optString("body"))
//                    .build();
//
//
//            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, notification);

        }

        else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.addLine(jsonObject.optString("body"));
            Notification notification;
            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    getApplicationContext());

            Intent notificationIntent = new Intent(getApplicationContext(), HomeAct.class);
            notificationIntent.putExtra("userType", "");
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
            notification = mBuilder.setSmallIcon(R.mipmap.noti_icon).setTicker("Pinerria").setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(jsonObject.optString("title"))
                    .setTicker("Pineria")
//                .setContentIntent(resultPendingIntent)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                    .setStyle(inboxStyle)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(jsonObject.optString("body")))
//                .setWhen(getTimeMilliSec(timeStamp))
                    .setSmallIcon(R.mipmap.noti_icon)
                    .setContentIntent(contentIntent)
                    // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(jsonObject.optString("body"))
                    .build();

            Random random = new Random();
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(random.nextInt(), notification);


        }




//    private void handleNotification(String message) {
//        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//            // app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message", message);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
//        }else{
//            // If the app is in background, firebase itself handles the notification
//        }
//    }

//    private void handleDataMessage(JSONObject json) {
//        Log.e(TAG, "push json: " + json.toString());
//
//        try {
//            JSONObject data = json.getJSONObject("data");
//
//            String title = data.getString("title");
//            String message = data.getString("message");
//            boolean isBackground = data.getBoolean("is_background");
//            String imageUrl = data.getString("image");
//            String timestamp = data.getString("timestamp");
//            JSONObject payload = data.getJSONObject("payload");
//
//            Log.e(TAG, "title: " + title);
//            Log.e(TAG, "message: " + message);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
//            Log.e(TAG, "imageUrl: " + imageUrl);
//            Log.e(TAG, "timestamp: " + timestamp);
//
//
//            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();
//            } else {
//                // app is in background, show the notification in notification tray
//                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//                resultIntent.putExtra("message", message);
//
//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
//            }
//        } catch (JSONException e) {
//            Log.e(TAG, "Json Exception: " + e.getMessage());
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//        }
//    }

        /**
         * Showing notification with text only
         */
//    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
//    }

        /**
         * Showing notification with text and image
         */
//    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
//    }
    }





    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }



}