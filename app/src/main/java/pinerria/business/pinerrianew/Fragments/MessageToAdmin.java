package pinerria.business.pinerrianew.Fragments;


import android.app.Dialog;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageToAdmin extends Fragment {


    public MessageToAdmin() {
        // Required empty public constructor
    }
    Spinner spiner;
    Button sendEnq,sendqu,fromAdmin;
    EditText subject,messages;
    String[] country = { "Log a Query", "Issues", "Suggestion" };
    String queryFor;
    List<HashMap<String,String>> DataList;
    List<HashMap<String,String>> DataListFromAdmin;
    Dialog dialog;
    GridView grigView;
    GridView grigView2;
    HelpAdapter helpAdapter;
   // MessageFrmmAdapter messageFrmmAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_message_to_admin, container, false);
        HomeAct.title.setText("Message to Admin");

        grigView=view.findViewById(R.id.grigView);
        sendqu=view.findViewById(R.id.sendqu);
        fromAdmin=view.findViewById(R.id.fromAdmin);

        DataList = new ArrayList<>();

        sendqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new ComposeMessage();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
            }
        });

        fromAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // DialogFromAdmin();
             //   Util.showPgDialog(dialog);
                Fragment fragment= new MessageFromAdmin();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction ft=fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();

            }
        });


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.myMessageToAdmin+"/"+ MyPrefrences.getUserID(getActivity())+"/Admin", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        grigView.setVisibility(View.VISIBLE);
                        //  imageNoListing.setVisibility(View.GONE);
                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);


                            HashMap map=new HashMap();
                            map.put("id", jsonObject.optString("id"));
                            map.put("recipient", jsonObject.optString("recipient"));
                            map.put("description", jsonObject.optString("description"));
                            map.put("comment", jsonObject.optString("comment"));
                            map.put("created_date", jsonObject.optString("created_date"));



                            HelpAdapter adapter=new HelpAdapter();
                            grigView.setAdapter(adapter);
                            DataList.add(map);
                        }
                    }
                    else{
                        grigView.setVisibility(View.GONE);
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

    private void DialogFromAdmin() {

        final Dialog dialog0 = new Dialog(getActivity());
        //dialog0.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog0.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog0.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog0.setContentView(R.layout.fragment_help_from_admin);
        ImageView back_img = (ImageView) dialog0.findViewById(R.id.back_img);
        grigView2=dialog0.findViewById(R.id.grigView);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.myMessageToAdmin+"/Admin"+"/"+ MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        grigView.setVisibility(View.VISIBLE);
                        //  imageNoListing.setVisibility(View.GONE);
                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);


                            HashMap map=new HashMap();
                            map.put("id", jsonObject.optString("id"));
                            map.put("recipient", jsonObject.optString("recipient"));
                            map.put("description", jsonObject.optString("description"));
                            map.put("comment", jsonObject.optString("comment"));
                            map.put("created_date", jsonObject.optString("created_date"));



                            HelpAdapter adapter=new HelpAdapter();
                            grigView.setAdapter(adapter);
                            DataList.add(map);
                        }
                    }
                    else{
                        grigView.setVisibility(View.GONE);
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


//        sendEnq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (chaeckValidation()){
//                    new HelpDeshApi(subject.getText().toString(),messages.getText().toString(),queryFor).execute();
//                }
//            }
//        });
        TextView title = (TextView) dialog0.findViewById(R.id.title);
        title.setText("Message From Admin");
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog0.cancel();

            }
        });
        dialog0.show();

    }



    private void DialogSendEnq() {

        final Dialog dialog0 = new Dialog(getActivity());
        //dialog0.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog0.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog0.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog0.setContentView(R.layout.fragment_help__send_enquiry);
        ImageView back_img = (ImageView) dialog0.findViewById(R.id.back_img);

        spiner=(Spinner)dialog0.findViewById(R.id.spiner);

        subject=(EditText)dialog0.findViewById(R.id.subject);
        messages=(EditText)dialog0.findViewById(R.id.messages);

        sendEnq=dialog0.findViewById(R.id.sendEnq);


        sendEnq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chaeckValidation()){

                       // new HelpDeshApi(subject.getText().toString(), messages.getText().toString(), queryFor, dialog0).execute();

                }
            }
        });

        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(aa);

        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                queryFor=spiner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        TextView title = (TextView) dialog0.findViewById(R.id.title);
        title.setText("Message To Admin");
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog0.cancel();

            }
        });


        dialog0.show();

    }

    private boolean chaeckValidation() {

        if (TextUtils.isEmpty(subject.getText().toString())){
            subject.setError("oops! subject is blank");
            subject.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(messages.getText().toString())) {
            messages.setError("oops! Message is blank");
            messages.requestFocus();
            return false;
        }
        return true;
    }

    class HelpAdapter extends BaseAdapter {
        LayoutInflater inflater;
        TextView id,receiver_id,queryFor,subject,message,send_date;
        ImageView image;
        HelpAdapter(){
            inflater=(LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return DataList.size();
        }

        @Override
        public Object getItem(int i) {
            return DataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view=inflater.inflate(R.layout.custonlistview_enquiry,viewGroup,false);

            send_date=view.findViewById(R.id.send_date);
            message=view.findViewById(R.id.message);
            subject=view.findViewById(R.id.subject);
            queryFor=view.findViewById(R.id.queryFor);
            receiver_id=view.findViewById(R.id.receiver_id);
            id=view.findViewById(R.id.id);

            send_date.setText("Date: "+DataList.get(i).get("created_date"));
            message.setText("Message: "+DataList.get(i).get("comment"));
            subject.setText("Subject: "+DataList.get(i).get("subject_id"));
            queryFor.setText("Query for: "+DataList.get(i).get("description"));
            //receiver_id.setText("To, "+DataList.get(i).get("receiver_id"));
            id.setText("Enquiry Id: "+DataList.get(i).get("id"));

            return view;
        }
    }

//    class MessageFrmmAdapter extends BaseAdapter {
//        LayoutInflater inflater;
//        TextView id,receiver_id,queryFor,subject,message,send_date;
//        ImageView image;
//        MessageFrmmAdapter(){
//            inflater=(LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//        @Override
//        public int getCount() {
//            return DataListFromAdmin.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return DataListFromAdmin.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//
//            view=inflater.inflate(R.layout.custonlistview_enquiry,viewGroup,false);
//
//            send_date=view.findViewById(R.id.send_date);
//            message=view.findViewById(R.id.message);
//            subject=view.findViewById(R.id.subject);
//            queryFor=view.findViewById(R.id.queryFor);
//            receiver_id=view.findViewById(R.id.receiver_id);
//            id=view.findViewById(R.id.id);
//
//
//            send_date.setText("Date: "+DataListFromAdmin.get(i).get("send_date"));
//            message.setText("Message: "+DataListFromAdmin.get(i).get("message"));
//            subject.setText("Subject: "+DataListFromAdmin.get(i).get("subject"));
//            queryFor.setText("Query for: "+DataListFromAdmin.get(i).get("queryFor"));
//            receiver_id.setText("To, "+DataListFromAdmin.get(i).get("receiver_id"));
//            id.setText("Enquiry Id: "+DataListFromAdmin.get(i).get("id"));
//
//            return view;
//        }
//    }



}
