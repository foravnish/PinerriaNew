package pinerria.business.pinerrianew.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUs extends Fragment {


    public ContactUs() {
        // Required empty public constructor
    }

    LinearLayout email_now,message_now,call_now;
    TextView website,emailId,mobileNo;
    Dialog dialog,dialog4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact_us, container, false);

        call_now=view.findViewById(R.id.call_now);
        message_now=view.findViewById(R.id.message_now);
        email_now=view.findViewById(R.id.email_now);

        mobileNo=view.findViewById(R.id.mobileNo);
        emailId=view.findViewById(R.id.emailId);
        website=view.findViewById(R.id.website);

        HomeAct.title.setText("Contact Us");

        call_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button Yes_action,No_action;
                TextView heading;
                dialog4 = new Dialog(getActivity());
                dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog4.setContentView(R.layout.update_state1);

                Yes_action=(Button)dialog4.findViewById(R.id.Yes_action);
                No_action=(Button)dialog4.findViewById(R.id.No_action);
                heading=(TextView)dialog4.findViewById(R.id.heading);


                heading.setText("Do you want to Call Now?");
                Yes_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog4.dismiss();
                        try
                        {
                            if(Build.VERSION.SDK_INT > 22)
                            {

                                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);
                                    return;
                                }

                                Log.d("fsdfgsdgfsdg",mobileNo.getText().toString());
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + mobileNo.getText().toString()));
                                startActivity(callIntent);
                            }
                            else {

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + mobileNo.getText().toString()));
                                startActivity(callIntent);
                            }
                        }
                        catch (Exception ex)
                        {ex.printStackTrace();
                        }

                    }
                });

                No_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog4.dismiss();
                    }
                });
                dialog4.show();
            }
        });
        message_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Button Yes_action,No_action;
                        TextView heading;
                        dialog4 = new Dialog(getActivity());
                        dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog4.setContentView(R.layout.update_state1);

                        Yes_action=(Button)dialog4.findViewById(R.id.Yes_action);
                        No_action=(Button)dialog4.findViewById(R.id.No_action);
                        heading=(TextView)dialog4.findViewById(R.id.heading);


                        heading.setText("Do you want to Message Now?");
                        Yes_action.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent intent = new Intent("android.intent.action.VIEW");
//
//                                Uri data = Uri.parse("sms:");
//                                intent.setData(data);
//                                startActivity(intent);


                                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                                smsIntent.setType("vnd.android-dir/mms-sms");
                                smsIntent.putExtra("address", mobileNo.getText().toString());
                                smsIntent.putExtra("sms_body","Body of Message");
                                startActivity(smsIntent);


                                dialog4.dismiss();

                            }
                        });

                        No_action.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog4.dismiss();
                            }
                        });
                        dialog4.show();


                    }
                });
        email_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Button Yes_action,No_action;
                        TextView heading;
                        dialog4 = new Dialog(getActivity());
                        dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog4.setContentView(R.layout.update_state1);

                        Yes_action=(Button)dialog4.findViewById(R.id.Yes_action);
                        No_action=(Button)dialog4.findViewById(R.id.No_action);
                        heading=(TextView)dialog4.findViewById(R.id.heading);


                        heading.setText("Do you want to Email Now?");
                        Yes_action.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto",emailId.getText().toString(), null));
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Pinerria");
                                intent.putExtra(Intent.EXTRA_TEXT, "");
                                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                                dialog4.dismiss();

                            }
                        });

                        No_action.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog4.dismiss();
                            }
                        });
                        dialog4.show();


                    }
                });

        return view;
    }

}
