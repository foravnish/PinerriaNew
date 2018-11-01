package pinerria.business.pinerrianew.Activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.gson.JsonObject;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import pinerria.business.pinerrianew.Fragments.PayOrder;
import pinerria.business.pinerrianew.PayUMoney.AppEnvironment;
import pinerria.business.pinerrianew.PayUMoney.AppPreference;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

public class PayOrderAct extends AppCompatActivity {
    Dialog dialog,dialog4;
    TextView paymentId,amount,descreption;
    Button submit;
    CheckBox checkBob;
    String  flag="false";
    EditText oder_id,user_email,tax_address,gst_number,company_name,mobileNo;

    ArrayList<HashMap<String, String>> custom_post_parameters;
    private static final int ACC_ID = 27791;// Provided by EBS
    private static final String SECRET_KEY = "87a9449095742721db3814e444495e9b";// Provided by EBS

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private AppPreference mAppPreference;
    String merKey,merId,salt;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);

        amount=findViewById(R.id.amount);
        descreption=findViewById(R.id.descreption);
        paymentId=findViewById(R.id.paymentId);
        submit= findViewById(R.id.submit);
        checkBob= findViewById(R.id.checkBob);


        company_name=findViewById(R.id.company_name);
        gst_number=findViewById(R.id.gst_number);
        tax_address=findViewById(R.id.tax_address);
        user_email=findViewById(R.id.user_email);
        mobileNo= findViewById(R.id.mobileNo);

        mAppPreference = new AppPreference();
        merKey= AppEnvironment.SANDBOX.merchant_Key();
        merId=AppEnvironment.SANDBOX.merchant_ID();
        salt=AppEnvironment.SANDBOX.salt();

        mobileNo.setText(MyPrefrences.getMobile(getApplicationContext()));

        dialog=new Dialog(PayOrderAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        // Util.showPgDialog(dialog);

        Log.d("sdfsdfsdfs",getIntent().getStringExtra("jsonArray"));


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



        try {
           JSONArray jsonArray=new JSONArray(getIntent().getStringExtra("jsonArray"));

            jsonObject=jsonArray.getJSONObject(0);

            amount.setText("Amount ₹ "+jsonObject.optString("amount"));
            descreption.setText(jsonObject.optString("description"));
            paymentId.setText("Transaction No: "+jsonObject.optString("payment_id"));

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validate()) {


                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();
                        Log.d("TimeCurrent",ts);
                        MyPrefrences.setDateTime(getApplicationContext(),ts);

                        submitOrderApi(jsonObject.optString("payment_id"),jsonObject.optString("amount"));

                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void submitOrderApi(final String payment_id, final String p_amt) {

            Util.showPgDialog(dialog);
            RequestQueue queue = Volley.newRequestQueue(PayOrderAct.this);
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Api.payOrderAmount, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Util.cancelPgDialog(dialog);
                    Log.e("dfsjfdfsdfgd", "Login Response: " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                         if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            // callEbsKit(PayOrderAct.this, Double.parseDouble(p_amt));
                             launchPayUMoneyFlow("", p_amt);


                         }

                         else{
                             Util.errorDialog(PayOrderAct.this,jsonObject.getString("message"));
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
                    params.put("payment_id", payment_id);

                            Random r = new Random();
                            int i1 = r.nextInt(800 - 650) + 65;
                            Log.d("fgsdgfsdghdfhd", String.valueOf(i1));

                    params.put("transaction_id", String.valueOf(i1));
                    params.put("payment_status", "pending");
                    params.put("company_name", company_name.getText().toString());
                    if (flag.equals("true")){
                        params.put("gst_number", "NA");
                    }
                    else if (flag.equals("false")){
                        params.put("gst_number", gst_number.getText().toString());

                    }

                    params.put("tax_address", tax_address.getText().toString());
                    params.put("user_email", user_email.getText().toString());
                    params.put("unique_number", MyPrefrences.getDateTime(getApplicationContext()));

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

    private boolean validate(){

        if (TextUtils.isEmpty(company_name.getText().toString()))
        {
            Util.errorDialog(PayOrderAct.this,"Enter Company Name");
            return false;
        }
        else if (TextUtils.isEmpty(tax_address.getText().toString()))
        {
            Util.errorDialog(PayOrderAct.this,"Enter Address");
            return false;
        }  else if (TextUtils.isEmpty(user_email.getText().toString()))
        {
            Util.errorDialog(PayOrderAct.this,"Enter Email Id");
            return false;
        }
        else if (!user_email.getText().toString().trim().matches(emailPattern))
        {
            Util.errorDialog(PayOrderAct.this,"Enter valid Email Id");
            return false;
        }

        else if (TextUtils.isEmpty(mobileNo.getText().toString()))
        {
            Util.errorDialog(PayOrderAct.this,"Enter Mobile No.");
            return false;
        }
        else if (mobileNo.getText().toString().length()<10)
        {
            Util.errorDialog(PayOrderAct.this,"Enter 10 digit Mobile No.");
            return false;
        }

        return true;

    }



//    private void callEbsKit(PayOrderAct buyProduct, double amount) {
//        /**
//         * Set Parameters Before Initializing the EBS Gateway, All mandatory
//         * values must be provided
//         */
//
//        /** Payment Amount Details */
//        // Total Amount
//
//        PaymentRequest.getInstance().setTransactionAmount(String.valueOf(amount));
//
//        /** Mandatory */
//
//        PaymentRequest.getInstance().setAccountId(ACC_ID);
//        PaymentRequest.getInstance().setSecureKey(SECRET_KEY);
//
//        // Reference No
////        Random r = new Random();
////        int i1 = r.nextInt(800 - 650) + 65;
////        Log.d("fgsdgfsdghdfhd", String.valueOf(i1));
//
//        Random r = new Random();
//        int i1 = r.nextInt(800 - 650) + 65;
//        Log.d("fgsdgfsdghdfhd", String.valueOf(i1));
//        PaymentRequest.getInstance().setReferenceNo(String.valueOf(i1));
//        /** Mandatory */
//
//        // Email Id
//        //PaymentRequest.getInstance().setBillingEmail("test_tag@testmail.com");
//
//        PaymentRequest.getInstance().setBillingEmail("customerhelpdesk@pinerria.com");
//        /** Mandatory */
//
//        PaymentRequest.getInstance().setFailureid(String.valueOf(amount));
//
//        // PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));
//        // System.out.println("FAILURE MESSAGE"+getResources().getString(R.string.payment_failure_message));
//
//        /** Mandatory */
//
//        // Currency
//        PaymentRequest.getInstance().setCurrency("INR");
//        /** Mandatory */
//
//        /** Optional */
//        // Your Reference No or Order Id for this transaction
//        PaymentRequest.getInstance().setTransactionDescription(
//                "Test Transaction");
//
//        /** Billing Details */
//        PaymentRequest.getInstance().setBillingName("Type");
//        /** Optional */
//        PaymentRequest.getInstance().setBillingAddress("North Mada Street");
//        /** Optional */
//        PaymentRequest.getInstance().setBillingCity("Chennai");
//        /** Optional */
//        PaymentRequest.getInstance().setBillingPostalCode("600019");
//        /** Optional */
//        PaymentRequest.getInstance().setBillingState("Tamilnadu");
//        /** Optional */
//        PaymentRequest.getInstance().setBillingCountry("IND");
//        /** Optional */
//        PaymentRequest.getInstance().setBillingPhone("01234567890");
//        /** Optional */
//
//        /** Shipping Details */
//        PaymentRequest.getInstance().setShippingName("Test_Name");
//        /** Optional */
//        PaymentRequest.getInstance().setShippingAddress("North Mada Street");
//        /** Optional */
//        PaymentRequest.getInstance().setShippingCity("Chennai");
//        /** Optional */
//        PaymentRequest.getInstance().setShippingPostalCode("600019");
//        /** Optional */
//        PaymentRequest.getInstance().setShippingState("Tamilnadu");
//        /** Optional */
//        PaymentRequest.getInstance().setShippingCountry("IND");
//        /** Optional */
//        PaymentRequest.getInstance().setShippingEmail("test@testmail.com");
//        /** Optional */
//        PaymentRequest.getInstance().setShippingPhone(MyPrefrences.getDateTime(getApplicationContext()));
//        /** Optional */
//
//        PaymentRequest.getInstance().setLogEnabled(String.valueOf(amount));
//
//
//        /**
//         * Payment option configuration
//         */
//
//        /** Optional */
//        PaymentRequest.getInstance().setHidePaymentOption(false);
//
//        /** Optional */
//        PaymentRequest.getInstance().setHideCashCardOption(false);
//
//        /** Optional */
//        PaymentRequest.getInstance().setHideCreditCardOption(false);
//
//        /** Optional */
//        PaymentRequest.getInstance().setHideDebitCardOption(false);
//
//        /** Optional */
//        PaymentRequest.getInstance().setHideNetBankingOption(false);
//
//        /** Optional */
//        PaymentRequest.getInstance().setHideStoredCardOption(false);
//
//        /**
//         * Initialise parameters for dyanmic values sending from merchant
//         */
//
//        custom_post_parameters = new ArrayList<HashMap<String, String>>();
//        HashMap<String, String> hashpostvalues = new HashMap<String, String>();
//        hashpostvalues.put("account_details", "saving");
//        hashpostvalues.put("merchant_type", "gold");
//        custom_post_parameters.add(hashpostvalues);
//
//        PaymentRequest.getInstance()
//                .setCustomPostValues(custom_post_parameters);
//        /** Optional-Set dyanamic values */
//
//        // PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));
//
//        EBSPayment.getInstance().init(PayOrderAct.this, ACC_ID, SECRET_KEY,
//                Config.Mode.ENV_LIVE, Config.Encryption.ALGORITHM_SHA512, "EBS");
//
//    }


    private void getProfileData() {
        Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.userById+"/"+ MyPrefrences.getUserID(getApplicationContext()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose_getProfileData", response.toString());

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



    /// TODO PayUMoney

    private void launchPayUMoneyFlow(String package_name, String total_value) {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
//        payUmoneyConfig.setDoneButtonText(((EditText) findViewById(R.id.status_page_et)).getText().toString());

        //Use this to set your custom title for the activity
//        payUmoneyConfig.setPayUmoneyActivityTitle(((EditText) findViewById(R.id.activity_title_et)).getText().toString());

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
//            amount = 1;
            amount = Double.parseDouble(total_value.toString());
            Log.d("sdfsdfsddsgdf",total_value.toString());
            // Log.d("fgdfgdfhdhdh", String.valueOf(Double.parseDouble(totlPrice.getText().toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //   Log.d("sdfsfsdfgsgsd", Getseter.preferences.getString("mobile",""));

        String txnId = System.currentTimeMillis() + "";
//        String phone = mobile_til.getEditText().getText().toString().trim();
        String phone =MyPrefrences.getMobile(getApplicationContext());
//        String productName = mAppPreference.getProductInfo();
//        String firstName = mAppPreference.getFirstName();
//        String email = email_til.getEditText().getText().toString().trim();
//        String email = MyPrefrences.getEMAILID(getApplicationContext()).toString();
//        String email = "customerhelpdesk@pinerria.com";
        String email = user_email.getText().toString();
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((AppController) getApplication()).getAppEnvironment();
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(mobileNo.getText().toString())
                .setProductName("Pinerria "+package_name)
                .setFirstName(company_name.getText().toString())
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(jsonObject.optString("payment_id"))
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
//                .setKey(appEnvironment.merchant_Key())
                .setKey(merKey.toString())
//                .setMerchantId(appEnvironment.merchant_ID());
                .setMerchantId(merId.toString());

        Log.d("dfdsfdsfsdfsd",merKey);
        Log.d("dfdsfdsfsdfsd",merId);

        try {
            mPaymentParams = builder.build();

            /*
             * Hash should always be generated from your server side.
             * */
            generateHashFromServer(mPaymentParams);

            /*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * *//*
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*/

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            // payNowButton.setEnabled(true);
        }
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        AppEnvironment appEnvironment = ((AppController) getApplication()).getAppEnvironment();
//        stringBuilder.append(appEnvironment.salt());
        stringBuilder.append(salt.toString());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        Log.d("fgdfhgdfhdfhd",postParams);

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }
    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PayOrderAct.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL(Api.gethashCode);

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());
                Log.d("fgdfgdfgdfg",response.toString());
                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();
            // payNowButton.setEnabled(true);

            Log.d("fgdfgdfgdfg",merchantHash.toString());

            //  merchantHash="35eceef8992006b59b2f46d7ef6ce13b3dcgfhfgj258996b871f863ed7fghfgjhfgee9e76bb357209f04b488afcc6a687f354a13750421e0ec85bcb40006441df530b84831c69b4";

            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(getApplicationContext(), "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {
                mPaymentParams.setMerchantHash(merchantHash);
                if (AppPreference.selectedTheme != -1) {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayOrderAct.this, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
                } else {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,  PayOrderAct.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("gffgdfgfhdhdh","true");
        Log.d("MainActivity123", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                String payuResponse = transactionResponse.getPayuResponse();
                String merchantResponse = transactionResponse.getTransactionDetails();


                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    Log.d("responseprint","success "+payuResponse);
                    Log.d("responseprint","success "+merchantResponse);


                    try {
                        JSONObject jsonObject=new JSONObject(payuResponse);
                        JSONObject resultJson=jsonObject.getJSONObject("result");

                        errorDialog2("1",payuResponse,merchantResponse,resultJson.optString("udf1"),resultJson.optString("amount"),resultJson.optString("txnid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                } else {
                    //Failure Transaction
                    Log.d("responseprint","failed "+payuResponse);

//                    new AlertDialog.Builder(PayOrderAct.this)
//                            .setCancelable(false)
//                            .setMessage("Payment Failed...")
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    dialog.dismiss();
//                                }
//                            }).show();

                    // new BuyPackagesApi2(getApplicationContext(),"failed","payment_failed").execute();

//                    submitOrderApiComplete("NA","NA","NA","NA","failure");
                    errorDialog2("2","NA","NA","NA","NA","NA");
                }

                // Response from Payumoney

//                new AlertDialog.Builder(PayActivity.this)
//                        .setCancelable(false)
//                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("dfsdgdfgdfg", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("dfsdgdfgdfg", "Both objects are null!");
            }
        }
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    private void submitOrderApiComplete(final String payment_id, final String p_id, final String txn_id, final String P_amount, final String status, final String Notimessage, final String paymntNo) {

        Util.showPgDialog(dialog);
        RequestQueue queue = Volley.newRequestQueue(PayOrderAct.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.updatePurchasedPackage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("fgfgfgdfgfhfghg", "Response: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        Intent intent=new Intent(PayOrderAct.this,HomeAct.class);
                        intent.putExtra("userType","");
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
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
                Toast.makeText(getApplicationContext(), "Please Connect to the Internet.", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf", "Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                 params.put("payment_id", p_id);
                 params.put("transaction_id", txn_id);
                 params.put("response", payment_id);
                 params.put("pay_amount", P_amount);
                 params.put("payment_status", status);
                 params.put("unique_number", MyPrefrences.getDateTime(getApplicationContext()));
                 params.put("notification", Notimessage);
                 params.put("payment_no", paymntNo);
                 params.put("mode", "banner");

                 Log.d("dfgdfgdfgdfgdfgd",MyPrefrences.getDateTime(getApplicationContext()));
                 Log.d("dfgdfgdfgdfgdfgd",paymntNo);


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


    private void errorDialog2(final String status, final String payment_id, final String p_id, final String paymntNo, final String amount, final String tnxId) {
        TextView Yes_action, No_action,btn;
        TextView heading;
        final EditText notificationMsg;

        dialog4 = new Dialog(PayOrderAct.this);
        dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog4.setContentView(R.layout.alertdialogcustom_pay_success);
        dialog4.setCancelable(false);

        Button ok = (Button) dialog4.findViewById(R.id.btn_ok);


        heading = (TextView) dialog4.findViewById(R.id.msg_txv);
        btn = (TextView) dialog4.findViewById(R.id.btn);
        notificationMsg = (EditText) dialog4.findViewById(R.id.notificationMsg);

        if (status.equalsIgnoreCase("1")){

            heading.setText("Your Payment Successfully Done ! \n\nYour intended promo will be visible to others after approval by admin, within two working days.");
            btn.setText("Congratulations!");
            notificationMsg.setVisibility(View.GONE);
        }

        else if(status.equalsIgnoreCase("2")){

            heading.setText("Your payment has not been successful.  Please try again");
            btn.setText("Error Message!");
            notificationMsg.setVisibility(View.GONE);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status.equalsIgnoreCase("1")){
                    submitOrderApiComplete(payment_id,p_id,tnxId,amount,"success",notificationMsg.getText().toString(),paymntNo);

                    Log.d("fgdfgsdfgrdtgdgd","true");
                }

                else if(status.equalsIgnoreCase("2")){
                    submitOrderApiComplete(payment_id,p_id,tnxId,amount,"failure",notificationMsg.getText().toString(),paymntNo);
                    Log.d("fgdfgsdfgrdtgdgd","false");
                }
                

            }
        });

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog4.dismiss();
//                Intent intent=new Intent(AddGSTDetails.this,HomeAct.class);
//                intent.putExtra("userType","");
//                startActivity(intent);
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                finish();intent);
//
//
//            }
//        });

        dialog4.show();
    }





}
