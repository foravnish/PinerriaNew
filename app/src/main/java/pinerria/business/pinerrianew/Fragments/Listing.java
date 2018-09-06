package pinerria.business.pinerrianew.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.makeramen.roundedimageview.RoundedImageView;

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
import pinerria.business.pinerrianew.Activites.Login;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;



/**
 * A simple {@link Fragment} subclass.
 */
public class Listing extends Fragment {


    public Listing() {
        // Required empty public constructor
    }
    List<HashMap<String,String>> AllProducts ;
    List<HashMap<String,String>> AllProductsLocation ;
    GridView expListView;
    ListView lvExp;
    HashMap<String,String> map;
    Dialog dialog;
    JSONObject jsonObject1;
    FloatingActionButton fabButton;
    String value="";
    List<String> data=new ArrayList<>();
    Button bubmit;
    Dialog dialog1;
    Boolean flag=false;
    Viewholder viewholder;
    JSONArray jsonArray;
    JSONArray jsonArrayLocation;
    ImageView imageNoListing;
    TextView ratingWise,locationWise;
    LinearLayout nearMeLayout;

//    ImageView imageNoListing;

    private GoogleApiClient googleApiClient;
    String lat,longi;
    String nearMeValue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_listing, container, false);
        AllProducts = new ArrayList<>();
        AllProductsLocation = new ArrayList<>();
        expListView = (GridView) view.findViewById(R.id.lvExp);
        imageNoListing =  view.findViewById(R.id.imageNoListing);
        ratingWise =  view.findViewById(R.id.ratingWise);
        nearMeLayout =  view.findViewById(R.id.nearMeLayout);
        locationWise =  view.findViewById(R.id.locationWise);
//        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);
//        fabButton = (FloatingActionButton) view.findViewById(R.id.fab);
        AllProducts = new ArrayList<>();

//        AdView adView = (AdView)view. findViewById(R.id.search_ad_view);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

      HomeAct.title.setText(getArguments().getString("subcategory"));

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);


        Log.d("fgdfgdfgdfg",MyPrefrences.getCityID(getActivity()));

        listingDataOfCom("off");

        Log.d("fgsdgsdfgsdfgdfsgdgd", String.valueOf(HomeAct.latitude));
        Log.d("fgsdgsdfgsdfgdfsgdgd", String.valueOf(HomeAct.longitude));

        nearMeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Util.showPgDialog(dialog);
//                listingDataOfCom("off");

                if (String.valueOf(HomeAct.latitude).equals("null")){
                    Log.d("dfgdfgdfgdfghd","sdfdsgd");

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please click on GPS on side menu")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();

                                }
                            });


                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Pinerria");
                    alert.show();


                }
                else{

                    popNearMe();


                }

            }
        });


        ratingWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.showPgDialog(dialog);
                listingDataOfCom("on");
            }
        });
        locationWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popLocation();

            }
        });

        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Fragment fragment=new DetailsPages();
//                Bundle bundle=new Bundle();
//                bundle.putString("id",AllProducts.get(i).get("id"));
//                bundle.putString("subcategory",AllProducts.get(i).get("subcategory"));
//                try {
//                    bundle.putString("jsonArray",jsonArray.get(i).toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                FragmentManager manager=getFragmentManager();
//                FragmentTransaction ft=manager.beginTransaction();
//                fragment.setArguments(bundle);
//                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
//                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();


                Fragment fragment=new Details();
                Bundle bundle=new Bundle();
                //bundle.putString("id",AllProducts.get(i).get("id"));
                //bundle.putString("subcategory",AllProducts.get(i).get("subcategory"));
                try {
                    bundle.putString("jsonArray",jsonArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FragmentManager manager=getFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();

            }
        });

        return view;
    }


    public void listingDataOfCom(final String wiseData) {

        AllProducts.clear();

        if (String.valueOf(HomeAct.latitude).equals("null")){
            Log.d("dfgdfgdfgdfghd","sdfdsgd");
            lat="";
            longi="";
        }
        else{
            lat=String.valueOf(HomeAct.latitude);
            longi=String.valueOf(HomeAct.longitude);
        }


        Log.d("gdfgdfgdfgd1",getArguments().getString("id"));
        Log.d("gdfgdfgdfgd2",MyPrefrences.getUserID(getActivity()));
        Log.d("gdfgdfgdfgd3",MyPrefrences.getCityID(getActivity()));
        Log.d("gdfgdfgdfgd4",getArguments().getString("value"));
        Log.d("gdfgdfgdfgd5",getArguments().getString("nearMe"));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.subCategoryBusiness+"?sort_rating="+wiseData+"&sub_category="+getArguments().getString("id")+"&user_id="+MyPrefrences.getUserID(getActivity())+"&city_id="+MyPrefrences.getCityID(getActivity())+
                "&latitue="+lat+"&longitue="+longi+"&location_id="+getArguments().getString("value")+"&near_me="+getArguments().getString("nearMe"), null, new Response.Listener<JSONObject>() {

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
                        jsonArrayLocation=response.getJSONArray("location");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            map=new HashMap();
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

                            Adapter adapter=new Adapter();
                            expListView.setAdapter(adapter);
                            AllProducts.add(map);
                        }
                    }
                    else{
                        expListView.setVisibility(View.GONE);
                        imageNoListing.setVisibility(View.VISIBLE);
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


    public class Viewholder{
        ImageView imgFav,stars;
        TextView address,name,price,area,keyword,totlaUsers,distance;
        LinearLayout priceLayout;
        MaterialRatingBar rating;
        RoundedImageView imgaeView;

    }
    class Adapter extends BaseAdapter {

        LayoutInflater inflater;


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


            convertView=inflater.inflate(R.layout.list_lsiting,parent,false);

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
//                    bundle.putString("liking",AllProducts.get(position).get("liking"));
//                    bundle.putString("std_code",AllProducts.get(position).get("std_code"));
//                    bundle.putString("pincode",AllProducts.get(position).get("pincode"));
//                    bundle.putString("cat_id",AllProducts.get(position).get("cat_id"));
//                    bundle.putString("companyLogo",AllProducts.get(position).get("companyLogo"));
//
//                    bundle.putString("latitude",AllProducts.get(position).get("latitude"));
//                    bundle.putString("longitude",AllProducts.get(position).get("longitude"));
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
//                    bundle.putString("status",AllProducts.get(position).get("status"));
//                    bundle.putString("jsonArray2",AllProducts.get(position).get("jsonArray2"));
//
//                    bundle.putString("name",AllProducts.get(position).get("c1_fname")+" "+AllProducts.get(position).get("c1_fname")+" "+AllProducts.get(position).get("c1_lname"));
//                    FragmentManager manager=getActivity().getSupportFragmentManager();
//                    FragmentTransaction ft=manager.beginTransaction();
//                    fragment.setArguments(bundle);
//                    ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//
//                }
//            });
//            viewholder.imgFav.setBackgroundResource(R.drawable.fav2);


//            viewholder.callNow1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    try
//                    {
//                        if(Build.VERSION.SDK_INT > 22)
//                        {
//                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                                // TODO: Consider calling
//                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);
//                                return;
//                            }
//                            Intent callIntent = new Intent(Intent.ACTION_CALL);
//                            callIntent.setData(Uri.parse("tel:" + AllProducts.get(position).get("c1_mobile1")));
//                            startActivity(callIntent);
//                        }
//                        else {
//                            Intent callIntent = new Intent(Intent.ACTION_CALL);
//                            callIntent.setData(Uri.parse("tel:" + AllProducts.get(position).get("c1_mobile1")));
//                            startActivity(callIntent);
//                        }
//                    }
//                    catch (Exception ex)
//                    {ex.printStackTrace();
//                    }
//
//                }
//            });

            //  Log.d("fdgdfgdfhgg",AllProducts.get(position).get("liking"));

            try {
                if (AllProducts.get(position).get("my_favourite").equals("No")){
                    viewholder.imgFav.setBackgroundResource(R.drawable.fav1);
    //                viewholder.imgFav.getLayoutParams().height = 50;
    //                viewholder.imgFav.getLayoutParams().width = 0;
                    flag=false;

                }
                else if (AllProducts.get(position).get("my_favourite").equals("Yes")){
                    viewholder.imgFav.setBackgroundResource(R.drawable.fav2);
    //                viewholder.imgFav.getLayoutParams().height = 50;
    //                viewholder.imgFav.getLayoutParams().width = 0;
                    flag=true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//
//            try {
//                if (AllProducts.get(position).get("premium").equalsIgnoreCase("Yes")){
//                    viewholder.stars.setVisibility(View.VISIBLE);
//                    viewholder.cardView.setCardBackgroundColor(Color.parseColor("#FFFDF4BE"));
//                    viewholder.callNow1.setVisibility(View.VISIBLE);
//                }
//                else if (AllProducts.get(position).get("premium").equalsIgnoreCase("No")){
//                    viewholder.stars.setVisibility(View.GONE);
//                    viewholder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
//                    viewholder.callNow1.setVisibility(View.GONE);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            try {
//                if (AllProducts.get(position).get("offer").equalsIgnoreCase("Yes")){
//                    viewholder.linerLayoutOffer.setVisibility(View.VISIBLE);
//
//                }
//                else if (AllProducts.get(position).get("offer").equalsIgnoreCase("No")){
//                    viewholder.linerLayoutOffer.setVisibility(View.GONE);
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            viewholder.imgFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (MyPrefrences.getUserLogin(getActivity())==true) {
                        if (AllProducts.get(position).get("my_favourite").equals("No") || flag == false) {

                            favourateApi("Yes", AllProducts.get(position).get("id"));
                            Log.d("fgdgdfgsdfgsdfg", "true");
                            viewholder.imgFav.setBackgroundResource(R.drawable.fav2);
//                        viewholder.imgFav.getLayoutParams().height = 50;
//                        viewholder.imgFav.getLayoutParams().width = 0;
                            flag = true;
                        } else if (AllProducts.get(position).get("my_favourite").equals("Yes") || flag == true) {

                            favourateApi("No", AllProducts.get(position).get("id"));
                            Log.d("fgdgdfgsdfgsdfg", "false");
                            viewholder.imgFav.setBackgroundResource(R.drawable.fav1);
//                        viewholder.imgFav.getLayoutParams().height = 50;
//                        viewholder.imgFav.getLayoutParams().width = 0;
                            flag = false;
                        }
                    }
                    else{
                        Util.errorDialog(getActivity(),"Please Login First!");
                    }
                }
            });


//            try {
////            viewholder.name.setText(AllProducts.get(position).get("company_name"));
//                viewholder.name.setText(AllProducts.get(position).get("company_name"));
//                viewholder.address.setText(AllProducts.get(position).get("address"));
//                viewholder.totlareview.setText(AllProducts.get(position).get("totlauser")+" Rating");
//                viewholder.area.setText(AllProducts.get(position).get("locationName"));
//
//                viewholder.subcatListing.setText(AllProducts.get(position).get("keywords"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


//            if (AllProducts.get(position).get("id0").equalsIgnoreCase("0")){
//                viewholder.img0.setVisibility(View.VISIBLE);
//            }
//            else{
//                viewholder.img0.setVisibility(View.GONE);
//            }

//            if (String.valueOf(HomeAct.latitude).equals("null")){
//                viewholder.distance.setText("");
//            }
//            else{
//                String str=AllProducts.get(position).get("distance");
//                String strr=str.length() < 4 ? str : str.substring(0, 4);
//                String str1=strr+" Km";
//                viewholder.distance.setText(str1);
//            }



//            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//            viewholder.imgaeView.setImageUrl(AllProducts.get(position).get("logo"),imageLoader);
//
//
//            if (!AllProducts.get(position).get("totlauser").equals("0")) {
//                 viewholder.rating.setRating(Float.parseFloat(AllProducts.get(position).get("rating")));
//            }
//
            Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "muli_semibold.ttf");
            Typeface face2=Typeface.createFromAsset(getActivity().getAssets(), "muli.ttf");
            viewholder.name.setTypeface(face);
//            viewholder.address.setTypeface(face2);
//            viewholder.totlareview.setTypeface(face2);
            viewholder.area.setTypeface(face2);
            viewholder.keyword.setTypeface(face2);
//            viewholder.distance.setTypeface(face2);





            return convertView;
        }
    }

    private void favourateApi(final String stat, final String id ) {
        //Util.showPgDialog(dialog);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.addUserBusinessFavourite,
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

//                                if (stat.equalsIgnoreCase("0")) {
//                                    imgFav.setBackgroundResource(R.drawable.fav2);
//                                    imgFav.getLayoutParams().height = 50;
//                                    imgFav.getLayoutParams().width = 0;
//                                    flag = false;
//                                }
//
//                                else if (stat.equalsIgnoreCase("1")) {
//                                    imgFav.setBackgroundResource(R.drawable.fav1);
//                                    imgFav.getLayoutParams().height = 50;
//                                    imgFav.getLayoutParams().width = 0;
//                                    flag = true;
//                                }
                              //  Toast.makeText(getActivity(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();

                            }
                            else{

                                if (jsonObject.getString("message").equalsIgnoreCase("please enter own id")){
                                    Toast.makeText(getActivity(), "Please Login", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                                }
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
                params.put("user_id", MyPrefrences.getUserID(getActivity()));
                params.put("bussiness_id", id.toString());
                params.put("favourite_yes_no",stat.toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(postRequest);
    }


//    class AdapterLocation extends BaseAdapter {
//
//        LayoutInflater inflater;
//        CheckBox textviwe;
//
//        Boolean flag=false;
//        AdapterLocation() {
//            inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
////            if (inflater == null) {
////                throw new AssertionError("LayoutInflater not found.");
////            }
//        }
//
//        @Override
//        public int getCount() {
//            return AllProductsLocation.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return AllProductsLocation.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//
//
//            convertView=inflater.inflate(R.layout.list_location,parent,false);
//
//            textviwe=convertView.findViewById(R.id.textviwe);
//            textviwe.setText(AllProductsLocation.get(position).get("location"));
//
//            data.clear();
//            textviwe.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Log.d("sdfsdfsdfgsgs",AllProductsLocation.get(position).get("id"));
//                    data.add(AllProductsLocation.get(position).get("id"));
//                }
//            });
//
//            bubmit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    value=data.toString().replace("[","").replace("]","").replace(" ","");
//                    Log.d("fgdgdfgdfgdfgdfg",value.toString());
//                    Log.d("fgdgdfdfdfgdfgdfgdfg",getArguments().getString("fragmentKey"));
//                   // listingDataOfCom(getArguments().getString("value"));
//                    dialog1.dismiss();
//
//                    Fragment fragment=new ListedPage();
//                    FragmentManager manager=getActivity().getSupportFragmentManager();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id",getArguments().getString("fragmentKey"));
//                    bundle.putString("title",getArguments().getString("title"));
//                    bundle.putString("search","no");
//                    bundle.putString("keyowd","");
//                    bundle.putString("value",value.toString());
//                    FragmentTransaction ft=manager.beginTransaction();
//                    fragment.setArguments(bundle);
//                    ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//
//                  //  data.clear();
//
//
//                }
//            });
//
//            return convertView;
//        }
//    }
//private synchronized void setUpGClient() {
//    try {
//        googleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(), 0, this.getActivity())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        googleApiClient.connect();
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}



    private void popLocation() {


        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
       // Util.showPgDialog(dialog);


        dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.alertdialogcustom3);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //TextView text = (TextView) dialog.findViewById(R.id.msg_txv);

        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);

        AllProducts = new ArrayList<>();
        lvExp = (ListView) dialog1.findViewById(R.id.lvExp);
        bubmit = (Button) footerView.findViewById(R.id.bubmit);





        lvExp.addFooterView(footerView);

        //text.setText(fromHtml(message));
//        Button ok = (Button) dialog1.findViewById(R.id.btn_ok);
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog1.dismiss();
//            }
//        });


        Log.d("fgdgdfgdfgdfg", String.valueOf(jsonArrayLocation));
        dialog1.show();
        AllProductsLocation.clear();
        for (int i=0;i<jsonArrayLocation.length();i++){
            JSONObject jsonObject= null;
            try {
                jsonObject = jsonArrayLocation.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HashMap<String,String> map=new HashMap();
            map.put("location_id",jsonObject.optString("location_id"));
            map.put("location",jsonObject.optString("location_name"));


            AdapterLocation  adapter=new AdapterLocation ();
            lvExp.setAdapter(adapter);
            AllProductsLocation.add(map);

        }



//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                "http://bizzcityinfo.com/Api/index.php/main/catIdToLocation"+"?cityId="+ "1"+"&catId="+"239", null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("Respose", response.toString());
//                dialog1.show();
//                Util.cancelPgDialog(dialog);
//                try {
//                    // Parsing json object response
//                    // response will be a json object
////                    String name = response.getString("name");
//
//                    if (response.getString("status").equalsIgnoreCase("success")){
//
//                        JSONArray jsonArray=response.getJSONArray("message");
//                        AllProductsLocation.clear();
//                        for (int i=0;i<jsonArray.length();i++){
//                            JSONObject jsonObject=jsonArray.getJSONObject(i);
//
//                            HashMap<String,String> map=new HashMap();
//                            map.put("id",jsonObject.optString("id"));
////                            map.put("state_id",jsonObject.optString("state_id"));
////                            map.put("city_id",jsonObject.optString("city_id"));
//                            map.put("location",jsonObject.optString("location"));
//
//
//                            AdapterLocation  adapter=new AdapterLocation ();
//                            lvExp.setAdapter(adapter);
//                            AllProductsLocation.add(map);
//
//                        }
//
//                    }
//                    else {
//                        Toast.makeText(getActivity(), "No Area Here...", Toast.LENGTH_LONG).show();
//                    }
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                    Util.cancelPgDialog(dialog);
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Respose", "Error: " + error.getMessage());
//                Toast.makeText(getActivity(),
//                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//                Util.cancelPgDialog(dialog);
//
//            }
//        });
//
//        // Adding request to request queue
//        jsonObjReq.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(jsonObjReq);



    }



    private void popNearMe() {

        dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.alertdialog_near_me);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.show();




        final RadioButton radio1 = (RadioButton) dialog1.findViewById(R.id.radio1);
        final RadioButton radio2 = (RadioButton) dialog1.findViewById(R.id.radio2);
        final RadioButton radio3 = (RadioButton) dialog1.findViewById(R.id.radio3);
        final RadioButton radio4 = (RadioButton) dialog1.findViewById(R.id.radio4);

        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nearMeValue="1";
                dialog1.dismiss();


                openFragmentForListing(nearMeValue);
            }
        });
        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nearMeValue="3";
                dialog1.dismiss();
                openFragmentForListing(nearMeValue);
            }
        });


        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nearMeValue="5";
                dialog1.dismiss();
                openFragmentForListing(nearMeValue);
            }
        });

        radio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nearMeValue="all";
                dialog1.dismiss();
                openFragmentForListing(nearMeValue);
            }
        });


    }

    private void openFragmentForListing(String nearMeValue) {

        Fragment fragment=new Listing();
        FragmentManager manager=getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("id", getArguments().getString("id"));
        bundle.putString("subcategory", getArguments().getString("subcategory"));
        bundle.putString("value",value.toString());
        bundle.putString("nearMe",nearMeValue.toString());

        FragmentTransaction ft=manager.beginTransaction();
        fragment.setArguments(bundle);
        ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();

    }


    class AdapterLocation extends BaseAdapter {

        LayoutInflater inflater;
        CheckBox textviwe;

        Boolean flag=false;
        AdapterLocation() {
            inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            if (inflater == null) {
//                throw new AssertionError("LayoutInflater not found.");
//            }
        }

        @Override
        public int getCount() {
            return AllProductsLocation.size();
        }

        @Override
        public Object getItem(int position) {
            return AllProductsLocation.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            convertView=inflater.inflate(R.layout.list_location,parent,false);

            textviwe=convertView.findViewById(R.id.textviwe);
            textviwe.setText(AllProductsLocation.get(position).get("location"));

            data.clear();
            textviwe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("sdfsdfsdfgsgs",AllProductsLocation.get(position).get("location_id"));
                    data.add(AllProductsLocation.get(position).get("location_id"));
                }
            });

            bubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    value=data.toString().replace("[","").replace("]","").replace(" ","");
                    Log.d("fgdgdfgdfgdfgdfg",value.toString());
                   // Log.d("fgdgdfdfdfgdfgdfgdfg",getArguments().getString("fragmentKey"));
                    // listingDataOfCom(getArguments().getString("value"));
                    dialog1.dismiss();

                    Fragment fragment=new Listing();
                    FragmentManager manager=getActivity().getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", getArguments().getString("id"));
                    bundle.putString("subcategory", getArguments().getString("subcategory"));
                    bundle.putString("value",value.toString());
                    bundle.putString("nearMe","");
                    FragmentTransaction ft=manager.beginTransaction();
                    fragment.setArguments(bundle);
                    ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();

                    //  data.clear();


                }
            });

            return convertView;
        }
    }



}
