package pinerria.business.pinerrianew.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class MyProducts extends Fragment {


    public MyProducts() {
        // Required empty public constructor
    }
    List<HashMap<String,String>> AllProducts ;
    ListView expListView;
    ListView lvExp;
    HashMap<String,String> map;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_products, container, false);


        AllProducts = new ArrayList<>();
        expListView = (ListView) view.findViewById(R.id.lvExp);
        HomeAct.title.setText("My Product");

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);
//
//        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Fragment fragment=new Listing();
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
                Api.userBusiness+"/"+ MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {

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
                            map.put("bussiness_name", jsonObject.optString("bussiness_name"));
                            map.put("category_id", jsonObject.optString("category_id"));
                            map.put("sub_category_id", jsonObject.optString("sub_category_id"));
                            map.put("image", jsonObject.optString("image"));
                            map.put("service_keyword", jsonObject.optString("service_keyword"));


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


    class Adapter extends BaseAdapter {

        LayoutInflater inflater;

        ImageView imgFav,stars;
        TextView address,nameBusiness,subCat1,Catname,subCatName,keyword;
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


            convertView=inflater.inflate(R.layout.my_product,parent,false);



            nameBusiness=convertView.findViewById(R.id.nameBusiness);
            Catname=convertView.findViewById(R.id.Catname);
            subCatName=convertView.findViewById(R.id.subCatName);
            keyword=convertView.findViewById(R.id.keyword);
            subCatImage=convertView.findViewById(R.id.subCatImage);

            nameBusiness.setText(AllProducts.get(position).get("bussiness_name"));
            Catname.setText(AllProducts.get(position).get("category_id"));
            subCatName.setText(AllProducts.get(position).get("sub_category_id"));
            keyword.setText(AllProducts.get(position).get("service_keyword"));

//            Transformation transformation = new RoundedTransformationBuilder()
//                    .borderColor(Color.BLACK)
//                    .borderWidthDp(3)
//                    .cornerRadiusDp(10)
//                    .oval(false)
//                    .build();

            Picasso.with(getActivity())
                    .load(AllProducts.get(position).get("image"))
                    .fit()
                    // .transform(transformation)
                    .into(subCatImage);

//            Picasso.with(getActivity()).load(AllProducts.get(position).get("image")).into(viewholder.subCatImage);

//            ImageLoader imageLoader=AppController.getInstance().getImageLoader();
//            viewholder.subCatImage.setImageUrl(AllProducts.get(position).get("image"),imageLoader);


//            Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "muli_semibold.ttf");
//            nameBusiness.setTypeface(face);
//            nameBusiness.setTypeface(face);


            return convertView;
        }
    }


}
