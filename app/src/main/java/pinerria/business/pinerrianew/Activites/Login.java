package pinerria.business.pinerrianew.Activites;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;


public class Login extends AppCompatActivity {

    TextView btn_forgot_pwd,btn_skip;
    Button login;
    EditText password,mobile;
    Dialog dialog;
    LinearLayout forRegistration;
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

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),HomeAct.class);
                intent.putExtra("userType","");
                startActivity(intent);

            }
        });

        btn_forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                loginAPi();
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

        final EditText otp_edit= (EditText) dialog2.findViewById(R.id.otp_edit);
        //TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
        //recieve.setText("Sent OTP on "+mob);
        Button submit2=(Button)dialog2.findViewById(R.id.submit2);
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


                        Intent intent = new Intent(Login.this, HomeAct.class);
                        intent.putExtra("userType","user");
                        startActivity(intent);
                        finish();
                        registerGCM();

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




}
