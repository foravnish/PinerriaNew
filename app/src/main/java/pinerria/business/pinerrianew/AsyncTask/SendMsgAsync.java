package pinerria.business.pinerrianew.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import pinerria.business.pinerrianew.Utils.Api;

import pinerria.business.pinerrianew.Utils.JSONParser;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;

/**
 * Created by user on 12/9/2016.
 */

public class SendMsgAsync extends AsyncTask<String, String, JSONObject> {
    private Context context;
    ProgressDialog progressDialog;
    String message,receive_id;

    public SendMsgAsync(Context context, String message,String receive_id) {
        this.context = context;
        this.message = message;
        this.receive_id = receive_id;

    }

    protected void onPreExecute() {
        // // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Send token");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        //  progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params1) {
        // constructor
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("sender_id", MyPrefrences.getUserID(context));
        params.put("message", message);
        params.put("reciver_id", receive_id);
       // params.put("sender_name", MyPrefrences.getUSENAME(context));
       // params.put("sender_image", PlayerDetails.getImage_url());
      //  params.put("date_time", Util.getTimeStamp());

        Log.d("fdgfdgdfgd1",MyPrefrences.getUserID(context));
        Log.d("fdgfdgdfgd2",message);
//        Log.d("fdgfdgdfgd3",Util.Sender_ID);
        Log.d("fdgfdgdfgd3",receive_id);
        Log.d("fdgfdgdfgd4",MyPrefrences.getUSENAME(context));
    //    Log.d("fdgfdgdfgd5",PlayerDetails.getImage_url());
      //  Log.d("fdgfdgdfgd6",Util.getTimeStamp());

        JSONParser parser = new JSONParser();
        JSONObject json = parser.makeHttpRequest(Api.URL_SEND_MESSAGE, "POST", params);
        //Log.e("response ", "" + json);
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        // TODO Auto-generated method stub
        super.onPostExecute(jsonObject);
        // progressDialog.cancel();

        Log.d("dfgdgfdgdfgd", String.valueOf(jsonObject));
        try {
            if (jsonObject.getString("status").equalsIgnoreCase("success")) {

            }
            else {
                Toast.makeText(context, "response " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            Log.d("draw root", "" + e);
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }
}
