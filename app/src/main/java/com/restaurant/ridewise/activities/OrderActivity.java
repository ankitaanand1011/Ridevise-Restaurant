package com.restaurant.ridewise.activities;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.adapter.OrderAdapter;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class OrderActivity extends AppCompatActivity {
    String TAG = "dashboard";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    RelativeLayout rl_progress;

    ImageView iv_back;
    TextView tv_add,tv_order_details,tv_customer_details,tv_add_orders,tv_order_status,
            tv_order_status_val,tv_order_by,tv_order_by_no,tv_start,tv_end;
    EditText et_t_amount,et_delivery_charge,et_amount,et_qty,et_item_details,et_pin,et_landmark,
            et_address,et_phn,et_customer_name;
    CheckBox cb_paid;
    String id;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initialization();
        function();
    }

    private void initialization() {
        globalClass = (GlobalClass)getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();
        rl_progress = findViewById(R.id.rl_progress);

        iv_back = findViewById(R.id. iv_back);
        tv_add = findViewById(R.id. tv_add);
        cb_paid = findViewById(R.id. cb_paid);
        et_t_amount = findViewById(R.id. et_t_amount);
        et_delivery_charge = findViewById(R.id. et_delivery_charge);
        et_amount = findViewById(R.id. et_amount);
        et_qty = findViewById(R.id. et_qty);
        et_item_details = findViewById(R.id. et_item_details);
        tv_order_details = findViewById(R.id. tv_order_details);
        et_pin = findViewById(R.id. et_pin);
        et_landmark = findViewById(R.id. et_landmark);
        et_address = findViewById(R.id. et_address);
        et_phn = findViewById(R.id. et_phn);
        et_customer_name = findViewById(R.id. et_customer_name);
        tv_customer_details = findViewById(R.id. tv_customer_details);
        tv_add_orders = findViewById(R.id. tv_add_orders);
        tv_order_status = findViewById(R.id. tv_order_status);
        tv_order_status_val = findViewById(R.id. tv_order_status_val);
        tv_order_by = findViewById(R.id. tv_order_by);
        tv_order_by_no = findViewById(R.id. tv_order_by_no);
        tv_start = findViewById(R.id. tv_start);
        tv_end = findViewById(R.id. tv_end);

    }

    private void function() {

        id = getIntent().getStringExtra("id");

        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");


        cb_paid.setTypeface(light);
        et_t_amount.setTypeface(light);
        et_delivery_charge.setTypeface(light);
        et_amount.setTypeface(light);
        et_qty.setTypeface(light);
        et_pin.setTypeface(light);
        et_item_details.setTypeface(light);
        et_landmark.setTypeface(light);
        et_address.setTypeface(light);
        et_phn.setTypeface(light);
        et_customer_name.setTypeface(light);
        tv_order_status_val.setTypeface(light);
        tv_start.setTypeface(light);
        tv_end.setTypeface(light);

        tv_order_by.setTypeface(regular);
        tv_order_by_no.setTypeface(regular);


        tv_add.setTypeface(medium);
        tv_add_orders.setTypeface(medium);


        tv_order_details.setTypeface(semi_bold);
        tv_customer_details.setTypeface(semi_bold);
        tv_order_status.setTypeface(semi_bold);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        order_details();
    }


    public void order_details() {
        //  mView.showDialog();

        rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "order_details";
        String url = ApplicationConstants.order_details;

        Log.d(TAG, "order_details: url > "+url);

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "order_details Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        if(status.equals("1")) {

                            JsonObject data = jobj.getAsJsonObject("response");
                            JsonObject order_details = data.getAsJsonObject("order_details");

                            String order_id = order_details.get("order_id").getAsString().replaceAll("\"", "");
                            String user_id = order_details.get("user_id").getAsString().replaceAll("\"", "");
                            String address = order_details.get("address").getAsString().replaceAll("\"", "");
                            String user_lat = order_details.get("user_lat").getAsString().replaceAll("\"", "");
                            String user_long = order_details.get("user_long").getAsString().replaceAll("\"", "");
                            String order_date = order_details.get("order_date").getAsString().replaceAll("\"", "");
                            String order_time = order_details.get("order_time").getAsString().replaceAll("\"", "");
                            String sub_total = order_details.get("sub_total").getAsString().replaceAll("\"", "");
                            String total_price = order_details.get("total_price").getAsString().replaceAll("\"", "");
                            String delivery_charge = order_details.get("delivery_charge").getAsString().replaceAll("\"", "");
                            String order_status = order_details.get("status").getAsString().replaceAll("\"", "");


                            StringBuilder all_items_name = new StringBuilder();
                            int qtySum=0;
                            int qtyNum;

                            JsonArray item_details = data.getAsJsonArray("item_details");
                            Log.d(TAG, "onResponse item_details: "+item_details);

                            for( int i = 0 ; i < item_details.size() ; i++ ) {

                                JsonObject obj_data = item_details.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  " + obj_data);
                                String title = obj_data.get("title").getAsString().replaceAll("\"", "");
                                String qty = obj_data.get("qty").getAsString().replaceAll("\"", "");

                                all_items_name.append(title).append(','+" ");

                                qtyNum = Integer.parseInt(qty);
                                qtySum += qtyNum;

                            }

                            JsonArray consumer_details = data.getAsJsonArray("consumer_details");
                            Log.d(TAG, "onResponse item_details: "+item_details);

                            for( int i = 0 ; i < consumer_details.size() ; i++ ) {

                                JsonObject obj_data = consumer_details.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  " + obj_data);
                                String c_name = obj_data.get("name").getAsString().replaceAll("\"", "");
                                String c_phone = obj_data.get("phone").getAsString().replaceAll("\"", "");
                                String c_address = obj_data.get("address").getAsString().replaceAll("\"", "");
                                String c_postal_code = obj_data.get("postal_code").getAsString().replaceAll("\"", "");


                                et_customer_name.setText(c_name);
                                et_phn.setText(c_phone);
                                et_address.setText(c_address);
                                et_pin.setText(c_postal_code);

                            }

                            double s_total = Double.parseDouble(sub_total);
                            @SuppressLint("DefaultLocale")
                            String su_total = String.format("%.2f", s_total);
                            double d_charge = Double.parseDouble(delivery_charge);
                            @SuppressLint("DefaultLocale")
                            String del_charge = String.format("%.2f", d_charge);
                            double total = Double.parseDouble(total_price);
                            @SuppressLint("DefaultLocale")
                            String total_pr = String.format("%.2f", total);



                            et_item_details.setText(all_items_name);
                            et_qty.setText(String.valueOf(qtySum));
                            et_amount.setText("₹  "+su_total);
                            et_delivery_charge.setText("₹  "+del_charge);
                            et_t_amount.setText("₹  "+total_pr);

                            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
                            Date newDate=spf.parse(order_date);
                            spf= new SimpleDateFormat("dd-MM-yyyy");
                            String s_date = spf.format(newDate);
                           
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
                            Date dt;
                            String s_time = "";
                            try {
                                dt = sdf.parse(order_time);
                                 s_time = sdfs.format(dt);
                         
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            tv_start.setText(s_date+", "+s_time);


                            switch (order_status) {
                                case "1":
                                    tv_order_status_val.setText("Order placed");
                                    break;
                                case "2":
                                    tv_order_status_val.setText("In progress");
                                    break;
                                case "3":
                                    tv_order_status_val.setText("Completed");
                                    break;
                                case "4":
                                    tv_order_status_val.setText("Canceled");
                                    break;
                            }

                            rl_progress.setVisibility(View.GONE);


                        }else{

                            Toasty.info(OrderActivity.this, message, Toast.LENGTH_LONG).show();
                        }

                        rl_progress.setVisibility(View.GONE);

                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    //  mView.hideDialog();

                }
            }, new Response.ErrorListener() {

                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "order_list Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    rl_progress.setVisibility(View.GONE);

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("order_id",id);

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(OrderActivity.this, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}