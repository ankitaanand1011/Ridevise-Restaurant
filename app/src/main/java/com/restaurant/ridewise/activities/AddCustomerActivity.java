package com.restaurant.ridewise.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.adapter.AddressAdapter;
import com.restaurant.ridewise.adapter.MenuAdapter;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AddCustomerActivity extends AppCompatActivity {
    String TAG = "add_customer";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    RelativeLayout rl_progress;

    ImageView iv_back;
    TextView tv_or,tv_customer,tv_next;
    EditText et_pin,et_landmark,et_address,et_phn,et_customer_name,et_search_number;
    RecyclerView rv_address;
    private ArrayList<HashMap<String, String>> address_list;
    SearchView search;
    String customer_record = "not_available";

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        initialization();
        function();
    }

    private void initialization() {

        Log.d(TAG, "initialization: "+customer_record);

        globalClass = (GlobalClass)getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();
        rl_progress = findViewById(R.id.rl_progress);

        iv_back = findViewById(R.id. iv_back);
        et_pin = findViewById(R.id. et_pin);
        et_landmark = findViewById(R.id. et_landmark);
        et_address = findViewById(R.id. et_address);
        et_phn = findViewById(R.id. et_phn);
        et_customer_name = findViewById(R.id. et_customer_name);
        tv_or = findViewById(R.id. tv_or);
        tv_customer = findViewById(R.id. tv_customer);
        rv_address = findViewById(R.id. rv_address);
        tv_next = findViewById(R.id. tv_next);
        et_search_number = findViewById(R.id. et_search_number);
        search = findViewById(R.id. search);

    }

    private void function() {

        address_list = new ArrayList<>();

       /* address_list.add("Anup Pan, 9674004919, Behala");
        address_list.add("Anup Pan, 9674004919, Arambagh");
        address_list.add("Anup Pan, 9674004919, Joka");*/

        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");



        et_pin.setTypeface(regular);
        et_landmark.setTypeface(regular);
        et_address.setTypeface(regular);
        et_phn.setTypeface(regular);
        et_customer_name.setTypeface(regular);
        tv_or.setTypeface(regular);
        et_search_number.setTypeface(regular);


        tv_customer.setTypeface(medium);
        tv_next.setTypeface(medium);




/*
        rv_address.setLayoutManager(new LinearLayoutManager(AddCustomerActivity.this, LinearLayoutManager.VERTICAL, false));
        AddressAdapter addressAdapter = new AddressAdapter(AddCustomerActivity.this, address_list);
        rv_address.setAdapter(addressAdapter);*/

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(customer_record.equals("available")) {
                    if (!et_customer_name.getText().toString().isEmpty()) {
                        if (!et_phn.getText().toString().isEmpty()) {
                            if (!et_address.getText().toString().isEmpty()) {
                                if (!et_pin.getText().toString().isEmpty()) {

                                    Log.d(TAG, "onClick: else> "+customer_record);
                                    Intent intent = new Intent(AddCustomerActivity.this, AddItemActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toasty.info(AddCustomerActivity.this, "Please enter pin code.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toasty.info(AddCustomerActivity.this, "Please enter address.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toasty.info(AddCustomerActivity.this, "Please enter mobile number.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toasty.info(AddCustomerActivity.this, "Please enter customer name.", Toast.LENGTH_LONG).show();
                    }

                }else {
                    if (!et_customer_name.getText().toString().isEmpty()) {
                        if (!et_phn.getText().toString().isEmpty()) {
                            if (!et_address.getText().toString().isEmpty()) {
                                if (!et_pin.getText().toString().isEmpty()) {

                                    Log.d(TAG, "onClick: else> "+customer_record);
                                    getLocationFromAddress(et_address.getText().toString());

                                } else {
                                    Toasty.info(AddCustomerActivity.this, "Please enter pin code.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toasty.info(AddCustomerActivity.this, "Please enter address.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toasty.info(AddCustomerActivity.this, "Please enter mobile number.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toasty.info(AddCustomerActivity.this, "Please enter customer name.", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: query"+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: "+newText);
                Log.d(TAG, "onQueryTextChange: length"+newText.length());

                if(newText.length()>=3) {
                      search_consumer(newText);
                }
                return false;
            }
        });
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(AddCustomerActivity.this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

            Log.d(TAG, "getLocationFromAddress: lat"+location.getLatitude());
            Log.d(TAG, "getLocationFromAddress: long"+location.getLongitude());


            add_consumer(et_customer_name.getText().toString(),
                    et_phn.getText().toString(),
                    strAddress,
                    et_pin.getText().toString(),
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()));



        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private void search_consumer(final String mobile) {
        // Tag used to cancel the request
        final String tag_string_req = "req_login";

        //rl_progress.setVisibility(View.VISIBLE);
        String url = ApplicationConstants.search_consumer;
        Log.d(TAG, "search_consumer:url>>>  "+url);
        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "search_consumer Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message = jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        if(status.equals("1")) {
                            customer_record = "available";
                            address_list.clear();
                            JsonArray data = jobj.getAsJsonArray("response");
                            Log.d(TAG, "onResponse data: "+data);

                            for( int i = 0 ; i < data.size() ; i++ ) {

                                JsonObject obj_data = data.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String id = obj_data.get("id").getAsString().replaceAll("\"", "");;
                                String name = obj_data.get("name").getAsString().replaceAll("\"", "");;
                                String email = obj_data.get("email").getAsString().replaceAll("\"", "");;
                                String phone = obj_data.get("phone").getAsString().replaceAll("\"", "");;
                                String address = obj_data.get("address").getAsString().replaceAll("\"", "");;
                                String postal_code = obj_data.get("postal_code").getAsString().replaceAll("\"", "");;
                                String add_lat = obj_data.get("add_lat").getAsString().replaceAll("\"", "");;
                                String add_long = obj_data.get("add_long").getAsString().replaceAll("\"", "");;
                               // String menu_image = obj_data.get("menu_image").getAsString().replaceAll("\"", "");;


                                Log.d(TAG, "onResponse email: "+email);
                                Log.d(TAG, "onResponse phone: "+phone);
                                Log.d(TAG, "onResponse address: "+address);
                                Log.d(TAG, "onResponse postal_code: "+postal_code);
                                Log.d(TAG, "onResponse add_lat: "+add_lat);
                                Log.d(TAG, "onResponse add_long: "+add_long);

                                if(add_lat.isEmpty() || add_lat.equals("")){
                                    add_lat ="25.6185295";
                                }
                                if(add_long.isEmpty() || add_long.equals("")){
                                    add_long ="88.12558369999999";
                                }


                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", id);
                                hashMap.put("name", name);
                                hashMap.put("email", email);
                                hashMap.put("phone", phone);
                                hashMap.put("address", address);
                                hashMap.put("postal_code", postal_code);
                                hashMap.put("add_lat", add_lat);
                                hashMap.put("add_long", add_long);
                              //  hashMap.put("menu_image", menu_image);

                                address_list.add(hashMap);
                            }



                            rv_address.setLayoutManager(new LinearLayoutManager(AddCustomerActivity.this, LinearLayoutManager.VERTICAL, false));
                            AddressAdapter addressAdapter = new AddressAdapter(AddCustomerActivity.this, address_list,
                                    et_pin,et_landmark,et_address,et_phn,et_customer_name);
                            rv_address.setAdapter(addressAdapter);
                            addressAdapter.notifyDataSetChanged();




                        }else{
                            customer_record="not_available";
                            rv_address.setVisibility(View.GONE);
                            Toasty.info(AddCustomerActivity.this, message, Toast.LENGTH_LONG).show();
                        }



                    }catch (Exception e){
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "search_consumer Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Connection Error", Toast.LENGTH_LONG).show();
                    //  pd.dismiss();
                    //  mView.hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();


                    params.put("phone", mobile);



                    Log.d(TAG, "getParams: "+params);

                    return params;
                }

            };

            globalClass.addToRequestQueue(AddCustomerActivity.this, strReq, tag_string_req);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void add_consumer(final String name,final String mobile,
                              final String address,final String pin,final String lat,final String lng) {
        // Tag used to cancel the request
        final String tag_string_req = "req_login";
        tv_next.setEnabled(false);
        rl_progress.setVisibility(View.VISIBLE);
        String url = ApplicationConstants.add_consumer;
        Log.d(TAG, "add_consumer:url>>>  "+url);
        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "add_consumer Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message = jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        if(status.equals("1")) {
                            JsonObject data = jobj.getAsJsonObject("response");
                            Log.d(TAG, "onResponse data: "+data);


                            String user_id = data.get("user_id").getAsString().replaceAll("\"", "");;
                            String name = data.get("name").getAsString().replaceAll("\"", "");;
                            String email = data.get("email").getAsString().replaceAll("\"", "");;
                         //   String phone = data.get("phone").getAsString().replaceAll("\"", "");;
                            String address = data.get("address").getAsString().replaceAll("\"", "");;
                           // String postal_code = data.get("postal_code").getAsString().replaceAll("\"", "");;
                            String add_lat = data.get("add_lat").getAsString().replaceAll("\"", "");;
                            String add_long = data.get("add_long").getAsString().replaceAll("\"", "");;

                            globalClass.setUser_lat(add_lat);
                            globalClass.setUser_long(add_long);
                            globalClass.setUser_id(user_id);
                            globalClass.setUser_name(name);
                            globalClass.setUser_phn(et_phn.getText().toString());
                            globalClass.setUser_email(email);
                            globalClass.setUser_address(address);
                            globalClass.setUser_pin(et_pin.getText().toString());

                            rl_progress.setVisibility(View.GONE);
                            tv_next.setEnabled(true);
                            Intent intent = new Intent(AddCustomerActivity.this, AddItemActivity.class);
                            startActivity(intent);


                        }else{
                            rl_progress.setVisibility(View.GONE);
                            Toasty.info(AddCustomerActivity.this, message, Toast.LENGTH_LONG).show();
                        }



                    }catch (Exception e){
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "search_consumer Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Connection Error", Toast.LENGTH_LONG).show();
                    rl_progress.setVisibility(View.GONE);
                    //  mView.hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();

                    params.put("name", name);
                    params.put("password", "123456");
                    params.put("address", address);
                    params.put("zip", pin);
                    params.put("email", "abc@xyz.com");
                    params.put("phone", mobile);
                    params.put("add_lat", lat);
                    params.put("add_long", lng);



                    Log.d(TAG, "getParams: "+params);

                    return params;
                }

            };

            globalClass.addToRequestQueue(AddCustomerActivity.this, strReq, tag_string_req);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
