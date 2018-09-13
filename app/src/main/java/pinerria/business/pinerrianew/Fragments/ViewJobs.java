package pinerria.business.pinerrianew.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.Format;
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
public class ViewJobs extends Fragment {


    public ViewJobs() {
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

    CirclePageIndicator indicator2;
    int currentPage = 0;
    List<Const> AllBaner   = new ArrayList<>();

    ImageView imageNoListing;
    JSONArray jsonArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        MenuItem item=menu.findItem(R.id.action_search);
//        item.setVisible(false);
//    }
//



//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.home1, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
//        MenuItem item=menu.findItem(R.id.action_search);
//        item.setVisible(false);
//    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_jobs, container, false);

        AllProducts = new ArrayList<>();
        AllProductsLocation = new ArrayList<>();
        expListView = (ListView) view.findViewById(R.id.lvExp);
        imageNoListing =  view.findViewById(R.id.imageNoListing);


//        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);
//        fabButton = (FloatingActionButton) view.findViewById(R.id.fab);
        AllProducts = new ArrayList<>();

        HomeAct.title.setText("View Jobs");

        //getTargetFragment().setMenuVisibility(false);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.userAllJob, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        expListView.setVisibility(View.VISIBLE);
                          imageNoListing.setVisibility(View.GONE);
                        jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            map=new HashMap();
                            map.put("id", jsonObject.optString("id"));
                            map.put("user_id", jsonObject.optString("user_id"));
                            map.put("created_date", jsonObject.optString("created_date"));
                            map.put("heading", jsonObject.optString("heading"));
                            map.put("salary", jsonObject.optString("salary"));
                            map.put("full_part_time", jsonObject.optString("full_part_time"));
                            map.put("experience", jsonObject.optString("experience"));
                            map.put("state_name", jsonObject.optString("state_name"));
                            map.put("city_name", jsonObject.optString("city_name"));
                            map.put("zone_name", jsonObject.optString("zone_name"));
                            map.put("user_name", jsonObject.optString("user_name"));
                            map.put("description", jsonObject.optString("description"));


                            Adapter adapter=new Adapter();
                            expListView.setAdapter(adapter);
                            AllProducts.add(map);
                        }
                    }
                    else{
                        expListView.setVisibility(View.GONE);
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


        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Fragment fragment = new DetailForJob();
                    Bundle bundle = new Bundle();
                try {
                    bundle.putString("array",jsonArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    fragment.setArguments(bundle);
                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

            }
        });

        return view;
    }
    class Adapter extends BaseAdapter {

        LayoutInflater inflater;

        TextView full_part_time,salary,heading,date,jobId,contactUs,experience,address,description;

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


            convertView=inflater.inflate(R.layout.all_job_list,parent,false);

            heading=convertView.findViewById(R.id.heading);
            description=convertView.findViewById(R.id.description);
            salary=convertView.findViewById(R.id.salary);
            full_part_time=convertView.findViewById(R.id.full_part_time);
            jobId=convertView.findViewById(R.id.jobId);
            date=convertView.findViewById(R.id.date);
            contactUs=convertView.findViewById(R.id.contactUs);
            experience=convertView.findViewById(R.id.experience);
            address=convertView.findViewById(R.id.address);

            heading.setText(AllProducts.get(position).get("heading"));
            description.setText(AllProducts.get(position).get("description"));

//            salary.setText(AllProducts.get(position).get("salary"));

            NumberFormat   formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            String currency=formatter.format(Double.parseDouble(AllProducts.get(position).get("salary")));

            salary.setText(currency);

            experience.setText(AllProducts.get(position).get("experience"));
            address.setText(AllProducts.get(position).get("city_name"));
//            address.setText(AllProducts.get(position).get("city_name")+", "+AllProducts.get(position).get("state_name")+", "+AllProducts.get(position).get("zone_name"));
            full_part_time.setText(AllProducts.get(position).get("full_part_time"));
            jobId.setText(AllProducts.get(position).get("user_name"));
            String year=AllProducts.get(position).get("created_date").substring(0,4);
            String month=AllProducts.get(position).get("created_date").substring(5,7);
            String day=AllProducts.get(position).get("created_date").substring(8,10);
            date.setText("Posted Date "+day+"-"+month+"-"+year);



//            contactUs.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Fragment fragment = new DetailForJob();
//                    FragmentManager manager = getFragmentManager();
//                    FragmentTransaction ft = manager.beginTransaction();
//                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
//
//
//
//                }
//            });

            return convertView;
        }
    }


}
