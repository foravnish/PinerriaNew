package pinerria.business.pinerrianew.Activites;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
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

import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.AppController;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

public class SelectZone extends AppCompatActivity {


    List<HashMap<String,String>> AllProducts ;

    Button btnGetQuote;
    Dialog dialog;
    TextView back;
    AutoCompleteTextView autoTextSearch;
    List<String> data=new ArrayList<>();
    List<String> data2=new ArrayList<>();
    String vasa;


    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_zone);

        AllProducts = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv);

        autoTextSearch = (AutoCompleteTextView) findViewById(R.id.autoTextSearch);


        dialog=new Dialog(SelectZone.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.city, null, new Response.Listener<JSONObject>() {

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

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            HashMap<String,String> map=new HashMap();
                            map.put("id",jsonObject.optString("id"));
                            map.put("name",jsonObject.optString("name"));



                            data.add(jsonObject.optString("name"));
                            //   data.add(jsonObject.optString("id"));


//
//                            Adapter adapter=new Adapter();
//                            expListView.setAdapter(adapter);
                            AllProducts.add(map);



                            adapter = new ArrayAdapter<String>(getApplicationContext(),  R.layout.list_city, R.id.textCity, data);
                            lv.setAdapter(adapter);


                            autoTextSearch.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                    // When user changed the Text
                                    SelectZone.this.adapter.getFilter().filter(cs);

                                    Log.d("fsdfsdfsdfsdf", String.valueOf(cs));
                                }

                                @Override
                                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                              int arg3) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void afterTextChanged(Editable arg0) {
                                    // TODO Auto-generated method stub
                                }
                            });


                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                Util.cancelPgDialog(dialog);
            }
        });


        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getApplicationContext(), ""+AllProducts.get(i).get("id"), Toast.LENGTH_SHORT).show();

                MyPrefrences.setCityID(getApplicationContext(),AllProducts.get(i).get("id"));
                MyPrefrences.setCityName(getApplicationContext(),AllProducts.get(i).get("name"));
                MyPrefrences.setCitySelect(getApplicationContext(), true);

                Intent intent=new Intent(SelectZone.this,HomeAct.class);
                intent.putExtra("userType","");
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                Log.d("sdfsdfsdfsdfgsdgfsd",AllProducts.get(i).get("id"));
            }
        });
    }
}
