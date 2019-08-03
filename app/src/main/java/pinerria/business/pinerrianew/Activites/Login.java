package pinerria.business.pinerrianew.Activites;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

import static android.text.Html.fromHtml;


public class Login extends AppCompatActivity {

    TextView btn_forgot_pwd,btn_skip,tnc;
    Button login;
    EditText password,mobile;
    Dialog dialog;
    LinearLayout forRegistration;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String otpString;
    EditText otp_edit;
    JSONObject jsonObject1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        dialog=new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        forRegistration=findViewById(R.id.forRegistration);
        btn_forgot_pwd=findViewById(R.id.btn_forgot_pwd);
        btn_skip=findViewById(R.id.skip);
        tnc=findViewById(R.id.tnc);

        Firebase.setAndroidContext(this);

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),HomeAct.class);
                intent.putExtra("userType","");
                startActivity(intent);

            }
        });
        tnc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Login.this,TermandConditions.class));

                    }
                });

        btn_forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (checkAndRequestPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.
                }*/

                final Dialog dialog2 = new Dialog(Login.this);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.get_mobile_no);
              //  dialog2.setCancelable(false);

                final EditText mobile_edit= (EditText) dialog2.findViewById(R.id.mobile_edit);
                mobile_edit.setText(mobile.getText().toString());
                //TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
                //recieve.setText("Sent OTP on "+mob);
                Button submit2=(Button)dialog2.findViewById(R.id.submit2);
                submit2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (TextUtils.isEmpty(mobile_edit.getText().toString())){

                            Toast.makeText(Login.this, "Please Enter Mobile No.", Toast.LENGTH_SHORT).show();
                        }

                        else {

                            mobileVerify_API(mobile_edit.getText().toString());

                        }
                    }
                });

                dialog2.show();
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()) {
                    loginAPi();
                }
            }
        });

        forRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Registration.class);
                startActivity(intent);
            }
        });
    }

    private void mobileVerify_API(final String mobileNo) {

        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.forgetPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("dfsjfdfsdfgd", "Login Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

                        optVerfy(mobileNo);


                    }
                    else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();

//                        Util.errorDialog(Login.this,jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.cancelPgDialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobileNo);


                return params;
            }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
        };
        // Adding request to request queue
        queue.add(strReq);


    }

    private void optVerfy(final String mobileNo) {

        final Dialog dialog2 = new Dialog(Login.this);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.otp_dialog_verfy);
//        dialog2.setCancelable(false);

        otp_edit= (EditText) dialog2.findViewById(R.id.otp_edit);
        TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
        TextView reSend= (TextView) dialog2.findViewById(R.id.reSend);
        recieve.setText("Sent OTP on "+mobileNo);
        Button submit2=(Button)dialog2.findViewById(R.id.submit2);
        reSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendOtp(mobileNo);
            }
        });
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(otp_edit.getText().toString())){

                    Toast.makeText(Login.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }

                else {

                    verifyOTP_API(mobileNo,otp_edit.getText().toString());

                }
            }
        });
        dialog2.show();

    }

    private void resendOtp(final String mobile) {

            Util.showPgDialog(dialog);

            RequestQueue queue = Volley.newRequestQueue(Login.this);
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Api.forgetPassword, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Util.cancelPgDialog(dialog);
                    Log.e("dfsjfdfsdfgdotp", "Login Response: " + response);


                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                        if (jsonObject.optString("status").equals("success")) {



                        }
                        else{
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                            Util.errorDialog(Login.this,jsonObject.getString("message"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.cancelPgDialog(dialog);
                    Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),"Please Connect to the Internet", Toast.LENGTH_LONG).show();
                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.e("fgdfgdfgdf","Inside getParams");

                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("mobile", mobile);


                    return params;
                }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
            };
            // Adding request to request queue
            queue.add(strReq);




    }


    private void verifyOTP_API(final String mob, final String otp) {

        Util.showPgDialog(dialog);


        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.checkOtp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("OTP", "top Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);

                        newPasswordApi(jsonObject1.optString("id"));



                    }
                    else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.cancelPgDialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("otp", otp);
                params.put("mobile", mob);

                return params;
            }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
        };
        // Adding request to request queue
        queue.add(strReq);

    }

    private void newPasswordApi(final String id) {

        final Dialog dialog2 = new Dialog(Login.this);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.new_password);
//        dialog2.setCancelable(false);

        final EditText newPassword= (EditText) dialog2.findViewById(R.id.newPassword);
        final EditText confirm_newPassword= (EditText) dialog2.findViewById(R.id.confirm_newPassword);
        //TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
        //recieve.setText("Sent OTP on "+mob);
        Button submit2=(Button)dialog2.findViewById(R.id.submit2);
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(newPassword.getText().toString())){

                    Toast.makeText(Login.this, "Please Enter New password ", Toast.LENGTH_SHORT).show();
                }

                else {
                    if (TextUtils.isEmpty(confirm_newPassword.getText().toString())){

                        Toast.makeText(Login.this, "Please Enter Confirm New password ", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        resetPwd_API(dialog2, confirm_newPassword.getText().toString(),id);
                    }

                }
            }
        });
        dialog2.show();

    }

    private void resetPwd_API(final Dialog dilog2, final String s1, final String id) {
        Util.showPgDialog(dialog);


        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.userResetPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("dfdffdfdfsjfdfsdfgd", "Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(),Login.class));


                    }
                    else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.cancelPgDialog(dialog);
                Log.e("fdgddffdfdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", id);
                params.put("new_password", s1);

                Log.d("dsfsdfsdfsdfs",id);
                Log.d("dsfsdfsdfsdfs",s1);


                return params;
            }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
        };
        // Adding request to request queue
        queue.add(strReq);

    }


    private void loginAPi() {

        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.userLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("dfsjfdfsdfgd", "Login Response: " + response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

                        JSONArray jsonArray=jsonObject.getJSONArray("message");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

//                        Intent intentReg = new Intent(Login.this, GCMRegistrationIntentService.class);
//                        startService(intentReg);


                        //  Toast.makeText(getApplicationContext(), "Login Successfully...", Toast.LENGTH_SHORT).show();

                        MyPrefrences.setUserLogin(getApplicationContext(), true);
                        MyPrefrences.setUserID(getApplicationContext(), jsonObject1.optString("id").toString());
                        MyPrefrences.setUSENAME(getApplicationContext(), jsonObject1.optString("name").toString());

                        MyPrefrences.setMobile(getApplicationContext(), jsonObject1.optString("mobile").toString());
                        MyPrefrences.setImage(getApplicationContext(),jsonObject1.optString("image").toString());
                        MyPrefrences.setCityID2(getApplicationContext(),jsonObject1.optString("city_id").toString());
                        MyPrefrences.setState(getApplicationContext(),jsonObject1.optString("state_id").toString());
                        MyPrefrences.setState(getApplicationContext(),jsonObject1.optString("state_id").toString());
                        MyPrefrences.setCompanyName(getApplicationContext(),jsonObject1.optString("company_name").toString());
                        MyPrefrences.setEMAILID(getApplicationContext(),jsonObject1.optString("email_address").toString());
                        MyPrefrences.setTexAddress(getApplicationContext(),jsonObject1.optString("tax_address").toString());


                        Intent intent = new Intent(Login.this, HomeAct.class);
                        intent.putExtra("userType","user");
                        startActivity(intent);
                        finish();
                        registerGCM();

                    }
                    else{
                        Log.d("gdfgdfgdfgdfgd",jsonObject.getString("message"));

                            if (jsonObject.getString("message").equals("User Registered But Not Verify Otp")){

                                otpVerfy(mobile.getText().toString());
                            }
                            else if (jsonObject.getString("message").equalsIgnoreCase("Mobile Number Not Registered. Please Register as a User.")){

                                errorDialog(Login.this,"Mobile Number Not Registered.\nPlease Register as a User.");
                            }
                            else if (jsonObject.getString("message").equalsIgnoreCase("Invalid Password. Please retry.")){

                                errorDialog2(Login.this,"Invalid Password. \nPlease retry.",password);
                            }


//                        else{
////                        if (jsonObject.getString("message").equals())
////                                errorDialog(Login.this,jsonObject.getString("message")+"."+"\nPlease Register as a User.");
//                                errorDialog(Login.this,jsonObject.getString("message"));
////                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.cancelPgDialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobile.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
        };
        // Adding request to request queue
        queue.add(strReq);

    }

    private void registerGCM() {
        Intent registrationComplete = null;
        String token = null;

            token= MyPrefrences.getgcm_token(this);
            //token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            //Log.w("GCMRegIntentService", "token:" + token);

            sendRegistrationTokenToServer(token);

    }
    private void sendRegistrationTokenToServer(final String token) {
        //Getting the user id from shared preferences
        //We are storing gcm token for the user in our mysql database
        final String id = MyPrefrences.getUserID(getApplicationContext());
        //Log.w("GCMRegIntentService", "loadUserid:" + id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_STORE_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {

                            Log.d("dfsdfsdfsdfsdfs",s);
                            JSONObject jsonObject=new JSONObject(s);
                            if (jsonObject.optString("status").equalsIgnoreCase("failure")){
                                //Toast.makeText(getApplicationContext(), "Some Error! Contact to Admin...", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Log.w("GCMRegIntentService", "sendRegistrationTokenToServer! ErrorListener:" );
                        Toast.makeText(getApplicationContext(), "sendRegistrationTokenToServer! ErrorListener", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fcm_id", token);
                params.put("user_id", id);

                Log.d("fdsfsdgfsdgdfgd",token);
                Log.d("fdsfsdgfsdgdfgd",id);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

                Log.d("sgdfgdfgdfgdfd",message);
                String upToNCharacters =message.substring(11, Math.min(message.length(),15));

                Log.d("gsdfgsdfdgvd",upToNCharacters);
//                otpString=message.substring(message.length()-8).replaceAll("\\s+","");
                otp_edit.setText(upToNCharacters);
            }
        }
    };


    private void otpVerfy(final String mob) {

        resendOtp(mob);
        final Dialog dialog2 = new Dialog(Login.this);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.otp_dialog_verfy);
        //dialog2.setCancelable(false);

        otp_edit= (EditText) dialog2.findViewById(R.id.otp_edit);
        TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
        TextView reSend= (TextView) dialog2.findViewById(R.id.reSend);
        reSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendOtp(mob);
            }
        });

        recieve.setText("Sent OTP on "+mob);
        Button submit2=(Button)dialog2.findViewById(R.id.submit2);
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(otp_edit.getText().toString())){

                    Toast.makeText(Login.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }

                else {

                    verifyOTP_API2(mob,otp_edit.getText().toString(),dialog2);

                }
            }
        });

        dialog2.show();

    }



    private void verifyOTP_API2(final String mob, final String otp, final Dialog dialogOTP) {

        Util.showPgDialog(dialog);


        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.checkOtp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("OTP", "topResponse: " + response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {


                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        jsonObject1 = jsonArray.getJSONObject(0);


                        //TODO Registration for Firebase


                        String url = "https://pinerria-home-business.firebaseio.com/users.json";

                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                            @Override
                            public void onResponse(String s) {
                                Util.cancelPgDialog(dialog);
                                Log.d("fdsfgdgdfgdfgd",""+s);

                                Log.d("sdfsdfsdfsdfsdfs",mob);

                                Firebase reference = new Firebase("https://pinerria-home-business.firebaseio.com/users");
                                Firebase reference2 = new Firebase("https://pinerria-home-business.firebaseio.com/users");
                                Firebase reference3 = new Firebase("https://pinerria-home-business.firebaseio.com/users");


                                if(s.equals("null")) {

                                    reference.child(mob).child("password").setValue("123456");
                                    reference2.child(mob).child("name").setValue(jsonObject1.optString("name").toString());
                                    reference3.child(mob).child("userId").setValue(jsonObject1.optString("id").toString());


                                    Toast.makeText(getApplicationContext(), "Registration Successfully... Please Login.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this,   Login.class));
                                    finish();

                                }
                                else {
                                    try {
                                        JSONObject obj = new JSONObject(s);

                                        if (!obj.has(mob)) {

                                            reference.child(mob).child("password").setValue("123456");
                                            reference2.child(mob).child("name").setValue(jsonObject1.optString("name").toString());
                                            reference3.child(mob).child("userId").setValue(jsonObject1.optString("id").toString());

                                            Toast.makeText(getApplicationContext(), "Registration Successfully... Please Login.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Login.this,   Login.class));
                                            finish();

                                        } else {
                                            Toast.makeText(Login.this, "User already exists", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Util.cancelPgDialog(dialog);
                            }

                        },new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                System.out.println("" + volleyError );
                                Util.cancelPgDialog(dialog);
                            }
                        });

                        RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                        rQueue.add(request);


                    }
                    else{
//                        dialogOTP.dismiss();
                        Util.cancelPgDialog(dialog);
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.cancelPgDialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("otp", otp);
                params.put("mobile", mob);

                return params;
            }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
        };
        // Adding request to request queue
        queue.add(strReq);

    }

    public static void errorDialog(final Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogcustom2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
        text.setText(message);
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                Intent intent=new Intent(context,Registration.class);
                context.startActivity(intent);

            }
        });
        dialog.show();

    }

    public static void errorDialog2(final Context context, String message, final EditText password) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogcustom2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
        text.setText(message);
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                password.requestFocus();


            }
        });
        dialog.show();

    }

    private boolean validate() {

        if (TextUtils.isEmpty(mobile.getText().toString())) {
            Util.errorDialog(Login.this, "Please enter Mobile No");
            return false;
        } else if (TextUtils.isEmpty(password.getText().toString())) {
            Util.errorDialog(Login.this, "Please enter Password");
            return false;

        }
            return true;

        }

}
