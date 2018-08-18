package pinerria.business.pinerrianew.Activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pinerria.business.pinerrianew.Fragments.AddJobs;
import pinerria.business.pinerrianew.Fragments.BannerRequest;
import pinerria.business.pinerrianew.Fragments.ContactUs;
import pinerria.business.pinerrianew.Fragments.Home;
import pinerria.business.pinerrianew.Fragments.Listing;
import pinerria.business.pinerrianew.Fragments.ManageBusiness;
import pinerria.business.pinerrianew.Fragments.MyJobs;
import pinerria.business.pinerrianew.Fragments.MyProducts;
import pinerria.business.pinerrianew.Fragments.Packages;
import pinerria.business.pinerrianew.Fragments.PayOrder;
import pinerria.business.pinerrianew.Fragments.SearchData;
import pinerria.business.pinerrianew.Fragments.Transcation;
import pinerria.business.pinerrianew.Fragments.ViewJobs;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.SqliteDatabase.DatabaseHelper;
import pinerria.business.pinerrianew.SqliteDatabase.Note;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;

import pinerria.business.pinerrianew.gcm.GCMRegistrationIntentService;

public class HomeAct extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView profile,home,chat,inbosearchx,packageList;
    public  static TextView title;
    TextView nameUser,MobileNo,zoneName;
    CircleImageView userImage,menuSearch;

    public  static boolean business=false;
    public  static boolean jobs=false;
    private DatabaseHelper db;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String pass="123456";
    private String user;

    private List<Note> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        profile=findViewById(R.id.profile);
        home=findViewById(R.id.home);
        chat=findViewById(R.id.chat);
        //  inbosearchx=findViewById(R.id.inbosearchx);
        packageList=findViewById(R.id.packageList);
        title=findViewById(R.id.title);
        menuSearch=findViewById(R.id.menuSearch);

        user=MyPrefrences.getMobile(getApplicationContext());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        nameUser=header.findViewById(R.id.nameUser);
        MobileNo=header.findViewById(R.id.MobileNo);
        zoneName=header.findViewById(R.id.zoneName);
        userImage=header.findViewById(R.id.userImage);

        db = new DatabaseHelper(HomeAct.this);


         setmRegistrationBroadcastReceiver();

        Log.d("dfdgdgfsdgdf",MyPrefrences.getCityName(getApplicationContext()));

//        Drawable mDrawable = getResources().getDrawable(R.drawable.package_l);
//        mDrawable.setColorFilter(new
//                PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY));

//        Drawable drawable =  getResources().getDrawable(R.drawable.package_l);
//        drawable = DrawableCompat.wrap(drawable);
//        DrawableCompat.setTint(drawable, Color.GREEN);
//        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_OVER);
//        packageList.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);


        if (MyPrefrences.getUserLogin(getApplicationContext())==true){
            nameUser.setText(MyPrefrences.getUSENAME(getApplicationContext()));
            MobileNo.setText("+91 "+MyPrefrences.getMobile(getApplicationContext()));
            Picasso.with(getApplicationContext())
                    .load(MyPrefrences.getImage(getApplicationContext()).replace(" ","%20"))
                    .fit()
                    // .transform(transformation)
                    .into(userImage);

            Menu menu = navigationView.getMenu();
            MenuItem nav_login = menu.findItem(R.id.nav_logout);
            nav_login.setTitle("Logout");
        }

        else if (MyPrefrences.getUserLogin(getApplicationContext())==false){
            nameUser.setText("Guest");
            MobileNo.setText("+91 "+"XXXXXXXXXX");

            Menu menu = navigationView.getMenu();
            MenuItem nav_login = menu.findItem(R.id.nav_logout);
            nav_login.setTitle("Login");
        }




        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.catToSubCat+"/"+ MyPrefrences.getUserID(getApplicationContext()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());
                //   Util.cancelPgDialog(dialog);
                try {


                    if (response.getString("status").equalsIgnoreCase("success")){

                        if (response.optString("business").equalsIgnoreCase("No")){
                            HomeAct.business=false;
                            Menu menu = navigationView.getMenu();
                            MenuItem nav_add_product = menu.findItem(R.id.nav_add_product);
                            MenuItem nav_manage_business = menu.findItem(R.id.nav_manage_business);
                            nav_add_product.setVisible(true);
                            nav_manage_business.setVisible(false);

                        }
                        else if (response.optString("business").equalsIgnoreCase("Yes")){
                            HomeAct.business=true;
                            Menu menu = navigationView.getMenu();
                            MenuItem nav_add_product = menu.findItem(R.id.nav_add_product);
                            MenuItem nav_manage_business = menu.findItem(R.id.nav_manage_business);
                            nav_add_product.setVisible(false);
                            nav_manage_business.setVisible(true);
                        }

                        if (response.optString("job").equalsIgnoreCase("No")){
                            HomeAct.jobs=false;
                            Menu menu = navigationView.getMenu();
                            MenuItem nav_add_jobs = menu.findItem(R.id.nav_add_jobs);
                            MenuItem nav_my_jobs = menu.findItem(R.id.nav_my_jobs);
                            nav_add_jobs.setVisible(true);
                            nav_my_jobs.setVisible(false);
                        }
                        else if (response.optString("job").equalsIgnoreCase("Yes")){
                            HomeAct.jobs=true;
                            Menu menu = navigationView.getMenu();
                            MenuItem nav_add_jobs = menu.findItem(R.id.nav_add_jobs);
                            MenuItem nav_my_jobs = menu.findItem(R.id.nav_my_jobs);
                            nav_add_jobs.setVisible(false);
                            nav_my_jobs.setVisible(true);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    //Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                // Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);



        if (HomeAct.business==true ){
            Log.d("fsdfsfsfs","true");
            Menu menu = navigationView.getMenu();
            MenuItem nav_add_product = menu.findItem(R.id.nav_add_product);
            MenuItem nav_manage_business = menu.findItem(R.id.nav_manage_business);
            nav_add_product.setVisible(false);
            nav_manage_business.setVisible(true);

        }
        else if (HomeAct.business==false){
            Log.d("fsdfsfsfs","false");
            Menu menu = navigationView.getMenu();
            MenuItem nav_add_product = menu.findItem(R.id.nav_add_product);
            MenuItem nav_manage_business = menu.findItem(R.id.nav_manage_business);
            nav_add_product.setVisible(true);
            nav_manage_business.setVisible(false);
        }

        if (HomeAct.jobs==true){
            Log.d("fsdfsfsdsfsfsfs","true");

            Menu menu = navigationView.getMenu();
            MenuItem nav_add_jobs = menu.findItem(R.id.nav_add_jobs);
            MenuItem nav_my_jobs = menu.findItem(R.id.nav_my_jobs);
            nav_add_jobs.setVisible(false);
            nav_my_jobs.setVisible(true);
        }
        else if (HomeAct.jobs==false ){
            Log.d("fsdfsfsdsfsfsfs","false");

            Menu menu = navigationView.getMenu();
            MenuItem nav_add_jobs = menu.findItem(R.id.nav_add_jobs);
            MenuItem nav_my_jobs = menu.findItem(R.id.nav_my_jobs);
            nav_add_jobs.setVisible(true);
            nav_my_jobs.setVisible(false);
        }


        Fragment fragment = new Home();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).commit();
        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);



//        if (getIntent().getStringExtra("userType").equalsIgnoreCase("my_product")){
//            Fragment fragment = new ManageBusiness();
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content_frame, fragment).commit();
//            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
//        }else {
//            Fragment fragment = new Home();
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content_frame, fragment).commit();
//            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
//        }

        zoneName.setText(MyPrefrences.getCityName(getApplicationContext())+"  ▼");
        zoneName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeAct.this,SelectZone.class));
            }
        });




        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Animation myAnim = AnimationUtils.loadAnimation(HomeAct.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                profile.startAnimation(myAnim);

                Intent intent=new Intent(HomeAct.this,ProfilePage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {


                final Animation myAnim = AnimationUtils.loadAnimation(HomeAct.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                home.startAnimation(myAnim);

                Fragment fragment = new Home();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment).commit();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);



            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                final Animation myAnim = AnimationUtils.loadAnimation(HomeAct.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                chat.startAnimation(myAnim);



                if (MyPrefrences.getUserLogin(getApplicationContext())==true) {
                   LoginForChat();


//                   Log.d("dfsdfsdfsdewrtfe", String.valueOf(db.getAllUSERS()));
//                    for (int i=0;i<db.getUserCount();i++){
//                        userList.addAll(db.getAllUSERS());
//                        Log.d("fsdffgfgfgfgfsdfsdfsdf",userList.get(i).getNote());
//
//                    }


                }
                else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeAct.this);
                    builder.setMessage("Please Login to Chat")
                            .setCancelable(false)
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent=new Intent(HomeAct.this,Login.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                    finish();
                                }
                            })
                            .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    //dialog.cancel();

                                    Intent intent=new Intent(HomeAct.this,HomeAct.class);
                                    intent.putExtra("userType","");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                    finish();

                                }
                            });
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Pinerria");
                    alert.show();

                }





            }
        });
        menuSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                final Animation myAnim = AnimationUtils.loadAnimation(HomeAct.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                menuSearch.startAnimation(myAnim);

                Fragment fragment = new SearchData();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment).commit();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);




            }
        });

        packageList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                final Animation myAnim = AnimationUtils.loadAnimation(HomeAct.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                packageList.startAnimation(myAnim);


                Fragment fragment = new Packages();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment).commit();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);


            }
        });



    }

    private void LoginForChat() {

//        String url = "https://chatapp-25d11.firebaseio.com/users.json";
        String url = "https://pinerria-home-business.firebaseio.com/users.json";
        final ProgressDialog pd = new ProgressDialog(HomeAct.this);
        pd.setMessage("Loading...");
        pd.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(s.equals("null")){
                    Toast.makeText(HomeAct.this, "user not found", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        JSONObject obj = new JSONObject(s);

                        if(!obj.has(user)){
                            Toast.makeText(HomeAct.this, "user not found", Toast.LENGTH_LONG).show();
                        }
                        else if(obj.getJSONObject(user).getString("password").equals(pass)){
                            UserDetails.username = user;
                            UserDetails.password = pass;
                            startActivity(new Intent(HomeAct.this, ChatUSer.class));
                        }
                        else {
                            Toast.makeText(HomeAct.this, "incorrect password", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                pd.dismiss();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                pd.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(HomeAct.this);
        rQueue.add(request);


    }


    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();

                Fragment fragment = new Listing();
                Bundle bundle = new Bundle();
                bundle.putString("id", query);
                bundle.putString("subcategory", query);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

                createNote(query.toString());

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    private void createNote(String note) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(note);

        // get the newly inserted note from db
        Note n = db.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            //  notesList.add(0, n);

            // refreshing the list
            //  adapter.notifyDataSetChanged();

            // toggleEmptyNotes();
        }
    }

    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        //Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));

//        if (InternetStatus.isConnectingToInternet(MainActivity.this)) {
//            new GetFrequest().execute();
//
//        }
    }
    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        //Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    private void setmRegistrationBroadcastReceiver() {
        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
                    //Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Fragment fragment = new Home();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_profile) {

            Intent intent=new Intent(HomeAct.this,ProfilePage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        } else if (id == R.id.nav_manage_business) {

            Fragment fragment = new ManageBusiness();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);


        } else if (id == R.id.nav_add_product) {

            Intent intent=new Intent(HomeAct.this,AddProduct.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        } else if (id == R.id.nav_gst_detail) {

            Intent intent=new Intent(HomeAct.this,AddGSTDetails.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        } else if (id == R.id.nav_my_product) {


            Fragment fragment = new MyProducts();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_package) {

            Fragment fragment = new Packages();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_add_banner) {

            Fragment fragment = new BannerRequest();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);


        } else if (id == R.id.nav_add_jobs) {

            Fragment fragment = new AddJobs();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_view_jobs) {

            Fragment fragment = new ViewJobs();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_trans) {

            Fragment fragment = new Transcation();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_my_jobs) {

            Fragment fragment = new MyJobs();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_pay_order) {

            Fragment fragment = new PayOrder();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);


         } else if (id == R.id.nav_contact) {

                        Fragment fragment = new ContactUs();
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                        ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);

        } else if (id == R.id.nav_logout) {

            Intent intent=new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finishAffinity();
            MyPrefrences.resetPrefrences(getApplicationContext());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
