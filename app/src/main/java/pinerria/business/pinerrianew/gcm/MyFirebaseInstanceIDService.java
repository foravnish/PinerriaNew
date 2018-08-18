package pinerria.business.pinerrianew.gcm;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


import pinerria.business.pinerrianew.Utils.MyPrefrences;

/**
 * Created by user on 11/2/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    Context context;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d(TAG, "Refreshed token: " + refreshedToken);
        Log.d("sdfsdgfsgfs",refreshedToken);
        MyPrefrences.setgcm_token(this,refreshedToken);





        //Displaying token on logcat
    }
}
