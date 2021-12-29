package com.restaurant.ridewise.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

    ImageView iv_back,iv_add_address;
    TextView tv_or,tv_customer,tv_next;
    EditText et_pin,et_landmark,et_address,et_phn,et_customer_name,et_search_number;
    RecyclerView rv_address;
    private ArrayList<HashMap<String, String>> address_list,address_details_list;
    SearchView search;
    String customer_record = "not_available";
    String user_id,phone;

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
        iv_add_address = findViewById(R.id. iv_add_address);

    }

    private void function() {

        address_list = new ArrayList<>();
        address_details_list = new ArrayList<>();

        user_id = getIntent().getStringExtra("user_id");
        phone = getIntent().getStringExtra("phone");

        globalClass.setUser_lat(user_id);
        globalClass.setUser_phn(phone);

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

        search_consumer(phone);

        et_search_number.setText(phone);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_address_dialog();
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

                }/*else {
                    if (!et_customer_name.getText().toString().isEmpty()) {
                        if (!et_phn.getText().toString().isEmpty()) {
                            if (!et_address.getText().toString().isEmpty()) {
                                if (!et_pin.getText().toString().isEmpty()) {

                                    Log.d(TAG, "onClick: else> " + customer_record);
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

                }*/

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

    public void add_address_dialog(){

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
        TextView  tv_customer = alertDialog.findViewById(R.id. tv_customer);
        EditText  et_state = alertDialog.findViewById(R.id. et_state);
        EditText  et_city = alertDialog.findViewById(R.id. et_city);

        et_phn.setVisibility(View.GONE);
        et_customer_name.setVisibility(View.GONE);
        et_state.setVisibility(View.VISIBLE);
        et_city.setVisibility(View.VISIBLE);


        et_pin.setTypeface(regular);
        et_landmark.setTypeface(regular);
        et_address.setTypeface(regular);
        et_phn.setTypeface(regular);
        et_customer_name.setTypeface(regular);
        tv_add_cus.setTypeface(medium);
        tv_customer.setTypeface(medium);
        tv_customer.setText("ADD NEW ADDRESS");

        tv_add_cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  if(!customer_record.equals("available")) {


                    //  if (!et_customer_name.getText().toString().isEmpty()) {
                    //      if (!et_phn.getText().toString().isEmpty()) {
                    if (!et_state.getText().toString().isEmpty()) {
                        if (!et_city.getText().toString().isEmpty()) {
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

                                    states(state, city, address, pin, landmark,alertDialog);


                                } else {
                                    Toasty.info(AddCustomerActivity.this, "Please enter pin code.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toasty.info(AddCustomerActivity.this, "Please enter address.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toasty.info(AddCustomerActivity.this, "Please enter city.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toasty.info(AddCustomerActivity.this, "Please enter state.", Toast.LENGTH_LONG).show();
                    }
                      /*  } else {
                            Toasty.info(AddCustomerActivity.this, "Please enter mobile number.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toasty.info(AddCustomerActivity.this, "Please enter customer name.", Toast.LENGTH_LONG).show();
                    }*/

              //  }

            }
        });
        alertDialog.show();
    }

    public LatLng getLocationFromAddress(String state_id, String city_id, String str_address,
                                         String pin, String landmark, Dialog alertDialog) {

        Geocoder coder = new Geocoder(AddCustomerActivity.this);
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
                    add_address(state_id,
                            city_id,
                            str_address,
                            pin,
                            String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()),
                            landmark,alertDialog );

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
                            JsonArray data = jobj.getAsJsonArray("response");
                            Log.d(TAG, "onResponse data: "+data);

                            for( int i = 0 ; i < data.size() ; i++ ) {

                                JsonObject obj_data = data.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                         /*       String id = obj_data.get("id").getAsString().replaceAll("\"", "");;
                                String name = obj_data.get("name").getAsString().replaceAll("\"", "");;
                                String email = obj_data.get("email").getAsString().replaceAll("\"", "");;
                                String phone = obj_data.get("phone").getAsString().replaceAll("\"", "");;
*/


                                JsonArray address_details = obj_data.getAsJsonArray("address_details");
                                Log.d(TAG, "onResponse data: "+address_details);


                                    for (int j = 0; j < address_details.size(); j++) {
                                        JsonObject address_obj_data = address_details.get(j).getAsJsonObject();
                                        Log.d(TAG, "onResponse obj_data:  " + address_obj_data);

                                        String address_id = address_obj_data.get("id").getAsString().replaceAll("\"", "");

                                        String name = address_obj_data.get("name").getAsString().replaceAll("\"", "");

                                        String user_id = address_obj_data.get("user_id").getAsString().replaceAll("\"", "");

                                        String address = address_obj_data.get("address").getAsString().replaceAll("\"", "");

                                        String zip = address_obj_data.get("zip").getAsString().replaceAll("\"", "");

                                        String add_lat = address_obj_data.get("add_lat").getAsString().replaceAll("\"", "");

                                        String add_long = address_obj_data.get("add_long").getAsString().replaceAll("\"", "");

                                        String landmark = address_obj_data.get("landmark").getAsString().replaceAll("\"", "");


                                        Log.d(TAG, "onResponse address: " + address);
                                        Log.d(TAG, "onResponse zip: " + zip);
                                        Log.d(TAG, "onResponse add_lat: " + add_lat);
                                        Log.d(TAG, "onResponse add_long: " + add_long);

                                        if (user_id.matches(user_id)) {

                                            HashMap<String, String> hashMap = new HashMap<>();

                                            hashMap.put("address", address);
                                            hashMap.put("zip", zip);
                                            hashMap.put("add_lat", add_lat);
                                            hashMap.put("add_long", add_long);
                                            hashMap.put("landmark", landmark);
                                            hashMap.put("name", name);
                                            hashMap.put("address_id", address_id);
                                            hashMap.put("user_id", user_id);
                                            //  hashMap.put("menu_image", menu_image);

                                            address_details_list.add(hashMap);
                                        }

                                    }





                            }

                            rv_address.setLayoutManager(new LinearLayoutManager(AddCustomerActivity.this, LinearLayoutManager.VERTICAL, false));
                            AddressAdapter addressAdapter = new AddressAdapter(AddCustomerActivity.this, address_details_list,
                                    et_pin,et_landmark,et_address,et_phn,et_customer_name,phone);
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

    private void add_address(final String state_id, final String city_id, final String address,
                             final String pin, final String lat, final String lng, final String landmark, Dialog alertDialog) {
        // Tag used to cancel the request
        final String tag_string_req = "req_login";
        tv_next.setEnabled(false);
        rl_progress.setVisibility(View.VISIBLE);
        String url = ApplicationConstants.add_address;
        Log.d(TAG, "add_address:url>>>  "+url);
        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "add_address Response: " + response);


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
                            String user_address_id = data.get("user_address_id").getAsString().replaceAll("\"", "");;
                            String name = data.get("name").getAsString().replaceAll("\"", "");;
                            String email = data.get("email").getAsString().replaceAll("\"", "");;
                            String address = data.get("address").getAsString().replaceAll("\"", "");;
                            String add_lat = data.get("add_lat").getAsString().replaceAll("\"", "");;
                            String add_long = data.get("add_long").getAsString().replaceAll("\"", "");;

                            globalClass.setUser_lat(add_lat);
                            globalClass.setUser_long(add_long);
                            globalClass.setUser_id(user_id);
                            globalClass.setUser_name(name);
                            globalClass.setUser_address_id(user_address_id);
                       //     globalClass.setUser_phn(et_phn.getText().toString());
                            globalClass.setUser_email(email);
                            globalClass.setUser_address(address);
                            globalClass.setUser_pin(et_pin.getText().toString());

                            rl_progress.setVisibility(View.GONE);
                            tv_next.setEnabled(true);
                            alertDialog.dismiss();
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

                    params.put("user_id",user_id);
                    params.put("address", address);
                    params.put("zip", pin);
                    params.put("state_id", state_id);
                    params.put("city_id", city_id);
                    params.put("add_lat", lat);
                    params.put("add_long", lng);
                    params.put("landmark", landmark);

                    Log.d(TAG, "getParams: "+params);

                    return params;
                }

            };

            globalClass.addToRequestQueue(AddCustomerActivity.this, strReq, tag_string_req);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void states(String state, String city, String address, final String pin, final String landmark, Dialog alertDialog) {
        // Tag used to cancel the request
        final String tag_string_req = "req_login";

        //rl_progress.setVisibility(View.VISIBLE);
        String url = ApplicationConstants.state_list;
        Log.d(TAG, "state_list:url>>>  "+url);
        try{
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>(){


                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "state_list Response: " + response);

                    Gson gson = new Gson();

                    try {

                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message = jobj.get("message").getAsString().replaceAll("\"", "");

                        Log.d(TAG, "Message: "+message);

                        if(status.equals("1")) {

                            JsonArray data = jobj.getAsJsonArray("response");
                            Log.d(TAG, "onResponse data: " + data);

                            for (int i = 0; i < data.size(); i++) {

                                JsonObject obj_data = data.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  " + obj_data);
                                String id = obj_data.get("id").getAsString().replaceAll("\"", "");
                                String name = obj_data.get("name").getAsString().replaceAll("\"", "");
                                String country_id = obj_data.get("country_id").getAsString().replaceAll("\"", "");

                                if(state.matches(name)){

                                    String state_id = id;
                                    city_list(state_id,city,address,pin,landmark,alertDialog);

                                }
                            }
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


                    //  params.put("phone", mobile);

                    Log.d(TAG, "getParams: "+params);

                    return params;
                }

            };

            globalClass.addToRequestQueue(AddCustomerActivity.this, strReq, tag_string_req);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void city_list(String stateId, String city, String address, final String pin, final String landmark, Dialog alertDialog) {
        // Tag used to cancel the request
        final String tag_string_req = "req_login";

        //rl_progress.setVisibility(View.VISIBLE);
        String url = ApplicationConstants.city_list;
        Log.d(TAG, "city_list:url>>>  "+url);
        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "city_list Response: " + response);

                    Gson gson = new Gson();

                    try {

                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message = jobj.get("message").getAsString().replaceAll("\"", "");

                        Log.d(TAG, "Message: "+message);

                        if(status.equals("1")) {
                            JsonArray data = jobj.getAsJsonArray("response");
                            Log.d(TAG, "onResponse data: "+data);

                            for( int i = 0 ; i < data.size() ; i++ ) {

                                JsonObject obj_data = data.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String id = obj_data.get("id").getAsString().replaceAll("\"", "");;
                                String name = obj_data.get("name").getAsString().replaceAll("\"", "");;


                                if(city.matches(name)){

                                    String city_id = id;

                                    getLocationFromAddress(stateId,city_id,address,pin,landmark,alertDialog);
                                }

                            }


                        }else{

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


                    params.put("state_id", stateId);

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
