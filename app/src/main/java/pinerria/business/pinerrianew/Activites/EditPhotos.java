package pinerria.business.pinerrianew.Activites;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.makeramen.roundedimageview.RoundedImageView;
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
import java.util.concurrent.TimeUnit;

import pinerria.business.pinerrianew.CameraAct.CropImageActivity;
import pinerria.business.pinerrianew.CameraAct.ImagePickerActivity;
import pinerria.business.pinerrianew.Fragments.ManageBusiness;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.JSONParser;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

import static android.text.Html.fromHtml;


public class EditPhotos extends AppCompatActivity {

    String filepath1, fileName1 =null;
    ProgressDialog progress;
    TextView profileBack;
    Dialog dialog;

    ImageView image1;
    FloatingActionButton img1;
    ImageView image2,img2;
    ImageView image3,img3;
    ImageView image4,img4;

    GridView gridview;

    String id="";
    private static final int REQUEST_PICK_IMAGE = 1002;
    File f=null;
    List<HashMap<String,String>> AllProducts ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photos);
        img1=findViewById(R.id.img1);
        image1=(ImageView)findViewById(R.id.imageV1);


        gridview=(GridView) findViewById(R.id.gridview);

        AllProducts = new ArrayList<>();

        ViewGalleryApi();

        dialog=new Dialog(EditPhotos.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("fsdfsdfsdf","main");
                if(isPermissionGranted()){
                    Log.d("fsdfsdfdfdfsdf","true");
                    pickImage();
                }else{
                    Log.d("fsdfsdfdfdfsdf","false");
                    ActivityCompat.requestPermissions(EditPhotos.this, new String[]{Manifest.permission.CAMERA}, 1);
                }

            }
        });

        Log.d("dsfsfsdfsdf",MyPrefrences.getUserID(getApplicationContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {



                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditPhotos.this);
                alertDialogBuilder.setMessage("Are you sure, You wanted to Delete ?");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        deleteApi(AllProducts.get(i).get("id"));
                                    }
                                });

                            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


                return false;
            }
        });
    }

    private void deleteApi(String id) {
        Log.d("dfsdsgdgdfgdg",id);

        Util.showPgDialog(dialog);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.deleteGalleryImage+"/"+id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeDelete", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        Toast.makeText(getApplicationContext(), "Photo deleted successfully", Toast.LENGTH_SHORT).show();

                        Intent intent =new Intent(getApplicationContext(),EditPhotos.class);
                        startActivity(intent);
                        finish();

                    }
                    else{

                        Util.errorDialog(EditPhotos.this,response.optString("message"));

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

    private void ViewGalleryApi() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.viewUserBussinessGallery+"/"+MyPrefrences.getUserID(getApplicationContext())+"/1", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        gridview.setVisibility(View.VISIBLE);
                        //  imageNoListing.setVisibility(View.GONE);
                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);


                            HashMap<String,String> map=new HashMap();
                            map.put("id", jsonObject.optString("id"));
                            map.put("image_name", jsonObject.optString("image_name"));
                            map.put("image", jsonObject.optString("image"));
                            map.put("status", jsonObject.optString("status"));
                            map.put("created_date", jsonObject.optString("created_date"));


                            Adapter adapter=new Adapter();
                            gridview.setAdapter(adapter);
                            AllProducts.add(map);
                        }
                    }
                    else{
                        gridview.setVisibility(View.GONE);
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
        image1.setVisibility(View.VISIBLE);
        image1.setImageBitmap(getImageFromStorage(imagePath));



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
                Util.errorDialog(EditPhotos.this,"Please Select Image");
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


        new AddProductData(filePath,fileName).execute();

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

                jsonObject = uploadImageFile(EditPhotos.this, val,path, fName);


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


                    image1.setVisibility(View.GONE);
                    img1.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), "Photo added successfully. Will be visible to others after approval", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(getApplicationContext(),EditPhotos.class));
                    finish();


//                    ViewGalleryApi();

//                    errorDialog(EditPhotos.this,"Photo added successfully. Will be visible to others after approval");



                } else {
//                    Toast.makeText(PostAdd.this, "Error " + json, Toast.LENGTH_LONG).show();
                    Util.errorDialog(EditPhotos.this, json.optString("message")+", Please make space for the new photo");
                }
            }
        }

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
                dialog.dismiss();
                context.startActivity(new Intent(context,EditPhotos.class));
               // context.finish();


            }
        });
        dialog.show();

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
                    .addFormDataPart("bussiness_id", "1")
                    .addFormDataPart("image_name", "s")
                    .addFormDataPart("image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                    .build();

            // Log.d("dfdsgsdgdfgdfh",id);

//            Log.d("fvfgdgdfhgghfhgdfh", amounts.getText().toString().replace("₹ ", ""));
//            Log.d("fvfgdgdfhgdfhqwdfs",amounts.getText().toString().replace("₹ ", ""));

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
//                     .url("http://divpreetsingh.info/app/ManiUploadsImageHere")
                    .header("Authorization", "Client-ID " + "...")
//                    .url("http://bizzcityinfo.com/AndroidApi.php?function=insertGalleryPhoto")
                    .url(Api.addUserBussinessGallery)
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





//    class Adapter extends BaseAdapter {
//        TextView points,require,name,address,postedDate,subCat,leadNo;
//        LayoutInflater inflater;
//        ImageView lock1,lock2,lock3,lock4,lock5;
//        ImageView image1;
//        LinearLayout qImage1;
//        LinearLayout edit,deleteed;
//        Adapter(){
//            inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        @Override
//        public int getCount() {
//            return DataList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return DataList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.custon_list_gallery, parent, false);
//
//            }
//            image1= (ImageView) convertView.findViewById(R.id.image1);
//            deleteed= (LinearLayout) convertView.findViewById(R.id.deleteed);
//            edit= (LinearLayout) convertView.findViewById(R.id.edit);
//
//            Picasso.with(EditPhotos.this).load(DataList.get(position).get("photo")).into(image1);
//
//
////            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                @Override
////                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////
////                    imageOne(1, 2);
////                    id=DataList.get(i).get("id");
////                    Log.d("dfdsgfdgdfghdfhdg",DataList.get(i).get("id"));
////
////
////                }
////            });
//
//
//            edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                    imageOne(1, 2);
//                    id=DataList.get(position).get("id");
//                    Log.d("dfdsgfdgdfghdfhdg",DataList.get(position).get("id"));
//                }
//            });
//
//            deleteed.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(EditPhotos.this);
//                    //Uncomment the below code to Set the message and title from the strings.xml file
//                    //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);
//
//                    //Setting message manually and performing action on button click
//                    builder.setMessage("Do you want to delete Now?")
//
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                    new DeleteGalary(getApplicationContext(),DataList.get(position).get("id")).execute();
//
//                                    dialog.cancel();
//
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    //  Action for 'NO' Button
//                                    dialog.cancel();
//                                }
//                            });
//
//                    //Creating dialog box
//                    AlertDialog alert = builder.create();
//                    //Setting the title manually
//                    alert.setTitle("Gallery");
//                    alert.show();
//
//
//
//
//
//                }
//            });
//
//
//
//
////            imageLoader = AppController.getInstance().getImageLoader();
////
////            name.setText(DataList.get(position).getName().toString());
////            image.setImageUrl(DataList.get(position).getDesc().toLowerCase(),imageLoader);
//
//            return convertView;
//        }
//    }



    class Adapter extends BaseAdapter {

        LayoutInflater inflater;
        TextView status;

        Adapter() {
            inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            if (inflater == null) {
//                throw new AssertionError("LayoutInflater not found.");
//            }
        }

        @Override
        public int getCount() {
            return AllProducts.size();
        }

        @Override
        public Object getItem(int position) {
            return AllProducts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            convertView=inflater.inflate(R.layout.custon_list_gallery,parent,false);


            RoundedImageView galeryImgRoundedImageView=convertView.findViewById(R.id.galeryImg);
            status=convertView.findViewById(R.id.status);

            Picasso.with(getApplicationContext())
                    .load(AllProducts.get(position).get("image"))
                    .fit()
                    // .transform(transformation)
                    .into(galeryImgRoundedImageView);

            if (AllProducts.get(position).get("status").equals("1")){
                status.setText("Approved");
                status.setTextColor(Color.GREEN);
            }
            else if (AllProducts.get(position).get("status").equals("0")){
                status.setText("Pending");
                status.setTextColor(Color.RED);
            }
            else if (AllProducts.get(position).get("status").equals("2")){
                status.setText("Reject");
                status.setTextColor(Color.YELLOW);
            }

            return convertView;
        }
    }





}
