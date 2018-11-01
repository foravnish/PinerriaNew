package pinerria.business.pinerrianew.gcm;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import pinerria.business.pinerrianew.Fragments.Home;
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

        Log.d("bgdfgbfbdfgdfgdfgf", jsonObject.optString("status"));

        if (!isAppIsInBackground(getApplicationContext())) {      //not in background
            // app is in foreground, broadcast the push message


            Log.d("bgdfgbfbdfgdfgdfgf", String.valueOf(HomeAct.chat_flag));


            if (jsonObject.optString("status").equals("chat") && HomeAct.chat_flag==true) {
                UserDetails.chatWith = jsonObject.optString("mobile_no");

                Log.d("vgfvgdfgdfgdfgdfg","one");
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(jsonObject.optString("body"));

                //  LoginForChat();

                Notification notification;
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        getApplicationContext());
//                Intent notificationIntent = new Intent(getApplicationContext(), Chat.class);
//                notificationIntent.putExtra("nameValue",jsonObject.optString("name"));
//                notificationIntent.putExtra("id",jsonObject.optString("receiver_id"));
//                notificationIntent.putExtra("value1","notifiscr");

                Intent notificationIntent = new Intent(getApplicationContext(), HomeAct.class);
                notificationIntent.putExtra("userType", "chatscr");

                UserDetails.mobileNo=jsonObject.optString("mobile_no");
                UserDetails.name=jsonObject.optString("name");
                UserDetails.chatId=jsonObject.optString("receiver_id");

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
                Log.d("vgfvgdfgdfgdfgdfg","two");
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
        else{         // in background
            // If the app is in background, firebase itself handles the notification


            Log.d("gsdfgdfgrfdfgdgdf1",jsonObject.optString("mobile_no"));       //  mobile no
            Log.d("gsdfgdfgrfdfgdgdf2",jsonObject.optString("name"));            // name
            Log.d("gsdfgdfgrfdfgdgdf3",jsonObject.optString("receiver_id"));     // user id of receiver

            if (jsonObject.optString("status").equals("chat")) {
                Log.d("vgfvgdfgdfgdfgdfg","three");
                UserDetails.chatWith = jsonObject.optString("mobile_no");

                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(jsonObject.optString("body"));

              //  LoginForChat();

                Notification notification;
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        getApplicationContext());
//                Intent notificationIntent = new Intent(getApplicationContext(), Chat.class);
//                notificationIntent.putExtra("nameValue",jsonObject.optString("name"));
//                notificationIntent.putExtra("id",jsonObject.optString("receiver_id"));
//                notificationIntent.putExtra("value1","notifiscr");

                Intent notificationIntent = new Intent(getApplicationContext(), HomeAct.class);
                notificationIntent.putExtra("userType", "chatscr");

                UserDetails.mobileNo=jsonObject.optString("mobile_no");
                UserDetails.name=jsonObject.optString("name");
                UserDetails.chatId=jsonObject.optString("receiver_id");

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
                Log.d("vgfvgdfgdfgdfgdfg","four");
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

//        if (jsonObject.optString("status").equals("chat")) {
//
//        }
//
//        else {
//            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//            inboxStyle.addLine(jsonObject.optString("body"));
//            Notification notification;
//            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                    getApplicationContext());
//
//            Intent notificationIntent = new Intent(getApplicationContext(), HomeAct.class);
//            notificationIntent.putExtra("userType", "notification");
//            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
//            notification = mBuilder.setSmallIcon(R.mipmap.noti_icon).setTicker("Pinerria").setWhen(0)
//                    .setAutoCancel(true)
//                    .setContentTitle(jsonObject.optString("title"))
//                    .setTicker("Pineria")
////                .setContentIntent(resultPendingIntent)
//                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
////                    .setStyle(inboxStyle)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(jsonObject.optString("body")))
////                .setWhen(getTimeMilliSec(timeStamp))
//                    .setSmallIcon(R.mipmap.noti_icon)
//                    .setContentIntent(contentIntent)
//                    // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))
////                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                    .setContentText(jsonObject.optString("body"))
//                    .build();
//
//            Random random = new Random();
//            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(random.nextInt(), notification);
//
//
//        }



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