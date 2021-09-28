package com.restaurant.ridewise.activities;

import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.adapter.CartAdapter;
import com.restaurant.ridewise.adapter.ItemsAdapter;
import com.restaurant.ridewise.adapter.MenuAdapter;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CartActivity extends AppCompatActivity {
    String TAG = "cart";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    RelativeLayout rl_progress;

    TextView tv_header,tv_subtotal,tv_subtotal_value,tv_delivery,tv_delivery_value,tv_tax,tv_tax_value,tv_total,
            tv_total_value,tv_item_count,tv_checkout;
    RecyclerView rv_cart;
    ArrayList<HashMap<String, String>> item_list;
    String total_payable;
    ImageView iv_back;

    String sub_total;
    String delivery_charge;
    String tax_charge;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initialization();
        function();
    }

    private void initialization() {
        globalClass = (GlobalClass)getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();
        rl_progress = findViewById(R.id.rl_progress);

        rv_cart = findViewById(R.id.rv_cart);
        tv_header = findViewById(R.id.tv_header);
        tv_subtotal = findViewById(R.id.tv_subtotal);
        tv_subtotal_value = findViewById(R.id.tv_subtotal_value);
        tv_delivery = findViewById(R.id.tv_delivery);
        tv_delivery_value = findViewById(R.id.tv_delivery_value);
        tv_tax = findViewById(R.id.tv_tax);
        tv_tax_value = findViewById(R.id.tv_tax_value);
        tv_total = findViewById(R.id.tv_total);
        tv_total_value = findViewById(R.id.tv_total_value);
        tv_item_count = findViewById(R.id.tv_item_count);
        tv_checkout = findViewById(R.id.tv_checkout);
        iv_back = findViewById(R.id.iv_back);
    }

    private void function() {
        item_list = new ArrayList<>();

      /*  item_list.add("Mutton Kasa (2pcs)");
        item_list.add("Mutton Kasa (4pcs)");
        item_list.add("Paneer Pakoda");*/

        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        tv_header.setTypeface(medium);
        tv_subtotal.setTypeface(medium);
        tv_subtotal_value.setTypeface(medium);

        tv_delivery.setTypeface(regular);
        tv_delivery_value.setTypeface(regular);
        tv_tax.setTypeface(regular);
        tv_tax_value.setTypeface(regular);
        tv_item_count.setTypeface(regular);
        tv_checkout.setTypeface(regular);

        tv_total.setTypeface(medium);
        tv_total_value.setTypeface(medium);




        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this,PaymentActivity.class);
                intent.putExtra("total_amount",total_payable);
                intent.putExtra("sub_total",sub_total);
                intent.putExtra("delivery_charge",delivery_charge);
                intent.putExtra("tax_charge",tax_charge);
                startActivity(intent);
            }
        });

        cart_details();
    }
    public void cart_details() {
        //  mView.showDialog();

        rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "cart_details";
        String url = ApplicationConstants.cart_details;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "menu_list Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        if(status.equals("1")) {
                            item_list.clear();
                            JsonObject data = jobj.getAsJsonObject("response");
                            sub_total = data.get("sub_total").getAsString().replaceAll("\"", "");;
                            delivery_charge = data.get("delivery_charge").getAsString().replaceAll("\"", "");;
                            tax_charge = data.get("tax_charge").getAsString().replaceAll("\"", "");;
                            total_payable = data.get("total_payable").getAsString().replaceAll("\"", "");;



                            JsonArray cart_details = data.getAsJsonArray("cart_details");
                            Log.d(TAG, "onResponse menu_list_arr: "+cart_details);

                            for( int i = 0 ; i < cart_details.size() ; i++ ) {

                                JsonObject obj_data = cart_details.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String qty = obj_data.get("qty").getAsString().replaceAll("\"", "");;
                                String cart_id = obj_data.get("cart_id").getAsString().replaceAll("\"", "");;
                                String menu_name = obj_data.get("menu_name").getAsString().replaceAll("\"", "");;
                                String menu_id = obj_data.get("menu_id").getAsString().replaceAll("\"", "");;
                                String category_id = obj_data.get("category_id").getAsString().replaceAll("\"", "");;
                                String description = obj_data.get("description").getAsString().replaceAll("\"", "");;
                                String price = obj_data.get("price").getAsString().replaceAll("\"", "");;
                                String veg_nonveg = obj_data.get("veg_nonveg").getAsString().replaceAll("\"", "");;
                                String prepare_time = obj_data.get("prepare_time").getAsString().replaceAll("\"", "");;
                                String menu_image = obj_data.get("menu_image").getAsString().replaceAll("\"", "");;


                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("menu_id", menu_id);
                                hashMap.put("menu_name", menu_name);
                                hashMap.put("category_id", category_id);
                                hashMap.put("cart_id", cart_id);
                                hashMap.put("description", description);
                                hashMap.put("veg_nonveg", veg_nonveg);
                                hashMap.put("price", price);
                                hashMap.put("prepare_time", prepare_time);
                                hashMap.put("menu_image", menu_image);
                                hashMap.put("qty", qty);

                                item_list.add(hashMap);
                            }


                            tv_subtotal_value.setText("₹  "+sub_total);
                            tv_delivery_value.setText("₹  "+delivery_charge);
                            tv_tax_value.setText("₹  "+tax_charge);
                            tv_total_value.setText("₹  "+total_payable);

                            String item_count = cart_details.size() +" ITEMS  ₹ "+total_payable;
                            tv_item_count.setText(item_count);



                            rv_cart.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                            CartAdapter cartAdapter = new CartAdapter(CartActivity.this, item_list);
                            rv_cart.setAdapter(cartAdapter);
                            cartAdapter.notifyDataSetChanged();




                        }else{

                            Toasty.info(CartActivity.this, message, Toast.LENGTH_LONG).show();
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
                    Log.e(TAG, "menu_list Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    rl_progress.setVisibility(View.GONE);

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("res_id",globalClass.getId());
                    params.put("user_id",globalClass.getUser_id());

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(CartActivity.this, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
