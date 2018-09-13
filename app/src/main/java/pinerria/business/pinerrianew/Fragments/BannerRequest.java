package pinerria.business.pinerrianew.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.Activites.PayActivity;
import pinerria.business.pinerrianew.Activites.Registration;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

import static android.text.Html.fromHtml;

/**
 * A simple {@link Fragment} subclass.
 */
public class BannerRequest extends Fragment {


    public BannerRequest() {
        // Required empty public constructor
    }

    EditText pName,cName,mobile,email,require;
    Button submitReq;
    Dialog dialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_banner_request, container, false);

        HomeAct.title.setText("Request a Banner");
        submitReq=view.findViewById(R.id.submitReq);

        pName=view.findViewById(R.id.pName);
        cName=view.findViewById(R.id.cName);
        mobile=view.findViewById(R.id.mobile);
        email=view.findViewById(R.id.email);
        require=view.findViewById(R.id.require);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(
                Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        //cName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
       // cName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
       // require.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        mobile.setText(MyPrefrences.getMobile(getActivity()));
     //   pName.setText(MyPrefrences.getUSENAME(getActivity()).toUpperCase());

        submitReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()){

                    submitBannerRequest();
                }

            }
        });

        return view;
    }


    private void submitBannerRequest() {
        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.bannerRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("BannerRequestResponse", "Response: " + response);

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.optString("status").equalsIgnoreCase("success")){
//                        Util.errorDialog(getActivity(),jsonObject.optString("message"));
                        errorDialog(getActivity(),jsonObject.optString("message"));

                    }
                    else{
                        Util.errorDialog(getActivity(),jsonObject.optString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.cancelPgDialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", MyPrefrences.getUserID(getActivity()));
                params.put("person_name", pName.getText().toString());
                params.put("company_name", cName.getText().toString());
                params.put("mobile", mobile.getText().toString());
                params.put("email", email.getText().toString());
                params.put("requirement", require.getText().toString());

                return params;
            }
//
        };
        queue.add(strReq);

    }


    private boolean validate(){

        if (TextUtils.isEmpty(pName.getText().toString()))
        {
           Util.errorDialog(getActivity(),"Enter Person Name");
            return false;
        }
        else if (TextUtils.isEmpty(mobile.getText().toString()))
        {
            Util.errorDialog(getActivity(),"Enter Mobile No");
            return false;
        }
        else if (mobile.getText().toString().length()<10)
        {
            Util.errorDialog(getActivity(),"Enter 10 digit Mobile No.");
            return false;
        }
        else if (TextUtils.isEmpty(email.getText().toString()))
        {
            Util.errorDialog(getActivity(),"Enter Email Id");
            return false;
        }
        else if (!email.getText().toString().trim().matches(emailPattern))
        {
            Util.errorDialog(getActivity(),"Enter valid Email Id");
            return false;
        }
        else if (TextUtils.isEmpty(require.getText().toString()))
        {
            Util.errorDialog(getActivity(),"Enter Requirement");
            return false;
        }



        return true;

    }

    public void errorDialog(final Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogcustom2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
        text.setText(fromHtml(message));
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog.dismiss();

                Intent intent = new Intent(getActivity(), HomeAct.class);
                intent.putExtra("userType","");
                startActivity(intent);
                getActivity().finish();

            }
        });
        dialog.show();

    }



}
