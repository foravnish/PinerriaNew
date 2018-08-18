package pinerria.business.pinerrianew.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.Const;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyJobs extends Fragment {


    public MyJobs() {
        // Required empty public constructor
    }

    List<HashMap<String,String>> AllProducts ;
    List<HashMap<String,String>> AllProductsLocation ;
    ListView expListView;
    ListView lvExp;
    HashMap<String,String> map;
    Dialog dialog;
    JSONObject jsonObject1;
    FloatingActionButton fabButton;
    String value="";
    List<String> data=new ArrayList<>();

    ViewPager viewPager,viewPager2;
    SubCategory.CustomPagerAdapter2 mCustomPagerAdapter2;
    CirclePageIndicator indicator2;
    int currentPage = 0;
    List<Const> AllBaner   = new ArrayList<>();

    TextView full_part_time,salary,heading,manajeJobs,address,jobId,date,experience,description;
    SwitchCompat switchJob;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_jobs, container, false);

        AllProducts = new ArrayList<>();
        AllProductsLocation = new ArrayList<>();
//        expListView = (ListView) view.findViewById(R.id.lvExp);
//        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);
//        fabButton = (FloatingActionButton) view.findViewById(R.id.fab);
        AllProducts = new ArrayList<>();

        HomeAct.title.setText("My Jobs");


        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);


        heading=view.findViewById(R.id.heading);
        salary=view.findViewById(R.id.salary);
        full_part_time=view.findViewById(R.id.full_part_time);
        switchJob=view.findViewById(R.id.switchJob);
        manajeJobs=view.findViewById(R.id.manajeJobs);
        address=view.findViewById(R.id.address);
        jobId=view.findViewById(R.id.jobId);
        date=view.findViewById(R.id.date);
        experience=view.findViewById(R.id.experience);
        description=view.findViewById(R.id.description);


        manajeJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddJobs();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment).commit();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

            }
        });



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.userJob+"/"+ MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        //expListView.setVisibility(View.VISIBLE);
                        //  imageNoListing.setVisibility(View.GONE);
                        JSONArray jsonArray=response.getJSONArray("message");

                            final JSONObject jsonObject=jsonArray.getJSONObject(0);


                        heading.setText(jsonObject.optString("heading"));

                        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                        String currency=formatter.format(Double.parseDouble(jsonObject.optString("salary")));

                        salary.setText(currency);

                        //salary.setText("₹ "+jsonObject.optString("salary"));
                        full_part_time.setText(jsonObject.optString("full_part_time"));
                        experience.setText(jsonObject.optString("experience"));
                        description.setText(jsonObject.optString("description"));

                        jobId.setText("ID "+jsonObject.optString("id"));
                        String year=jsonObject.optString("created_date").substring(0,4);
                        String month=jsonObject.optString("created_date").substring(5,7);
                        String day=jsonObject.optString("created_date").substring(8,10);
                        date.setText("Posted Date "+day+"-"+month+"-"+year);


                        // address.setText(AllProducts.get(position).get("city_name")+", "+AllProducts.get(position).get("state_name")+", "+AllProducts.get(position).get("zone_name")+", India");

                        if (jsonObject.optString("user_job_status").equalsIgnoreCase("1")){
                            Log.d("sdfsdfsdfsdfs","true");
                            switchJob.setChecked(true);

                        }
                        else if (jsonObject.optString("user_job_status").equalsIgnoreCase("0")){
                            Log.d("sdfsdfsdfsdfs","false");
                            switchJob.setChecked(false);
                        }

                        switchJob.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                               // Toast.makeText(getActivity(), position+" "+b, Toast.LENGTH_SHORT).show();

                                changeStatus(jsonObject.optString("id"),b);
                            }
                        });


                            map=new HashMap();
                            map.put("id", jsonObject.optString("id"));
                            map.put("created_date", jsonObject.optString("created_date"));
                            map.put("heading", jsonObject.optString("heading"));
                            map.put("salary", jsonObject.optString("salary"));
                            map.put("full_part_time", jsonObject.optString("full_part_time"));
                            map.put("user_job_status", jsonObject.optString("user_job_status"));




//                            Adapter adapter=new Adapter();
//                            expListView.setAdapter(adapter);
//                            AllProducts.add(map);

                    }
                    else{
                       // expListView.setVisibility(View.GONE);
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

    class Adapter extends BaseAdapter {

        LayoutInflater inflater;

        TextView full_part_time,salary,heading;
        SwitchCompat switchJob;

        Adapter() {
            inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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


            convertView=inflater.inflate(R.layout.my_job_list,parent,false);

            heading=convertView.findViewById(R.id.heading);
            salary=convertView.findViewById(R.id.salary);
            full_part_time=convertView.findViewById(R.id.full_part_time);
            switchJob=convertView.findViewById(R.id.switchJob);

            heading.setText(AllProducts.get(position).get("heading"));
            salary.setText("₹ "+AllProducts.get(position).get("salary"));
            full_part_time.setText(AllProducts.get(position).get("full_part_time"));


            if (AllProducts.get(position).get("user_job_status").equalsIgnoreCase("1")){
                Log.d("sdfsdfsdfsdfs","true");
                switchJob.setChecked(true);

            }
            else if (AllProducts.get(position).get("user_job_status").equalsIgnoreCase("0")){
                Log.d("sdfsdfsdfsdfs","false");
                switchJob.setChecked(false);
            }

            switchJob.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    //Toast.makeText(getActivity(), position+" "+b, Toast.LENGTH_SHORT).show();

                    //changeStatus(position,b);
                }
            });
//            Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "muli_semibold.ttf");
//            viewholder.name.setTypeface(face);


            return convertView;
        }
    }

    private void changeStatus(String id ,boolean b) {

        Util.showPgDialog(dialog);
        String val = null;
        if (b==true){
             val="1";


        }
        else if (b==false){
            val="0";
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Api.changeJobStatus+"/"+id+"/"+val, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("statusjob", "Response: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.cancelPgDialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        });
        // Adding request to request queue
        queue.add(strReq);

    }


}
