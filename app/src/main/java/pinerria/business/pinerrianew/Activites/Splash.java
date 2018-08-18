package pinerria.business.pinerrianew.Activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import pinerria.business.pinerrianew.Fragments.Home;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

public class Splash extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread background = new Thread() {
            public void run() {
                try {

                    sleep(1*1000);

//                    Intent intent = new Intent(Splash.this, Login.class);
//                    startActivity(intent);
//                    finish();

                    if (MyPrefrences.getCitySelect(Splash.this)==true){
                        Intent intent=new Intent(Splash.this,HomeAct.class);
                        intent.putExtra("userType","");
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(Splash.this, SelectZone.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }



//                    Intent intent=new Intent(Splash.this,HomeAct.class);
//                    intent.putExtra("userType","");
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                    finish();


//
//                    Intent intent=new Intent(Splash.this,HomeAct.class);
//                    intent.putExtra("userType","");
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                    finish();



                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();

    }
}
