package pinerria.business.pinerrianew.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pinerria.business.pinerrianew.Activites.AddProduct;
import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.Activites.Login;
import pinerria.business.pinerrianew.Activites.Splash;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

import static android.text.Html.fromHtml;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddJobs extends Fragment {


    public AddJobs() {
        // Required empty public constructor
    }

    ImageView regiImage;
    Button submitJob;
    EditText rePass,password,salary,heading,description,address;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView forLogin,uploadImage;

    private static final int REQUEST_PICK_IMAGE = 1002;
    Bitmap imageBitmap;
    File f=null;

    Dialog dialog;
    Spinner typeJobs,experience;
    String DataList[]={"Full Time","Part Time"};
    String val,ValExp;
    JSONObject jsonObject;
    String [] ExperienceData ={"1 Year","2 Year","3 Year","4 Year","5 Year","6 Year","7 Year","8 Year","9 Year","10 Year"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_jobs, container, false);

        heading= view.findViewById(R.id.heading);
        description= view.findViewById(R.id.description);
        salary=view.findViewById(R.id.salary);
        submitJob=view.findViewById(R.id.submitJob);
        typeJobs=view.findViewById(R.id.typeJobs);
        experience=view.findViewById(R.id.experience);
        address=view.findViewById(R.id.address);


        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);


        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(
                Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);


        ArrayAdapter aa = new ArrayAdapter(getActivity(),R.layout.simple_spinner_item,DataList);
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        typeJobs.setAdapter(aa);


        ArrayAdapter aaExp = new ArrayAdapter(getActivity(),R.layout.simple_spinner_item,ExperienceData);
        aaExp.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        experience.setAdapter(aaExp);


        if (Home.jobs==true){
            Log.d("fsddgdgd","true");
            HomeAct.title.setText("Edit Jobs");
            getFilledJobData();
        }
        else if (Home.jobs==false){
            Log.d("fsddgdgd","flase");
            HomeAct.title.setText("Add Jobs");
        }

        if (MyPrefrences.getUserLogin(getActivity())==true) {

        }
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Please Login to Add Job")
                    .setCancelable(false)
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            startActivity(new Intent(getActivity(),Login.class));
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            //dialog.cancel();
                            Fragment fragment = new Home();
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction ft = manager.beginTransaction();
                            ft.replace(R.id.content_frame, fragment).commit();
                            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);



                        }
                    });
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Pinerria");
            alert.show();

        }


        typeJobs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 val=typeJobs.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        experience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ValExp=experience.getSelectedItem().toString().substring(0,1);
                Log.d("fdsdsdsgdgdfg",ValExp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submitJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(getActivity(),  val, Toast.LENGTH_SHORT).show();

                if(validate()){
                    if (Home.jobs==true){
                        Log.d("fsddgdgfgfgdfgdfd","true");

                        submitJobsEdit(jsonObject.optString("id"),val,ValExp);
                    }
                    else if (Home.jobs==false){
                        Log.d("fsddgdgfgfgdfgdfd","flase");
                        submitJobs(val,ValExp);
                    }
                }

            }
        });


        return view;

    }

    private void submitJobsEdit(final String id, final String value, final String valExp) {

        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.editUserJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("EditUserJob", "Response: " + response);

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.optString("status").equalsIgnoreCase("success")){

//                        Toast.makeText(getActivity(), ""+jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        errorDialog(getActivity(),jsonObject.optString("message"));



                    }
                    else{
                        Util.errorDialog(getActivity(),jsonObject.optString("message"));
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
                Toast.makeText(getActivity(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("job_id", id);
                params.put("heading", heading.getText().toString());
                params.put("salary", salary.getText().toString());
                params.put("full_part_time", value);
                params.put("experience", valExp);
                params.put("description", description.getText().toString());
                params.put("address", address.getText().toString());

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

    private void getFilledJobData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.userJob+"/"+MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        JSONArray jsonArray=response.getJSONArray("message");

                        jsonObject=jsonArray.getJSONObject(0);


                        heading.setText(jsonObject.optString("heading"));
                        salary.setText(jsonObject.optString("salary"));
                        description.setText(jsonObject.optString("description"));
                        address.setText(jsonObject.optString("address"));

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


    private void submitJobs(final String value, final String valExp) {

        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.addUserJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("addUserJob", "Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.optString("status").equalsIgnoreCase("success")){
                        errorDialog(getActivity(),jsonObject.optString("message"));
//                        Toast.makeText(getActivity(), ""+jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
//
//                        Fragment fragment = new MyJobs();
//                        FragmentManager manager = getFragmentManager();
//                        FragmentTransaction ft = manager.beginTransaction();
//                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//                        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

                    }
                    else{
                        Util.errorDialog(getActivity(),jsonObject.optString("message"));
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
                Toast.makeText(getActivity(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", MyPrefrences.getUserID(getActivity()));
                params.put("heading", heading.getText().toString());
                params.put("salary", salary.getText().toString());
                params.put("full_part_time", value);
                params.put("experience", valExp);
                params.put("description", description.getText().toString());
                params.put("address", address.getText().toString());

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

        if (TextUtils.isEmpty(heading.getText().toString()))
        {
            Util.errorDialog(getActivity(),"Enter Job Title.");
            return false;
        }
        else if (TextUtils.isEmpty(description.getText().toString()))
        {
            Util.errorDialog(getActivity(),"Enter Description");
            return false;
        }
        else if (TextUtils.isEmpty(salary.getText().toString()))
        {
            Util.errorDialog(getActivity(),"Enter Salary");
            return false;
        }
        else if (TextUtils.isEmpty(address.getText().toString()))
        {
            Util.errorDialog(getActivity(),"Enter Address");
            return false;
        }



        return true;

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

                Intent intent=new Intent(context,HomeAct.class);
                intent.putExtra("userType","jobs");
                context.startActivity(intent);

//                Fragment fragment = new MyJobs();
//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction ft = manager.beginTransaction();
//                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

            }
        });
        dialog.show();

    }

}
