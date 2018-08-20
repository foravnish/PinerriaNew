package pinerria.business.pinerrianew.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import com.eftimoff.viewpagertransformers.FlipHorizontalTransformer;
import com.eftimoff.viewpagertransformers.ForegroundToBackgroundTransformer;
import com.eftimoff.viewpagertransformers.RotateUpTransformer;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import pinerria.business.pinerrianew.Activites.AddProduct;
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
public class Home extends Fragment implements NavigationView.OnNavigationItemSelectedListener {


    public Home() {
        // Required empty public constructor
    }
    ListView listView;
    Dialog dialog;
    List<HashMap<String,String>> AllProducts ;

    ViewPager viewPager,viewPager2;
    CustomPagerAdapter2 mCustomPagerAdapter2;
    List<Const> AllBaner   = new ArrayList<>();
    int currentPage = 0;
    CirclePageIndicator indicator2;

    public  static boolean business;
    public  static boolean jobs;
   // FabSpeedDial fab;


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

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        HomeAct.title.setText("PINERRIA "+MyPrefrences.getCityName(getActivity()).toUpperCase() );

        listView=view.findViewById(R.id.listView);
       // fab=(FabSpeedDial)view.findViewById(R.id.fab);
       // FabSpeedDial fab = (FabSpeedDial) view.findViewById(R.id.fab);


        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);

        AllProducts = new ArrayList<>();

//        View headerView = ((LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header, null, false);
//        listView.addHeaderView(headerView);



        viewPager2 = (ViewPager) view.findViewById(R.id.slider2);
        indicator2 = (CirclePageIndicator)view.findViewById(R.id.indicat2);

        viewPager2.setPageTransformer(true, new ForegroundToBackgroundTransformer());


        position=0;


        final FabSpeedDial fabSpeedDial = (FabSpeedDial) view.findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                int id=menuItem.getItemId();

                if (id==R.id.one){
                    Intent intent=new Intent(getActivity(),AddProduct.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                }

                else if (id==R.id.two){
                    Fragment fragment = new Packages();
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

                }
                else if (id==R.id.three){
                    Fragment fragment = new BannerRequest();
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                }
                else if (id==R.id.four){

//                    Fragment fragment = new BannerRequest();
//                    FragmentManager manager = getFragmentManager();
//                    FragmentTransaction ft = manager.beginTransaction();
//                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                }

                return false;
            }
        });



        ////  Begin Baner Api

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Fragment fragment=new SubCategory();
               // i=i-1;
                FragmentManager manager=getFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame, fragment).addToBackStack(null);
                ft.commit();
                Bundle bundle=new Bundle();
                bundle.putString("id",AllProducts.get(i).get("id"));
                bundle.putString("category",AllProducts.get(i).get("category"));
                fragment.setArguments(bundle);


//                Fragment fragment=new SubCatagoryFragment();
//                FragmentManager manager=getFragmentManager();
//                FragmentTransaction ft=manager.beginTransaction();
//                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
//                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//                Bundle bundle=new Bundle();
//                position=position-1;
//                bundle.putString("cat_id",DataList.get(position).getID().toString());
//                bundle.putString("type","normal");
//                bundle.putString("query","");
//                Log.d("fsdgfsdgdf",DataList.get(position).getID().toString());
//                Log.d("fsdgfsdgdf", String.valueOf(position));
//                fragment.setArguments(bundle);


            }
        });


        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET,
                Api.banner+"/"+MyPrefrences.getCityID(getActivity())+"/555", null, new Response.Listener<JSONObject>() {

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

                         //   if (jsonObject.optString("cat_id").equalsIgnoreCase("Home")) {
                                AllBaner.add(new Const(jsonObject.optString("id"), jsonObject.optString("title"), jsonObject.optString("image"), jsonObject.optString("url"), null, null, null,null,null,null));
                           // }

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
                Api.catToSubCat+"/"+ MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());
                Util.cancelPgDialog(dialog);
                try {


                    if (response.getString("status").equalsIgnoreCase("success")){

                        if (response.optString("business").equalsIgnoreCase("Yes")){
                             business =true;

                        }
                        else if (response.optString("business").equalsIgnoreCase("No")){
                            business =false;
                        }

                        if (response.optString("job").equalsIgnoreCase("Yes")){
                            jobs =true;
                        }
                        else if (response.optString("job").equalsIgnoreCase("No")){
                            jobs =false;
                        }
                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String, String> map = new HashMap<>();

                            map.put("id", jsonObject.optString("id"));
                            map.put("category", jsonObject.optString("category"));
                            map.put("meta_tag", jsonObject.optString("meta_tag"));
                            map.put("image", jsonObject.optString("image"));
                            map.put("meta_tag", jsonObject.optString("meta_tag"));
                            map.put("icon", jsonObject.optString("icon"));


                            if (!jsonObject.optString("subcategory").equals("")) {
                                Log.d("sfsdfsdfsdfs","true");
                                JSONArray jsonArray1 = jsonObject.getJSONArray("subcategory");

                                if (jsonArray1.length()==1){
                                    JSONObject jsonObject0 = jsonArray1.getJSONObject(0);
                                    map.put("subcategory0", jsonObject0.optString("subcategory"));
                                    map.put("id0", jsonObject0.optString("id"));

                                }
                                else if (jsonArray1.length()==2){
                                    JSONObject jsonObject0 = jsonArray1.getJSONObject(0);
                                    map.put("subcategory0", jsonObject0.optString("subcategory"));
                                    map.put("id0", jsonObject0.optString("id"));
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(1);
                                    map.put("subcategory1", jsonObject1.optString("subcategory"));
                                    map.put("id1", jsonObject1.optString("id"));
                                }
                                else if (jsonArray1.length()==3) {
                                    JSONObject jsonObject0 = jsonArray1.getJSONObject(0);
                                    map.put("subcategory0", jsonObject0.optString("subcategory"));
                                    map.put("id0", jsonObject0.optString("id"));
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(1);
                                    map.put("subcategory1", jsonObject1.optString("subcategory"));
                                    map.put("id1", jsonObject1.optString("id"));
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(2);
                                    map.put("subcategory2", jsonObject2.optString("subcategory"));
                                    map.put("id2", jsonObject2.optString("id"));
                                }
                                else if (jsonArray1.length()==4) {
                                    JSONObject jsonObject0 = jsonArray1.getJSONObject(0);
                                    map.put("subcategory0", jsonObject0.optString("subcategory"));
                                    map.put("id0", jsonObject0.optString("id"));
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(1);
                                    map.put("subcategory1", jsonObject1.optString("subcategory"));
                                    map.put("id1", jsonObject1.optString("id"));
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(2);
                                    map.put("subcategory2", jsonObject2.optString("subcategory"));
                                    map.put("id2", jsonObject2.optString("id"));
                                    JSONObject jsonObject3 = jsonArray1.getJSONObject(3);
                                    map.put("subcategory3", jsonObject3.optString("subcategory"));
                                    map.put("id3", jsonObject3.optString("id"));
                                }

                            }
                            Adapter adapter = new Adapter();
                            listView.setAdapter(adapter);
                            AllProducts.add(map);
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






//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//
//                        Button Yes_action,No_action;
//                        TextView heading;
//                        dialog4 = new Dialog(getActivity());
//                        dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog4.setContentView(R.layout.update_state1);
//
//                        Yes_action=(Button)dialog4.findViewById(R.id.Yes_action);
//                        No_action=(Button)dialog4.findViewById(R.id.No_action);
//                        heading=(TextView)dialog4.findViewById(R.id.heading);
//
//
//                        heading.setText("Are you sure you want to exit?");
//                        Yes_action.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //System.exit(0);
//                                //getActivity().finish();
//                                getActivity().finishAffinity();
//
//                            }
//                        });
//
//                        No_action.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog4.dismiss();
//                            }
//                        });
//                        dialog4.show();
////
//
//                        //Toast.makeText(getActivity(), "back", Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });


        return  view;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_manage_business:
//                // Not implemented here
//                return false;
//            case R.id.fragment_menu_item:
//                // Do Fragment menu item stuff here
//                return true;
//            default:
//                break;
//        }
//
//        return false;
//    }


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



    class Adapter extends BaseAdapter {

        LayoutInflater inflater;
        TextView subCat5,subCat4,subCat3,subCat2,subCat1,catName;
        LinearLayout circlelayout;
        NetworkImageView catIcon;
        NetworkImageView imgList;

        Adapter() {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (position%2==0) {
                convertView = inflater.inflate(R.layout.list_home, parent, false);
            }
            else {
                convertView = inflater.inflate(R.layout.list_home2, parent, false);
            }

            catName=convertView.findViewById(R.id.catName);
            subCat1=convertView.findViewById(R.id.subCat1);
            subCat2=convertView.findViewById(R.id.subCat2);
            subCat3=convertView.findViewById(R.id.subCat3);
            subCat4=convertView.findViewById(R.id.subCat4);
            subCat5=convertView.findViewById(R.id.subCat5);
            circlelayout=convertView.findViewById(R.id.circlelayout);
            imgList=convertView.findViewById(R.id.imgList);
            catIcon=convertView.findViewById(R.id.catIcon);


            catName.setText(AllProducts.get(position).get("meta_tag"));

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imgList.setImageUrl(AllProducts.get(position).get("image").toString().replace(" ","%20"),imageLoader);
            catIcon.setImageUrl(AllProducts.get(position).get("icon").toString().replace(" ","%20"),imageLoader);

            catIcon.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.white));

//            Picasso.with(getActivity())
//                    .load(AllProducts.get(position).get("image"))
//                    .fit()
//                    // .transform(transformation)
//                    .into(imgList);

//            Typeface faceB = Typeface.createFromAsset(getActivity().getAssets(), "muli_bold.ttf");
//            Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "muli.ttf");
//            catName.setTypeface(faceB);
//            subCat1.setTypeface(face);
//            subCat2.setTypeface(face);
//            subCat3.setTypeface(face);
//            subCat4.setTypeface(face);
//            subCat5.setTypeface(face);

            if (position%5==0){
                circlelayout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.yellow));
            }
            else if (position%5==1){
                circlelayout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.pink));
            }
            else if (position%5==2){
                circlelayout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.orange));
            }
            else if (position%5==3){
                circlelayout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.red));
            }
            else if (position%5==4){
                circlelayout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.green));
            }



//            if (!AllProducts.get(position).get("subcategory2").equals("")){
//                subCat3.setVisibility(View.VISIBLE);
//                subCat3.setText(AllProducts.get(position).get("subcategory2"));
//            }
//            else{
//                subCat3.setVisibility(View.GONE);
//            }
//            if (!AllProducts.get(position).get("subcategory3").equals("")){
//                subCat4.setVisibility(View.VISIBLE);
//                subCat4.setText(AllProducts.get(position).get("subcategory3"));
//            }
//            else{
//                subCat4.setVisibility(View.GONE);
//            }


            subCat1.setText(AllProducts.get(position).get("subcategory0"));
            subCat2.setText(AllProducts.get(position).get("subcategory1"));
            subCat3.setText(AllProducts.get(position).get("subcategory2"));
            subCat4.setText(AllProducts.get(position).get("subcategory3"));


            subCat1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    try {
                        Log.d("dfsdfsdfs", AllProducts.get(position).get("id0"));
                        Fragment fragment = new Listing();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", AllProducts.get(position).get("id0"));
                        bundle.putString("subcategory", AllProducts.get(position).get("subcategory0"));
                        bundle.putString("value", "");
                        bundle.putString("nearMe","");
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        fragment.setArguments(bundle);
                        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


            subCat2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        Log.d("dfsdfsdfs", AllProducts.get(position).get("id1"));

                        Fragment fragment = new Listing();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", AllProducts.get(position).get("id1"));
                        bundle.putString("subcategory", AllProducts.get(position).get("subcategory1"));
                        bundle.putString("value", "");
                        bundle.putString("nearMe","");
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        fragment.setArguments(bundle);
                        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            subCat3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        Log.d("dfsdfsdfs", AllProducts.get(position).get("id2"));
                        Fragment fragment = new Listing();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", AllProducts.get(position).get("id2"));
                        bundle.putString("subcategory", AllProducts.get(position).get("subcategory2"));
                        bundle.putString("value", "");
                        bundle.putString("nearMe","");
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        fragment.setArguments(bundle);
                        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            subCat4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        Log.d("dfsdfsdfs", AllProducts.get(position).get("id3"));

                        Fragment fragment = new Listing();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", AllProducts.get(position).get("id3"));
                        bundle.putString("subcategory", AllProducts.get(position).get("subcategory3"));
                        bundle.putString("value", "");
                        bundle.putString("nearMe","");
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        fragment.setArguments(bundle);
                        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


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

            imageView.setImageUrl(AllBaner.get(position).getEventName().toString().replace(" ","%20"),imageLoader);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AllBaner.get(position).getPhoto().toString().isEmpty() ) {

                        //  Toast.makeText(getActivity(), "blank", Toast.LENGTH_SHORT).show();
                    }
                    else{
//                        Toast.makeText(getActivity(), AllBaner.get(position).getOrgby().toString(), Toast.LENGTH_SHORT).show();


                        Intent intent=new Intent(getActivity(), WebViewOpen.class);
                        intent.putExtra("link",AllBaner.get(position).getPhoto().toString());
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



}
