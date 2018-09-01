package pinerria.business.pinerrianew.Fragments;


import android.app.Dialog;
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
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ComposeMessage extends Fragment {


    public ComposeMessage() {
        // Required empty public constructor
    }
//    String[] country = { "Log a Query", "Issues", "Suggestion" };
    List<String> country =new ArrayList<>();

    Button sendEnq;
    EditText messages,subject;
    Dialog dialog;
    Spinner spiner;
    String queryFor;
    List<HashMap<String,String>> DataDropDown;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_compose_message, container, false);

        subject=view.findViewById(R.id.subject);
        messages=view.findViewById(R.id.messages);
        sendEnq=view.findViewById(R.id.sendEnq);
        spiner=view.findViewById(R.id.spiner);

        DataDropDown=new ArrayList<>();

        getDropDowData();

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        HomeAct.title.setText("Compose Message to Admin");

        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!DataDropDown.get(i).get("id").equals("")) {
                    queryFor = DataDropDown.get(i).get("id");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        sendEnq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chaeckValidation()){

                    submitDataMessage();
                }
            }
        });

        return view;
    }

    private void getDropDowData() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.messageSubjectDropdown, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposecategoryCity", response.toString());
                Util.cancelPgDialog(dialog);
                try {

                    if (response.getString("status").equalsIgnoreCase("success")){

                        country.clear();
                        DataDropDown.clear();

//                        HashMap<String, String> map2 = new HashMap<>();
//                        map2.put("id", "");
//                        AllCity.add(map2);
//                        cityList.add("Select City");


                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("name", jsonObject.optString("name"));

                            country.add(jsonObject.optString("name"));

                            ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,country);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner.setAdapter(aa);

                            DataDropDown.add(map);



                        }
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

    private void submitDataMessage() {


        Util.showPgDialog(dialog);


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.messageToAdmin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("MessageToAdminResponse", "Login Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

                        Fragment fragment = new MessageToAdmin();
                        FragmentManager manager = getFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);


                    }
                    else{
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                        Util.errorDialog(getActivity(),jsonObject.getString("message"));
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
                params.put("recipient_id", "Admin");
                params.put("user_id", MyPrefrences.getUserID(getActivity()));
                params.put("subject_id", queryFor);
                params.put("description", subject.getText().toString());
                params.put("comment", messages.getText().toString());

                Log.d("fdsfdsgfdgdfg",queryFor);

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

    private boolean chaeckValidation() {

        if (TextUtils.isEmpty(subject.getText().toString())){
            Util.errorDialog(getActivity(),"Required Subject ");
            return false;
        }
        else if (TextUtils.isEmpty(messages.getText().toString())) {
            Util.errorDialog(getActivity(),"Required Message ");
            return false;
        }
        return true;
    }



}
