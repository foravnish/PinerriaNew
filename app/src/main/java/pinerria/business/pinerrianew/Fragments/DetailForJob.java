package pinerria.business.pinerrianew.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pinerria.business.pinerrianew.Activites.Chat;
import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.Activites.Login;
import pinerria.business.pinerrianew.Activites.UserDetails;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailForJob extends Fragment {

    Dialog dialog;
    public DetailForJob() {
        // Required empty public constructor
    }

    TextView comName,heading,address,phone,description,salary,experience,full_part_time,date,jobId;
    LinearLayout call,chat_now,shareDetail;
    NetworkImageView imageView;
    JSONObject jsonObject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detail_for_job, container, false);

        imageView=view.findViewById(R.id.imageView);
        call=view.findViewById(R.id.call);
        chat_now=view.findViewById(R.id.chat_now);
        shareDetail=view.findViewById(R.id.shareDetail);

        comName=view.findViewById(R.id.comName);
        heading=view.findViewById(R.id.heading);
        description=view.findViewById(R.id.description);
        address=view.findViewById(R.id.address);
        salary=view.findViewById(R.id.salary);
        experience=view.findViewById(R.id.experience);
        full_part_time=view.findViewById(R.id.full_part_time);
        date=view.findViewById(R.id.date);
        jobId=view.findViewById(R.id.jobId);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(
                Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        Log.d("arraydfgdfgdfg",getArguments().getString("array").toString());

        try {
            jsonObject=new JSONObject(getArguments().getString("array"));

            ImageLoader imageLoader= AppController.getInstance().getImageLoader();
            imageView.setImageUrl(jsonObject.optString("image").toString().replace(" ","%20"),imageLoader);

            HomeAct.title.setText(jsonObject.optString("heading"));
            heading =  view.findViewById(R.id.heading);
            address =  view.findViewById(R.id.address);
            phone =  view.findViewById(R.id.phone);

            comName.setText(jsonObject.optString("user_name").toUpperCase());
            heading.setText(jsonObject.optString("heading"));
            description.setText(jsonObject.optString("description"));
            address.setText(jsonObject.optString("address")+", "+jsonObject.optString("city_name"));
//            phone.setText(jsonObject.optString("mobile"));
            phone.setText("Enabled");


            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            String currency=formatter.format(Double.parseDouble(jsonObject.optString("salary")));

            salary.setText(currency);

            experience.setText(jsonObject.optString("experience"));
          //  address.setText(jsonObject.optString("city_name")+", "+jsonObject.optString("zone_name"));
            full_part_time.setText(jsonObject.optString("full_part_time"));
            jobId.setText(jsonObject.optString("user_name"));
            String year=jsonObject.optString("created_date").substring(0,4);
            String month=jsonObject.optString("created_date").substring(5,7);
            String day=jsonObject.optString("created_date").substring(8,10);
            date.setText("Posted Date "+day+"-"+month+"-"+year);

            shareDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
//                String shareBody =comName.getText().toString()+ " "+phone.getText().toString();
                    String shareBody = "Hi, \nThere is a job on Pinerria which might be of interest to you.\n"+heading.getText().toString()+"\n"+salary.getText().toString()+"\n"+address.getText().toString()+"\n"+"Please check it out.\n"+comName.getText().toString();

                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Details");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });



            chat_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  callFeature();

                }
            });



            if (jsonObject.optString("mobile_status").equals("Yes")){
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (MyPrefrences.getUserLogin(getActivity())==true) {

                             maintainCallHistory(jsonObject.optString("id"),jsonObject.optString("mobile"));
//                            try
//                            {
//                                if(Build.VERSION.SDK_INT > 22)
//                                {
//                                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                                        // TODO: Consider calling
//                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);
//                                        return;
//                                    }
//                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
//                                    callIntent.setData(Uri.parse("tel:" + jsonObject.optString("mobile")));
//                                    startActivity(callIntent);
//                                }
//                                else {
//                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
//                                    callIntent.setData(Uri.parse("tel:" +jsonObject.optString("mobile")));
//                                    startActivity(callIntent);
//                                }
//                            }
//                            catch (Exception ex)
//                            {ex.printStackTrace();
//                            }
                        }
                        else{

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Please Login to Call")
                                    .setCancelable(false)
                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            Intent intent=new Intent(getActivity(),Login.class);
                                            startActivity(intent);
                                            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                            getActivity().finish();
                                        }
                                    })
                                    .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                            //dialog.cancel();


                                            Intent intent=new Intent(getActivity(),HomeAct.class);
                                            intent.putExtra("userType","");
                                            startActivity(intent);
                                            getActivity(). overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                            getActivity(). finish();

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

            else {
              //  call.setTextColor(Color.parseColor("#545454"));
                phone.setText("N/A");
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (MyPrefrences.getUserLogin(getActivity())==true) {

                            Toast.makeText(getActivity(), "The Job poster has not enabled the call feature.", Toast.LENGTH_SHORT).show();

                        }
                        else{

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Please Login to Call")
                                    .setCancelable(false)
                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            Intent intent=new Intent(getActivity(),Login.class);
                                            startActivity(intent);
                                            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                            getActivity().finish();
                                        }
                                    })
                                    .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                            //dialog.cancel();


                                            Intent intent=new Intent(getActivity(),HomeAct.class);
                                            intent.putExtra("userType","");
                                            startActivity(intent);
                                            getActivity(). overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                            getActivity(). finish();

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




//            addReview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (MyPrefrences.getUserLogin(getActivity())==true) {
//
//                        Fragment fragment = new WriteReview();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("id", jsonObject.optString("id"));
//                        bundle.putString("company_name", jsonObject.optString("bussiness_name"));
//                        bundle.putString("address", jsonObject.optString("address")+" "+jsonObject.optString("city_name")+" "+jsonObject.optString("state_name"));
//                        FragmentManager manager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction ft = manager.beginTransaction();
//                        fragment.setArguments(bundle);
//                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//                    }
//                    else{
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        //Uncomment the below code to Set the message and title from the strings.xml file
//                        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);
//
//                        //Setting message manually and performing action on button click
//                        builder.setMessage("Please Login to post review!")
//
//                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
//                                        startActivity(new Intent(getActivity(),Login.class));
//                                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                                    }
//                                })
//                                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        //  Action for 'NO' Button
//                                        dialog.cancel();
//                                    }
//                                });
//
//                        //Creating dialog box
//                        AlertDialog alert = builder.create();
//                        //Setting the title manually
//                        alert.setTitle("BizzCityInfo");
//                        alert.show();
//
//
//                        //Util.errorDialog(getActivity(),"Please Login First!");
//                    }
//
//                }
//            });




        } catch (JSONException e) {
            e.printStackTrace();
        }




        return  view;
    }

    private void callFeature() {

        if (MyPrefrences.getUserLogin(getActivity())==true) {

            UserDetails.chatWith = jsonObject.optString("mobile");
            Intent intent=new Intent(getActivity(), Chat.class);
            intent.putExtra("nameValue",jsonObject.optString("user_name"));
            intent.putExtra("id",jsonObject.optString("user_id"));
            intent.putExtra("value1","0");
            startActivity(intent);

            Log.d("mobileNo",jsonObject.optString("mobile"));

        }
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Please Login to Chat")
                    .setCancelable(false)
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            Intent intent=new Intent(getActivity(),Login.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            //dialog.cancel();


                            Intent intent=new Intent(getActivity(),HomeAct.class);
                            intent.putExtra("userType","");
                            startActivity(intent);
                            getActivity(). overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            getActivity(). finish();

                        }
                    });
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Pinerria");
            alert.show();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    callFeature();
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    private void maintainCallHistory(final String b_id, final String mobileNo) {
        Util.showPgDialog(dialog);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.addBusinessCall,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Util.cancelPgDialog(dialog);
                        // response
                        Log.d("ResponseCall", response);
                        Util.cancelPgDialog(dialog);
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+mobileNo));
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Toast.makeText(getActivity(), "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                        Util.cancelPgDialog(dialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {

                Log.d("dfsdfgsdgdsfgdfg",MyPrefrences.getUserID(getActivity()));
                Log.d("dfsdfgsdgdsfgdfg",b_id);
                Log.d("dfsdfgsdgdsfgdfg",mobileNo);

                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id", MyPrefrences.getUserID(getActivity()));
                params.put("business_id", b_id);
                params.put("business_mobile", mobileNo);

                return params;
            }
        };


        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(postRequest);


    }


}
