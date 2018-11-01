package pinerria.business.pinerrianew.Activites;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import pinerria.business.pinerrianew.CameraAct.ImagePickerActivity;
import pinerria.business.pinerrianew.Fragments.Listing;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.JSONParser;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

import static android.text.Html.fromHtml;

public class ProfilePage extends AppCompatActivity {

    Dialog dialog;
    CircleImageView profileImage;
    TextView personName,mobileNo,tve_gender,tve_rod,tve_location,tve_user_id;
    Button changePassword,submitChangePassword;
    EditText oldPassword,newPassword,newPassword2;
    ImageView imgChange;
    private static final int REQUEST_PICK_IMAGE = 1002;
    File f=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        dialog=new Dialog(ProfilePage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);

        profileImage=findViewById(R.id.profileImage);
        personName=findViewById(R.id.personName);
        mobileNo=findViewById(R.id.mobileNo);
        tve_gender=findViewById(R.id.tve_gender);
        tve_rod=findViewById(R.id.tve_rod);
        tve_location=findViewById(R.id.tve_location);
        tve_user_id=findViewById(R.id.tve_user_id);
        changePassword=findViewById(R.id.changePassword);
        imgChange=findViewById(R.id.imgChange);

        getProfileData();


        imgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("fsdfsdfsdf","main");
                if(isPermissionGranted()){
                    Log.d("fsdfsdfdfdfsdf","true");
                    pickImage();
                }else{
                    Log.d("fsdfsdfdfdfsdf","false");
                    ActivityCompat.requestPermissions(ProfilePage.this, new String[]{Manifest.permission.CAMERA}, 1);
                }

            }
        });


        if (MyPrefrences.getUserLogin(getApplicationContext())==true) {

        }
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePage.this);
            builder.setMessage("Please Login to Profile Page")
                    .setCancelable(false)
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent intent=new Intent(ProfilePage.this,Login.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            finish();
                        }
                    })
                    .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            //dialog.cancel();

                            Intent intent=new Intent(ProfilePage.this,HomeAct.class);
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


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog0 = new Dialog(ProfilePage.this);
                //dialog0.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog0.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog0.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog0.setContentView(R.layout.dialog_change_password);
                ImageView back_img = (ImageView) dialog0.findViewById(R.id.back_img);

                oldPassword =  dialog0.findViewById(R.id.oldPassword);
                newPassword =  dialog0.findViewById(R.id.newPassword);
                newPassword2 =  dialog0.findViewById(R.id.newPassword2);
                submitChangePassword =  dialog0.findViewById(R.id.submitChangePassword);

                TextView title = (TextView) dialog0.findViewById(R.id.title);
                title.setText("Change Password");
                back_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog0.cancel();


                    }
                });

                submitChangePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkValidation()){

                            //new ChangePassword(getActivity(),password.getText().toString(),newPwd.getText().toString()).execute();

                            Util.showPgDialog(dialog);
                            StringRequest postRequest = new StringRequest(Request.Method.POST, Api.userChangePassword,
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

                                                    // Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                                                    errorDialog(ProfilePage.this,jsonObject.getString("message") );

                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(ProfilePage.this, "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                                            Util.cancelPgDialog(dialog);
                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams()
                                {
                                    Map<String, String>  params = new HashMap<String, String>();
                                    params.put("user_id", MyPrefrences.getUserID(getApplicationContext()));
                                    params.put("new_password", newPassword.getText().toString());
                                    params.put("old_password", oldPassword.getText().toString());

                                    return params;
                                }
                            };
                            postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            postRequest.setShouldCache(false);

                            AppController.getInstance().addToRequestQueue(postRequest);


                        }

                    }
                });
                dialog0.show();

            }
        });



    }

    private boolean  checkValidation() {
        if (TextUtils.isEmpty(oldPassword.getText().toString()))
        {
            oldPassword.setError("Oops! Password blank");
            oldPassword.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(newPassword.getText().toString()))
        {
            newPassword.setError("Oops! New Password blank");
            newPassword.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(newPassword2.getText().toString())){
            newPassword2.setError("Oops! Confirm Password blank");
            newPassword2.requestFocus();
            return false;
        }
        else if (!newPassword.getText().toString().equals(newPassword2.getText().toString())){

            //Util.errorDialog(getActivity(),"Confirm Password must be same !");
            Toast.makeText(getApplicationContext(), "Confirm Password must be same !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (oldPassword.getText().toString().length()<3){
            oldPassword.setError("Oops! Password length must 3 ");
            oldPassword.requestFocus();
            return false;
        }

        else if (newPassword.getText().toString().length()<3){
            newPassword.setError("Oops! New Password length must 3 ");
            newPassword.requestFocus();
            return false;
        }

        else if (newPassword2.getText().toString().length()<3){
            newPassword2.setError("Oops! Confirm Password length must 3 ");
            newPassword2.requestFocus();
            return false;
        }
        return true;
    }

    private void errorDialog(ProfilePage changePassword, String message) {

        final Dialog dialog = new Dialog(ProfilePage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogcustom2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
        text.setText(fromHtml(message));
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
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
                        personName=findViewById(R.id.personName);
                        mobileNo=findViewById(R.id.mobileNo);
                        tve_gender=findViewById(R.id.tve_gender);
                        tve_rod=findViewById(R.id.tve_rod);
                        tve_location=findViewById(R.id.tve_location);

                        tve_user_id.setText(jsonObject.optString("id"));
                        String upperString = jsonObject.optString("name").substring(0,1).toUpperCase() + jsonObject.optString("name").substring(1);
                        personName.setText(upperString);

                       // personName.setText(jsonObject.optString("name"));

                        mobileNo.setText("+91 "+jsonObject.optString("mobile"));
                        tve_gender.setText(jsonObject.optString("gender"));
                        tve_rod.setText(jsonObject.optString("created_date"));
                        tve_location.setText(jsonObject.optString("city_name"));

                        Picasso.with(getApplicationContext())
                                .load(jsonObject.optString("image").replace(" ","%20"))
                                .fit()
                                // .transform(transformation)
                                .into(profileImage);

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

                    //Log.d("dfsdfsdfsdfs", CropImageActivity.picName);

                    setImage(imagePath);
                    break;
            }
        } else {
            System.out.println("Failed to load image");
        }
    }
    private void setImage(String imagePath) {

        profileImage.setImageBitmap(getImageFromStorage(imagePath));



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




            String path2 = null;
            String filename = null;

            try {
                path2 = f.toString();
                filename = path2.substring(path.lastIndexOf("/") + 1);
                Log.d("dsfdfsdfsfs", filename);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (filename==null){
                Util.errorDialog(ProfilePage.this,"Please Select Image");
            }
            else{
                //Toast.makeText(AddProduct.this, "yes", Toast.LENGTH_SHORT).show();
                PostData( path, filename);
            }


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

        new EditProfileImg(filePath,fileName).execute();

    }


    private class EditProfileImg extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        String val, path, fName, min, kmsDone, mobile, emailID, brand;
        HashMap<String, String> params = new HashMap<>();

        //EditText descreption,ageOfProd,headline,min,kmsDone,mobile,emailID;
        EditProfileImg(String path,String fName) {
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

                jsonObject = uploadImageFile(ProfilePage.this, val,path, fName);


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


//                    image1.setVisibility(View.GONE);
//                    img1.setVisibility(View.VISIBLE);
//                    Toast.makeText(getApplicationContext(), "Profile Change Successfully..", Toast.LENGTH_LONG).show();
                    Util.errorDialog(ProfilePage.this, json.optString("message"));
//                    ViewGalleryApi();


                } else {

//                    Toast.makeText(PostAdd.this, "Error " + json, Toast.LENGTH_LONG).show();
                    Util.errorDialog(ProfilePage.this, json.optString("message"));
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


                    .addFormDataPart("user_id", MyPrefrences.getUserID(getApplicationContext()))
                    .addFormDataPart("name", personName.getText().toString())
                    .addFormDataPart("image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                    .build();

            // Log.d("dfdsgsdgdfgdfh",id);

//            Log.d("fvfgdgdfhgghfhgdfh", amounts.getText().toString().replace("₹ ", ""));
//            Log.d("fvfgdgdfhgdfhqwdfs",amounts.getText().toString().replace("₹ ", ""));

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
//                     .url("http://divpreetsingh.info/app/ManiUploadsImageHere")
                    .header("Authorization", "Client-ID " + "...")
//                    .url("http://bizzcityinfo.com/AndroidApi.php?function=insertGalleryPhoto")
                    .url(Api.EditUserProfile)
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





}
