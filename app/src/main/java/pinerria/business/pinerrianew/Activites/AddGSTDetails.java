package pinerria.business.pinerrianew.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

import static android.text.Html.fromHtml;

public class AddGSTDetails extends AppCompatActivity {


    Dialog dialog;
    TextView paymentId,amount,descreption;
    Button submit;
    CheckBox checkBob;
    String  flag="false";
    EditText oder_id,user_email,tax_address,gst_number,company_name;

    ArrayList<HashMap<String, String>> custom_post_parameters;
    private static final int ACC_ID = 27791;// Provided by EBS
    private static final String SECRET_KEY = "87a9449095742721db3814e444495e9b";// Provided by EBS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_polity);

        amount=findViewById(R.id.amount);
        descreption=findViewById(R.id.descreption);
        paymentId=findViewById(R.id.paymentId);
        submit= findViewById(R.id.submit);
        checkBob= findViewById(R.id.checkBob);

        company_name=findViewById(R.id.company_name);
        gst_number=findViewById(R.id.gst_number);
        tax_address=findViewById(R.id.tax_address);
        user_email=findViewById(R.id.user_email);


        dialog=new Dialog(AddGSTDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        // Util.showPgDialog(dialog);

       // Log.d("sdfsdfsdfs",getIntent().getStringExtra("jsonArray"));


        getProfileData();


        checkBob.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //  Toast.makeText(getApplicationContext(), ""+b, Toast.LENGTH_SHORT).show();

                if (b==true){
                    flag="true";
                    gst_number.setText("N/A");
                    gst_number.setFocusable(false);

                }
                else if(b==false){
                    flag="false";
                    gst_number.setText("");
                    gst_number.setFocusable(true);
                    gst_number.setFocusableInTouchMode(true);
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validate()) {


                        submitOrderApi();

                    }
                }
            });



//        try {
//            JSONArray jsonArray=new JSONArray(getIntent().getStringExtra("jsonArray"));
//
//            final JSONObject jsonObject=jsonArray.getJSONObject(0);
//
//            amount.setText("Amount ₹ "+jsonObject.optString("amount"));
//            descreption.setText(jsonObject.optString("description"));
//            paymentId.setText("Transaction No: "+jsonObject.optString("payment_id"));
//
//            submit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (validate()) {
//                        //submitOrderApi(jsonObject.optString("payment_id"),jsonObject.optString("amount"));
//
//                    }
//                }
//            });
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }


    private void getProfileData() {
        Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.userById+"/"+ MyPrefrences.getUserID(getApplicationContext()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){


                        //  imageNoListing.setVisibility(View.GONE);
                        JSONArray jsonArray=response.getJSONArray("message");

                        JSONObject jsonObject=jsonArray.getJSONObject(0);


                        company_name=findViewById(R.id.company_name);
                        gst_number=findViewById(R.id.gst_number);
                        tax_address=findViewById(R.id.tax_address);
                        user_email=findViewById(R.id.user_email);

                        company_name.setText(jsonObject.optString("company_name"));
                        gst_number.setText(jsonObject.optString("gst_number"));
                        tax_address.setText(jsonObject.optString("tax_address"));
                        user_email.setText(jsonObject.optString("email_address"));

                    }
                    else{
                        //   Toast.makeText(getApplicationContext(), "Some Error", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    private boolean validate(){

        if (TextUtils.isEmpty(company_name.getText().toString()))
        {
            Util.errorDialog(AddGSTDetails.this,"Type Company Name");
            return false;
        }


//        else if (TextUtils.isEmpty(gst_number.getText().toString())) {
//            if (flag.equals("true")) {
//                Util.errorDialog(PayOrderAct.this, "GST No ");
//                return false;
//            }
//            else{
//                return true;
//            }
//        }

        else if (TextUtils.isEmpty(tax_address.getText().toString()))
        {
            Util.errorDialog(AddGSTDetails.this,"Type Address");
            return false;
        }  else if (TextUtils.isEmpty(user_email.getText().toString()))
        {
            Util.errorDialog(AddGSTDetails.this,"Type Email Id");
            return false;
        }


        return true;

    }


    private void submitOrderApi() {

        Util.showPgDialog(dialog);
        RequestQueue queue = Volley.newRequestQueue(AddGSTDetails.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.updateGSTDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("updateGSTDetails", "Login Response: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        errorDialog(AddGSTDetails.this,jsonObject.getString("message"));
                        //callEbsKit(PayOrderAct.this, Double.parseDouble(p_amt));

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
                Toast.makeText(getApplicationContext(), "Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf", "Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", MyPrefrences.getUserID(getApplicationContext()));
                params.put("company_name", company_name.getText().toString());
                params.put("gst_number", gst_number.getText().toString());
                params.put("tax_address", tax_address.getText().toString());
                params.put("email_address", user_email.getText().toString());


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
        text.setText(fromHtml(message));
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context.getApplicationContext(),HomeAct.class);
                intent.putExtra("userType","");
                context.startActivity(intent);


            }
        });
        dialog.show();

    }


}
