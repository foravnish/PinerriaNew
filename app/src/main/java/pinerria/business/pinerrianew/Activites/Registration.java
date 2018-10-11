package pinerria.business.pinerrianew.Activites;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.firebase.client.Firebase;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

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
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.JSONParser;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

import static android.text.Html.fromHtml;


public class Registration extends AppCompatActivity {

    //    ImageView regiImage;
    // ImageView regiImage;
    Button registration;
    EditText rePass,password,mobile,namePerson,emailId;
    RadioGroup radioGroup;
    TextView forLogin,skip;

    private static final int REQUEST_PICK_IMAGE = 1002;
    Bitmap imageBitmap;
    File f=null;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    Dialog dialog, dialog4;
    Spinner country,state,city;
    List<HashMap<String,String>> AllStateList ;
    List<HashMap<String,String>> AllCity ;
    String id="",id2="",idCountry="",idstate="",idCity="";
    List<String> stateList = new ArrayList<String>();
    List<String> cityList = new ArrayList<String>();
    ArrayAdapter adapterState,adapterCity;
    String stateString,cityString;
    TextView tncButton;
    private String pass="123456";
    CheckBox checkBox;


    List<String> keyData=new ArrayList<>();
    List<String> valData=new ArrayList<>();
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    JSONObject jsonObject1;
    EditText otp_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        namePerson=(EditText) findViewById(R.id.namePerson);
        emailId=(EditText) findViewById(R.id.emailId);
        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.password);
        rePass=findViewById(R.id.rePass);
        registration=findViewById(R.id.registration);
        // regiImage=findViewById(R.id.regiImage);
        forLogin=findViewById(R.id.forLogin);
        skip=findViewById(R.id.skip);
        checkBox=findViewById(R.id.checkBox);
        tncButton=findViewById(R.id.tncButton);
        //uploadImage=findViewById(R.id.uploadImage);

        state=findViewById(R.id.state);
        city=findViewById(R.id.city);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        dialog=new Dialog(Registration.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        AllStateList = new ArrayList<>();
        AllCity = new ArrayList<>();
//        stateList("1");
        cityList();

        Firebase.setAndroidContext(this);

        keyData.add("name");
        keyData.add("password");

        valData.add("avnish");
        valData.add("amit");



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//        {
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//        }
//        else
//        {
//            //your code
//
//        }


        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        } else {

        }



        tncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Registration.this,TermandConditions.class));


//                Log.d("fgdgdfgdfgdfgdg", String.valueOf(checkBox.isChecked()));
//
//                if (checkBox.isChecked()==true){
//                    Log.d("fdgsdgsdfgsdfgsdgrfgds","yes");
//                }
//                else{
//                    Log.d("fdgsdgsdfgsdfgsdgrfgds","not ");
//                }

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
//                }
//                stateString=state.getSelectedItem().toString();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!AllCity.get(i).get("id").equals("")) {
                    idCity = AllCity.get(i).get("id");
                    idstate = AllCity.get(i).get("state_id");

                    Log.d("sdfsdfsdfsd", idCity);
                    Log.d("sdfsdfsdfsd", idstate);
                }
                cityString=city.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        uploadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("fsdfsdfsdf","main");
//                if(isPermissionGranted()){
//                    Log.d("fsdfsdfdfdfsdf","true");
//                    pickImage();
//                }else{
//                    Log.d("fsdfsdfdfdfsdf","false");
//                    ActivityCompat.requestPermissions(Registration.this, new String[]{Manifest.permission.CAMERA}, 1);
//                }
//
//            }
//        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    final RadioButton radioButton = (RadioButton) findViewById(selectedId);

//                    if (checkBox.isChecked()==true){
//                        Log.d("fdgsdgsdfgsdfgsdgrfgds","yes");

                        if(validate()){


//                    if (stateString.equalsIgnoreCase("Select State")){
//                        Util.errorDialog(Registration.this,"Please Select State");
//                    }
//                    else{

                                PostData(radioButton.getText().toString(),mobile.getText().toString());

                            }


//                }

//                    else{
//                        Log.d("fdgsdgsdfgsdfgsdgrfgds","not ");
//                        Util.errorDialog(Registration.this,"Please indicate acceptance of Terms & Conditions");
//                    }




            }
        });

        forLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),HomeAct.class);
                intent.putExtra("userType","");
                startActivity(intent);
            }
        });
    }

    private boolean validate() {

        if (TextUtils.isEmpty(namePerson.getText().toString())) {
            Util.errorDialog(Registration.this, "Enter Your Name");
            return false;
        } else if (TextUtils.isEmpty(mobile.getText().toString())) {
            Util.errorDialog(Registration.this, "Enter Mobile No.");
            return false;
        } else if (mobile.getText().toString().length() < 10) {
            Util.errorDialog(Registration.this, "Enter 10 digit Mobile No.");
            return false;
        }
        else if (!TextUtils.isEmpty(emailId.getText().toString())) {
            if (!emailId.getText().toString().trim().matches(emailPattern)) {
                Util.errorDialog(Registration.this, "Enter valid Email Id.");
                return false;
            }
        } if (TextUtils.isEmpty(password.getText().toString())) {
            Util.errorDialog(Registration.this, "Enter Password.");
            return false;
        } if (TextUtils.isEmpty(rePass.getText().toString())) {
            Util.errorDialog(Registration.this, "Enter Confirm Password.");
            return false;
        } if (!rePass.getText().toString().equals(password.getText().toString())) {
//            Toast.makeText(getApplicationContext(), "Both Password should be match", Toast.LENGTH_SHORT).show();
            Util.errorDialog(Registration.this, "Confirm Password dose not match.");
            return false;
        }  if (cityString.equalsIgnoreCase("Select City")) {
            Util.errorDialog(Registration.this, "Please Select City.");
            return false;
        }  if (radioGroup.getCheckedRadioButtonId() == -1) {
            Util.errorDialog(Registration.this, "Please Select Gender.");
            return false;
        }  if (checkBox.isChecked() == false) {
            Util.errorDialog(Registration.this, "Please indicate acceptance of Terms & Conditions.");
            return false;
        }

            return true;

    }


    private void PostData(final String produ,String mob) {

        try {
//            Log.d("sdfsdfasdfsdfsdf1",filePath);
//            Log.d("sdfsdfasdfsdfsdf2",fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }


        new AddProductData(produ,mob).execute();

    }

    private class AddProductData extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        String val,   min, kmsDone, mobile, emailID, brand,mob;
        HashMap<String, String> params = new HashMap<>();

        //EditText descreption,ageOfProd,headline,min,kmsDone,mobile,emailID;
        AddProductData(String val,String mob) {
            this.val = val;
//            this.path = path;
//            this.fName = fName;
            this.mob = mob;

        }

        @Override
        protected void onPreExecute() {
            Util.showPgDialog(dialog);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject jsonObject = null;
            try {

                jsonObject = uploadImageFile(Registration.this, val);


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


                Log.d("fgdfgdfgdfgdfgdfg", String.valueOf(json));
                if (json.optString("status").equalsIgnoreCase("success")) {

                    // Toast.makeText(getApplicationContext(), "Registration Successfully..", Toast.LENGTH_LONG).show();

                    otpVerfy(mob);

                } else {
//                    Toast.makeText(PostAdd.this, "Error " + json, Toast.LENGTH_LONG).show();
                    errorDialog(Registration.this, json.optString("message"));
                }
            }
        }

    }

    private void otpVerfy(final String mob) {


        final Dialog dialog2 = new Dialog(Registration.this);
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

                    Toast.makeText(Registration.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }

                else {

                    verifyOTP_API(mob,otp_edit.getText().toString(),dialog2);

                }
            }
        });

        dialog2.show();

    }

    private void verifyOTP_API(final String mob, final String otp, final Dialog dialogOTP) {

        Util.showPgDialog(dialog);


        RequestQueue queue = Volley.newRequestQueue(Registration.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.checkOtp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("OTP", "top Response: " + response);


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

                                    reference.child(mob).child("password").setValue(pass);
                                    reference2.child(mob).child("name").setValue(namePerson.getText().toString());
                                    reference3.child(mob).child("userId").setValue(jsonObject1.optString("id").toString());

                                    errorDialog(Registration.this,"Thanks for registering with Pinerria. You can now perform the following tasks:\n" +
                                            "Add your business on Pinerria\n" +
                                            "Make that Business as Premium\n"+
                                            "Post a job\n"+
                                            "Contact other sellers; and\n"+
                                            "Give Review and Rating on other businesses.");

                                   // Toast.makeText(getApplicationContext(), "Registration Successfully... Please Login.", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(Registration.this,   Login.class));
//                                    finish();

                                }
                                else {
                                    try {
                                        JSONObject obj = new JSONObject(s);

                                        if (!obj.has(mob)) {

                                            reference.child(mob).child("password").setValue(pass);
                                            reference2.child(mob).child("name").setValue(namePerson.getText().toString());
                                            reference3.child(mob).child("userId").setValue(jsonObject1.optString("id").toString());

                                            Toast.makeText(getApplicationContext(), "Registration Successfully... Please Login.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Registration.this,   Login.class));
                                            finish();

                                        } else {
                                            Toast.makeText(Registration.this, "User already exists", Toast.LENGTH_LONG).show();
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

                        RequestQueue rQueue = Volley.newRequestQueue(Registration.this);
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


    private JSONObject uploadImageFile(Context context, String value) {

        // sourceFile2= new File("");

        //File sourceFile1 = new File(filepath1);

        String result = null;
        //Log.e("FindPlayerPageAsync", "File...::::" + sourceFile1 + " : " + sourceFile1.exists());
        // Log.e("file name", ": " + fileName1);
        JSONObject jsonObject = null;

        try {

            ////for image
            //final MediaType MEDIA_TYPE_PNG = filepath1.endsWith("png") ? MediaType.parse("image/png") : MediaType.parse("image");

            // Log.e("file name", ": " + fileName1);

            //   Log.d("fgdgdfgdfgdf1",getIntent().getStringExtra("areatypenum"));

            //Log.d("dfsdfsdgfsdgd",id.toString());
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)

                    .addFormDataPart("name", namePerson.getText().toString())
                    .addFormDataPart("emailId", emailId.getText().toString())
                    .addFormDataPart("mobile", mobile.getText().toString())
                    .addFormDataPart("gender", value)
                    .addFormDataPart("password", password.getText().toString())
                    .addFormDataPart("state_id", idstate)
                    .addFormDataPart("city_id", idCity)


                    //.addFormDataPart("image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                    .build();

            Log.d("dfdsgsdgdfgdfh",idCity);
            Log.d("dfdsgsdgdfgdfh",idstate);


            Log.d("gdfgsdfgsgsgsdg",emailId.getText().toString());

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
//                     .url("http://divpreetsingh.info/app/ManiUploadsImageHere")
                    .header("Authorization", "Client-ID " + "...")
//                    .url("http://bizzcityinfo.com/AndroidApi.php?function=insertGalleryPhoto")
                    .url(Api.userRegistration)
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

//
//    public boolean isPermissionGranted() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return true;
//        }
//
//    }
//
//    public void pickImage() {
//        startActivityForResult(new Intent(this, ImagePickerActivity.class), REQUEST_PICK_IMAGE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            pickImage();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_PICK_IMAGE:
//                    String imagePath = data.getStringExtra("image_path");
//
//                    setImage(imagePath);
//                    break;
//            }
//        } else {
//            System.out.println("Failed to load image");
//        }
//    }

//    private void setImage(String imagePath) {
//        regiImage.setImageBitmap(getImageFromStorage(imagePath));
//    }
//
//    private Bitmap getImageFromStorage(String path) {
//        try {
//            f = new File(path);
//            // First decode with inJustDecodeBounds=true to check dimensions
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = false;
//            // Calculate inSampleSize
//            options.inSampleSize = calculateInSampleSize(options, 512, 512);
//
//            Log.d("sdfasafsdfsdfsdfsdf",f.toString());
//            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
//            return b;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) > reqHeight
//                    && (halfWidth / inSampleSize) > reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }


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
    private void cityList() {

//        Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.city, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposecategoryCity", response.toString());
                Util.cancelPgDialog(dialog);
                try {

                    if (response.getString("status").equalsIgnoreCase("success")){

                        AllCity.clear();
                        cityList.clear();

                        HashMap<String, String> map2 = new HashMap<>();
                        map2.put("id", "");
                        AllCity.add(map2);
                        cityList.add("Select City");


                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("name", jsonObject.optString("name"));
                            map.put("state_id", jsonObject.optString("state_id"));

                            cityList.add(jsonObject.optString("name"));

                            adapterCity = new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_item,cityList);
                            adapterCity.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            city.setAdapter(adapterCity);

                            AllCity.add(map);



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


    private void resendOtp(final String mobile) {


        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(Registration.this);
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



                    }
                    else{
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                        Util.errorDialog(Registration.this,jsonObject.getString("message"));
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



    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
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

                Intent intent=new Intent(context,Login.class);
                context.startActivity(intent);
                // finish();


            }
        });
        dialog.show();

    }


}
