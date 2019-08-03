package pinerria.business.pinerrianew.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Parser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import pinerria.business.pinerrianew.Activites.Chat;
import pinerria.business.pinerrianew.Activites.ChatUSer;
import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.Activites.Login;
import pinerria.business.pinerrianew.Activites.UserDetails;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.SqliteDatabase.DatabaseHelper;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;

import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class Details extends Fragment {


    public Details() {
        // Required empty public constructor
    }

    private String pass="123456";
    private String user;


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Dialog dialog;
    List<HashMap<String,String>> AllBaner;
    RecyclerView.Adapter mAdapter;
    TextView dealsIn,totlaUsers,desc;
    NetworkImageView imageView;
    LinearLayout chat_now,call;
    TextView comName,nameUser,address,phone;
    LinearLayout addReview,shareDetail;
    JSONObject jsonObject;
    ImageView imgFav;
    Boolean flag=false;
    CardView cardGallery;
    MaterialRatingBar rating;
    private DatabaseHelper db;
    LinearLayout priceLayout;
    TextView price;
    JSONArray jsonArray;
    public  static ViewPager viewpager;
    public  static List<HashMap<String,String>> ImgData;
    public  static CustomPagerAdapter customPagerAdapter;
    public  static JSONObject jsonObject2;
  //  public  static CircleIndicator indicator ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_details, container, false);

       // Log.d("sdfsdfdsgd",getArguments().getString("id"));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view2);
        dealsIn = (TextView) view.findViewById(R.id.dealsIn);
        imageView = (NetworkImageView) view.findViewById(R.id.imageView);
        chat_now =  view.findViewById(R.id.chat_now);
        call =  view.findViewById(R.id.call);
        addReview =  view.findViewById(R.id.addReview);
        shareDetail =  view.findViewById(R.id.shareDetail);
        imgFav =  view.findViewById(R.id.imgFav);
        cardGallery =  view.findViewById(R.id.cardGallery);
        totlaUsers =  view.findViewById(R.id.totlaUsers);
        desc =  view.findViewById(R.id.desc);

        comName =  view.findViewById(R.id.comName);
        nameUser =  view.findViewById(R.id.nameUser);
        address =  view.findViewById(R.id.address);
        phone =  view.findViewById(R.id.phone);
        rating=view.findViewById(R.id.rating);
        price=view.findViewById(R.id.price);
        priceLayout=view.findViewById(R.id.priceLayout);


        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        user=MyPrefrences.getMobile(getActivity());


        //indicator = (CircleIndicator)dialog.findViewById(R.id.indicator);
        ImgData =new ArrayList<>();
        customPagerAdapter=new CustomPagerAdapter(getActivity());


      //  HomeAct.title.setText("Pinerria");

        AllBaner=new ArrayList<>();
        //new GalleryApi(getActivity()).execute();

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(
                Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        String val="Jute Bags Dealers,Carry Bag Dealers,Hand Bags,Rugs and Mats,Plastic Bag";


        Log.d("dfsdfsdfs",getArguments().getString("jsonArray"));

        db = new DatabaseHelper(getActivity());

        try {
             jsonObject=new JSONObject(getArguments().getString("jsonArray"));

            ImageLoader imageLoader= AppController.getInstance().getImageLoader();
            imageView.setImageUrl(jsonObject.optString("image").toString().replace(" ","%20"),imageLoader);
            //imageView.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_img_grad));
            HomeAct.title.setText(jsonObject.optString("bussiness_name"));
            comName =  view.findViewById(R.id.comName);
            nameUser =  view.findViewById(R.id.nameUser);
            address =  view.findViewById(R.id.address);

            Log.d("dfsdgsdfgsdfgsdfgds",jsonObject.optString("user_name"));

            comName.setText(jsonObject.optString("user_name"));
            nameUser.setText(jsonObject.optString("bussiness_name"));
            address.setText(jsonObject.optString("address")+" "+jsonObject.optString("city_name"));
//            phone.setText(jsonObject.optString("primary_mobile"));
            phone.setText("Enabled");

            totlaUsers.setText(" ("+jsonObject.optString("total_rating_user")+" Reviews)");
            desc.setText(jsonObject.optString("desc"));


//            if (jsonObject.optString("call_button").equals("Yes")){
//                price.setVisibility(View.VISIBLE);
//
//            }
//            else if (jsonObject.optString("call_button").equals("No")){
//                price.setVisibility(View.GONE);
//
//            }

            if (!jsonObject.optString("total_rating").equals("")) {
                rating.setRating(Float.parseFloat(jsonObject.optString("total_rating")));
            }

//            rating.setRating(Float.parseFloat("3.5"));
           // rating.setRating(3.5f);

            if (jsonObject.optString("min_price").equals("")){
                priceLayout.setVisibility(View.GONE);
            }
            else{
                priceLayout.setVisibility(View.VISIBLE);
                price.setText("Price ₹ "+jsonObject.optString("min_price"));

                if (!jsonObject.optString("max_price").equals("")){
                    price.setText("Price ₹ "+jsonObject.optString("min_price")+"-"+jsonObject.optString("max_price"));
                }

            }



            ViewGalleryApi(jsonObject.optString("user_id"));

            if (jsonObject.optString("my_favourite").equals("No")){
                imgFav.setImageResource(R.drawable.fav1);
                flag=false;

            }
            else if (jsonObject.optString("my_favourite").equals("Yes")){
                imgFav.setImageResource(R.drawable.fav2);
                flag=true;
            }


            if (jsonObject.optString("call_button").equals("Yes")){
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (MyPrefrences.getUserLogin(getActivity())==true) {

                            maintainCallHistory(jsonObject.optString("id"),jsonObject.optString("primary_mobile"));
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
//                                    callIntent.setData(Uri.parse("tel:" + jsonObject.optString("primary_mobile")));
//                                    startActivity(callIntent);
//                                }
//                                else {
//                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
//                                    callIntent.setData(Uri.parse("tel:" +jsonObject.optString("primary_mobile")));
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

            else if (jsonObject.optString("call_button").equals("No")){
               // call.setTextColor(Color.parseColor("#545454"));
                phone.setText("N/A");
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (MyPrefrences.getUserLogin(getActivity())==true) {

                            Toast.makeText(getActivity(), "The seller has not enabled the call feature", Toast.LENGTH_SHORT).show();

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

            imgFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (MyPrefrences.getUserLogin(getActivity())==true) {
                        if (jsonObject.optString("my_favourite").equals("No") || flag == false) {

                            favourateApi("Yes", jsonObject.optString("id"));
                            Log.d("fgdgdfgsdfgsdfg", "true");
                            imgFav.setImageResource(R.drawable.fav2);
//                        viewholder.imgFav.getLayoutParams().height = 50;
//                        viewholder.imgFav.getLayoutParams().width = 0;
                            flag = true;
                        } else if (jsonObject.optString("my_favourite").equals("Yes") || flag == true) {

                            favourateApi("No", jsonObject.optString("id"));
                            Log.d("fgdgdfgsdfgsdfg", "false");
                            imgFav.setImageResource(R.drawable.fav1);
//                        viewholder.imgFav.getLayoutParams().height = 50;
//                        viewholder.imgFav.getLayoutParams().width = 0;
                            flag = false;
                        }
                    }
                    else{

                        Util.errorDialog(getActivity(),"Please Login First!");
                    }
                }
            });


            shareDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
//                String shareBody =comName.getText().toString()+ " "+phone.getText().toString();
                    String shareBody ="Hi, \n" + "Please visit my business page on Pinerria app. Click on the link below. "+jsonObject.optString("business_share_url");
//                    String shareBody="http://www.google.com";

                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Business Page on Pinerria");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });

            addReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MyPrefrences.getUserLogin(getActivity())==true) {

                        Fragment fragment = new WriteReview();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", jsonObject.optString("id"));
                        bundle.putString("user_id", jsonObject.optString("user_id"));
                        bundle.putString("company_name", jsonObject.optString("bussiness_name"));
                        bundle.putString("address", jsonObject.optString("address")+" "+jsonObject.optString("city_name"));
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        fragment.setArguments(bundle);
                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    }
                    else{

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        //Uncomment the below code to Set the message and title from the strings.xml file
                        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                        //Setting message manually and performing action on button click
                        builder.setMessage("Please Login to post review!")

                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        startActivity(new Intent(getActivity(),Login.class));
                                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                    }
                                })
                                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });

                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Pinerria");
                        alert.show();


                        //Util.errorDialog(getActivity(),"Please Login First!");
                    }

            }
            });


            dealsIn.setText("✓ "+jsonObject.optString("service_keyword").replaceAll(",","\n✓ "));


        } catch (JSONException e) {
            e.printStackTrace();
        }




        chat_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (MyPrefrences.getUserLogin(getActivity())==true) {

//                    Intent intent=new Intent(getActivity(), ChatRoomActivity.class);
//                    intent.putExtra("name",jsonObject.optString("user_name"));
//                    intent.putExtra("id",jsonObject.optString("user_id"));
//                    intent.putExtra("image",jsonObject.optString("image").toString().replace(" ","%20"));
//                    startActivity(intent);


                    LoginForChat();



                    Log.d("dfsdfsdfsdfgsdgdfgertg",jsonObject.optString("primary_mobile"));

                    //db.insertUSERS(jsonObject.optString("primary_mobile"));

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
        });

        return view;

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



    private void favourateApi(final String stat, final String id ) {
       // Util.showPgDialog(dialog);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.addUserBusinessFavourite,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Util.cancelPgDialog(dialog);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("success")){

//                                if (stat.equalsIgnoreCase("0")) {
//                                    imgFav.setBackgroundResource(R.drawable.fav2);
//                                    imgFav.getLayoutParams().height = 50;
//                                    imgFav.getLayoutParams().width = 0;
//                                    flag = false;
//                                }
//
//                                else if (stat.equalsIgnoreCase("1")) {
//                                    imgFav.setBackgroundResource(R.drawable.fav1);
//                                    imgFav.getLayoutParams().height = 50;
//                                    imgFav.getLayoutParams().width = 0;
//                                    flag = true;
//                                }
                                //  Toast.makeText(getActivity(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();

                            }
                            else{

                                if (jsonObject.getString("message").equalsIgnoreCase("please enter own id")){
                                    Toast.makeText(getActivity(), "Please Login", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getActivity(), "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                        Util.cancelPgDialog(dialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", MyPrefrences.getUserID(getActivity()));
                params.put("bussiness_id", id.toString());
                params.put("favourite_yes_no",stat.toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(postRequest);
    }

    private void ViewGalleryApi(String userId) {
        Log.d("sdfdsfsdfsfs",userId);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.viewOtherUserBussinessGallery+"/"+userId +"/1", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){
                        cardGallery.setVisibility(View.VISIBLE);
                        jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            final JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();
                            map.put("id",jsonObject1.optString("id"));
                            map.put("image",jsonObject1.optString("image"));
                            map.put("image2",jsonObject1.optString("image2"));

                            mAdapter = new HLVAdapter(getActivity());

                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            AllBaner.add(map);
                        }
                    }
                    else{
                        cardGallery.setVisibility(View.GONE);
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
    }


//    public static Fragment NewInstance(String id,String array) {
//        Bundle args = new Bundle();
//        args.putString("id", id);
//        args.putString("array", array);
//
//        Details fragment = new Details();
//        fragment.setArguments(args);
//
//        return fragment;
//    }

//    private class GalleryApi extends AsyncTask<String, Void, String> {
//        Context context;
//        String id;
//        public GalleryApi(Context context ){
//            this.context = context;
//            this.id=id;
//            dialog=new Dialog(getActivity());
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            Util.showPgDialog(dialog);
//        }
//
//
//        @Override
//        protected String doInBackground(String... strings) {
//            HashMap<String,String> map=new HashMap<>();
//
//            map.put("function","getBusinessGalleryImages");
//            map.put("companyId", "4");
//
//            JSONParser jsonParser=new JSONParser();
//            String result =jsonParser.makeHttpRequest(Api.Login,"GET",map);
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            Log.e("response", ": " + s);
//            Util.cancelPgDialog(dialog);
//            try {
//                AllBaner.clear();
//                final JSONObject jsonObject = new JSONObject(s);
//                if (jsonObject != null) {
//                    if (jsonObject.optString("status").equalsIgnoreCase("success")) {
//
//                        final JSONArray jsonArray=jsonObject.getJSONArray("message");
//                        for (int i=0;i<jsonArray.length();i++){
//                            final JSONObject jsonObject1=jsonArray.getJSONObject(i);
//
//
//                            HashMap<String,String> map=new HashMap<>();
//                            map.put("id",jsonObject1.optString("id"));
//                            map.put("photo",jsonObject1.optString("photo"));
//
//
//                            mAdapter = new HLVAdapter(getActivity());
//
//                            mRecyclerView.setAdapter(mAdapter);
//                            mAdapter.notifyDataSetChanged();
//                            AllBaner.add(map);
//
//
//                        }
//                    }
//                    else {
//                        //  Util.errorDialog(context,jsonObject.optString("message"));
//                    }
//                }
//            } catch (JSONException e) {
//                Util.errorDialog(getActivity(),"Some Error! Please try again...");
//                e.printStackTrace();
//            }
//        }
//
//    }

    public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder> {

        ArrayList<String> alName;
        ArrayList<Integer> alImage;
        Context context;

        public HLVAdapter(Context context) {
            super();
            this.context = context;
            this.alName = alName;
            this.alImage = alImage;
        }

        @Override
        public HLVAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_gallery, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(HLVAdapter.ViewHolder viewHolder, final int i) {


//            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//            viewHolder.imgThumbnail.setImageUrl(AllBaner.get(i).get("photo"),imageLoader);
            Log.d("fdssdffdfdfdsdfsdfs",AllBaner.get(i).get("image"));

            Picasso.with(context).load(AllBaner.get(i).get("image").replace(" ","%20")).into(viewHolder.imgThumbnail);


//            viewHolder.actPrice.setPaintFlags(viewHolder.actPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            // final Typeface tvFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "muli_bold.ttf");

            viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showFullImageDialog(getActivity(),AllBaner.get(i).get("image").replace(" ","%20"));
                    showFullImageDialog(getActivity(), jsonArray.toString(),i, "Image");
                }
            });
        }

        @Override
        public int getItemCount() {
            return AllBaner.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

            public ImageView imgThumbnail;

            //private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);
                imgThumbnail = (ImageView) itemView.findViewById(R.id.s1_15);
//                tvSpecies = (TextView) itemView.findViewById(R.id.tv_species);
//                actPrice = (TextView) itemView.findViewById(R.id.actPrice);
//                desc = (TextView) itemView.findViewById(R.id.desc);
//                oldPrice = (TextView) itemView.findViewById(R.id.oldPrice);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            public TextView tvSpecies,act_price,oldPrice,actPrice,desc;

            @Override
            public void onClick(View view) {
            }
            @Override
            public boolean onLongClick(View view) {
                return false;
            }

        }

    }

        public static void showFullImageDialog(Context context,String url, int pos,String titlename) {
            final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.showfullimage);
            ImageView back_img = (ImageView) dialog.findViewById(R.id.back_img);
//          ImageView fact_image = (ImageView) dialog.findViewById(R.id.fact_image);
            viewpager = (ViewPager) dialog.findViewById(R.id.viewpager);

            Log.d("fgdfgsdfgdfgdfg",url);
           // indicator = (CircleIndicator)dialog.findViewById(R.id.indicator);
            ImgData =new ArrayList<>();
            customPagerAdapter=new CustomPagerAdapter(context);
            String Prev_Response=url;

            try {
               // jsonObject2=new JSONObject(Prev_Response);

                JSONArray jsonArray=new JSONArray(Prev_Response);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    Log.d("gbfhfghfgh",jsonObject1.toString());
                    HashMap<String,String> map=new HashMap<>();
                    map.put("img",jsonObject1.optString("image2"));
                    ImgData.add(map);
                    viewpager.setAdapter(customPagerAdapter);
                    viewpager.setCurrentItem(pos);
                   // indicator.setViewPager(viewpager);
                    //  indicator.setViewPager(viewPager);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//        Util.showImage(context, imageurl, fact_image);
//        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(fact_image);
//        //photoViewAttacher.onDrag(2,2);
//        photoViewAttacher.update();
            TextView title = (TextView) dialog.findViewById(R.id.title);
            title.setText(titlename);

            back_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }



    public static class CustomPagerAdapter extends PagerAdapter {
        LayoutInflater layoutInflater;
        Button download;
        Context  context;
        Drawable drawable;
        byte[] BYTE;
        ByteArrayOutputStream byteArrayOutputStream;
        public CustomPagerAdapter(Context context) {
            this.context=context;
        }

        @Override
        public int getCount() {
            return ImgData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view==object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            //ZoomageView networkImageView;
//            ImageView networkImageView;

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.custom_photogallery2, container, false);
            ZoomageView networkImageView = (ZoomageView) view.findViewById(R.id.networkImageView);

            Log.d("fgdfgdfghsg",ImgData.get(position).get("img").toString());

//            ImageLoader imageLoader=AppController.getInstance().getImageLoader();
//            networkImageView.setImageUrl(ImgData.get(position).get("img"),imageLoader);

//            Picasso.with(context).load(ImgData.get(position).get("img")).transform(new CropSquareTransformation()).into(networkImageView);
            Picasso.with(context).load(ImgData.get(position).get("img")).into(networkImageView);
//

//            byteArrayOutputStream=new ByteArrayOutputStream();
//
////            drawable = context.getResources().getDrawable(R.drawable.sdf);
////            Bitmap bitmap1 = ((BitmapDrawable)drawable).getBitmap();
//
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            Bitmap bitmap1 = BitmapFactory.decodeFile(ImgData.get(position).get("img"), options);
//
//            bitmap1.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);
//            BYTE = byteArrayOutputStream.toByteArray();
//            Bitmap bitmap2 = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);
//            networkImageView.setImageBitmap(bitmap2);


//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            Bitmap bitmap = BitmapFactory.decodeFile(ImgData.get(position).get("img"), options);
//
//            CropSquareTransformation  ob=new CropSquareTransformation();
//            //ob.transform(bitmap);
//            networkImageView.setImageBitmap(ob.transform(bitmap));


//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            Bitmap bitmap = BitmapFactory.decodeFile(ImgData.get(position).get("img"), options);
////
//            int  size=Math.min(bitmap.getWidth(),bitmap.getHeight());
//            int mwidth=(bitmap.getWidth()-size)/2;
//            int mhieght=(bitmap.getHeight()-size)/2;
//
//            Bitmap bitmap1=Bitmap.createBitmap(bitmap,mwidth,mhieght,size,size);
//            if (bitmap1!=bitmap){
//                bitmap.recycle();
//            }
//            networkImageView.setImageBitmap(bitmap1);


//            Bitmap scaledBitmap = scaleDown(bitmap, 200, true);
//
//            networkImageView.setImageBitmap(scaledBitmap);

//            Picasso.with(context).load(ImgData.get(position).get("img")).resize(200, 100).centerCrop().into(networkImageView);

//            Util.showImage(context, ImgData.get(position).get("img"), networkImageView);
//            PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(networkImageView);
//
////            photoViewAttacher.onDrag(2,2);
//            photoViewAttacher.update();


//            networkImageView.setOnClickListener(new View.OnClickListener() {
//                @O    verride
//                public void onClick(View view) {
//                    Log.d("gfhbgfgjhfgjfh",ImgData.get(position).get("img"));
//
////                    Util.showFullImageDialog(getActivity(), ImgData.get(position).get("img"), getArguments().getString("category"));
//                    Util.showFullImageDialog(context, context.getArguments().getString("DataList"), getArguments().getString("category"));
//
//                }
//            });
            (container).addView(view);

//            thread=new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    viewPager.setCurrentItem(2);
//                }
//            };
//
//            thread.start();
//
            return view;
        }

        public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                       boolean filter) {
            float ratio = Math.min(
                    (float) maxImageSize / realImage.getWidth(),
                    (float) maxImageSize / realImage.getHeight());
            int width = Math.round((float) ratio * realImage.getWidth());
            int height = Math.round((float) ratio * realImage.getHeight());

            Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                    height, filter);
            return newBitmap;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((LinearLayout) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.POSITION_NONE;
        }
    }


    private void LoginForChat() {

//        String url = "https://chatapp-25d11.firebaseio.com/users.json";
        String url = "https://pinerria-home-business.firebaseio.com/users.json";
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Log.d("gfdgdfgdfgsdfgdf",s);
                if(s.equals("null")){
                    Toast.makeText(getActivity(), "user not found", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        JSONObject obj = new JSONObject(s);

                        if(!obj.has(user)){
                            Toast.makeText(getActivity(), "user not found", Toast.LENGTH_LONG).show();
                        }
                        else if(obj.getJSONObject(user).getString("password").equals(pass)){
                            UserDetails.username = user;
                            UserDetails.password = pass;
//                            startActivity(new Intent(getActivity(), ChatUSer.class));
                            UserDetails.chatWith = jsonObject.optString("primary_mobile");
                            Intent intent=new Intent(getActivity(),Chat.class);
                            intent.putExtra("nameValue",jsonObject.optString("user_name"));
                            intent.putExtra("id",jsonObject.optString("user_id"));
                            intent.putExtra("value1","0");
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getActivity(), "incorrect password", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                pd.dismiss();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                pd.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);


    }


}
