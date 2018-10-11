package pinerria.business.pinerrianew.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.SqliteDatabase.DatabaseHelper;
import pinerria.business.pinerrianew.SqliteDatabase.Note;
import pinerria.business.pinerrianew.Utils.MyPrefrences;


public class ChatUSer extends AppCompatActivity {

    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();
    ArrayList<String> userId = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    ImageView backBtn;

    private List<Note> userList = new ArrayList<>();
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        backBtn = (ImageView) findViewById(R.id.backBtn);

        pd = new ProgressDialog(ChatUSer.this);
        pd.setMessage("Loading...");
        pd.show();

        db = new DatabaseHelper(ChatUSer.this);

//        String url = "https://android-chat-app-e711d.firebaseio.com/users.json";
        String url = "https://pinerria-home-business.firebaseio.com/users.json";
//        String url = "https://pinerria-home-business.firebaseio.com/messages.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

                Log.d("sdfsdfdfgfhfghfghfsdfs",s);

                doOnSuccess(s);

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(ChatUSer.this);
        rQueue.add(request);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al2.get(position);
                Intent intent=new Intent(ChatUSer.this, Chat.class);
                intent.putExtra("nameValue",al.get(position));
                intent.putExtra("id",userId.get(position));
                intent.putExtra("value","0");
                startActivity(intent);

                Log.d("dfgdfgsdfgsdfgdfg1",al2.get(position));
                Log.d("dfgdfgsdfgsdfgdfg2",al.get(position));
                Log.d("dfgdfgsdfgsdfgdfg3",userId.get(position));
            }
        });
    }


//    public void doOnSuccess(String s){
//        try {
//            JSONObject obj = new JSONObject(s);
//
//            Iterator i = obj.keys();
//            String key = "";
//
//            while(i.hasNext()){
//                key = i.next().toString();
//
//                Log.d("fsdfsdfsdfsdfsf",key);
//
//                if(!key.equals(UserDetails.username)) {
//
//                    al.add(key);
//
//
//                }
//
//                totalUsers++;
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        if(totalUsers <=1){
//            noUsersText.setVisibility(View.VISIBLE);
//            usersList.setVisibility(View.GONE);
//        }
//        else{
//            noUsersText.setVisibility(View.GONE);
//            usersList.setVisibility(View.VISIBLE);
//            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
//        }
//
//        pd.dismiss();
//    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Log.d("fsdfsdffdfdfdfdfdsdfs", String.valueOf(obj));

            JSONObject resobj = new JSONObject( String.valueOf(obj));
            Iterator<?> keys = resobj.keys();
            while(keys.hasNext() ) {
                String key = (String)keys.next();
                if ( resobj.get(key) instanceof JSONObject ) {
                    JSONObject xx = new JSONObject(resobj.get(key).toString());
                    Log.d("redfdsfssdffdfds1",xx.getString("name"));

                }
            }



            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();


                Log.d("fsdfsdfsdfsdfsf",key);
                if (key.contains(MyPrefrences.getMobile(getApplicationContext()))) {



                    Log.d("FindData",key);
                }
                else{
                    Log.d("FindData","Not Found");
                }



                Log.d("dfgdsfgdgdfgergd",UserDetails.username);
                JSONObject xx = null;
                if(!key.equals(UserDetails.username)) {


                    if ( resobj.get(key) instanceof JSONObject ) {
                        xx = new JSONObject(resobj.get(key).toString());
                        Log.d("redfdsfssdffdfds1",xx.getString("name"));
                        Log.d("redfdsfssdffdfds2",xx.getString("userId"));

                    }

                    //String conver_user=key.substring(0,10);
                    //Log.d("fdgdfgdfgdfg1",conver_user);
                    //  Log.d("fdgdfgdfgdfg2",MyPrefrences.getMobile(getApplicationContext()));

//                    if (conver_user.equals(MyPrefrences.getMobile(getApplicationContext()))){
//                        al.add(key);
//                    }


                    //JSONObject xx = new JSONObject(obj.get(key).toString());

//                    Log.d("gfdgdfgdfgd", String.valueOf(value));
                    //  Log.d("gfdgdfgdfgd", String.valueOf(xx.getJSONObject("name")));
                    al.add(xx.getString("name"));
                    userId.add(xx.getString("userId"));
                    al2.add(key);

                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }




}
//                    Log.d("dfsdfsdfsdewrtfe", String.valueOf(db.getAllUSERS()));
//                    for (int i1=0;i1<db.getUserCount();i1++){
//                        userList.addAll(db.getAllUSERS());
//                        Log.d("fsdffgfgfgfgfsdfsdfsdf",userList.get(i1).getNote());
//
////                        if (key.equals(userList.get(i1).getNote())) {
//                        if (key==userList.get(i1).getNote()) {
//
//                            al.add(key);
//                        }
//                    }
