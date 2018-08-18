package pinerria.business.pinerrianew.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pinerria.business.pinerrianew.Activites.Login;
import pinerria.business.pinerrianew.Activites.PayActivity;
import pinerria.business.pinerrianew.Activites.PayOrderAct;
import pinerria.business.pinerrianew.Activites.Registration;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.Const;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayOrder extends Fragment {


    public PayOrder() {
        // Required empty public constructor
    }

    Button submit;
    EditText oder_id,user_email,tax_address,gst_number,company_name;
    Dialog dialog;
    LinearLayout linerlaout;
    TextView descreption,amount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pay_order, container, false);

        submit=view.findViewById(R.id.submit);

        oder_id=view.findViewById(R.id.oder_id);


        linerlaout=view.findViewById(R.id.linerlaout);
        amount=view.findViewById(R.id.amount);
        descreption=view.findViewById(R.id.descreption);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
       // Util.showPgDialog(dialog);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {


                    Util.showPgDialog(dialog);
                    JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET,
                            Api.orderIdDetails + "/" + oder_id.getText().toString(), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Resposeorder", response.toString());
                            Util.cancelPgDialog(dialog);

                            if (response.optString("status").equalsIgnoreCase("success")) {

                                try {
                                    JSONArray jsonArray = response.getJSONArray("message");
                                    final JSONObject jsonObject = jsonArray.getJSONObject(0);

                                    Intent intent = new Intent(getActivity(), PayOrderAct.class);
                                    intent.putExtra("jsonArray", jsonArray.toString());
                                    getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                    startActivity(intent);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else if (response.optString("status").equalsIgnoreCase("failure")) {
                                Util.errorDialog(getActivity(), response.optString("message"));

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
                }
            }
        });
        return view;
    }



    private boolean validate(){

        if (TextUtils.isEmpty(oder_id.getText().toString()))
        {
            Util.errorDialog(getActivity(),"Type Order Id");
            return false;
        }



        return true;

    }





}

