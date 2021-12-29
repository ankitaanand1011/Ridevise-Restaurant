package com.restaurant.ridewise.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.restaurant.ridewise.adapter.CustomerSearchAdapter;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SearchCustomerActivity extends AppCompatActivity {
    String TAG = "search_customer";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    RelativeLayout rl_progress,rl_customer_add;

    ImageView iv_back;
    TextView tv_or,tv_customer,tv_add_cus,tv_add_customer,tv_text;
    EditText et_search_number;
    RecyclerView rv_address;
    private ArrayList<HashMap<String, String>> address_list;
    SearchView search;
    String customer_record = "not_available";



    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);

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

      //  tv_or = findViewById(R.id. tv_or);
        tv_customer = findViewById(R.id. tv_customer);
        tv_add_customer = findViewById(R.id. tv_add_customer);
        tv_text = findViewById(R.id. tv_text);

        rv_address = findViewById(R.id. rv_address);

        et_search_number = findViewById(R.id. et_search_number);
        search = findViewById(R.id. search);
        rl_customer_add = findViewById(R.id. rl_customer_add);

    }

    private void function() {

        address_list = new ArrayList<>();
       // address_details_list = new ArrayList<>();


        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");



      //  tv_or.setTypeface(regular);
        et_search_number.setTypeface(regular);
        tv_add_customer.setTypeface(regular);
        tv_text.setTypeface(regular);


        tv_customer.setTypeface(medium);




        tv_add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_customer_dialog();
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    public void add_customer_dialog(){

        Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_add_customer);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));


        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");

       // EditText editText = (EditText) dialogView.findViewById(R.id.label_field);
        EditText  et_pin = alertDialog.findViewById(R.id. et_pin);
        EditText  et_landmark = alertDialog.findViewById(R.id. et_landmark);
        EditText  et_address = alertDialog.findViewById(R.id. et_address);
        EditText  et_phn = alertDialog.findViewById(R.id. et_phn);
        EditText  et_customer_name = alertDialog.findViewById(R.id. et_customer_name);
        TextView  tv_add_cus = alertDialog.findViewById(R.id. tv_add_cus);
        EditText  et_state = alertDialog.findViewById(R.id. et_state);
        EditText  et_city = alertDialog.findViewById(R.id. et_city);


        et_pin.setTypeface(regular);
        et_landmark.setTypeface(regular);
        et_address.setTypeface(regular);
        et_phn.setTypeface(regular);
        et_customer_name.setTypeface(regular);
        tv_add_cus.setTypeface(medium);


        tv_add_cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!customer_record.equals("available")) {



                    if (!et_customer_name.getText().toString().isEmpty()) {
                        if (!et_phn.getText().toString().isEmpty()) {
                          //  if (!et_state.getText().toString().isEmpty()) {
                           //     if (!et_city.getText().toString().isEmpty()) {
                                     if (!et_address.getText().toString().isEmpty()) {
                                        if (!et_pin.getText().toString().isEmpty()) {

                                           String customer_name = et_customer_name.getText().toString();
                                           String phn = et_phn.getText().toString();
                                           String state = et_state.getText().toString();
                                           String city = et_city.getText().toString();
                                           String address = et_address.getText().toString();
                                           String pin = et_pin.getText().toString();
                                           String landmark = et_landmark.getText().toString();
                                            Log.d(TAG, "onClick: else> " + customer_record);

                                            //states(state,city);
                                            getLocationFromAddress(customer_name,phn,address,pin,landmark,alertDialog);

                                        } else {
                                                Toasty.info(SearchCustomerActivity.this, "Please enter pin code.", Toast.LENGTH_LONG).show();
                                        }
                                     } else {
                                    Toasty.info(SearchCustomerActivity.this, "Please enter address.", Toast.LENGTH_LONG).show();
                                    }
                                /*} else {
                                    Toasty.info(SearchCustomerActivity.this, "Please enter city.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toasty.info(SearchCustomerActivity.this, "Please enter state.", Toast.LENGTH_LONG).show();
                            }*/
                        } else {
                            Toasty.info(SearchCustomerActivity.this, "Please enter mobile number.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toasty.info(SearchCustomerActivity.this, "Please enter customer name.", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        alertDialog.show();
    }

    public LatLng getLocationFromAddress(String customer_name, String phn, String str_address,
                                         String pin, String landmark, Dialog alertDialog) {

        Geocoder coder = new Geocoder(SearchCustomerActivity.this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(str_address, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

            Log.d(TAG, "getLocationFromAddress: lat"+location.getLatitude());
            Log.d(TAG, "getLocationFromAddress: long"+location.getLongitude());

            if(!String.valueOf(location.getLatitude()).isEmpty()){
                if(!String.valueOf(location.getLongitude()).isEmpty()){
                    add_consumer(customer_name, phn, str_address, pin,
                            String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()),landmark,alertDialog);

                }
            }

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


                @SuppressLint("NotifyDataSetChanged")
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
                            rl_customer_add.setVisibility(View.GONE);
                            JsonArray data = jobj.getAsJsonArray("response");
                            Log.d(TAG, "onResponse data: "+data);

                            for( int i = 0 ; i < data.size() ; i++ ) {

                                JsonObject obj_data = data.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String id = obj_data.get("id").getAsString().replaceAll("\"", "");;
                                String name = obj_data.get("name").getAsString().replaceAll("\"", "");;
                                String email = obj_data.get("email").getAsString().replaceAll("\"", "");;
                                String phone = obj_data.get("phone").getAsString().replaceAll("\"", "");;




                                Log.d(TAG, "onResponse email: "+email);
                                Log.d(TAG, "onResponse phone: "+phone);



                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", id);
                                hashMap.put("name", name);
                                hashMap.put("email", email);
                                hashMap.put("phone", phone);



                                address_list.add(hashMap);
                            }

                            rv_address.setLayoutManager(new LinearLayoutManager(SearchCustomerActivity.this, LinearLayoutManager.VERTICAL, false));
                            CustomerSearchAdapter customerSearchAdapter = new CustomerSearchAdapter(SearchCustomerActivity.this, address_list );
                            rv_address.setAdapter(customerSearchAdapter);
                            customerSearchAdapter.notifyDataSetChanged();


                        }else{
                            customer_record="not_available";
                            rv_address.setVisibility(View.GONE);
                            tv_text.setText(getResources().getString(R.string.add_customer_text1));
                            rl_customer_add.setVisibility(View.VISIBLE);
                            Toasty.info(SearchCustomerActivity.this, message, Toast.LENGTH_LONG).show();
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

            globalClass.addToRequestQueue(SearchCustomerActivity.this, strReq, tag_string_req);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void add_consumer(final String name, final String mobile, final String address,
                              final String pin, final String lat, final String lng, final String landmark,
                              Dialog alertDialog) {
        // Tag used to cancel the request
        final String tag_string_req = "req_login";
      //  tv_add_cus.setEnabled(false);
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
                            String user_address_id = data.get("user_address_id").getAsString().replaceAll("\"", "");;
                            String address = data.get("address").getAsString().replaceAll("\"", "");;
                            String add_lat = data.get("add_lat").getAsString().replaceAll("\"", "");;
                            String add_long = data.get("add_long").getAsString().replaceAll("\"", "");;

                            globalClass.setUser_lat(add_lat);
                            globalClass.setUser_long(add_long);
                            globalClass.setUser_id(user_id);
                            globalClass.setUser_name(name);
                            globalClass.setUser_phn(mobile);
                            globalClass.setUser_email(email);
                            globalClass.setUser_address(address);
                            globalClass.setUser_pin(pin);
                            globalClass.setUser_address_id(user_address_id);

                            rl_progress.setVisibility(View.GONE);
                        //    tv_next.setEnabled(true);
                            alertDialog.dismiss();
                            Intent intent = new Intent(SearchCustomerActivity.this, AddItemActivity.class);
                            startActivity(intent);


                        }else{
                            rl_progress.setVisibility(View.GONE);
                            Toasty.info(SearchCustomerActivity.this, message, Toast.LENGTH_LONG).show();
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
                    params.put("landmark", landmark);

                    Log.d(TAG, "getParams: "+params);

                    return params;
                }

            };

            globalClass.addToRequestQueue(SearchCustomerActivity.this, strReq, tag_string_req);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
