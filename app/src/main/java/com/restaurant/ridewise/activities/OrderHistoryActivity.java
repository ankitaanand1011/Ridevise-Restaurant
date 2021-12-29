package com.restaurant.ridewise.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.adapter.ItemsAdapter;
import com.restaurant.ridewise.adapter.OrderHistoryAdapter;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class OrderHistoryActivity extends AppCompatActivity {
    String TAG = "order_history";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    ImageView iv_back;
    TextView tv_header;
    RelativeLayout rl_progress;
    RecyclerView rv_order_history;
    private ArrayList<HashMap<String, String>> order_list;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        initialization();
        function();
    }

    private void initialization() {
        globalClass = (GlobalClass)getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();
        rl_progress = findViewById(R.id.rl_progress);

        iv_back = findViewById(R.id. iv_back);
        tv_header = findViewById(R.id.tv_header);
        rv_order_history = findViewById(R.id.rv_order_history);

    }

    private void function() {

        order_list = new ArrayList<>();

      /*  order_list.add("#ord-20210612-001");
        order_list.add("#ord-20210612-002");
        order_list.add("#ord-20210612-003");
        order_list.add("#ord-20210612-004");
        order_list.add("#ord-20210612-005");*/

        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        tv_header.setTypeface(medium);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        order_history();


    }

    private void order_history() {
        //  mView.showDialog();

        rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "order_history";
        String url = ApplicationConstants.order_history;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "order_history Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        if(status.equals("1")) {

                            JsonArray data = jobj.getAsJsonArray("response");
                            Log.d(TAG, "onResponse data: "+data);

                            for( int i = 0 ; i < data.size() ; i++ ) {

                                JsonObject obj_data = data.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String order_id = obj_data.get("order_id").getAsString().replaceAll("\"", "");;
                                String address = obj_data.get("address").getAsString().replaceAll("\"", "");;
                                String order_date = obj_data.get("order_date").getAsString().replaceAll("\"", "");;
                                String order_time = obj_data.get("order_time").getAsString().replaceAll("\"", "");;
                            //    String delivery_date = obj_data.get("delivery_date").getAsString().replaceAll("\"", "");;
                            //    String delivery_time = obj_data.get("delivery_time").getAsString().replaceAll("\"", "");;
                                String sub_total = obj_data.get("sub_total").getAsString().replaceAll("\"", "");;
                                String total_price = obj_data.get("total_price").getAsString().replaceAll("\"", "");;
                                String delivery_charge = obj_data.get("delivery_charge").getAsString().replaceAll("\"", "");;
                               // String tax_charge = obj_data.get("tax_charge").getAsString().replaceAll("\"", "");;
                                String status1 = obj_data.get("status").getAsString().replaceAll("\"", "");;
                                String user_name = obj_data.get("user_name").getAsString().replaceAll("\"", "");;
                              //  String payment_type = obj_data.get("payment_type").getAsString().replaceAll("\"", "");;

                                Log.d(TAG, "onResponse:order_id>>> " + order_id);
                                Log.d(TAG, "onResponse:address>>> " + address);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("order_id", order_id);
                                hashMap.put("address", address);
                                hashMap.put("order_date", order_date);
                                hashMap.put("order_time", order_time);
                             //   hashMap.put("delivery_date", delivery_date);
                              //  hashMap.put("delivery_time", delivery_time);
                                hashMap.put("sub_total", sub_total);
                                hashMap.put("total_price", total_price);
                                hashMap.put("delivery_charge", delivery_charge);
                                hashMap.put("status", status1);
                               // if(tax_charge.equals("null")){
                                    hashMap.put("tax_charge", "0");
                              /*  }else {
                                    hashMap.put("tax_charge", tax_charge);
                                }*/

                             //   if(payment_type.equals("null")){
                                    hashMap.put("payment_type", "COD");
                                    hashMap.put("user_name", user_name);
                               /* }else {
                                    hashMap.put("payment_type", payment_type);
                                }
*/
                                order_list.add(hashMap);
                            }





                                Log.d(TAG, "onResponse: out loop i");

                                rv_order_history.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this, LinearLayoutManager.VERTICAL, false));
                                OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, order_list);
                                rv_order_history.setAdapter(orderHistoryAdapter);
                                orderHistoryAdapter.notifyDataSetChanged();




                            }else{

                                Toasty.info(OrderHistoryActivity.this, message, Toast.LENGTH_LONG).show();
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
                    Log.e(TAG, "order_history Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    rl_progress.setVisibility(View.GONE);

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("res_id",globalClass.getId());

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(OrderHistoryActivity.this, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
