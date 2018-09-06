package pinerria.business.pinerrianew.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import de.hdodenhof.circleimageview.CircleImageView;
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
public class Packages extends Fragment {


    public Packages() {
        // Required empty public constructor
    }
    JSONArray jsonArray;
    JSONArray userInfoAyyay;
    GridView gridview;

    List<HashMap<String,String>> AllProducts ;
    Dialog dialog;
    HashMap<String,String> map;
    JSONObject jsonObject2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_packages, container, false);

        gridview=(GridView) view.findViewById(R.id.gridview);

        AllProducts = new ArrayList<>();
        HomeAct.title.setText("Select Packages");

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);

//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Fragment fragment=new Listing();
//                i=i-1;
//                Bundle bundle=new Bundle();
//                bundle.putString("id",AllProducts.get(i).get("id"));
//                bundle.putString("subcategory",AllProducts.get(i).get("subcategory"));
//                FragmentManager manager=getActivity().getSupportFragmentManager();
//                FragmentTransaction ft=manager.beginTransaction();
//                fragment.setArguments(bundle);
//                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
//                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//
//
//            }
//        });


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.businessPackage+"/"+ MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        gridview.setVisibility(View.VISIBLE);
                        //  imageNoListing.setVisibility(View.GONE);
                        jsonArray=response.getJSONArray("message");


                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            map=new HashMap();
                            map.put("id", jsonObject.optString("id"));
                            map.put("package_name", jsonObject.optString("package_name"));
                            map.put("actual_value", jsonObject.optString("actual_value"));
                            map.put("duration", jsonObject.optString("duration"));
                            map.put("discount_percent", jsonObject.optString("discount_percent"));

                            Adapter adapter=new Adapter();
                            gridview.setAdapter(adapter);
                            AllProducts.add(map);
                        }

                        if (!response.getString("userInfo").equals("")) {
                            userInfoAyyay = response.getJSONArray("userInfo");
                            for ( int j=0;j<userInfoAyyay.length();j++) {
                                jsonObject2 = userInfoAyyay.getJSONObject(j);
                            }

                        }

                    }
                    else{
                        gridview.setVisibility(View.GONE);
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


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




                if (MyPrefrences.getUserLogin(getActivity())==true) {



                    if (Home.packageData==true){
                     Util.errorDialog(getActivity(),"You have already package purchased.");
                    }
                    else if (Home.packageData==false){
                        if (jsonObject2.optString("company_name").equalsIgnoreCase("")){
//                        Toast.makeText(getApplicationContext(), "blank", Toast.LENGTH_SHORT).show();
//
//                        Long tsLong = System.currentTimeMillis()/1000;
//                        String ts = tsLong.toString();
//                        Log.d("TimeCurrent",ts);
//                        MyPrefrences.setDateTime(getActivity(),ts);

                            Intent intent=new Intent(getActivity(),AddGSTDetails.class);
                            intent.putExtra("type","packageBefore");
                            intent.putExtra("amount","");
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        }
                        else{

                            Intent intent=new Intent(getActivity(), PayActivity.class);
                            try {
                                intent.putExtra("jsonArray",jsonArray.get(i).toString());
                                intent.putExtra("userInfo",userInfoAyyay.get(0).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                        }


                    }



                    Log.d("fgdgdfgdfgdfgdgd",jsonObject2.optString("company_name"));




                }
                else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please Login to Purchase Package")
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


            }
        });





        return view;
    }



    class Adapter extends BaseAdapter {

        LayoutInflater inflater;
        TextView pName,labelTxt,validate,price,price1,buynow,discount,duration,discountColor,validateColor,discountFont,validatFont;
        RelativeLayout discountTag;
        LinearLayout qImage2;
        RelativeLayout relativeColor;
        LinearLayout linerColor;
        CircleImageView profileImage;


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


            convertView=inflater.inflate(R.layout.custonlistview_packages_new,parent,false);


            pName=(TextView)convertView.findViewById(R.id.pName);
            labelTxt=(TextView)convertView.findViewById(R.id.labelTxt);
//            creditpoints=(TextView)convertView.findViewById(R.id.creditpoints);
//            creditpoints2=(TextView)convertView.findViewById(R.id.creditpoints2);
            price=(TextView)convertView.findViewById(R.id.price);
            price1=(TextView)convertView.findViewById(R.id.price1);
            buynow=(TextView) convertView.findViewById(R.id.buynow);
            discountColor=(TextView) convertView.findViewById(R.id.discountColor);
            validateColor=(TextView) convertView.findViewById(R.id.validateColor);
            validatFont=(TextView) convertView.findViewById(R.id.validatFont);
            discountFont=(TextView) convertView.findViewById(R.id.discountFont);
            duration=(TextView) convertView.findViewById(R.id.duration);
            discount=(TextView) convertView.findViewById(R.id.discount);
            validate=(TextView) convertView.findViewById(R.id.validate);
            relativeColor=(RelativeLayout) convertView.findViewById(R.id.relativeColor);
            linerColor=(LinearLayout) convertView.findViewById(R.id.linerColor);
            profileImage=(CircleImageView) convertView.findViewById(R.id.profileImage);

//            discount=(TextView) convertView.findViewById(R.id.discount);
//            totalCredit=(TextView) convertView.findViewById(R.id.totalCredit);
//            discountTag=(RelativeLayout) convertView.findViewById(R.id.discountTag);
//            qImage2=(LinearLayout) convertView.findViewById(R.id.qImage2);

            pName.setText(AllProducts.get(position).get("package_name"));
            labelTxt.setText(AllProducts.get(position).get("package_name"));
            price.setText(AllProducts.get(position).get("actual_value"));
            duration.setText(AllProducts.get(position).get("duration")+" Months");
            validate.setText(AllProducts.get(position).get("duration")+" Months");
            discount.setText(AllProducts.get(position).get("discount_percent")+ "%");


            if (position==0) {
                relativeColor.setBackgroundColor(Color.parseColor("#2b6abb"));
                validateColor.setTextColor(Color.parseColor("#2b6abb"));
                discountColor.setTextColor(Color.parseColor("#2B6AB9"));
                linerColor.setBackgroundColor(Color.parseColor("#B6B002"));
                buynow.setBackgroundResource(R.drawable.strock_buy_now);
                profileImage.setImageResource(R.drawable.package2);
            }
            else if (position==1) {
                relativeColor.setBackgroundColor(Color.parseColor("#e17d17"));
                validateColor.setTextColor(Color.parseColor("#e17d17"));
                discountColor.setTextColor(Color.parseColor("#e17d17"));
                linerColor.setBackgroundColor(Color.parseColor("#0E5E75"));
                buynow.setBackgroundResource(R.drawable.strock_buy_now2);
                profileImage.setImageResource(R.drawable.package22);
            }
            else if (position==2) {
                relativeColor.setBackgroundColor(Color.parseColor("#01857a"));
                validateColor.setTextColor(Color.parseColor("#01857a"));
                discountColor.setTextColor(Color.parseColor("#01857a"));
                linerColor.setBackgroundColor(Color.parseColor("#02B024"));
                buynow.setBackgroundResource(R.drawable.strock_buy_now3);
                profileImage.setImageResource(R.drawable.package3);
            }
            else if (position==3) {
                relativeColor.setBackgroundColor(Color.parseColor("#e04646"));
                validateColor.setTextColor(Color.parseColor("#e04646"));
                discountColor.setTextColor(Color.parseColor("#e04646"));
                linerColor.setBackgroundColor(Color.parseColor("#2C2C2C"));
                buynow.setBackgroundResource(R.drawable.strock_buy_now4);
                profileImage.setImageResource(R.drawable.package4);
            }

            Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "muli.ttf");
            Typeface face_bold=Typeface.createFromAsset(getActivity().getAssets(), "muli_bold.ttf");
            pName.setTypeface(face);
            labelTxt.setTypeface(face);
            buynow.setTypeface(face);
            discount.setTypeface(face_bold);
            validate.setTypeface(face_bold);
            price.setTypeface(face_bold);

            validatFont.setTypeface(face);
            discountFont.setTypeface(face);



//            discount.setText(AllProducts.get(position).get("bonus_in_percentage")+" % \nExtra");
//            creditpoints.setText(AllProducts.get(position).get("creadit_points")+" Points ");


//            totalCredit.setText("Total Credit Points "+DataList.get(position).get("total_credit"));
//            int perCredit= (int) Double.parseDouble(DataList.get(position).get("credit_per_value"));
//            int points= (int) Double.parseDouble(DataList.get(position).get("creadit_points"));

//            price.setText(String.valueOf(perCredit*points));
//
//            if (DataList.get(position).get("bonus_credit").equals("0")){
//                discountTag.setVisibility(View.INVISIBLE);
//                creditpoints2.setVisibility(View.GONE);
//            }
//            else{
//                creditpoints2.setText("+ "+DataList.get(position).get("bonus_credit")+" Points Extra");
//            }


//            qImage2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //Toast.makeText(getActivity(), "open", Toast.LENGTH_SHORT).show();
//                    Util.e(getActivity(), "Price", "1 Credits Point =  ₹ 25 ");
//                }
//            });
//


            return convertView;
        }
    }


}
