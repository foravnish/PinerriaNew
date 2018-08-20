package pinerria.business.pinerrianew.Activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebs.android.sdk.Config;
import com.ebs.android.sdk.EBSPayment;
import com.ebs.android.sdk.PaymentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pinerria.business.pinerrianew.Fragments.Packages;
import pinerria.business.pinerrianew.Fragments.WriteReview;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

import static android.text.Html.fromHtml;

public class PayActivity extends AppCompatActivity {

    private static final int ACC_ID = 27791;// Provided by EBS
    private static final String SECRET_KEY = "87a9449095742721db3814e444495e9b";// Provided by EBS

    TextView pName,creditpoints,creditpoints2,price,discount,duration,price1,price2,price3,price4;
    Dialog dialog;
    JSONObject jsonObject;
    JSONObject jsonObject2;
    Button buynow;

    ArrayList<HashMap<String, String>> custom_post_parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Log.d("dfgdfgdfgdfd",getIntent().getStringExtra("jsonArray"));
        Log.d("ertfgsdgdfgdf",getIntent().getStringExtra("userInfo"));
        dialog=new Dialog(PayActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);




        pName=(TextView)findViewById(R.id.pName);
//            creditpoints=(TextView)convertView.findViewById(R.id.creditpoints);
//            creditpoints2=(TextView)convertView.findViewById(R.id.creditpoints2);
        price=(TextView)findViewById(R.id.price);
        price1=(TextView)findViewById(R.id.price1);
        price2=(TextView)findViewById(R.id.price2);
        price3=(TextView)findViewById(R.id.price3);
        price4=(TextView)findViewById(R.id.price4);

        buynow=(Button) findViewById(R.id.buynow);
        duration=(TextView) findViewById(R.id.duration);



        try {
            jsonObject=new JSONObject(getIntent().getStringExtra("jsonArray"));

            pName.setText(jsonObject.optString("package_name"));
            price1.setText("₹ "+jsonObject.optString("actual_value"));
            price2.setText(jsonObject.optString("discount_percent")+" %");
            price3.setText("₹ "+jsonObject.optString("after_discount_price"));
            price4.setText(jsonObject.optString("gst")+" %");
            price.setText("₹ "+jsonObject.optString("total_value"));
            duration.setText(jsonObject.optString("duration")+" Months Validity");



        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            jsonObject2=new JSONObject(getIntent().getStringExtra("userInfo"));






        } catch (JSONException e) {
            e.printStackTrace();
        }


        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (MyPrefrences.getUserLogin(getApplicationContext())==true) {

                    double amount= Double.parseDouble(jsonObject.optString("total_value"));

//                    callEbsKit(PayActivity.this, amount);

                    if (jsonObject2.optString("company_name").equalsIgnoreCase("")){
//                        Toast.makeText(getApplicationContext(), "blank", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(PayActivity.this,AddGSTDetails.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                    else{


                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();
                        Log.d("TimeCurrent",ts);
                        MyPrefrences.setDateTime(getApplicationContext(),ts);

                        PurchasePackage(amount,jsonObject2.optString("company_name"),
                                jsonObject2.optString("gst_number"),jsonObject2.optString("tax_address"),jsonObject2.optString("email_address"));
                    }

                }
                else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                    builder.setMessage("Please Login to Purchase Package")
                            .setCancelable(false)
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent=new Intent(PayActivity.this,Login.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                    finish();
                                }
                            })
                            .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();


//                                    Intent intent=new Intent(PayActivity.this,HomeAct.class);
//                                    intent.putExtra("userType","");
//                                    startActivity(intent);
//                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                                    finish();

                                }
                            });
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Pinerria");
                    alert.show();

                }

            }
        });
    }

    private void PurchasePackage(final double p_amt, final String com_name, final String gst, final String address, final String email) {

        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(PayActivity.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.addPurchasedPackage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("PackageResponse", " Response: " + response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

//                        callEbsKit(PayActivity.this, p_amt);
                        callEbsKit(PayActivity.this, 1);

                    }
                    else{
                        Util.errorDialog(PayActivity.this,jsonObject.optString("message"));

                        //Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
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
                params.put("package_id", jsonObject.optString("id"));
                params.put("package_name", jsonObject.optString("package_name"));
                params.put("package_duration",jsonObject.optString("duration"));
                params.put("actual_value", jsonObject.optString("actual_value"));
                params.put("discount_percent", jsonObject.optString("discount_percent"));
                params.put("after_discount_price", jsonObject.optString("after_discount_price"));
                params.put("gst", jsonObject.optString("gst"));
                params.put("total_amount", jsonObject.optString("total_value"));
                params.put("user_id", MyPrefrences.getUserID(getApplicationContext()));
                params.put("payment_id", "N/A");
                params.put("transaction_id","N/A");
                params.put("response","N/A");
                params.put("pay_amount", jsonObject.optString("total_value"));
                params.put("payment_status", "Pending");
                params.put("company_name", com_name);
                params.put("gst_number", gst);
                params.put("tax_address", address);
                params.put("user_email", email);
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
    public void errorDialog(final Context context, String message) {
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
//                dialog.dismiss();

                Intent intent = new Intent(PayActivity.this, HomeAct.class);
                intent.putExtra("userType","");
                startActivity(intent);
                finish();

            }
        });
        dialog.show();

    }


    private void callEbsKit(PayActivity buyProduct, double amount) {
        /**
         * Set Parameters Before Initializing the EBS Gateway, All mandatory
         * values must be provided
         */

        /** Payment Amount Details */
        // Total Amount

        PaymentRequest.getInstance().setTransactionAmount(String.valueOf(amount));

        /** Mandatory */

        PaymentRequest.getInstance().setAccountId(ACC_ID);
        PaymentRequest.getInstance().setSecureKey(SECRET_KEY);

        // Reference No
//        Random r = new Random();
//        int i1 = r.nextInt(800 - 650) + 65;
//        Log.d("fgsdgfsdghdfhd", String.valueOf(i1));

        Random r = new Random();
        int i1 = r.nextInt(800 - 650) + 65;
        Log.d("fgsdgfsdghdfhd", String.valueOf(i1));
        PaymentRequest.getInstance().setReferenceNo(String.valueOf(i1));
        /** Mandatory */

        // Email Id
        //PaymentRequest.getInstance().setBillingEmail("test_tag@testmail.com");

        PaymentRequest.getInstance().setBillingEmail("customerhelpdesk@pinerria.com");
        /** Mandatory */

        PaymentRequest.getInstance().setFailureid(String.valueOf(amount));

        // PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));
        // System.out.println("FAILURE MESSAGE"+getResources().getString(R.string.payment_failure_message));

        /** Mandatory */

        // Currency
        PaymentRequest.getInstance().setCurrency("INR");
        /** Mandatory */

        /** Optional */
        // Your Reference No or Order Id for this transaction
        PaymentRequest.getInstance().setTransactionDescription(
                "Test Transaction");

        /** Billing Details */
        PaymentRequest.getInstance().setBillingName("Type");
        /** Optional */
        PaymentRequest.getInstance().setBillingAddress("North Mada Street");
        /** Optional */
        PaymentRequest.getInstance().setBillingCity("Chennai");
        /** Optional */
        PaymentRequest.getInstance().setBillingPostalCode("600019");
        /** Optional */
        PaymentRequest.getInstance().setBillingState("Tamilnadu");
        /** Optional */
        PaymentRequest.getInstance().setBillingCountry("IND");
        /** Optional */
        PaymentRequest.getInstance().setBillingPhone("01234567890");
        /** Optional */

        /** Shipping Details */
        PaymentRequest.getInstance().setShippingName("Test_Name");
        /** Optional */
        PaymentRequest.getInstance().setShippingAddress("North Mada Street");
        /** Optional */
        PaymentRequest.getInstance().setShippingCity("Chennai");
        /** Optional */
        PaymentRequest.getInstance().setShippingPostalCode("600019");
        /** Optional */
        PaymentRequest.getInstance().setShippingState("Tamilnadu");
        /** Optional */
        PaymentRequest.getInstance().setShippingCountry("IND");
        /** Optional */
        PaymentRequest.getInstance().setShippingEmail("test@testmail.com");
        /** Optional */
        PaymentRequest.getInstance().setShippingPhone(MyPrefrences.getDateTime(getApplicationContext()));
        /** Optional */

        PaymentRequest.getInstance().setLogEnabled(String.valueOf(amount));


        /**
         * Payment option configuration
         */

        /** Optional */
        PaymentRequest.getInstance().setHidePaymentOption(false);

        /** Optional */
        PaymentRequest.getInstance().setHideCashCardOption(false);

        /** Optional */
        PaymentRequest.getInstance().setHideCreditCardOption(false);

        /** Optional */
        PaymentRequest.getInstance().setHideDebitCardOption(false);

        /** Optional */
        PaymentRequest.getInstance().setHideNetBankingOption(false);

        /** Optional */
        PaymentRequest.getInstance().setHideStoredCardOption(false);

        /**
         * Initialise parameters for dyanmic values sending from merchant
         */

        custom_post_parameters = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> hashpostvalues = new HashMap<String, String>();
        hashpostvalues.put("account_details", "saving");
        hashpostvalues.put("merchant_type", "gold");
        custom_post_parameters.add(hashpostvalues);

        PaymentRequest.getInstance()
                .setCustomPostValues(custom_post_parameters);
        /** Optional-Set dyanamic values */

        // PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));

        EBSPayment.getInstance().init(PayActivity.this, ACC_ID, SECRET_KEY,
                Config.Mode.ENV_LIVE, Config.Encryption.ALGORITHM_SHA512, "EBS");

    }


}
