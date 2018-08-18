package pinerria.business.pinerrianew.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import pinerria.business.pinerrianew.R;

import static android.text.Html.fromHtml;

/**
 * Created by Andriod Avnish on 12-Apr-18.
 */

public class Util {

    public static String CITYID="1";  //// Ghaziabad
    ///////////////show progress dialog for Async Task


    public static int ChatOn;
    public static String Sender_Img = "sd";
    public static boolean isSportBgChn;
    public static int NOTIFICATION_ID = 121;
    public static String API_KEY = "AIzaSyAN1vRVTVvXPAeyUn80ZGarj2oIZzna33g";
    public static int comment_api_call;
    public static String Sender_ID = "0";
    public static String Sender_Name = "not available";
    public static String Noti_Msg = "sd";
    public static int Noti_Bg;
    public static int LoadPro_Image;
    public static int ChatDtl_On;
    public static String Get_Rquest = "0";
    public static int Get_Rquest_InBg;
    public static int Show_Coach_Dtls;
    public static int Get_Comments = 0;
    public static int User_Like = 5;
    public static int Total_Like = 0;
    public static int Set_Total_Like = 0;
    public static int PageRefreshDismis=0;
    public static boolean isAddressSave;
    public static boolean Add_Qty;

    public static  int FindPlayerAsynccountingVaue=0;

    public  static  String FindPlayerAsynccounting;





    public static void showImage(Context context, String url, ImageView imageView) {
        if (!url.isEmpty() && url != null) {
            Picasso.with(context).load(url).placeholder(R.color.colorPrimary).error(R.color.colorPrimaryDark).into(imageView);
        }
    }

    public static void showPgDialog(Dialog dialog) {

        dialog.setContentView(R.layout.dialogprogress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


//        progressDialog.setMessage("Please Wait....");
//        progressDialog.show();
    }

    public static void cancelPgDialog(Dialog dialog) {
//        if (progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    public static void errorDialog(Context context, String message) {
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
                dialog.dismiss();
            }
        });
        dialog.show();

    }


}
