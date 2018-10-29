package pinerria.business.pinerrianew.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.Const;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;
import pinerria.business.pinerrianew.Activites.WebViewOpen;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategory extends Fragment {


    public SubCategory() {
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
    CustomPagerAdapter2 mCustomPagerAdapter2;
    CirclePageIndicator indicator2;
    int currentPage = 0;
    List<Const> AllBaner   = new ArrayList<>();




    int position;
    private static int NUM_PAGES=0;
    private Handler handler=new Handler();
    private Runnable runnale=new Runnable(){
        public void run(){
            viewPager2.setCurrentItem(position,true);
            if(position>=NUM_PAGES ) position=0;
            else position++;
            // Move to the next page after 10s
            handler.postDelayed(runnale, 3000);
        }
    };




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sub_category, container, false);

        AllProducts = new ArrayList<>();
        AllProductsLocation = new ArrayList<>();
        expListView = (ListView) view.findViewById(R.id.lvExp);
//        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);
//        fabButton = (FloatingActionButton) view.findViewById(R.id.fab);
        AllProducts = new ArrayList<>();

        position=0;
        HomeAct.title.setText(getArguments().getString("category"));
//        AdView adView = (AdView)view. findViewById(R.id.search_ad_view);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

        //  HomeAct.linerFilter.setVisibility(View.VISIBLE);

        Log.d("fdsdfsdgfsdgs",getArguments().getString("id"));

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);

//        View headerView = ((LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header, null, false);
//        expListView.addHeaderView(headerView);

        viewPager2 = (ViewPager) view.findViewById(R.id.slider2);
        indicator2 = (CirclePageIndicator)view.findViewById(R.id.indicat2);

        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment=new Listing();
            //    i=i-1;
                Bundle bundle=new Bundle();
                bundle.putString("id",AllProducts.get(i).get("id"));
                bundle.putString("subcategory",AllProducts.get(i).get("subcategory"));
                FragmentManager manager=getActivity().getSupportFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();

            }
        });


        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET,
                Api.banner+"/"+ MyPrefrences.getCityID(getActivity())+"/" +getArguments().getString("id"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeBaner", response.toString());
                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");
                    HashMap<String,String> hashMap = null;
                    if (response.getString("status").equalsIgnoreCase("success")){
                        AllBaner.clear();
                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            NUM_PAGES=jsonArray.length();
//                            if (jsonObject.optString("cat_id").equalsIgnoreCase("Home")) {
//                                AllBaner.add(new Const(jsonObject.optString("id"), jsonObject.optString("cat_id"), jsonObject.optString("subcategory"), jsonObject.optString("image"), jsonObject.optString("meta_keywords"), jsonObject.optString("meta_description"), jsonObject.optString("meta_title"),null,null,null));
//                            }
                            AllBaner.add(new Const(jsonObject.optString("id"), jsonObject.optString("title"), jsonObject.optString("url"), jsonObject.optString("image"), null, null, null,null,null,null));


                            mCustomPagerAdapter2=new CustomPagerAdapter2(getActivity());
                            viewPager2.setAdapter(mCustomPagerAdapter2);
                            indicator2.setViewPager(viewPager2);
                            // indicator2.setViewPager(viewPager2);
                            mCustomPagerAdapter2.notifyDataSetChanged();



                        }
//                        final Handler handler = new Handler();
//
//                        final Runnable update = new Runnable() {
//
//                            public void run() {
//                                if (currentPage == AllBaner.size()) {
//                                    currentPage = 0;
//                                }
//                                viewPager2.setCurrentItem(currentPage++);
//                            }
//                        };
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                handler.post(update);
//                            }
//                        }, 100, 5000);


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

        jsonObjReq2.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq2);

        ///// End baner APi



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.subCatByCatId+"/"+getArguments().getString("id"), null, new Response.Listener<JSONObject>() {

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
                        //  imageNoListing.setVisibility(View.GONE);
                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);


                            map=new HashMap();
                            map.put("id", jsonObject.optString("id"));
                            map.put("category_id", jsonObject.optString("category_id"));
                            map.put("subcategory", jsonObject.optString("subcategory"));
                            map.put("image", jsonObject.optString("image"));
                            map.put("meta_tag", jsonObject.optString("meta_tag"));


                            Adapter adapter=new Adapter();
                            expListView.setAdapter(adapter);
                            AllProducts.add(map);
                        }
                    }
                    else{
                        expListView.setVisibility(View.GONE);
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



    public class Viewholder{
        ImageView imgFav,stars;
        TextView address,name,subCat1,area,subcatListing,distance;
        ImageView callNow1;
        LinearLayout liner,linerLayoutOffer;
        MaterialRatingBar rating;
        NetworkImageView imgaeView;
        CardView cardView;
        //   ShimmerTextView offersText;
        //   Shimmer shimmer;
        ImageView img1,img2,img3,img4,img5;
        LinearLayout footer_layout;
//        NetworkImageView subCatImage;
        RoundedImageView subCatImage;

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


            convertView=inflater.inflate(R.layout.sub_cat_list,parent,false);

            final Viewholder viewholder=new Viewholder();

            viewholder.name=convertView.findViewById(R.id.name);
            viewholder.subCatImage=convertView.findViewById(R.id.subCatImage);
            viewholder.subCat1=convertView.findViewById(R.id.subCat1);
//
//

            viewholder.name.setText(AllProducts.get(position).get("subcategory"));
            viewholder.subCat1.setText(AllProducts.get(position).get("meta_tag"));

//            Transformation transformation = new RoundedTransformationBuilder()
//                    .borderColor(Color.BLACK)
//                    .borderWidthDp(3)
//                    .cornerRadiusDp(10)
//                    .oval(false)
//
//                    .build();

            Picasso.with(getActivity())
                        .load(AllProducts.get(position).get("image").replace(" ","%20"))
                    .fit()
                   // .transform(transformation)
                    .into(viewholder.subCatImage);

//            Picasso.with(getActivity()).load(AllProducts.get(position).get("image")).into(viewholder.subCatImage);

//            ImageLoader imageLoader=AppController.getInstance().getImageLoader();
//            viewholder.subCatImage.setImageUrl(AllProducts.get(position).get("image"),imageLoader);


            Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "muli_semibold.ttf");
            viewholder.name.setTypeface(face);


            return convertView;
        }
    }


    class CustomPagerAdapter2 extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter2(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return AllBaner.size();
        }



        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }



        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.page_item, container, false);

            NetworkImageView imageView = (NetworkImageView) itemView.findViewById(R.id.imageView);


            ImageLoader imageLoader = AppController.getInstance().getImageLoader();

            imageView.setImageUrl(AllBaner.get(position).getPhoto().toString().replace(" ","%20"),imageLoader);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AllBaner.get(position).getEventName().toString().isEmpty() ) {

//                          Toast.makeText(getActivity(), "blank", Toast.LENGTH_SHORT).show();
                    }
                    else{
//                        Toast.makeText(getActivity(), AllBaner.get(position).getOrgby().toString(), Toast.LENGTH_SHORT).show();


                        Intent intent=new Intent(getActivity(), WebViewOpen.class);
                        intent.putExtra("link",AllBaner.get(position).getEventName().toString());
                        startActivity(intent);

//                        Fragment fragment=new WebViewOpen();
//                        Bundle bundle=new Bundle();
//                        bundle.putString("link",AllBaner.get(position).getOrgby().toString());
//                        FragmentManager manager=getActivity().getSupportFragmentManager();
//                        FragmentTransaction ft=manager.beginTransaction();
//                        fragment.setArguments(bundle);
//                        ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                    }
                }
            });

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    public void onPause(){
        super.onPause();
        if(handler!=null)
            handler.removeCallbacks(runnale);
    }
    public void onResume(){
        super.onResume();
        // Start auto screen slideshow after 1s
        handler.postDelayed(runnale, 3000);
    }



}
