package com.restaurant.ridewise.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import es.dmoral.toasty.Toasty;

public class PaymentActivity extends AppCompatActivity {
    String TAG = "cart";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    RelativeLayout rl_progress;

    TextView tv_header,tv_total,tv_total_value,tv_select_mode,tv_place_order;
    RadioButton rb_cod,rb_online_payment;
    ImageView iv_back;
    String total_amount;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initialization();
        function();
    }


    private void initialization() {
        globalClass = (GlobalClass)getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();
        rl_progress = findViewById(R.id.rl_progress);

        tv_header = findViewById(R.id.tv_header);
        tv_total = findViewById(R.id.tv_total);
        tv_total_value = findViewById(R.id.tv_total_value);
        tv_select_mode = findViewById(R.id.tv_select_mode);
        rb_cod = findViewById(R.id.rb_cod);
        rb_online_payment = findViewById(R.id.rb_online_payment);
        tv_place_order = findViewById(R.id.tv_place_order);
        iv_back = findViewById(R.id.iv_back);

    }
    private void function() {

        String total_amount= getIntent().getStringExtra("total_amount");
        String sub_total= getIntent().getStringExtra("sub_total");
        String delivery_charge= getIntent().getStringExtra("delivery_charge");
        String tax_charge= getIntent().getStringExtra("tax_charge");

        tv_total_value.setText("₹  "+total_amount);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(new Date());
        Log.d(TAG, "function: time"+time);

        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        tv_header.setTypeface(medium);

        tv_total.setTypeface(regular);
        tv_total_value.setTypeface(regular);
        tv_select_mode.setTypeface(regular);
        tv_place_order.setTypeface(regular);

        rb_cod.setTypeface(light);
        rb_online_payment.setTypeface(light);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_order(total_amount,sub_total,delivery_charge,time,tax_charge);
            }
        });


    }


    private void place_order(String total_amount,String sub_total,String delivery_charge, String time, String tax_charge) {
        //  mView.showDialog();
        tv_place_order.setEnabled(false);
        rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "place_order";
        String url = ApplicationConstants.order;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "place_order Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                       // ((CartActivity)context).cart_details();
                        rl_progress.setVisibility(View.GONE);
                        tv_place_order.setEnabled(true);
                     //   Toasty.success(PaymentActivity.this, message, Toast.LENGTH_LONG).show();

                        message_dialog(message);



                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    rl_progress.setVisibility(View.GONE);

                    //  mView.hideDialog();

                }
            }, new Response.ErrorListener() {

                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "place_order Error: " + error.getMessage());
                    Toast.makeText(PaymentActivity.this, "Connection Error", Toast.LENGTH_LONG).show();
                    rl_progress.setVisibility(View.GONE);

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id",globalClass.getUser_id());
                    params.put("lat","");
                    params.put("long","");
                    params.put("address",globalClass.getUser_address());
                    params.put("total_price",total_amount);
                    params.put("user_lat",globalClass.getUser_lat());
                    params.put("user_long",globalClass.getUser_long());
                    params.put("order_time",time);
                    params.put("res_id",globalClass.getId());
                    params.put("delivery_charge",delivery_charge);
                    params.put("sub_total",sub_total);
                    params.put("tax_charge",tax_charge);

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(PaymentActivity.this, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void message_dialog(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PaymentActivity.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        /*builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        dialog.cancel();
                        menu_delete(menu_id);
                    }
                });*/

        builder1.setNegativeButton(
                "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        Intent intent = new Intent(PaymentActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}
