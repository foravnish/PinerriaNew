package pinerria.business.pinerrianew.Activites;

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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pinerria.business.pinerrianew.R;


public class Chat extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    ImageView backBtn;
    TextView toolbarTxtName;

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





        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                // formattedDate have current date/time


                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    map.put("date", formattedDate);

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);


                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String date = map.get("date").toString();
                // String time = map.get("date").toString();

                if(userName.equals(UserDetails.username)){
//                    addMessageBox("You:-\n" + message, date,1);
                    addMessageBox( message, date,1);
                }
                else{
//                    addMessageBox(UserDetails.chatWith + ":-\n" + message, date,2);
                    addMessageBox(message, date,2);
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

    public void addMessageBox(String message,String date, int type){

//        TextView textView = new TextView(Chat.this);
//        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;


        View headerView ;
        TextView txtView,dateTxt;
        if(type == 1) {
            headerView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header_r, null, false);
            txtView=headerView.findViewById(R.id.txtView);
            dateTxt=headerView.findViewById(R.id.dateTxt);

            txtView.setText(message);
            dateTxt.setText(date);
            //lp2.gravity = Gravity.LEFT;
           // textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            headerView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header_l, null, false);

            txtView=headerView.findViewById(R.id.txtView);
            dateTxt=headerView.findViewById(R.id.dateTxt);

            txtView.setText(message);
            dateTxt.setText(date);
           // lp2.gravity = Gravity.RIGHT;
            //textView.setBackgroundResource(R.drawable.rounded_corner2);
        }

        txtView.setLayoutParams(lp2);
        layout.addView(headerView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }



}
