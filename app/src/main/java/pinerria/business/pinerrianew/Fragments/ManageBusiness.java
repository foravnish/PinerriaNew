package pinerria.business.pinerrianew.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import pinerria.business.pinerrianew.Activites.AddProduct;
import pinerria.business.pinerrianew.Activites.EditPhotos;
import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.Activites.Login;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageBusiness extends Fragment {


    public ManageBusiness() {
        // Required empty public constructor
    }

    TextView editPhoto,editBusiness,msgToAdmin;
    TextView address,nameBusiness,subCat1,Catname,subCatName,keyword,myjobs,transcation,chat,area,totlaUsers,membership,membership2;
    NetworkImageView subCatImage;
    Dialog dialog;
    MaterialRatingBar rating;
    SwitchCompat switchBusiness;
    JSONObject jsonObject;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_manage_business, container, false);
        editPhoto=view.findViewById(R.id.editPhoto);

        nameBusiness=view.findViewById(R.id.nameBusiness);
      //  Catname=view.findViewById(R.id.Catname);
       // subCatName=view.findViewById(R.id.subCatName);
        keyword=view.findViewById(R.id.keyword);
        subCatImage=view.findViewById(R.id.subCatImage);
        editBusiness=view.findViewById(R.id.editBusiness);
        msgToAdmin=view.findViewById(R.id.msgToAdmin);
        myjobs=view.findViewById(R.id.myjobs);
        transcation=view.findViewById(R.id.transcation);
        chat=view.findViewById(R.id.chat);

        area=view.findViewById(R.id.area);
        totlaUsers=view.findViewById(R.id.totlaUsers);
        rating=view.findViewById(R.id.rating);
        membership=view.findViewById(R.id.membership);
        membership2=view.findViewById(R.id.membership2);
        switchBusiness=view.findViewById(R.id.switchBusiness);


        HomeAct.title.setText("Manage Business");

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);

        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), EditPhotos.class);
                startActivity(intent);
            }
        });

        editBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(),AddProduct.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        msgToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (MyPrefrences.getUserLogin(getActivity())==true) {

                        Fragment fragment = new MessageToAdmin();
                        FragmentManager manager = getFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Please Login to Message To Admin")
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

        myjobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddJobs();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

            }
        });

        transcation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new Transcation();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.userBusiness+"/"+ MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {

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

                        jsonObject=jsonArray.getJSONObject(0);

                        nameBusiness.setText(jsonObject.optString("bussiness_name"));
                        //Catname.setText(jsonObject.optString("category_id"));
                        //subCatName.setText(jsonObject.optString("sub_category_id"));
                        keyword.setText( jsonObject.optString("service_keyword"));
                        totlaUsers.setText("( "+jsonObject.optString("total_rating_user")+" User)" );
                        area.setText( jsonObject.optString("city_name")+", "+jsonObject.optString("state_name")+", "+jsonObject.optString("zone_name"));

                        if (!jsonObject.optString("total_rating").equals("")) {
                            rating.setRating(Float.parseFloat(jsonObject.optString("total_rating")));
                        }

                        ImageLoader imageLoader=AppController.getInstance().getImageLoader();
                        subCatImage.setImageUrl(jsonObject.optString("image"),imageLoader);


                        if (jsonObject.optString("user_package").equalsIgnoreCase("N0")){
                            membership.setText("Regular");
                            //membership2.setText("Upgrade to Premium");
                        }
                        else {

                            JSONArray jsonArray1=jsonObject.getJSONArray("user_package");
                            JSONObject jsonObject2=jsonArray1.optJSONObject(0);

                            membership.setText("Premium");
                            membership2.setText("Validity "+jsonObject2.optString("expiry_date"));
                        }


                        if (jsonObject.optString("user_job").equalsIgnoreCase("No")){
                            myjobs.setText("Add Job");
                        }
                        else if (jsonObject.optString("user_job").equalsIgnoreCase("Yes")){
                            myjobs.setText("Edit Job");
                        }



                        if (jsonObject.optString("call_button").equalsIgnoreCase("1")){
                            Log.d("sdfsdfsdfsdfs","true");
                            switchBusiness.setChecked(true);

                        }
                        else if (jsonObject.optString("call_button").equalsIgnoreCase("0")){
                            Log.d("sdfsdfsdfsdfs","false");
                            switchBusiness.setChecked(false);
                        }

                        switchBusiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                // Toast.makeText(getActivity(), position+" "+b, Toast.LENGTH_SHORT).show();

                                changeStatusBusinessCall(jsonObject.optString("id"),b);
                            }
                        });



//                        Picasso.with(getActivity())
//                                .load(jsonObject.optString("image"))
//                                .fit()
//                                .into(subCatImage);




                    }
                    else{

                        // imageNoListing.setVisibility(View.VISIBLE);
                        //  Toast.makeText(getActivity(), "No Record Found...", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);





        return view;
    }

    private void changeStatusBusinessCall(String id ,boolean b) {

        Util.showPgDialog(dialog);
        String val = null;
        if (b==true){
            val="1";


        }
        else if (b==false){
            val="0";
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Api.changeBusinessCallStatus+"/"+id+"/"+val, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("statusjob", "Response: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.cancelPgDialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        });
        // Adding request to request queue
        queue.add(strReq);

    }



}
