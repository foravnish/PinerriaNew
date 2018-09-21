package pinerria.business.pinerrianew.Activites;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;


public class Chat extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    ImageView backBtn;
    TextView toolbarTxtName;
    int val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        toolbarTxtName = (TextView) findViewById(R.id.toolbarTxtName);

        Log.d("gdfgdgsdgdfgdfgd",getIntent().getStringExtra("id"));

        Firebase.setAndroidContext(this);
//        reference1 = new Firebase("https://android-chat-app-e711d.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
//        reference2 = new Firebase("https://android-chat-app-e711d.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        Log.d("dfsdfsdgdfg",UserDetails.username);
        Log.d("dfsdfsdgdfg",UserDetails.chatWith);

        reference1 = new Firebase("https://pinerria-home-business.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://pinerria-home-business.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        toolbarTxtName.setText(getIntent().getStringExtra("name"));
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                Log.d("gdfgdfgdfgd",messageText);

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
                String formattedDate = df.format(c.getTime());
                // formattedDate have current date/time


                if(!messageText.equals("")){

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    map.put("date", formattedDate);

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                    chatNotificationApi(messageArea.getText().toString());

                    messageArea.setText("");


                }
            }
        });


        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


//                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                Intent notificationIntent = new Intent(getApplicationContext(), ChatUSer.class);
//                notificationIntent.putExtra("name", "");
//                notificationIntent.putExtra("value", "1");
//                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
//
//                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Chat.this)
//                        .setSmallIcon(R.drawable.message)
//                        .setContentTitle("New Message from " + "name")
//                        .setContentText("mgs")
//                        .setOnlyAlertOnce(true)
//                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                        .setContentIntent(contentIntent);
//                mBuilder.setAutoCancel(true);
//                mBuilder.setLocalOnly(false);
//                mNotificationManager.notify(1, mBuilder.build());


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);


                Log.d("fsddsgdgdf","true");

                val=val+1;
                String message = map.get("message").toString();
                String userName = map.get("user").toString();


               // String date = map.get("date").toString();


                String hr=map.get("date").toString().substring(12,14);
                String min=map.get("date").toString().substring(15,17);
                String time=hr+":"+min;

                String day=map.get("date").toString().substring(0,2);
                String month=map.get("date").toString().substring(3,5);
                String year=map.get("date").toString().substring(6,10);

                Log.d("fdsgfsdgfdgdfgd", String.valueOf(map.size()));
                String date = day+"-"+month+"-"+year;
                String dt2;


//                if (date2.equals(date2)){
//                     dt2=";
//                }
//                else{
//                    dt2=date2;
//                }

                if(userName.equals(UserDetails.username)){
//                    addMessageBox("You:-\n" + message, date,1);
                    addMessageBox( message, time,date,1, getIntent().getStringExtra("name"));
                }
                else{
//                    addMessageBox(UserDetails.chatWith + ":-\n" + message, date,2);
                    addMessageBox(message, time,date,2,getIntent().getStringExtra("name"));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {



            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void chatNotificationApi(final String msg) {
      //  Util.showPgDialog(dialog);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.sendNotificationByUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                     //   Util.cancelPgDialog(dialog);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("success")) {

//                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                errorDialog("Rating Edited successfully.");

                            } else {
                               // Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                       // Toast.makeText(getActivity(), "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                       // Util.cancelPgDialog(dialog);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",MyPrefrences.getUserID(getApplicationContext()));
                params.put("reciever_id",getIntent().getStringExtra("id"));
                params.put("message",msg);

                Log.d("fsdfsdfsfsfsef1",MyPrefrences.getUserID(getApplicationContext()));
                Log.d("fsdfsdfsfsfsef2",getIntent().getStringExtra("id"));
                Log.d("fsdfsdfsfsfsef3",msg);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(postRequest);

    }


    public void addMessageBox(String message,String time,String date, int type,String name){

//        TextView textView = new TextView(Chat.this);
//        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        Log.d("dfsddfdfgdfsdgsg", String.valueOf(val));

        View headerView ;
        TextView txtView,dateTxt,dateTab;
        if(type == 1) {
            headerView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header_r, null, false);
            txtView=headerView.findViewById(R.id.txtView);
            dateTxt=headerView.findViewById(R.id.dateTxt);
            dateTab=headerView.findViewById(R.id.dateTab);

            txtView.setText(message);
            dateTxt.setText(date+", "+time);

            dateTab.setText(date);
            //lp2.gravity = Gravity.LEFT;
            // textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            headerView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header_l, null, false);

            txtView=headerView.findViewById(R.id.txtView);
            dateTxt=headerView.findViewById(R.id.dateTxt);
            dateTab=headerView.findViewById(R.id.dateTab);

            txtView.setText(message);
            dateTxt.setText(date+", "+time);

            dateTab.setText(date);
            // lp2.gravity = Gravity.RIGHT;
            //textView.setBackgroundResource(R.drawable.rounded_corner2);

//
//            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            Intent notificationIntent = new Intent(getApplicationContext(), ChatUSer.class);
//            notificationIntent.putExtra("name", "");
//            notificationIntent.putExtra("value", "1");
//            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
//
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Chat.this)
//                    .setSmallIcon(R.drawable.message)
//                    .setContentTitle("New Message from " + name)
//                    .setContentText(message)
//                    .setOnlyAlertOnce(true)
//                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                    .setContentIntent(contentIntent);
//            mBuilder.setAutoCancel(true);
//            mBuilder.setLocalOnly(false);
//            mNotificationManager.notify(1, mBuilder.build());

        }

        txtView.setLayoutParams(lp2);
        layout.addView(headerView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


}
