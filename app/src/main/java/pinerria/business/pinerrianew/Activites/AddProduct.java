package pinerria.business.pinerrianew.Activites;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import pinerria.business.pinerrianew.CameraAct.ImagePickerActivity;
import pinerria.business.pinerrianew.Fragments.Home;
import pinerria.business.pinerrianew.Fragments.MyProducts;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.JSONParser;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

public class AddProduct extends AppCompatActivity {

//    ImageView regiImage;
    CircleImageView regiImage;
    Button postAdd;
    EditText rePass,password,mobile,nameBusiness,mobileSecond,landline,email,address,zip,keyword,maxPrice,minPrice,newLocation;
    TextInputLayout newLocation1;
    RadioGroup radioGroup;
//    TextView forLogin,uploadImage;
    private static final int REQUEST_PICK_IMAGE = 1002;
    Bitmap imageBitmap;
    File f=null;

    Dialog dialog, dialog4;
    Spinner subCatSpin,catSpin;
    Spinner country,state,city;
    String id="",id2="",idLocation="",idstate="",idCity="";

    List<HashMap<String,String>> AllProducts ;
    List<HashMap<String,String>> AllCountry ;
    List<HashMap<String,String>> AllStateList ;
    List<HashMap<String,String>> AllCity ;
    List<HashMap<String,String>> AllProducts2 ;
    ArrayAdapter aa;
    ArrayAdapter aa2;
    ArrayAdapter adapterCountry,adapterState,adapterCity;

    List<String> CatList = new ArrayList<String>();
    List<String> CatList2 = new ArrayList<String>();
    List<String> countryList = new ArrayList<String>();
    List<String> stateList = new ArrayList<String>();
    List<String> cityList = new ArrayList<String>();

    JSONObject jsonObject;

    String catString ,subCatString,stateString,cityString;
    TextView toolbarTxt;
    LinearLayout linearPrice;
    CheckBox checkBobPrice;
    String flag="no";
    Spinner location;
    String loc_id="";
    public double minPirce=0,maxPirce=0;
    TextView addLocation;
    View viewShow;
    LinearLayout linearShow;
    SwitchCompat switchBusiness;
    String val_mobile="Yes";
    boolean flagLocation=false;
    boolean isData=false;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        nameBusiness=(EditText) findViewById(R.id.nameBusiness);
        mobile=findViewById(R.id.mobile);
        mobileSecond=findViewById(R.id.mobileSecond);
        landline=findViewById(R.id.landline);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        country=findViewById(R.id.country);
        state=findViewById(R.id.state);
        city=findViewById(R.id.city);
        toolbarTxt=findViewById(R.id.toolbarTxt);
        minPrice=findViewById(R.id.minPrice);
        maxPrice=findViewById(R.id.maxPrice);
        linearPrice=findViewById(R.id.linearPrice);
        checkBobPrice=findViewById(R.id.checkBobPrice);
        location=findViewById(R.id.location);
        addLocation=findViewById(R.id.addLocation);
        newLocation1=findViewById(R.id.newLocation1);
        newLocation=findViewById(R.id.newLocation);
        viewShow=findViewById(R.id.viewShow);
        linearShow=findViewById(R.id.linearShow);
        switchBusiness=findViewById(R.id.switchBusiness);

        //Toolbar mActionBarToolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(mActionBarToolbar);
       // this.getSupportActionBar().setTitle("My title");

        zip=findViewById(R.id.zip);
        keyword=findViewById(R.id.keyword);

        postAdd=findViewById(R.id.postAdd);
        regiImage=findViewById(R.id.regiImage);
//        uploadImage=findViewById(R.id.uploadImage);

        catSpin=findViewById(R.id.catSpin);
        subCatSpin=findViewById(R.id.subCatSpin);

        AllProducts = new ArrayList<>();
        AllProducts2 = new ArrayList<>();
        AllCountry = new ArrayList<>();
        AllStateList = new ArrayList<>();
        AllCity = new ArrayList<>();

        dialog=new Dialog(AddProduct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        locationList();

       // categoryData();
//        stateList("1");

      //  country();


        switchBusiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Toast.makeText(getActivity(), position+" "+b, Toast.LENGTH_SHORT).show();

                if (b==true){
                    val_mobile="Yes";
                }
                else if (b==false){
                    val_mobile="No";
                }
            }
        });




        Log.d("dfgdfgdfgdfgd",MyPrefrences.getCityID2(getApplicationContext()));
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLocationPop();
            }
        });

        checkBobPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //  Toast.makeText(getApplicationContext(), ""+b, Toast.LENGTH_SHORT).show();

                if (b==true){
                    flag="yes";
                    linearPrice.setVisibility(View.VISIBLE);
                    MyPrefrences.setIsPrice(getApplicationContext(),true);

                }
                else if(b==false){
                    flag="no";
                    maxPrice.setText("");
                    minPrice.setText("");
                    linearPrice.setVisibility(View.GONE);
                    MyPrefrences.setIsPrice(getApplicationContext(),false);
                }
            }
        });


        Log.d("dfsdfsdfsdf",MyPrefrences.getUserID(getApplicationContext()));


        if (MyPrefrences.getUserLogin(getApplicationContext())==true) {


        }

        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(AddProduct.this);
            builder.setMessage("Please Login to Add Business")
                    .setCancelable(false)
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            Intent intent=new Intent(AddProduct.this,Login.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            finish();
                        }
                    })
                    .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            //dialog.cancel();


                            Intent intent=new Intent(AddProduct.this,HomeAct.class);
                            intent.putExtra("userType","");
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            finish();

                        }
                    });
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Pinerria");
            alert.show();

        }



        if (Home.business==true){
            Log.d("fsddgdgd","true");
            toolbarTxt.setText("Edit Business");

            getFilledData();
            nameBusiness.setFocusable(false);
            mobile.setFocusable(false);



        }
        else if (Home.business==false){
            Log.d("fsddgdgd","flase");
            toolbarTxt.setText("Add Business");

        }

        mobile.setText(MyPrefrences.getMobile(getApplicationContext()));

        catSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!catSpin.getSelectedItem().toString().equalsIgnoreCase("Select Category")) {
                    id=AllProducts.get(i).get("id");
                    subCatData2(id);
                    Log.d("sdfsdfsdfsd",id);
                }
                Log.d("dsfsfsdfsdfsfs",catSpin.getSelectedItem().toString());
                catString=catSpin.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subCatSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!subCatSpin.getSelectedItem().toString().equalsIgnoreCase("Select Sub Category")) {
                    id2=AllProducts2.get(i).get("id");
                    Log.d("ssdsdsdfsdfsdfsd",id2);
                }

                Log.d("dsfsfdfdfsdsdfsdfsfs",subCatSpin.getSelectedItem().toString());
                subCatString=subCatSpin.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //if (!country.getSelectedItem().toString().equalsIgnoreCase("India")) {
                try {
                    idLocation=AllCity.get(i).get("id");
                    Log.d("sdfsdfsdfsd",idLocation);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (location.getSelectedItem().toString().equalsIgnoreCase("Add Location")){
                    //Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                    addLocationPop();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                if (!AllStateList.get(i).get("id").equals("")) {
//                    idstate=AllStateList.get(i).get("id");
//                    cityList(idstate);
//                    Log.d("sdfsdfsdfsd",idstate);
//                    Util.showPgDialog(dialog);
//
//                 }
//                stateString=state.getSelectedItem().toString();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                if (!AllCity.get(i).get("id").equals("")) {
//                    idCity = AllCity.get(i).get("id");
//
//                    Log.d("sdfsdfsdfsd", idCity);
//                }
//                cityString=city.getSelectedItem().toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });



        regiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("fsdfsdfsdf","main");
                if(isPermissionGranted()){
                    Log.d("fsdfsdfdfdfsdf","true");
                    pickImage();
                }else{
                    Log.d("fsdfsdfdfdfsdf","false");
                    ActivityCompat.requestPermissions(AddProduct.this, new String[]{Manifest.permission.CAMERA}, 1);
                }

            }
        });



        postAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log.d("dsfsfsdfsdfsfsfs",jsonObject.optString("image").toString());
//
//                String rep = jsonObject.optString("image").toString().replace("http://pinerria.com/assets/images/business/", "");
//                Log.d("dfdgdfgdfgd", rep);



                if (idLocation.equals("")) {

                    if (newLocation.getText().toString().equals("")) {
                        Util.errorDialog(AddProduct.this, "Can't blank Location");
                    }
                    else{

                        idLocation=jsonObject.optString("location_id");
                        if (validate()) {

                            if (Home.business == true) {

                                String path = null;
                                String filename = null;

                                try {
                                    path = f.toString();
                                    filename = path.substring(path.lastIndexOf("/") + 1);
                                    Log.d("dsfdfsdfsfs", filename);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (filename == null) {
//                                Util.errorDialog(AddProduct.this,"Please Select Image");


                                    String rep = jsonObject.optString("image").toString().replace(Api.BASEURL_FOR_IMAGE, "");
                                    Log.d("dfdgdfgdfgd", rep);

                                    PostDataEdit2(rep, jsonObject.optString("id"));
                                } else {
                                    //Toast.makeText(AddProduct.this, "yes", Toast.LENGTH_SHORT).show();
                                    PostDataEdit(path, filename, jsonObject.optString("id"));

                                }
                            } else if (Home.business == false) {
                                String path = null;
                                String filename = null;

                                try {
                                    path = f.toString();
                                    filename = path.substring(path.lastIndexOf("/") + 1);
                                    Log.d("dsfdfsdfsfs", filename);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (filename == null) {
                                    Util.errorDialog(AddProduct.this, "Please Select Image");
                                } else {
                                    //Toast.makeText(AddProduct.this, "yes", Toast.LENGTH_SHORT).show();
                                    PostData(path, filename);

                                }


                            }


                        }


                    }
                }
                else {


                   // idLocation=jsonObject.optString("location_id");
                    if (validate()) {

                        if (Home.business == true) {

                            String path = null;
                            String filename = null;

                            try {
                                path = f.toString();
                                filename = path.substring(path.lastIndexOf("/") + 1);
                                Log.d("dsfdfsdfsfs", filename);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (filename == null) {
//                                Util.errorDialog(AddProduct.this,"Please Select Image");


                                String rep = jsonObject.optString("image").toString().replace(Api.BASEURL_FOR_IMAGE, "");
                                Log.d("dfdgdfgdfgd", rep);

                                PostDataEdit2(rep, jsonObject.optString("id"));
                            } else {
                                //Toast.makeText(AddProduct.this, "yes", Toast.LENGTH_SHORT).show();
                                PostDataEdit(path, filename, jsonObject.optString("id"));

                            }
                        } else if (Home.business == false) {
                            String path = null;
                            String filename = null;

                            try {
                                path = f.toString();
                                filename = path.substring(path.lastIndexOf("/") + 1);
                                Log.d("dsfdfsdfsfs", filename);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (filename == null) {
                                Util.errorDialog(AddProduct.this, "Please Select Image");
                            } else {
                                //Toast.makeText(AddProduct.this, "yes", Toast.LENGTH_SHORT).show();
                                PostData(path, filename);

                            }


                        }


                    }

                }





            }
//                    }
//                }



        });


    }

    private void addLocationPop() {

          final Dialog dialog2 = new Dialog(AddProduct.this);
//          dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setContentView(R.layout.pop_add_location);
            //dialog2.setCancelable(false);

            final EditText addLocation= (EditText) dialog2.findViewById(R.id.addLocation);
            Button submitAddLoc= (Button) dialog2.findViewById(R.id.submitAddLoc);


            submitAddLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (TextUtils.isEmpty(addLocation.getText().toString())){

                        Toast.makeText(AddProduct.this, "Please Type Location", Toast.LENGTH_SHORT).show();
                    }

                    else {

                        addLocationApi(addLocation.getText().toString(),dialog2);

                    }
                }
            });

            dialog2.show();

        }

    private void addLocationApi(final String locationString, final Dialog dialog2) {

        Util.showPgDialog(dialog);


        RequestQueue queue = Volley.newRequestQueue(AddProduct.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.addLocation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("dfdffdfdfsjfdfsdfgd", "Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

                        newLocation1.setVisibility(View.VISIBLE);
                        linearShow.setVisibility(View.GONE);
                        viewShow.setVisibility(View.GONE);
                        newLocation.setText(locationString);
                        idLocation=jsonObject.optString("message");

                        Toast.makeText(getApplicationContext(),"Location Added" , Toast.LENGTH_SHORT).show();

                        dialog2.dismiss();
//                        startActivity(new Intent(getApplicationContext(),Login.class));


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
                params.put("state_id", MyPrefrences.getState(getApplicationContext()));
                params.put("city_id", MyPrefrences.getCityID2(getApplicationContext()));
                params.put("location_name", locationString);

                Log.d("dsfsdfsdfsdfs",MyPrefrences.getState(getApplicationContext()));
                Log.d("dsfsdfsdfsdfs",MyPrefrences.getCityID2(getApplicationContext()));
                Log.d("dsfsdfsdfsdfs",locationString);


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

    private void getFilledData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.userBusiness+"/"+ MyPrefrences.getUserID(getApplicationContext()), null, new Response.Listener<JSONObject>() {

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

//                            map=new HashMap();
//                            map.put("id", jsonObject.optString("id"));
//                            map.put("bussiness_name", jsonObject.optString("bussiness_name"));
//                            map.put("category_id", jsonObject.optString("category_id"));
//                            map.put("sub_category_id", jsonObject.optString("sub_category_id"));
//                            map.put("image", jsonObject.optString("image"));
//                            map.put("service_keyword", jsonObject.optString("service_keyword"));

                        nameBusiness.setText(jsonObject.optString("bussiness_name"));
                        mobile.setText(jsonObject.optString("primary_mobile"));
                        address.setText(jsonObject.optString("address"));
                        keyword.setText(jsonObject.optString("service_keyword"));
                        minPrice.setText(jsonObject.optString("min_price"));
                        maxPrice.setText(jsonObject.optString("max_price"));

                        loc_id=jsonObject.optString("location_id");

//                        newLocation1.setVisibility(View.VISIBLE);
//                        linearShow.setVisibility(View.GONE);
//                        viewShow.setVisibility(View.GONE);

                        Log.d("dfdgdfgdfghdfhgdfh",jsonObject.optString("call_button"));

                        newLocation.setText(jsonObject.optString("location_name"));

                        if (jsonObject.optString("min_price").equals("")) {
                            checkBobPrice.setChecked(false);
                            //flag="0";
                        }
                        else {
                            checkBobPrice.setChecked(true);
                          //  flag="1";
                        }


                        if (jsonObject.optString("call_button").equalsIgnoreCase("Yes")){
                            switchBusiness.setChecked(true);
                            val_mobile="Yes";

                        }
                        else if (jsonObject.optString("call_button").equalsIgnoreCase("No")){
                            switchBusiness.setChecked(false);
                            val_mobile="No";

                        }


//                        ImageLoader imageLoader=AppController.getInstance().getImageLoader();
//                        regiImage.setImageUrl(jsonObject.optString("image").replace(" ","%20"),imageLoader);

                        Picasso.with(AddProduct.this)
                                .load(jsonObject.optString("image").replace(" ","%20"))
                                .fit()
                                // .transform(transformation)
                                .into(regiImage);



                        if (isData==true) {
                            for (int i = 0; i < AllCity.size(); i++) {
                                Log.d("sdfsdgfsdgfsdfgsdg", AllCity.get(i).get("id"));

                                Log.d("dfgdfgdfgdfgdgd", jsonObject.optString("location_id"));
                                if (AllCity.get(i).get("id").equals(jsonObject.optString("location_id"))) {
                                   // Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
                                    flagLocation = true;
                                    location.setSelection(i);
                                    break;

                                } else {
                                    //  Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
                                    flagLocation = false;
                                }

                            }
                        }else if (isData==false){

                        }


                        Log.d("gsdgfsdgdfgdfgddfgd", String.valueOf(flagLocation));


                    }

                    if (flagLocation==false){
                        newLocation1.setVisibility(View.VISIBLE);
                        linearShow.setVisibility(View.GONE);
                        viewShow.setVisibility(View.GONE);
                    }
                    else if (flagLocation==true){
                        newLocation1.setVisibility(View.GONE);
                        linearShow.setVisibility(View.VISIBLE);
                        viewShow.setVisibility(View.VISIBLE);
                    }

                    else{
                       // expListView.setVisibility(View.GONE);
                        // imageNoListing.setVisibility(View.VISIBLE);
                        //  Toast.makeText(getActivity(), "No Record Found...", Toast.LENGTH_SHORT).show();
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


        try {
            minPirce= Double.parseDouble(minPrice.getText().toString());
            maxPirce= Double.parseDouble(maxPrice.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(nameBusiness.getText().toString()))
        {
           Util.errorDialog(AddProduct.this,"Type Business Name");
            return false;
        }
        else if (TextUtils.isEmpty(mobile.getText().toString()))
        {
            Util.errorDialog(AddProduct.this,"Type Mobile No");
            return false;
        }
//        else if (TextUtils.isEmpty(email.getText().toString()))
//        {
//            email.setError("Oops! Email Id blank");
//            email.requestFocus();
//            return false;
//        }
        else if (TextUtils.isEmpty(address.getText().toString()))
        {
            Util.errorDialog(AddProduct.this,"Type Address");
            return false;
        }
//        else if (TextUtils.isEmpty(zip.getText().toString()))
//        {
//            zip.setError("Oops! Pincode blank");
//            zip.requestFocus();
//            return false;
//        }
        else if (TextUtils.isEmpty(keyword.getText().toString()))
        {
            Util.errorDialog(AddProduct.this,"Type Keyword");
            return false;
        }
        if (checkBobPrice.isChecked()) {
        if (TextUtils.isEmpty(minPrice.getText().toString())) {
                Util.errorDialog(AddProduct.this, "Please enter 'Starting Price'");
                return false;
            }
        }
        else {
            return true;
        }

        if (TextUtils.isEmpty(maxPrice.getText().toString())){
            return true;
        }
//        else if (TextUtils.isEmpty(maxPrice.getText().toString()))
//        {
//            Util.errorDialog(AddProduct.this,"Type Max Price");
//            return false;
//        }



        if (minPirce>=maxPirce){
            Util.errorDialog(AddProduct.this,"'Higher Range' must be greater than 'Starting Price'");
            return false;
        }


        return true;

    }


    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    public void pickImage() {
        startActivityForResult(new Intent(this, ImagePickerActivity.class), REQUEST_PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    String imagePath = data.getStringExtra("image_path");

                    setImage(imagePath);
                    break;
            }
        } else {
            System.out.println("Failed to load image");
        }
    }

    private void setImage(String imagePath) {

        regiImage.setImageBitmap(getImageFromStorage(imagePath));
    }

    private Bitmap getImageFromStorage(String path) {
        try {
            f = new File(path);
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 512, 512);

            Log.d("sdfasafsdfsdfsdfsdf",f.toString());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void PostData(String filePath,String fileName) {

        try {
            Log.d("sdfsdfasdfsdfsdf1",filePath);
            Log.d("sdfsdfasdfsdfsdf2",fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }


        new AddProductData(filePath,fileName).execute();

    }


    private void PostDataEdit(String filePath,String fileName,String b_id) {

        try {
            Log.d("sdfsdfasdfsdfsdf1",filePath);
            Log.d("sdfsdfasdfsdfsdf2",fileName);
            Log.d("sdfsdfasdfsdfsdf2",b_id);
        } catch (Exception e) {
            e.printStackTrace();
        }


        new AddProductDataEdit(filePath,fileName,b_id).execute();

    }

    private void PostDataEdit2(String fileName,String b_id) {

        try {
          //  Log.d("sdfsdfasdfsdfsdf1",filePath);
            Log.d("sdfsdfasdfsdfsdf2",fileName);
            Log.d("sdfsdfasdfsdfsdf2",b_id);
        } catch (Exception e) {
            e.printStackTrace();
        }


        new AddProductDataEdit2(fileName,b_id).execute();

    }




    private class AddProductData extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        String val, path, fName, min, kmsDone, mobile, emailID, brand;
        HashMap<String, String> params = new HashMap<>();

        //EditText descreption,ageOfProd,headline,min,kmsDone,mobile,emailID;
        AddProductData(String path,String fName) {
            this.val = val;
            this.path = path;
            this.fName = fName;

        }

        @Override
        protected void onPreExecute() {
            Util.showPgDialog(dialog);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject jsonObject = null;
            try {

                jsonObject = uploadImageFile(AddProduct.this, val,path, fName);

                if (jsonObject != null) {

                    return jsonObject;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            String message = "";
            String data = "";

//            if (progress.isShowing())
//                progress.dismiss();

            Util.cancelPgDialog(dialog);
            if (json != null) {


                if (json.optString("status").equalsIgnoreCase("success")) {

                    Log.d("dsfdsgdgdfgdfgdfgdfgd", String.valueOf(json));

                    MyPrefrences.setBusinesID(getApplicationContext(),json.optString("message"));

                    Toast.makeText(getApplicationContext(), "Business Added Successfully...", Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(AddProduct.this,HomeAct.class);
                    intent.putExtra("userType","my_product");
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();


                } else {
                        Toast.makeText(getApplicationContext(), ""+json.optString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private JSONObject uploadImageFile(Context context, String value, String filepath1, String fileName1) {

        // sourceFile2= new File("");

        File sourceFile1 = new File(filepath1);

        String result = null;
        Log.e("FindPlayerPageAsync", "File...::::" + sourceFile1 + " : " + sourceFile1.exists());
        Log.e("file name", ": " + fileName1);
        JSONObject jsonObject = null;

        try {

            ////for image
            final MediaType MEDIA_TYPE_PNG = filepath1.endsWith("png") ? MediaType.parse("image/png") : MediaType.parse("image");

            Log.e("file name", ": " + fileName1);

            //   Log.d("fgdgdfgdfgdf1",getIntent().getStringExtra("areatypenum"));

            //Log.d("dfsdfsdgfsdgd",id.toString());
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)

                    .addFormDataPart("bussiness_name", nameBusiness.getText().toString())
                    .addFormDataPart("user_id", MyPrefrences.getUserID(getApplicationContext()))
                    .addFormDataPart("primary_mobile", mobile.getText().toString())
                    .addFormDataPart("address", address.getText().toString())
                    .addFormDataPart("city_id",MyPrefrences.getCityID2(getApplicationContext()))
                    .addFormDataPart("state_id", MyPrefrences.getState(getApplicationContext()))
                    .addFormDataPart("service_keyword", keyword.getText().toString())
                    .addFormDataPart("max_price", maxPrice.getText().toString())
                    .addFormDataPart("min_price", minPrice.getText().toString())
                    .addFormDataPart("price_status", flag)
                    .addFormDataPart("call_button_status", val_mobile)
                    .addFormDataPart("location_id", idLocation)

                    .addFormDataPart("image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                    .build();

             Log.d("dfsdfdgdfgdfgdfg",MyPrefrences.getCityID2(getApplicationContext()));
             Log.d("dfsdfdgdfgdfgdfg",MyPrefrences.getState(getApplicationContext()));
             Log.d("dfgdfgdfgd",idLocation);


//            Log.d("fvfgdgdfhgghfhgdfh", amounts.getText().toString().replace("₹ ", ""));
//            Log.d("fvfgdgdfhgdfhqwdfs",amounts.getText().toString().replace("₹ ", ""));

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
//                     .url("http://divpreetsingh.info/app/ManiUploadsImageHere")
                    .header("Authorization", "Client-ID " + "...")
//                    .url("http://bizzcityinfo.com/AndroidApi.php?function=insertGalleryPhoto")
                    .url(Api.addUserBussiness)
//                    .url("http://templatestheme.com/demo/tradeone/ws/post_offer.php")
                    // .addHeader("enctype", "multipart/form-data")
                    .post(requestBody)
                    .build();


            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(15, TimeUnit.SECONDS);
            client.setWriteTimeout(15, TimeUnit.SECONDS);
            client.setReadTimeout(15, TimeUnit.SECONDS);


            Log.e("request1", ":url:  " + request.urlString() + ", header: " + request.headers() + ", body " + request.body());
            com.squareup.okhttp.Response response = client.newCall(request).execute();
            result = response.body().string();
            Log.e("responseMultipart", ": " + result);
            jsonObject = new JSONObject(result);
            Log.e("result", ": " + result);
            return jsonObject;
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("FindPlayerPageAsync", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("FindPlayerPageAsync", "Other Error: " + e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_SHORT).show();
        }
        return jsonObject;
    }





    private class AddProductDataEdit extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        String val, path, fName, b_id, kmsDone, mobile, emailID, brand;
        HashMap<String, String> params = new HashMap<>();

        //EditText descreption,ageOfProd,headline,min,kmsDone,mobile,emailID;
        AddProductDataEdit(String path,String fName,String b_id) {
            this.val = val;
            this.path = path;
            this.fName = fName;
            this.b_id = b_id;

        }

        @Override
        protected void onPreExecute() {
            Util.showPgDialog(dialog);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject jsonObject = null;
            try {

                jsonObject = uploadImageFileEdit(AddProduct.this, val,path, fName,b_id);

                if (jsonObject != null) {

                    return jsonObject;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            String message = "";
            String data = "";

//            if (progress.isShowing())
//                progress.dismiss();

            Util.cancelPgDialog(dialog);
            if (json != null) {


                if (json.optString("status").equalsIgnoreCase("success")) {

                    Log.d("dsfdsgdgdfgdfgdfgdfgd", String.valueOf(json));

                    Toast.makeText(getApplicationContext(), "Business Edit Successfully...", Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(AddProduct.this,HomeAct.class);
                    intent.putExtra("userType","my_product");
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(), ""+json.optString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private JSONObject uploadImageFileEdit(Context context, String value, String filepath1, String fileName1,String b_id) {

        // sourceFile2= new File("");

        File sourceFile1 = new File(filepath1);

        String result = null;
        Log.e("FindPlayerPageAsync", "File...::::" + sourceFile1 + " : " + sourceFile1.exists());
        Log.e("file name", ": " + fileName1);
        JSONObject jsonObject = null;

        try {

            ////for image
            final MediaType MEDIA_TYPE_PNG = filepath1.endsWith("png") ? MediaType.parse("image/png") : MediaType.parse("image");

            Log.e("file name", ": " + fileName1);

            //   Log.d("fgdgdfgdfgdf1",getIntent().getStringExtra("areatypenum"));

            //Log.d("dfsdfsdgfsdgd",id.toString());
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)

                    .addFormDataPart("business_id", b_id)

                    .addFormDataPart("bussiness_name", nameBusiness.getText().toString())
                    .addFormDataPart("user_id", MyPrefrences.getUserID(getApplicationContext()))
                    .addFormDataPart("primary_mobile", mobile.getText().toString())
                    .addFormDataPart("address", address.getText().toString())
                    .addFormDataPart("city_id",MyPrefrences.getCityID2(getApplicationContext()))
                    .addFormDataPart("state_id", MyPrefrences.getState(getApplicationContext()))
                    .addFormDataPart("service_keyword", keyword.getText().toString())
                    .addFormDataPart("max_price", maxPrice.getText().toString())
                    .addFormDataPart("min_price", minPrice.getText().toString())
                    .addFormDataPart("price_status", flag)
                    .addFormDataPart("call_button_status", val_mobile)
                    .addFormDataPart("location_id", idLocation)
                  //  .addFormDataPart("show_mobile", val_mobile)
                    .addFormDataPart("image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                    .build();

            Log.d("dfsdfdgdfgdfgdfg",MyPrefrences.getCityID2(getApplicationContext()));
            Log.d("dfsdfdgdfgdfgdfg",MyPrefrences.getState(getApplicationContext()));
            Log.d("dfgdfgdfgd",idLocation);
            Log.d("gdfgdfgdfgdfgdfgdgdg",val_mobile);


//            Log.d("fvfgdgdfhgghfhgdfh", amounts.getText().toString().replace("₹ ", ""));
//            Log.d("fvfgdgdfhgdfhqwdfs",amounts.getText().toString().replace("₹ ", ""));

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
//                     .url("http://divpreetsingh.info/app/ManiUploadsImageHere")
                    .header("Authorization", "Client-ID " + "...")
//                    .url("http://bizzcityinfo.com/AndroidApi.php?function=insertGalleryPhoto")
                    .url(Api.editUserBussiness)
//                    .url("http://templatestheme.com/demo/tradeone/ws/post_offer.php")
                    // .addHeader("enctype", "multipart/form-data")
                    .post(requestBody)
                    .build();


            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(15, TimeUnit.SECONDS);
            client.setWriteTimeout(15, TimeUnit.SECONDS);
            client.setReadTimeout(15, TimeUnit.SECONDS);


            Log.e("request1", ":url:  " + request.urlString() + ", header: " + request.headers() + ", body " + request.body());
            com.squareup.okhttp.Response response = client.newCall(request).execute();
            result = response.body().string();
            Log.e("responseMultipart", ": " + result);
            jsonObject = new JSONObject(result);
            Log.e("result", ": " + result);
            return jsonObject;
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("FindPlayerPageAsync", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("FindPlayerPageAsync", "Other Error: " + e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_SHORT).show();
        }
        return jsonObject;
    }


    private class AddProductDataEdit2 extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        String val,  fName, b_id, kmsDone, mobile, emailID, brand;
        HashMap<String, String> params = new HashMap<>();

        //EditText descreption,ageOfProd,headline,min,kmsDone,mobile,emailID;
        AddProductDataEdit2(String fName,String b_id) {
            this.val = val;
          //  this.path = path;
            this.fName = fName;
            this.b_id = b_id;

        }

        @Override
        protected void onPreExecute() {
            Util.showPgDialog(dialog);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject jsonObject = null;
            try {

                jsonObject = uploadImageFileEdit2(AddProduct.this, val, fName,b_id);

                if (jsonObject != null) {

                    return jsonObject;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            String message = "";
            String data = "";

//            if (progress.isShowing())
//                progress.dismiss();

            Util.cancelPgDialog(dialog);
            if (json != null) {


                if (json.optString("status").equalsIgnoreCase("success")) {

                    Log.d("dsfdsgdgdfgdfgdfgdfgd", String.valueOf(json));




                    Toast.makeText(getApplicationContext(), "Business Edit Successfully...", Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(AddProduct.this,HomeAct.class);
                    intent.putExtra("userType","my_product");
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(), ""+json.optString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private JSONObject uploadImageFileEdit2(Context context, String value, String fileName1,String b_id) {

        // sourceFile2= new File("");

   //     File sourceFile1 = new File(filepath1);

        String result = null;
        //Log.e("FindPlayerPageAsync", "File...::::" + sourceFile1 + " : " + sourceFile1.exists());
        Log.e("file name", ": " + fileName1);
        JSONObject jsonObject = null;

        try {

            ////for image
           // final MediaType MEDIA_TYPE_PNG = filepath1.endsWith("png") ? MediaType.parse("image/png") : MediaType.parse("image");

            Log.e("file name", ": " + fileName1);

            //   Log.d("fgdgdfgdfgdf1",getIntent().getStringExtra("areatypenum"));

            //Log.d("dfsdfsdgfsdgd",id.toString());
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)

                    .addFormDataPart("business_id", b_id)

                    .addFormDataPart("bussiness_name", nameBusiness.getText().toString())
                    .addFormDataPart("user_id", MyPrefrences.getUserID(getApplicationContext()))
                    .addFormDataPart("primary_mobile", mobile.getText().toString())
                    .addFormDataPart("address", address.getText().toString())
                    .addFormDataPart("city_id",MyPrefrences.getCityID2(getApplicationContext()))
                    .addFormDataPart("state_id", MyPrefrences.getState(getApplicationContext()))
                    .addFormDataPart("service_keyword", keyword.getText().toString())
                    .addFormDataPart("max_price", maxPrice.getText().toString())
                    .addFormDataPart("min_price", minPrice.getText().toString())
                    .addFormDataPart("price_status", flag)
                    .addFormDataPart("call_button_status", val_mobile)
                    .addFormDataPart("location_id", idLocation)
//                    .addFormDataPart("show_mobile", val_mobile)
                    .addFormDataPart("image", "")
                    .addFormDataPart("old_image", fileName1)
                    .build();

            Log.d("dfsdfdgdfgdfgdfg",MyPrefrences.getCityID2(getApplicationContext()));
            Log.d("dfsdfdgdfgdfgdfg",MyPrefrences.getState(getApplicationContext()));
            Log.d("dfgdfgdfgd",idLocation);

            Log.d("gdfgdfgdfgdfgdfgdgdg",val_mobile);

//            Log.d("fvfgdgdfhgghfhgdfh", amounts.getText().toString().replace("₹ ", ""));
//            Log.d("fvfgdgdfhgdfhqwdfs",amounts.getText().toString().replace("₹ ", ""));

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
//                     .url("http://divpreetsingh.info/app/ManiUploadsImageHere")
                    .header("Authorization", "Client-ID " + "...")
//                    .url("http://bizzcityinfo.com/AndroidApi.php?function=insertGalleryPhoto")
                    .url(Api.editUserBussiness)
//                    .url("http://templatestheme.com/demo/tradeone/ws/post_offer.php")
                    // .addHeader("enctype", "multipart/form-data")
                    .post(requestBody)
                    .build();


            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(15, TimeUnit.SECONDS);
            client.setWriteTimeout(15, TimeUnit.SECONDS);
            client.setReadTimeout(15, TimeUnit.SECONDS);


            Log.e("request1", ":url:  " + request.urlString() + ", header: " + request.headers() + ", body " + request.body());
            com.squareup.okhttp.Response response = client.newCall(request).execute();
            result = response.body().string();
            Log.e("responseMultipart", ": " + result);
            jsonObject = new JSONObject(result);
            Log.e("result", ": " + result);
            return jsonObject;
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("FindPlayerPageAsync", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("FindPlayerPageAsync", "Other Error: " + e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_SHORT).show();
        }
        return jsonObject;
    }




    private void categoryData() {

        Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.category, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Resposecategory", response.toString());
                Util.cancelPgDialog(dialog);
                try {


                    if (response.getString("status").equalsIgnoreCase("success")){

                        AllProducts.clear();
                        CatList.clear();

                        HashMap<String, String> map2 = new HashMap<>();
                        map2.put("Category", "");
                        AllProducts.add(map2);
                        CatList.add("Select Category");


                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("category", jsonObject.optString("category"));


                            CatList.add(jsonObject.optString("category"));


                            aa = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,CatList);
                            aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            catSpin.setAdapter(aa);

                            AllProducts.add(map);



                        }
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

    private void subCatData2(String id) {

        Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.subCatByCatId+"/"+id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Resposesucategory", response.toString());
                Util.cancelPgDialog(dialog);
                try {


                    if (response.getString("status").equalsIgnoreCase("success")){

                        AllProducts2.clear();
                        CatList2.clear();

                        HashMap<String, String> map2 = new HashMap<>();
                        map2.put("subCategory", "");
                        AllProducts2.add(map2);
                        CatList2.add("Select Sub Category");



                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("subcategory", jsonObject.optString("subcategory"));

                            CatList2.add(jsonObject.optString("subcategory"));


                            aa2 = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,CatList2);
                            aa2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            subCatSpin.setAdapter(aa2);

                            AllProducts2.add(map);

                        }
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


    private void country() {

        Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.country, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeCountry", response.toString());
                Util.cancelPgDialog(dialog);
                try {


                    if (response.getString("status").equalsIgnoreCase("success")){

                        AllCountry.clear();
                        countryList.clear();


                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("country", jsonObject.optString("country"));


                            countryList.add(jsonObject.optString("country"));


                            adapterCountry = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,countryList);
                            adapterCountry.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            country.setAdapter(adapterCountry);

                            AllCountry.add(map);



                        }
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


    private void stateList(String idCountry) {

       // Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.stateByCountryId+"/"+idCountry, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Resposecategory", response.toString());
                Util.cancelPgDialog(dialog);
                try {


                    if (response.getString("status").equalsIgnoreCase("success")){

                        AllStateList.clear();
                        stateList.clear();

                        HashMap<String, String> map2 = new HashMap<>();
                        map2.put("id", "");
                        AllStateList.add(map2);
                        stateList.add("Select State");



                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("name", jsonObject.optString("name"));


                            stateList.add(jsonObject.optString("name"));


                            adapterState = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,stateList);
                            adapterState.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            state.setAdapter(adapterState);

                            AllStateList.add(map);

                        }
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


//    private void cityList(String idState) {
//
////        Util.showPgDialog(dialog);
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                Api.cityByStateId+"/"+idState, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("Resposecategory", response.toString());
//                Util.cancelPgDialog(dialog);
//                try {
//
//
//                    if (response.getString("status").equalsIgnoreCase("success")){
//
//                        AllCity.clear();
//                        cityList.clear();
//
//
//                        HashMap<String, String> map2 = new HashMap<>();
//                        map2.put("id", "");
//                        AllCity.add(map2);
//                        cityList.add("Select Location");
//
//                        JSONArray jsonArray=response.getJSONArray("message");
//                        for (int i=0;i<jsonArray.length();i++){
//                            JSONObject jsonObject=jsonArray.getJSONObject(i);
//
//                            HashMap<String,String> map=new HashMap<>();
//
//                            map.put("id", jsonObject.optString("id"));
//                            map.put("name", jsonObject.optString("name"));
//
//
//                            cityList.add(jsonObject.optString("name"));
//
//
//                            adapterCity = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,cityList);
//                            adapterCity.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//                            city.setAdapter(adapterCity);
//
//                            AllCity.add(map);
//
//
//
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                    Util.cancelPgDialog(dialog);
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Respose", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//                Util.cancelPgDialog(dialog);
//
//            }
//        });
//
//        // Adding request to request queue
//        jsonObjReq.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
//
//
//
//    }

    private void locationList() {

        Log.d("dfgdfgdfgdfgd",MyPrefrences.getCityID2(getApplicationContext()));

//        Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.locationByCityId+"/"+MyPrefrences.getCityID2(getApplicationContext()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposecategoryCity", response.toString());
                Util.cancelPgDialog(dialog);
                try {

                    if (response.getString("status").equalsIgnoreCase("success")){
                        isData=true;

                        AllCity.clear();
                        cityList.clear();

                        HashMap<String, String> map2 = new HashMap<>();
                        map2.put("id", "");
                        AllCity.add(map2);
                        cityList.add("Select Location");


                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("location", jsonObject.optString("location"));
                            map.put("city_id", jsonObject.optString("city_id"));
                            map.put("state_id", jsonObject.optString("state_id"));


                            cityList.add(jsonObject.optString("location"));

                            adapterCity = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,cityList);
                            adapterCity.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            location.setAdapter(adapterCity);

                            AllCity.add(map);

                        }
                        cityList.add("Add Location");
                    }
                    else{
                        AllCity.clear();
                        cityList.clear();

                        HashMap<String, String> map2 = new HashMap<>();
                        map2.put("id", "");
                        AllCity.add(map2);
                        cityList.add("Select Location");
                        cityList.add("Add Location");

                        HashMap<String,String> map=new HashMap<>();

                        adapterCity = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,cityList);
                        adapterCity.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                        location.setAdapter(adapterCity);

                        AllCity.add(map);
                        isData= false;
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




}
