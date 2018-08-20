package pinerria.business.pinerrianew.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.MyPrefrences;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranscationDetails extends Fragment {


    public TranscationDetails() {
        // Required empty public constructor
    }

    TextView name,company_name,package_name,package_duration,payment_id,purchase_date,transaction_id,expiry_date,actual_value,discount_percent,after_discount_price,total_amount,gst,status;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_transcation_details, container, false);


        name=view.findViewById(R.id.name);
        company_name=view.findViewById(R.id.company_name);
        package_name=view.findViewById(R.id.package_name);
        package_duration=view.findViewById(R.id.package_duration);
        payment_id=view.findViewById(R.id.payment_id);
        purchase_date=view.findViewById(R.id.purchase_date);
        transaction_id=view.findViewById(R.id.transaction_id);
        expiry_date=view.findViewById(R.id.expiry_date);
        actual_value=view.findViewById(R.id.actual_value);
        discount_percent=view.findViewById(R.id.discount_percent);
        after_discount_price=view.findViewById(R.id.after_discount_price);
        gst=view.findViewById(R.id.gst);
        total_amount=view.findViewById(R.id.total_amount);
        status=view.findViewById(R.id.status);

        HomeAct.title.setText("Transaction Detail");

        Log.d("fdbgdfdfhdfhdf",getArguments().getString("jsonArray"));

        try {
            JSONObject jsonObject=new JSONObject(getArguments().getString("jsonArray"));

            String upperString = MyPrefrences.getUSENAME(getActivity()).substring(0,1).toUpperCase() + MyPrefrences.getUSENAME(getActivity()).substring(1);
            name.setText(upperString);
            company_name.setText(jsonObject.optString("company_name"));
            String upperStatus = jsonObject.optString("payment_status").substring(0,1).toUpperCase() + jsonObject.optString("payment_status").substring(1);

            status.setText(upperStatus);
            package_name.setText(jsonObject.optString("package_name"));
            package_duration.setText(jsonObject.optString("package_duration")+" Months");
            payment_id.setText(jsonObject.optString("payment_id"));
            transaction_id.setText(jsonObject.optString("transaction_id"));
            actual_value.setText("₹ "+jsonObject.optString("actual_value"));
            discount_percent.setText("₹ "+jsonObject.optString("discount_percent")+" %");
            after_discount_price.setText("₹ "+jsonObject.optString("after_discount_price"));
            gst.setText("₹ "+jsonObject.optString("gst")+" %");
            total_amount.setText("₹ "+jsonObject.optString("total_amount"));

            String year=jsonObject.optString("purchase_date").substring(0,4);
            String month=jsonObject.optString("purchase_date").substring(5,7);
            String day=jsonObject.optString("purchase_date").substring(8,10);
            purchase_date.setText(day+"-"+month+"-"+year);

            String year2=jsonObject.optString("expiry_date").substring(0,4);
            String month2=jsonObject.optString("expiry_date").substring(5,7);
            String day2=jsonObject.optString("expiry_date").substring(8,10);
            expiry_date.setText(day2+"-"+month2+"-"+year2);

//            Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "muli.ttf");
//            Typeface face_bold=Typeface.createFromAsset(getActivity().getAssets(), "muli_bold.ttf");
//            name.setTypeface(face_bold);
//            company_name.setTypeface(face);
//            package_name.setTypeface(face);
//            package_duration.setTypeface(face);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

}
