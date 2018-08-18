package pinerria.business.pinerrianew.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
public class MessageFromAdmin extends Fragment {


    public MessageFromAdmin() {
        // Required empty public constructor
    }

    Dialog dialog;
    GridView grigView2;
    List<HashMap<String,String>> DataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_message_from_admin, container, false);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);

        grigView2=view.findViewById(R.id.grigView);
        DataList = new ArrayList<>();


        HomeAct.title.setText("Message From Admin");
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

                        grigView2.setVisibility(View.VISIBLE);
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
                            grigView2.setAdapter(adapter);
                            DataList.add(map);
                        }
                    }
                    else{
                        grigView2.setVisibility(View.GONE);
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


    class HelpAdapter extends BaseAdapter {
        LayoutInflater inflater;
        TextView id,receiver_id,queryFor,subject,message,send_date,messageTxt;

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
            messageTxt=view.findViewById(R.id.messageTxt);

            send_date.setText("Date: "+DataList.get(i).get("created_date"));
            message.setText("Message: "+DataList.get(i).get("comment"));
            subject.setText("Subject: "+DataList.get(i).get("subject_id"));
            queryFor.setText("Query for: "+DataList.get(i).get("description"));
            //receiver_id.setText("To, "+DataList.get(i).get("receiver_id"));
            id.setText("Enquiry Id: "+DataList.get(i).get("id"));
            messageTxt.setText("Message From Admin");
            return view;
        }
    }


}
