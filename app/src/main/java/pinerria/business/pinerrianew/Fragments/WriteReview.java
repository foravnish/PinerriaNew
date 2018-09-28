package pinerria.business.pinerrianew.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

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
import pinerria.business.pinerrianew.Activites.PayActivity;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

import static android.text.Html.fromHtml;

/**
 * A simple {@link Fragment} subclass.
 */
public class WriteReview extends Fragment {


    public WriteReview() {
        // Required empty public constructor
    }
    Button bubmitReview;
    EditText input_name,input_email,editText;
    MaterialRatingBar rtbar;
    Dialog dialog;
    TextView comName,comAdd,totalReviwe;
    String rate="";


    List<HashMap<String,String>> AllProducts ;
    ListView expListView;
    HashMap<String,String> map;
    JSONObject jsonObject1;
    FloatingActionButton fabButton;
    String value="";
    List<String> data=new ArrayList<>();
    private boolean isReached = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_write_review, container, false);


        comName=view.findViewById(R.id.comName);
        comAdd=view.findViewById(R.id.comAdd);
        editText=view.findViewById(R.id.editText);
        rtbar=view.findViewById(R.id.rtbar);
        bubmitReview=view.findViewById(R.id.submitReview);
        totalReviwe=view.findViewById(R.id.totalReviwe);

        //input_name.setText(MyPrefrences.getUSENAME(getActivity()));

        HomeAct.title.setText("Write a Review");
        AllProducts = new ArrayList<>();
        expListView = (ListView) view.findViewById(R.id.lvExp);

        //    expListView.addHeaderView(header);

        comName.setText(getArguments().getString("company_name"));
        comAdd.setText(getArguments().getString("address"));
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //  dialog.setCancelable(false);
        Util.showPgDialog(dialog);

        Log.d("sdfsdgsfgdfgd",getArguments().getString("id"));
        Log.d("sdfsdgsfgdfgd",getArguments().getString("user_id"));

//        if (MyPrefrences.getUserLogin(getActivity())==false){
//            Toast.makeText(getActivity(), "login", Toast.LENGTH_SHORT).show();
//        }

        rtbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("sfsgfsdgdfgdfg","true");
                MaterialRatingBar bar = (MaterialRatingBar) view;
                Log.d("dfsdfgsdgdfgdfg", String.valueOf(bar.getRating()));
            }
        });
        rtbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, final float rating, final boolean fromUser) {
                if (fromUser) {
//                    ratingBar.setRating(Math.ceil(rating));
                    Log.d("dfsdfsdfsdfsdf", String.valueOf(rating));
                    rate= String.valueOf(rating);
                }
            }
        });

//        editText.setOnKeyListener(new DialogInterface.OnKeyListener()
//        {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
//                {
//                    switch (i)
//                    {
//                        case KeyEvent.KEYCODE_DPAD_CENTER:
//                        case KeyEvent.KEYCODE_ENTER:
//                            addCourseFromTextBox();
//                            return true;
//                        default:
//                            break;
//                    }
//                }
//                return false;
//            }
//
//
//        });

//        editText.addTextChangedListener(new TextWatcher(){
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // if edittext has 10chars & this is not called yet, add new line
//                if(editText.getText().length() == 10 && !isReached) {
//                    editText.append("\n");
//                    isReached = true;
//                }
//                // if edittext has less than 10chars & boolean has changed, reset
//                if(editText.getText().length() < 10 && isReached) isReached = false;
//            }
//        });

//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    // do something, e.g. set your TextView here via .setText()
//                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    return true;
//                }
//                return false;
//            }
//        });

        bubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getArguments().getString("user_id").equals(MyPrefrences.getUserID(getActivity()))){

                    Util.errorDialog(getActivity(),"You can not give a Review and Rating for your own business.");
                }
                else {

                    if (!rate.isEmpty()) {
                        Util.showPgDialog(dialog);
                        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.userBusinessReviewSubmit,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                        Log.d("Response", response);
                                        Util.cancelPgDialog(dialog);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equalsIgnoreCase("success")) {

//                                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                errorDialog(jsonObject.getString("message"));

                                            } else {

                                                popUpForUpdateRating("You have already given Review and Rating to this business. Do you want to update?",jsonObject.getString("rating_id"));

                                                //Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        Toast.makeText(getActivity(), "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                                        Util.cancelPgDialog(dialog);
                                    }
                                }
                        ) {


                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("business_id", getArguments().getString("id"));
                                params.put("review", editText.getText().toString());
                                params.put("rating", rate);
                                params.put("user_id", MyPrefrences.getUserID(getActivity()));
                                Log.d("sdfsfsdfsdgfs", MyPrefrences.getUserID(getActivity()));
                                Log.d("sdfsfsdfsdgfs", getArguments().getString("id"));

                                return params;
                            }
                        };
                        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        postRequest.setShouldCache(false);

                        AppController.getInstance().addToRequestQueue(postRequest);
                    } else {
                        Toast.makeText(getActivity(), "Please give a Rating", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.userBusinessRating+"/"+ getArguments().getString("id"), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());

                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");

                    if (response.getString("status").equalsIgnoreCase("success")){

                        JSONArray jsonArray=response.getJSONArray("message");
                        Log.d("dfgsdgdfgdfgdfgd", String.valueOf(jsonArray.length()));
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            totalReviwe.setText("Total Reviews ("+jsonArray.length()+")");

                            //   JSONObject jsonObject2=jsonObject.getJSONObject("comapnyDetails");

                            map=new HashMap();
                            map.put("id",jsonObject.optString("id"));
                            map.put("rating",jsonObject.optString("rating"));
                            map.put("review",jsonObject.optString("review"));
                            map.put("rating_date",jsonObject.optString("rating_date"));
                            map.put("user_name",jsonObject.optString("user_name"));
                            map.put("created_date",jsonObject.optString("created_date"));


                            Adapter adapter=new Adapter();
                            expListView.setAdapter(adapter);
                            AllProducts.add(map);

//                            if (jsonObject.optString("user_id").equals(MyPrefrences.getUserID(getActivity()))){
//                                editText.setText(jsonObject.optString("review"));
//                                rtbar.setRating(Float.parseFloat(jsonObject.optString("rating")));
//                            }
                        }
                    }
                    else{
                        // Toast.makeText(getActivity(), "No Reviews in this Company...", Toast.LENGTH_SHORT).show();
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

    private void popUpForUpdateRating(String msg, final String r_id) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int id) {
                        dialog1.cancel();



                        Util.showPgDialog(dialog);
                        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.updateMyRating,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                        Log.d("ResponseUpdate", response);
                                        Util.cancelPgDialog(dialog);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equalsIgnoreCase("success")) {

//                                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                errorDialog("Rating Updated successfully.");

                                            } else {
                                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        Toast.makeText(getActivity(), "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                                        Util.cancelPgDialog(dialog);
                                    }
                                }
                        ) {


                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("rating_id",r_id);
                                params.put("user_id", MyPrefrences.getUserID(getActivity()));
                                params.put("review",editText.getText().toString());
                                params.put("rating", rate);

                                Log.d("dfdgdfgdfgh1",r_id);
                                Log.d("dfdgdfgdfgh2",MyPrefrences.getUserID(getActivity()));
                                Log.d("dfdgdfgdfgh3",editText.getText().toString());
                                Log.d("dfdgdfgdfgh4",rate);



//                                params.put("business_id", getArguments().getString("id"));
//                                params.put("review", editText.getText().toString());
//                                params.put("rating", rate);
//                                params.put("user_id", MyPrefrences.getUserID(getActivity()));

                                return params;
                            }
                        };
                        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        postRequest.setShouldCache(false);

                        AppController.getInstance().addToRequestQueue(postRequest);



                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int id) {
                        //  Action for 'NO' Button
                        dialog1.cancel();


//                                    Intent intent=new Intent(PayActivity.this,HomeAct.class);
//                                    intent.putExtra("userType","");
//                                    startActivity(intent);
//                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                                    finish();

                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Review");
        alert.show();
    }

    public class Viewholder{
        NetworkImageView iamge;
        TextView comName,date_txt,c1_email,c1_mobile1,uname,review,user_icon;
        LinearLayout liner;
        MaterialRatingBar rtbar2;
    }

    private void errorDialog(String res) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogcustom2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
        text.setText(fromHtml(res));
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Intent intent=new Intent(getActivity(),HomeAct.class);
                intent.putExtra("userType","");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);

//                Fragment fragment=new WriteReview();
//                Bundle bundle=new Bundle();
//                bundle.putString("id",getArguments().getString("id"));
//                bundle.putString("company_name", comName.getText().toString());
//                bundle.putString("address", comAdd.getText().toString());
//                FragmentManager manager=getActivity().getSupportFragmentManager();
//                FragmentTransaction ft=manager.beginTransaction();
//                fragment.setArguments(bundle);
//                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();

            }
        });
        dialog.show();

    }

    class Adapter extends BaseAdapter {

        LayoutInflater inflater;


        Boolean flag=false;
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
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView=inflater.inflate(R.layout.list_view_rating,parent,false);

            final Viewholder viewholder=new Viewholder();

            viewholder.comName=convertView.findViewById(R.id.comName);
           // viewholder.c1_mobile1=convertView.findViewById(R.id.c1_mobile1);
            viewholder.review=convertView.findViewById(R.id.review);
            viewholder.user_icon=convertView.findViewById(R.id.user_icon);
            viewholder.rtbar2=convertView.findViewById(R.id.rtbar2);
            viewholder.date_txt=convertView.findViewById(R.id.date_txt);

//            viewholder.comName.setText(AllProducts.get(position).get("uname"));
            if (!AllProducts.get(position).get("user_name").equals("")) {
                viewholder.comName.setText(AllProducts.get(position).get("user_name").substring(0, 1).toUpperCase() + AllProducts.get(position).get("user_name").substring(1).toUpperCase());
            }
            viewholder.user_icon.setText(AllProducts.get(position).get("user_name").toUpperCase());
//            viewholder.c1_mobile1.setText(AllProducts.get(position).get("mobile"));
//            String number;
//            number="+91-XXXXXXX"+AllProducts.get(position).get("mobile").substring(AllProducts.get(position).get("mobile").length() - 3);
//            viewholder.c1_mobile1.setText(number);
            viewholder.review.setText(AllProducts.get(position).get("review"));
            viewholder.date_txt.setText(AllProducts.get(position).get("created_date").substring(0,10));
            viewholder.rtbar2.setRating(Float.parseFloat(AllProducts.get(position).get("rating")));

            return convertView;
        }
    }

}
