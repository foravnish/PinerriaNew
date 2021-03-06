package pinerria.business.pinerrianew.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pinerria.business.pinerrianew.Activites.AddGSTDetails;
import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.Activites.Login;
import pinerria.business.pinerrianew.Activites.PayActivity;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notifications extends Fragment {


    public Notifications() {
        // Required empty public constructor
    }
    JSONArray jsonArray;
    GridView gridview;

    List<HashMap<String,String>> AllProducts ;
    Dialog dialog;
    HashMap<String,String> map;
    ImageView imageNoListing;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notifications, container, false);

        gridview=(GridView) view.findViewById(R.id.gridview);
        imageNoListing=(ImageView) view.findViewById(R.id.imageNoListing);

        AllProducts = new ArrayList<>();
        HomeAct.title.setText("My Notification");

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);




        if (MyPrefrences.getUserLogin(getActivity())==true) {

            getNotification();


        }
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Please Login to My Notifications")
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
                            dialog.cancel();


                        }
                    });
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Pinerria");
            alert.show();

        }



//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//                Fragment fragment = new TranscationDetails();
//                Bundle bundle=new Bundle();
//                try {
//                    bundle.putString("jsonArray",jsonArray.get(i).toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction ft = manager.beginTransaction();
//                fragment.setArguments(bundle);
//                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//
//
//            }
//        });


        return view;
    }


    private void getNotification() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.userNotificationsList+"/"+ MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("NotificationsList", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        gridview.setVisibility(View.VISIBLE);
                        imageNoListing.setVisibility(View.GONE);
                        jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            map=new HashMap();
                            map.put("id", jsonObject.optString("id"));
                            map.put("message", jsonObject.optString("message"));
                            map.put("created", jsonObject.optString("created"));

                            Adapter adapter=new Adapter();
                            gridview.setAdapter(adapter);
                            AllProducts.add(map);
                        }
                    }
                    else{
                        gridview.setVisibility(View.GONE);
                        imageNoListing.setVisibility(View.VISIBLE);
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


    class Adapter extends BaseAdapter {

        LayoutInflater inflater;
        TextView pName,tracscationNo,expDate,purDate,price,buynow,status,PaidAmount,duration;
        RelativeLayout discountTag;
        LinearLayout qImage2;


        Adapter() {
            inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (inflater == null) {
                throw new AssertionError("LayoutInflater not found.");
            }
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


            convertView=inflater.inflate(R.layout.custonlistview_notification,parent,false);

            pName=(TextView)convertView.findViewById(R.id.pName);
            //tracscationNo=(TextView)convertView.findViewById(R.id.tracscationNo);
            purDate=(TextView)convertView.findViewById(R.id.purDate);
            // expDate=(TextView)convertView.findViewById(R.id.expDate);
            price=(TextView)convertView.findViewById(R.id.price);
            //buynow=(TextView) convertView.findViewById(R.id.buynow);
            //status=(TextView) convertView.findViewById(R.id.status);
            //PaidAmount=(TextView) convertView.findViewById(R.id.PaidAmount);
            duration=(TextView) convertView.findViewById(R.id.duration);


            pName.setText(AllProducts.get(position).get("message"));
           // price.setText("₹ "+AllProducts.get(position).get("actual_value"));

            String year=AllProducts.get(position).get("created").substring(0,4);
            String month=AllProducts.get(position).get("created").substring(5,7);
            String day=AllProducts.get(position).get("created").substring(8,10);
            purDate.setText(day+"-"+month+"-"+year);


            //  expDate.setText("Exp Date: "+AllProducts.get(position).get("expiry_date"));
            // status.setText("Status: "+AllProducts.get(position).get("payment_status"));
            //PaidAmount.setText("Paid Amount ₹ "+AllProducts.get(position).get("paid_amount"));
          //  duration.setText(AllProducts.get(position).get("package_duration")+" Months");
            // tracscationNo.setText("Txn No: "+AllProducts.get(position).get("transaction_id"));


            return convertView;
        }
    }


}
