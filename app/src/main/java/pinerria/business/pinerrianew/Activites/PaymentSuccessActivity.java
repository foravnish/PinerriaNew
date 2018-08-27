package pinerria.business.pinerrianew.Activites;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebs.android.sdk.PaymentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pinerria.business.pinerrianew.Fragments.Home;
import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.Utils.Api;
import pinerria.business.pinerrianew.Utils.JSONParser;
import pinerria.business.pinerrianew.Utils.MyPrefrences;
import pinerria.business.pinerrianew.Utils.Util;


public class PaymentSuccessActivity extends Activity {
	String payment_id;
	String PaymentId;
	String AccountId;
	String MerchantRefNo;
	String Amount;
	String DateCreated;
	String Description;
	String Mode;
	String IsFlagged;
	String BillingName;
	String BillingAddress;
	String BillingCity;
	String BillingState;
	String BillingPostalCode;
	String BillingCountry;
	String BillingPhone;
	String BillingEmail;
	String DeliveryName;
	String DeliveryAddress;
	String DeliveryCity;
	String DeliveryState;
	String DeliveryPostalCode;
	String DeliveryCountry;
	String DeliveryPhone;
	String PaymentStatus;
	String PaymentMode;
	String SecureHash;

	LinearLayout tryAgainLayout;
	Button btn_payment_success;
	JSONObject jObject;
	String Type1;
	Dialog dialog,dialog4;
	Spinner spiner1,spiner2;
	String[] spi1={"Select","Excellent","Good","Satisfactory","Needs improvement","Poor"};
	String[] spi2={"Select","Yes","No"};
	JSONObject jsonObject2;
	String paymId,tarID,amount;
	EditText comment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_payment_success);

		Log.d("dfsfsfsfs","true");
		tryAgainLayout=(LinearLayout)findViewById(R.id.ll_button);
		btn_payment_success = (Button) findViewById(R.id.btn_payment_success);
		Intent intent = getIntent();


		payment_id = intent.getStringExtra("payment_id");
		try {

			jsonObject2=new JSONObject(intent.getStringExtra("payment_id"));
			Log.d("gdfgdfghdfh",jsonObject2.optString("PaymentId"));
			MyPrefrences.setPaymentId(getApplicationContext(),jsonObject2.optString("PaymentId"));

			paymId=jsonObject2.optString("PaymentId");
			tarID=jsonObject2.optString("TransactionId");
			//tarID=jsonObject2.optString("scr");
			amount=jsonObject2.optString("Amount");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("fgdgdfgdfgdfhdf",payment_id);

		System.out.println("Success and Failure response to merchant app..." + " " + payment_id);
//
//		if (MyPrefrences.getPaymentId(getApplicationContext()).equalsIgnoreCase(jsonObject2.optString("PaymentId"))){
//			new PostingID("2").execute();
//		}

		getJsonReport();


		try {
			jObject = new JSONObject(payment_id.toString());
			Type1=jObject.getString("BillingName").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		btn_payment_success.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("Status1233","success");

				submitOrderApi(payment_id,paymId,tarID,amount,"success");


				//new PostingID("1").execute();

//				Intent  intent=new Intent(getApplicationContext(), PostAdd.class);
//				intent.putExtra("Cat_id","");
//				intent.putExtra("threemonths","");
//				intent.putExtra("sixmonths","");
//				intent.putExtra("areatype","");

//				intent.putExtra("areatypenum","");
//				intent.putExtra("heading","");
//				intent.putExtra("forPayment",Type1);
//				startActivity(intent);
			}
		});

		Button btn_retry = (Button) findViewById(R.id.btn_retry);
		btn_retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),PaymentActivity.class);
				PaymentSuccessActivity.this.finish();
				startActivity(i);

			}
		});
		Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				submitOrderApi(payment_id,paymId,tarID,amount,"failure");
				//new PostingID("2").execute();
			}
		});

	}


	private void submitOrderApi(final String payment_id, final String p_id, final String txn_id, final String P_amount, final String status) {

		Util.showPgDialog(dialog);
		RequestQueue queue = Volley.newRequestQueue(PaymentSuccessActivity.this);
		StringRequest strReq = new StringRequest(Request.Method.POST,
				Api.updatePurchasedPackage, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Util.cancelPgDialog(dialog);
				Log.e("dfsjfdfsdfgd", "Login Response: " + response);

				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equalsIgnoreCase("success")) {


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
				Toast.makeText(getApplicationContext(), "Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
			}
		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Log.e("fgdfgdfgdf", "Inside getParams");

				// Posting parameters to login url
				Map<String, String> params = new HashMap<>();
				params.put("payment_id", p_id);
				params.put("transaction_id", txn_id);
				params.put("response", payment_id);
				params.put("pay_amount", P_amount);
				params.put("payment_status", status);
				params.put("unique_number", MyPrefrences.getDateTime(getApplicationContext()));



				return params;
			}

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
		};
		// Adding request to request queue
		queue.add(strReq);


	}


//	class PostingID extends AsyncTask<String,Void,String> {
//
//		String status,password;
//		public PostingID(String status){
//
//			this.status=status;
//			dialog=new Dialog(PaymentSuccessActivity.this);
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//			Util.showPgDialog(dialog);
//
//		}
//
//		@Override
//		protected String doInBackground(String... strings) {
//			HashMap<String,String> map=new HashMap<>();
//
//			Log.d("paymentStatus",status.toString());
//
//			//map.put("postingId", MyPrefrences.getPostingId(getApplicationContext()));
//			map.put("paymentStatus", status.toString());
//			map.put("getJsonData", payment_id);
//			map.put("paymentId", paymId);
//			map.put("transactionId", tarID);
//			map.put("amount", amount);
//
//			JSONParser jsonParser=new JSONParser();
//			String result =jsonParser.makeHttpRequest(Api.paymentSuccess,"POST",map);
//
//
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String s) {
//			super.onPostExecute(s);
//
//			Log.e("response", ": " + s);
//
//			Util.cancelPgDialog(dialog);
//			try {
//				final JSONObject jsonObject = new JSONObject(s);
//				if (jsonObject != null) {
//					if (jsonObject.optString("status").equalsIgnoreCase("success")) {
//						//PaymentSuccessActivity.this.finish();
//
//						if (status.toString().equalsIgnoreCase("1")) {
//
//							errorDialog2("1");
//						}
//						else if (status.toString().equalsIgnoreCase("2")) {
//
//							errorDialog2("2");
//
////							Util.errorDialog(PaymentSuccessActivity.this,"Your Posting not listing.");
//
////							final Dialog dialog = new Dialog(PaymentSuccessActivity.this);
////							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////							dialog.setContentView(R.layout.alertdialogcustom);
////							dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////							TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
////							text.setText(fromHtml("Error!\nYour advertisement is not posted successfully."));
////							Button ok = (Button) dialog.findViewById(R.id.btn_ok);
////							ok.setOnClickListener(new OnClickListener() {
////								@Override
////								public void onClick(View view) {
////									Intent intent =new Intent(PaymentSuccessActivity.this, MainActivity.class);
////									intent.putExtra("isflag","0");
////									startActivity(intent);
////									finish();
////
////								}
////							});
////							ok.setOnClickListener(new View.OnClickListener() {
////								@Override
////								public void onClick(View view) {
////									dialog.dismiss();
////								}
////							});
////							dialog.show();
//						}
//
//					}
//					else if (jsonObject.optString("status").equalsIgnoreCase("failure")){
//						errorDialog2("2");
//					}
//
//				}
//			} catch (JSONException e) {
//				Util.errorDialog(PaymentSuccessActivity.this,"Some Error! or Please connect to the internet.");
//				e.printStackTrace();
//			}
//		}
//
//	}

//	private void errorDialog2(final String status) {
//		TextView Yes_action, No_action,btn;
//		TextView heading;
//
//		dialog4 = new Dialog(PaymentSuccessActivity.this);
//		dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog4.setContentView(R.layout.alertdialogcustom_feedback_form);
//		dialog4.setCancelable(false);
//
//		Button ok = (Button) dialog4.findViewById(R.id.btn_ok);
//		Button cancel = (Button) dialog4.findViewById(R.id.cancel);
//
//		heading = (TextView) dialog4.findViewById(R.id.msg_txv);
//		btn = (TextView) dialog4.findViewById(R.id.btn);
//
//		if (status.equalsIgnoreCase("1")){
//			heading.setText("Your advertisement has been posted successfully.\n" +
//					"It will be visible to your targeted customers after the admin approval i.e. within 48 working hours.\n" +
//					"Your Posting ID is : #" + MyPrefrences.getPostingId2(getApplicationContext())+"\n"+
//					"Your Payment ID is : " + paymId);
//			btn.setText("Congratulations!");
//	}
//
//
//	else if(status.equalsIgnoreCase("2")){
//			heading.setText("Payment not successful.  Hence, your ad will not be visible to your clients.");
//			btn.setText("Error Message!");
//	}
//
//		spiner1 = (Spinner) dialog4.findViewById(R.id.spiner1);
//		spiner2 = (Spinner) dialog4.findViewById(R.id.spiner2);
//		comment = (EditText) dialog4.findViewById(R.id.comment);
////
////		comment.setImeOptions(EditorInfo.IME_ACTION_DONE);
////		comment.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
////		comment.setLines(6);
////		comment.setHorizontallyScrolling(true);
////		comment.setSingleLine();
//
//
//		ArrayAdapter aa1 = new ArrayAdapter(PaymentSuccessActivity.this,R.layout.simple_spinner_item,spi1);
//		aa1.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//		spiner1.setAdapter(aa1);
//
//		ArrayAdapter aa2 = new ArrayAdapter(PaymentSuccessActivity.this,R.layout.simple_spinner_item,spi2);
//		aa2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//		spiner2.setAdapter(aa2);
//
//
//		spiner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//				if (i==1){
//					comment.setVisibility(View.VISIBLE);
//				}
//				else if (i==2){
//					comment.setVisibility(View.GONE);
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> adapterView) {
//
//			}
//		});
//
//		ok.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
////                Intent intent=new Intent(PostAdd.this,MainActivity.class);
////                intent.putExtra("isflag","1");
////                startActivity(intent);
//				// feedbackForm();
//				if(checkValidation()){
//					new FeedbackApi(MyPrefrences.getPostingId2(getApplicationContext()),spiner1.getSelectedItem().toString(),spiner2.getSelectedItem().toString(),comment.getText().toString()).execute();
//				}
//
//			}
//		});
//
//		cancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				dialog4.dismiss();
//				if (status.equalsIgnoreCase("1")) {
//					Intent intent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
//					intent.putExtra("isflag", "1");
//					startActivity(intent);
//				}
//				else if (status.equalsIgnoreCase("2")) {
//					Intent intent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
//					intent.putExtra("isflag", "0");
//					startActivity(intent);
//				}
//
//			}
//		});
//
//		dialog4.show();
//	}

	private boolean  checkValidation() {

		 if (spiner1.getSelectedItem().toString().equalsIgnoreCase("Select")){
			Toast.makeText(getApplicationContext(), "Select Experience", Toast.LENGTH_SHORT).show();
			return false;
		}
		else if (spiner2.getSelectedItem().toString().equalsIgnoreCase("Select")){

			Toast.makeText(getApplicationContext(), "Select Technical Difficulties", Toast.LENGTH_SHORT).show();
			return false;
		}

		else if (TextUtils.isEmpty(comment.getText().toString()))
		{
			if (spiner2.getSelectedItem().toString().equalsIgnoreCase("yes")) {
				comment.setError("Oops! Comment blank");
				comment.requestFocus();
				return false;
			}
			else if (spiner2.getSelectedItem().toString().equalsIgnoreCase("no")){
				return true;
			}
		}

		return true;
	}



	private void getJsonReport() {
		String response = payment_id;

		///Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();

		JSONObject jObject;
		try {
			jObject = new JSONObject(response.toString());
			PaymentId = jObject.getString("PaymentId");
			AccountId = jObject.getString("AccountId");
			MerchantRefNo = jObject.getString("MerchantRefNo");
			Amount = jObject.getString("Amount");
			DateCreated = jObject.getString("DateCreated");
			Description = jObject.getString("Description");
			Mode = jObject.getString("Mode");
			IsFlagged = jObject.getString("IsFlagged");
			BillingName = jObject.getString("BillingName");
			BillingAddress = jObject
					.getString("BillingAddress");
			BillingCity = jObject.getString("BillingCity");
			BillingState = jObject.getString("BillingState");
			BillingPostalCode = jObject
					.getString("BillingPostalCode");
			BillingCountry = jObject
					.getString("BillingCountry");
			BillingPhone = jObject.getString("BillingPhone");
			BillingEmail = jObject.getString("BillingEmail");
			DeliveryName = jObject.getString("DeliveryName");
			DeliveryAddress = jObject
					.getString("DeliveryAddress");
			DeliveryCity = jObject.getString("DeliveryCity");
			DeliveryState = jObject.getString("DeliveryState");
			DeliveryPostalCode = jObject
					.getString("DeliveryPostalCode");
			DeliveryCountry = jObject
					.getString("DeliveryCountry");
			DeliveryPhone = jObject.getString("DeliveryPhone");
			PaymentStatus = jObject.getString("PaymentStatus");
			PaymentMode = jObject.getString("PaymentMode");
			SecureHash = jObject.getString("SecureHash");
			System.out.println("paymentid_rsp" + PaymentId);

			if(PaymentStatus.equalsIgnoreCase("failed")){
				Log.d("sdfsfsdfsf","failed");
				tryAgainLayout.setVisibility(View.VISIBLE);
				btn_payment_success.setVisibility(View.GONE);
			}else{
				Log.d("sdfsfsdfsf","success");
				btn_payment_success.setVisibility(View.VISIBLE);
				tryAgainLayout.setVisibility(View.GONE);
			}

			TableLayout table_payment = (TableLayout) findViewById(R.id.table_payment);
			ArrayList<String> arrlist = new ArrayList<String>();
			arrlist.add("PaymentId");
			//arrlist.add("AccountId ");
			arrlist.add("MerchantRefNo");
			arrlist.add("Amount");
			arrlist.add("DateCreated");
			//arrlist.add("Description");
			//arrlist.add("Mode");
			//arrlist.add("IsFlagged");
			//arrlist.add("BillingName");
			//rrlist.add("BillingAddress");
			//arrlist.add("BillingCity");
			//arrlist.add("BillingState");
			//arrlist.add("BillingPostalCode");
			//arrlist.add("BillingCountry");
			//arrlist.add("BillingPhone");
			//arrlist.add("BillingEmail");
			//arrlist.add("DeliveryName");
			//arrlist.add("DeliveryAddress");
			//arrlist.add("DeliveryCity");
			//arrlist.add("DeliveryState");
			//arrlist.add("DeliveryPostalCode");
			//arrlist.add("DeliveryCountry");
			//arrlist.add("DeliveryPhone");
			arrlist.add("PaymentStatus");
			arrlist.add("PaymentMode");
			arrlist.add("SecureHash");

			ArrayList<String> arrlist1 = new ArrayList<String>();
			arrlist1.add(PaymentId);
			//arrlist1.add(AccountId );
			arrlist1.add(MerchantRefNo);
			arrlist1.add(Amount);
			arrlist1.add(DateCreated);
//			arrlist1.add(Description);
//			arrlist1.add(Mode);
//			arrlist1.add(IsFlagged);
//			arrlist1.add(BillingName);
//			arrlist1.add(BillingAddress);
//			arrlist1.add(BillingCity);
//			arrlist1.add(BillingState);
//			arrlist1.add(BillingPostalCode);
//			arrlist1.add(BillingCountry);
//			arrlist1.add(BillingPhone);
//			arrlist1.add(BillingEmail);
//			arrlist1.add(DeliveryName);
//			arrlist1.add(DeliveryAddress);
//			arrlist1.add(DeliveryCity);
//			arrlist1.add(DeliveryState);
//			arrlist1.add(DeliveryPostalCode);
//			arrlist1.add(DeliveryCountry);
//			arrlist1.add(DeliveryPhone);
			arrlist1.add(PaymentStatus);
			arrlist1.add(PaymentMode);
			arrlist1.add(SecureHash);

			for(int i=0;i<arrlist.size();i++){
				TableRow row = new TableRow(this);

				TextView textH = new TextView(this);
				TextView textC = new TextView(this);
				TextView textV = new TextView(this);

				textH.setText(arrlist.get(i));
				textC.setText(":  ");
				textV.setText(arrlist1.get(i));
				textV.setTypeface(null, Typeface.BOLD);

				row.addView(textH);
				row.addView(textC);
				row.addView(textV);

				table_payment.addView(row);
			}
			
			/*tv_payment_id.setText("PaymentId : " + PaymentId
					+ "\n" + "AccountId : " + AccountId + "\n"
					+ "MerchantRefNo : " + MerchantRefNo + "\n"
					+ "Amount : " + Amount + "\n"
					+ "DateCreated : " + DateCreated + "\n"
					+ "Description : " + Description + "\n"
					+ "Mode : " + Mode + "\n" + "IsFlagged: "
					+ IsFlagged + "\n" + "BillingName : "
					+ BillingName + "\n" + "BillingAddress : "
					+ BillingAddress + "\n" + "BillingCity : "
					+ BillingCity + "\n" + "BillingState : "
					+ BillingState + "\n"
					+ "BillingPostalCode : " + BillingPostalCode
					+ "\n" + "BillingCountry : " + BillingCountry
					+ "\n" + "BillingPhone : " + BillingPhone
					+ "\n" + "BillingEmail : " + BillingEmail
					+ "\n" + "DeliveryName : " + DeliveryName
					+ "\n" + "DeliveryAddress : "
					+ DeliveryAddress + "\n" + "DeliveryCity : "
					+ DeliveryCity + "\n" + "DeliveryState : "
					+ DeliveryState + "\n"
					+ "DeliveryPostalCode : "
					+ DeliveryPostalCode + "\n"
					+ "DeliveryCountry : " + DeliveryCountry
					+ "\n" + "DeliveryPhone : " + DeliveryPhone
					+ "\n" + "PaymentStatus : " + PaymentStatus
					+ "\n" + "PaymentMode : " + PaymentMode
					+ "\n" + "SecureHash : " + SecureHash + "\n");*/

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


//	class FeedbackApi extends AsyncTask<String, Void, String> {
//
//		String pId, val1,val2,comm;
//
//		public FeedbackApi(String pId, String val1, String val2, String comm) {
//
//			this.pId = pId;
//			this.val1 = val1;
//			this.val2 = val2;
//			this.comm = comm;
//			dialog = new Dialog(PaymentSuccessActivity.this);
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//			Util.showPgDialog(dialog);
//
//		}
//
//		@Override
//		protected String doInBackground(String... strings) {
//			HashMap<String, String> map = new HashMap<>();
//
//			map.put("userId", MyPrefrences.getUserID(getApplicationContext()));
//			map.put("postinfId", pId);
//			map.put("exp", val1.toString());
//			map.put("technical", val2.toString());
//			map.put("comment", comm.toString());
//
//			JSONParser jsonParser = new JSONParser();
//			String result = jsonParser.makeHttpRequest(Api.feedbackSend, "POST", map);
//
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String s) {
//			super.onPostExecute(s);
//
//			Log.e("response", ": " + s);
//
//			Util.cancelPgDialog(dialog);
//			try {
//				final JSONObject jsonObject = new JSONObject(s);
//				if (jsonObject != null) {
//					if (jsonObject.optString("status").equalsIgnoreCase("success")) {
//
//						Toast.makeText(getApplicationContext(), "Your Feedback Submitted Successfully...", Toast.LENGTH_LONG).show();
//
//						dialog4.dismiss();
//						Intent intent = new Intent(PaymentSuccessActivity.this, HomeAct.class);
//						intent.putExtra("isflag", "1");
//						startActivity(intent);
//
//
//					} else {
//						Util.errorDialog(PaymentSuccessActivity.this, jsonObject.optString("message"));
//					}
//				}
//			} catch (JSONException e) {
//				Util.errorDialog(PaymentSuccessActivity.this, "Some Error! or Please connect to the internet.");
//				e.printStackTrace();
//			}
//		}
//
//	}


	private void errorDialog2(final String status) {
		TextView Yes_action, No_action,btn;
		TextView heading;

		dialog4 = new Dialog(PaymentSuccessActivity.this);
		dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog4.setContentView(R.layout.alertdialogcustom_feedback_form);
		dialog4.setCancelable(false);


		Button cancel = (Button) dialog4.findViewById(R.id.cancel);

		heading = (TextView) dialog4.findViewById(R.id.msg_txv);


		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog4.dismiss();

				Intent intent=new Intent(getApplicationContext(),HomeAct.class);
				intent.putExtra("userType","");
				startActivity(intent);
				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
				finish();



			}
		});

		dialog4.show();
	}




}