package pinerria.business.pinerrianew.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFavourate extends Fragment {


    public MyFavourate() {
        // Required empty public constructor
    }
    List<HashMap<String,String>> AllProducts ;
    GridView expListView;
    HashMap<String,String> map;
    Dialog dialog;
    JSONObject jsonObject1;
    ImageView imageNoListing;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_favourate, container, false);

        AllProducts = new ArrayList<>();
        expListView = (GridView) view.findViewById(R.id.lvExp);
        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);

        HomeAct.title.setText("My Favourites");

//        if (MyPrefrences.getUserLogin(getActivity())==false){
//            // Toast.makeText(getActivity(), "login", Toast.LENGTH_SHORT).show();
//            Fragment fragment = new LoginNow();
//            FragmentManager manager = getActivity().getSupportFragmentManager();
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//        }


        Log.d("dsfsdfsdfsdgfsd", MyPrefrences.getUserID(getActivity()));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.favoriteList+"?myId="+ MyPrefrences.getUserID(getActivity())+"&cityId="+MyPrefrences.getCityID(getActivity())
                , null, new Response.Listener<JSONObject>() {

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

                        JSONArray jsonArray0=response.getJSONArray("message");
                        for (int i=0;i<jsonArray0.length();i++) {
                            JSONObject jsonObject = jsonArray0.getJSONObject(i);


                                map = new HashMap();
                                map.put("id", jsonObject.optString("id"));
                                map.put("bussiness_name", jsonObject.optString("bussiness_name"));
                                map.put("city_name", jsonObject.optString("city_name"));
                                map.put("service_keyword", jsonObject.optString("service_keyword"));
                                map.put("image", jsonObject.optString("image"));
                                map.put("total_rating_user", jsonObject.optString("total_rating_user"));
                                map.put("total_rating", jsonObject.optString("total_rating"));
                                map.put("my_favourite", jsonObject.optString("my_favourite"));
                                map.put("min_price", jsonObject.optString("min_price"));
                                map.put("max_price", jsonObject.optString("max_price"));





                                Adapter adapter = new Adapter();
                                expListView.setAdapter(adapter);
                                AllProducts.add(map);
                        }
                    }
                    else if (response.getString("status").equalsIgnoreCase("failure")){



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


//        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//                Fragment fragment=new ListingDetails();
//                Bundle bundle=new Bundle();
//                bundle.putString("company_name",AllProducts.get(i).get("company_name"));
//                bundle.putString("address",AllProducts.get(i).get("address"));
//                bundle.putString("c1_mobile1",AllProducts.get(i).get("c1_mobile1"));
//                bundle.putString("name",AllProducts.get(i).get("c1_fname")+" "+AllProducts.get(i).get("c1_fname")+" "+AllProducts.get(i).get("c1_lname"));
//                FragmentManager manager=getActivity().getSupportFragmentManager();
//                FragmentTransaction ft=manager.beginTransaction();
//                fragment.setArguments(bundle);
//                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//            }
//        });



        return view;
    }


    public class Viewholder{
        ImageView imgFav,stars;
        TextView address,name,totlareview,subcatListing,distance;
        LinearLayout liner,linerLayoutOffer;
        MaterialRatingBar rating;
        CardView cardView;
        ImageView callNow1;
        NetworkImageView imgaeView;
        ImageView img1,img2,img3,img4,img5;
        LinearLayout footer_layout;
        TextView price,area,keyword,totlaUsers;
        LinearLayout priceLayout;
    }
    class Adapter extends BaseAdapter {

        LayoutInflater inflater;

        Boolean flag = true;

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


            convertView = inflater.inflate(R.layout.list_lsiting, parent, false);

            final Viewholder viewholder=new Viewholder();

            viewholder.name=convertView.findViewById(R.id.name);
//
            viewholder.imgFav=convertView.findViewById(R.id.imgFav);
//            viewholder.liner=convertView.findViewById(R.id.liner);
//            viewholder.totlareview=convertView.findViewById(R.id.totlareview);
            viewholder.rating=convertView.findViewById(R.id.rating);
            viewholder.area=convertView.findViewById(R.id.area);
//            viewholder.distance=convertView.findViewById(R.id.distance);
            viewholder.imgaeView=convertView.findViewById(R.id.imgaeView);
            viewholder.totlaUsers=convertView.findViewById(R.id.totlaUsers);
            viewholder.price=convertView.findViewById(R.id.price);
//
            viewholder.keyword=convertView.findViewById(R.id.keyword);
            viewholder.distance=convertView.findViewById(R.id.distance);
            viewholder.priceLayout=convertView.findViewById(R.id.priceLayout);

            viewholder.name.setText(AllProducts.get(position).get("bussiness_name"));
            viewholder.area.setText(AllProducts.get(position).get("city_name"));
            viewholder.keyword.setText(AllProducts.get(position).get("service_keyword"));
            viewholder.totlaUsers.setText(" ("+AllProducts.get(position).get("total_rating_user")+" Review's)");
            if (!AllProducts.get(position).get("total_rating").equals("")) {
                viewholder.rating.setRating(Float.parseFloat(AllProducts.get(position).get("total_rating")));
            }

            if (AllProducts.get(position).get("min_price").equals("")){
                viewholder.priceLayout.setVisibility(View.GONE);
            }
            else{
                viewholder.priceLayout.setVisibility(View.VISIBLE);
                viewholder.price.setText("Price ₹ "+AllProducts.get(position).get("min_price"));

                if (!AllProducts.get(position).get("max_price").equals("")){
                    viewholder.price.setText("Price ₹ "+AllProducts.get(position).get("min_price")+"-"+AllProducts.get(position).get("max_price"));
                }

            }



            Picasso.with(getActivity())
                    .load(AllProducts.get(position).get("image").replace(" ","%20"))
                    .fit()
                    // .transform(transformation)
                    .into(viewholder.imgaeView);

//            viewholder.liner.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Toast.makeText(getActivity(), "yes", Toast.LENGTH_SHORT).show();
//
//                    Fragment fragment=new ListingTabDetails();
//                    Bundle bundle=new Bundle();
//                    bundle.putString("id",AllProducts.get(position).get("id"));
//                    bundle.putString("company_name",AllProducts.get(position).get("company_name"));
//                    bundle.putString("address",AllProducts.get(position).get("address"));
//                    bundle.putString("c1_mobile1",AllProducts.get(position).get("c1_mobile1"));
//                    bundle.putString("rating",AllProducts.get(position).get("rating"));
//                    bundle.putString("totlauser",AllProducts.get(position).get("totlauser"));
//                    bundle.putString("locationName",AllProducts.get(position).get("locationName"));
//                    bundle.putString("keywords",AllProducts.get(position).get("keywords"));
//                    bundle.putString("liking","0");
//                    bundle.putString("std_code",AllProducts.get(position).get("std_code"));
//                    bundle.putString("pincode",AllProducts.get(position).get("pincode"));
//                    bundle.putString("companyLogo",AllProducts.get(position).get("companyLogo"));
//
//                    bundle.putString("latitude",AllProducts.get(position).get("latitude"));
//                    bundle.putString("longitude",AllProducts.get(position).get("longitude"));
//
//                    bundle.putString("status",AllProducts.get(position).get("status"));
//                    bundle.putString("jsonArray2",AllProducts.get(position).get("jsonArray2"));
//
//                    bundle.putString("payment_mode",AllProducts.get(position).get("payment_mode"));
//                    bundle.putString("closing_time",AllProducts.get(position).get("closing_time"));
//                    bundle.putString("closing_time2",AllProducts.get(position).get("closing_time2"));
//                    bundle.putString("opening_time",AllProducts.get(position).get("opening_time"));
//                    bundle.putString("opening_time2",AllProducts.get(position).get("opening_time2"));
//                    bundle.putString("min_order_amnt",AllProducts.get(position).get("min_order_amnt"));
//                    bundle.putString("min_order_qty",AllProducts.get(position).get("min_order_qty"));
//                    bundle.putString("closing_days",AllProducts.get(position).get("closing_days"));
//
//
//
//                    bundle.putString("name",AllProducts.get(position).get("c1_fname")+" "+AllProducts.get(position).get("c1_fname")+" "+AllProducts.get(position).get("c1_lname"));
//                    FragmentManager manager=getActivity().getSupportFragmentManager();
//                    FragmentTransaction ft=manager.beginTransaction();
//                    fragment.setArguments(bundle);
//                    ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//
//                }
//            });

          //viewholder.imgFav.setBackgroundResource(R.drawable.fav2);




//            viewholder.imgFav.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if ( flag==true ){
//                        favourateApi("0",AllProducts.get(position).get("id"));
//                        viewholder.imgFav.setBackgroundResource(R.drawable.fav1);
////                        viewholder.imgFav.getLayoutParams().height = 50;
////                        viewholder.imgFav.getLayoutParams().width = 0;
//                        flag=false;
//
//
//                        Log.d("fgdgdfgsdfgsdfg","true");
//                    }
//                    else if ( flag==false) {
//                        favourateApi("1",AllProducts.get(position).get("id"));
//                        viewholder.imgFav.setBackgroundResource(R.drawable.fav2);
////                        viewholder.imgFav.getLayoutParams().height = 50;
////                        viewholder.imgFav.getLayoutParams().width = 0;
//                        flag=true;
//
//                        Log.d("fgdgdfgsdfgsdfg","false");
//                    }
//                }
//            });

//            viewholder.name.setText(AllProducts.get(position).get("company_name"));
            viewholder.name.setText(AllProducts.get(position).get("company_name"));
            viewholder.address.setText(AllProducts.get(position).get("address"));
            viewholder.totlareview.setText(AllProducts.get(position).get("totlauser") + " Reviews");
//
            viewholder.subcatListing.setText(AllProducts.get(position).get("keywords"));

            viewholder.distance.setVisibility(View.GONE);





            if (!AllProducts.get(position).get("totlauser").equals("0")) {
                viewholder.rating.setRating(Float.parseFloat(AllProducts.get(position).get("rating")));
            }

            Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "muli_semibold.ttf");
            Typeface face2= Typeface.createFromAsset(getActivity().getAssets(), "muli.ttf");
            viewholder.name.setTypeface(face);
            viewholder.address.setTypeface(face2);
            viewholder.totlareview.setTypeface(face2);
            viewholder.subcatListing.setTypeface(face2);
            viewholder.distance.setTypeface(face2);



            return convertView;
        }
    }


    private void favourateApi(final String stat, final String id) {
        Util.showPgDialog(dialog);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.favorite,
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

                               // Toast.makeText(getActivity(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(getActivity(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                        Util.cancelPgDialog(dialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("myId", MyPrefrences.getUserID(getActivity()));
                params.put("postId", id.toString());
                params.put("favoriteStatus",stat.toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(postRequest);
    }


}



