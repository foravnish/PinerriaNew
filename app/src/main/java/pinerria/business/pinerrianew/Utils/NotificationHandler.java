package pinerria.business.pinerrianew.Utils;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;



import java.util.List;

import pinerria.business.pinerrianew.Activites.ChatRoomActivity;
import pinerria.business.pinerrianew.R;

/**
 * Created by user on 11/2/2016.
 */

public class NotificationHandler {
    private Context mContext;

    public NotificationHandler(Context mContext) {
        this.mContext = mContext;
    }


    //This method would display the notification
    public void showNotificationMessage(final String title, final String message, String type, int id) {

        Intent intent = null;
        int requestID=0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.drawable.logo);
        if (id == NotifHandler.Same_Id) {
            requestID= NotifHandler.Notification_Id;
            NotifHandler.Num_Noti= NotifHandler.Num_Noti+1;
        } else {
            NotifHandler.Same_Id = id;
            NotifHandler.Num_Noti = 1;
            requestID = (int) System.currentTimeMillis();
            NotifHandler.Notification_Id = requestID;

        }
        if (type.equalsIgnoreCase("send_message")) {
            intent = new Intent(mContext, ChatRoomActivity.class);
        } else if (type.equalsIgnoreCase("get_request")) {
            Util.Get_Rquest_InBg = 0;
            intent = new Intent(mContext, ChatRoomActivity.class);

        } else {
        }
      /*  RemoteViews contentView = new RemoteViews(mContext.getPackageName(),R.layout.com_facebook_activity_layout);
        contentView.setImageViewResource(R.id.image, R.drawable.ic_launcher);
        contentView.setTextViewText(R.id.title, "Custom notification");
        contentView.setTextViewText(R.id.text, "This is a custom layout");
     */

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, requestID, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo));
        builder.setContentTitle(NotifHandler.Num_Noti+" message from "+title);
        builder.setTicker(title);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(message);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        notificationManager.notify(requestID, builder.build());
    }


    //This method will check whether the app is in background or not
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
