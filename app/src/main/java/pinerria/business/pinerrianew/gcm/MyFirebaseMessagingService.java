package pinerria.business.pinerrianew.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;

import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.NotificationHandler;
import pinerria.business.pinerrianew.Utils.Util;

/**
 * Created by user on 11/2/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Map<String, String> message;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Log.e("message", ": " + remoteMessage.getData());

        message = remoteMessage.getData();
//        Log.d("fgdghbfdhdfghfgjh", String.valueOf(remoteMessage.getData()));
        Log.d("fgdghbfdhdfghfgjh", remoteMessage.getData()+"");

        //Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
       // sendNotification1(message.toString());
        if (message.containsKey("type")) {
            Log.d("gdfgdfgdfgdfghd","type");
            //sendNotification(message);

        }
    }


    //This method is generating a notification and displaying the notification

    private void sendNotification(Map<String, String> msg) {
        //Creating a broadcast intent
        Intent pushNotification;

        //We will create this class to handle notifications
        NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());
        //If the app is in foreground
        if (!NotificationHandler.isAppIsInBackground(getApplicationContext())) {
            Log.d("fsdfsdfgsdgfsdg","true");
            //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            if (message.get("type").equalsIgnoreCase("send_message")
                    && Util.ChatOn == 1 && message.get("user_id").equalsIgnoreCase(Util.Sender_ID)) {
                //Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                pushNotification = new Intent(Util.Noti_Msg);
                //Adding notification data to the intent
                pushNotification.putExtra("message", message.get("message"));
                pushNotification.putExtra("date_time", message.get("date_time"));
                pushNotification.putExtra("sender_id", message.get("user_id"));
                pushNotification.putExtra("sender_name", message.get("sender_name"));
                pushNotification.putExtra("sender_image", message.get("sender_image"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                //Log.e("on chat screen", ": ");
            } else if (message.get("type").equalsIgnoreCase("get_request") && Util.ChatDtl_On == 1) {
               // Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                pushNotification = new Intent(Util.Get_Rquest);
                //Adding notification data to the intent
                pushNotification.putExtra("message", message.get("message"));
                pushNotification.putExtra("date_time", message.get("date_time"));
                pushNotification.putExtra("sender_id", message.get("user_id"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            } else {
                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                Util.Noti_Bg = 1;
                Util.Sender_ID = message.get("user_id");
                Util.Sender_Name = message.get("sender_name");
                Util.Sender_Img = message.get("sender_image");

                notificationHandler.showNotificationMessage(Util.Sender_Name, message.get("message"), message.get("type")
                        , Integer.valueOf(message.get("user_id")));
            }

            //Sending a broadcast to the chatroom to add the new message

        } else {
            Log.d("fsdfsdfgsdgfsdg","false");
            //Log.e("App in Background", ": ");
            //If app is in foreground displaying push notification
            //Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
            Util.Noti_Bg = 1;
            Util.Sender_ID = message.get("user_id");
            Util.Sender_Name = message.get("sender_name");
            Util.Sender_Img = message.get("sender_image");
            notificationHandler.showNotificationMessage(Util.Sender_Name, message.get("message"), message.get("type")
                    , Integer.valueOf(message.get("user_id")));
            // LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


            //sendNotification(title, message);

        }
    }


//    private void sendNotification1(String message) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        int requestCode = 0;
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
//        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.logo)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
//    }
}
