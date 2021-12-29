package com.restaurant.ridewise.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.adapter.AddressAdapter;
import com.restaurant.ridewise.adapter.ItemsAdapter;
import com.restaurant.ridewise.adapter.MenuAdapter;
import com.restaurant.ridewise.adapter.NonVegAdapter;
import com.restaurant.ridewise.adapter.VegAdapter;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AddItemActivity extends AppCompatActivity {
    String TAG = "add_item";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    RelativeLayout rl_progress;
    
    ImageView iv_back;
    TextView tv_all,tv_veg,tv_next,tv_search_order,tv_nonveg,tv_view_cart,tv_item_count;
    RecyclerView rv_item_list,rv_veg_list,rv_non_veg_list;
    private ArrayList<HashMap<String, String>>  menu_list,veg_list,non_veg_list;
    SearchView search;
    RelativeLayout  ll_select_orders;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        initialization();
        function();
    }

    private void initialization() {
        
        globalClass = (GlobalClass)getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();
        rl_progress = findViewById(R.id.rl_progress);
        
        iv_back = findViewById(R.id. iv_back);
        tv_search_order = findViewById(R.id. tv_search_order);
        tv_all = findViewById(R.id. tv_all);
        tv_veg = findViewById(R.id. tv_veg);
        tv_nonveg = findViewById(R.id. tv_nonveg);
        tv_view_cart = findViewById(R.id. tv_view_cart);
        tv_item_count = findViewById(R.id. tv_item_count);
        rv_item_list = findViewById(R.id. rv_item_list);
        rv_veg_list = findViewById(R.id. rv_veg_list);
        rv_non_veg_list = findViewById(R.id. rv_non_veg_list);
        search = findViewById(R.id. search);
        ll_select_orders = findViewById(R.id. ll_select_orders);

    }

    private void function() {
        menu_list = new ArrayList<>();
        veg_list = new ArrayList<>();
        non_veg_list = new ArrayList<>();

        rv_non_veg_list.setVisibility(View.GONE);
        rv_veg_list.setVisibility(View.GONE);
        rv_item_list.setVisibility(View.VISIBLE);


        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");



        tv_all.setTypeface(regular);
        tv_veg.setTypeface(regular);
        tv_nonveg.setTypeface(regular);
        tv_view_cart.setTypeface(regular);
        tv_item_count.setTypeface(regular);



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
                search_list(newText);
                    //search_consumer(newText);

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
      

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_view_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddItemActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        tv_all.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                tv_all.setBackground(getResources().getDrawable(R.drawable.bg_solid_green_left_side));
                tv_veg.setBackground(getResources().getDrawable(R.drawable.tv_bg_green_center));
                tv_nonveg.setBackground(getResources().getDrawable(R.drawable.tv_bg_green_right_side));

                tv_all.setTextColor(getResources().getColor(R.color.white));
                tv_veg.setTextColor(getResources().getColor(R.color.black));
                tv_nonveg.setTextColor(getResources().getColor(R.color.black));

                rv_non_veg_list.setVisibility(View.GONE);
                rv_veg_list.setVisibility(View.GONE);
                rv_item_list.setVisibility(View.VISIBLE);
            }
        });
        tv_veg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                tv_all.setBackground(getResources().getDrawable(R.drawable.tv_bg_green_left_side));
                tv_veg.setBackground(getResources().getDrawable(R.drawable.bg_soild_green_center));
                tv_nonveg.setBackground(getResources().getDrawable(R.drawable.tv_bg_green_right_side));


                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_veg.setTextColor(getResources().getColor(R.color.white));
                tv_nonveg.setTextColor(getResources().getColor(R.color.black));


                rv_non_veg_list.setVisibility(View.GONE);
                rv_veg_list.setVisibility(View.VISIBLE);
                rv_item_list.setVisibility(View.GONE);
            }
        });
        tv_nonveg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                tv_all.setBackground(getResources().getDrawable(R.drawable.tv_bg_green_left_side));
                tv_veg.setBackground(getResources().getDrawable(R.drawable.tv_bg_green_center));
                tv_nonveg.setBackground(getResources().getDrawable(R.drawable.bg_solid_green_right_side));

                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_veg.setTextColor(getResources().getColor(R.color.black));
                tv_nonveg.setTextColor(getResources().getColor(R.color.white));

                rv_non_veg_list.setVisibility(View.VISIBLE);
                rv_veg_list.setVisibility(View.GONE);
                rv_item_list.setVisibility(View.GONE);
            }
        });

        menu_list();

    }

    public void menu_list() {
        //  mView.showDialog();

        rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "menu_list";
        String url = ApplicationConstants.menu_list;

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
                            menu_list.clear();
                            veg_list.clear();
                            non_veg_list.clear();

                            JsonObject data = jobj.getAsJsonObject("response");
                            // Log.d(TAG, "onResponse data: "+data);

                            JsonArray menu_list_arr = data.getAsJsonArray("menu_list");
                            Log.d(TAG, "onResponse menu_list_arr: "+menu_list_arr);

                            for( int i = 0 ; i < menu_list_arr.size() ; i++ ) {

                                JsonObject obj_data = menu_list_arr.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String menu_id = obj_data.get("menu_id").getAsString().replaceAll("\"", "");;
                                String menu_name = obj_data.get("menu_name").getAsString().replaceAll("\"", "");;
                                String category_id = obj_data.get("category_id").getAsString().replaceAll("\"", "");;
                                String category_name = obj_data.get("category_name").getAsString().replaceAll("\"", "");;
                                String description = obj_data.get("description").getAsString().replaceAll("\"", "");;
                                String veg_nonveg = obj_data.get("veg_nonveg").getAsString().replaceAll("\"", "");;
                                String price = obj_data.get("price").getAsString().replaceAll("\"", "");;
                                String prepare_time = obj_data.get("prepare_time").getAsString().replaceAll("\"", "");;
                                String menu_image = obj_data.get("menu_image").getAsString().replaceAll("\"", "");;


                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("menu_id", menu_id);
                                hashMap.put("menu_name", menu_name);
                                hashMap.put("category_id", category_id);
                                hashMap.put("category_name", category_name);
                                hashMap.put("description", description);
                                hashMap.put("veg_nonveg", veg_nonveg);
                                hashMap.put("price", price);
                                hashMap.put("prepare_time", prepare_time);
                                hashMap.put("menu_image", menu_image);

                                menu_list.add(hashMap);
                            }

                            JsonArray veg_menu = data.getAsJsonArray("veg_menu");
                            Log.d(TAG, "onResponse veg_menu: "+veg_menu);

                            for( int i = 0 ; i < veg_menu.size() ; i++ ) {

                                JsonObject obj_data = veg_menu.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String menu_id = obj_data.get("id").getAsString().replaceAll("\"", "");;
                                String menu_name = obj_data.get("title").getAsString().replaceAll("\"", "");;
                                String category_id = obj_data.get("category_id").getAsString().replaceAll("\"", "");;
                            //    String category_name = obj_data.get("category_name").getAsString().replaceAll("\"", "");;
                                String description = obj_data.get("description").getAsString().replaceAll("\"", "");;
                                String veg_nonveg = obj_data.get("veg_nonveg").getAsString().replaceAll("\"", "");;
                                String price = obj_data.get("price").getAsString().replaceAll("\"", "");;
                                String prepare_time = obj_data.get("prepare_time").getAsString().replaceAll("\"", "");;
                               // String menu_image = obj_data.get("menu_image").getAsString().replaceAll("\"", "");;


                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("menu_id", menu_id);
                                hashMap.put("menu_name", menu_name);
                                hashMap.put("category_id", category_id);
                            //    hashMap.put("category_name", category_name);
                                hashMap.put("description", description);
                                hashMap.put("veg_nonveg", veg_nonveg);
                                hashMap.put("price", price);
                                hashMap.put("prepare_time", prepare_time);
                              //  hashMap.put("menu_image", menu_image);

                                veg_list.add(hashMap);
                            }

                            JsonArray nonveg_menu = data.getAsJsonArray("nonveg_menu");
                            Log.d(TAG, "onResponse nonveg_menu: "+nonveg_menu);

                            for( int i = 0 ; i < nonveg_menu.size() ; i++ ) {

                                JsonObject obj_data = nonveg_menu.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String menu_id = obj_data.get("id").getAsString().replaceAll("\"", "");;
                                String menu_name = obj_data.get("title").getAsString().replaceAll("\"", "");;
                                String category_id = obj_data.get("category_id").getAsString().replaceAll("\"", "");;
                                //    String category_name = obj_data.get("category_name").getAsString().replaceAll("\"", "");;
                                String description = obj_data.get("description").getAsString().replaceAll("\"", "");;
                                String veg_nonveg = obj_data.get("veg_nonveg").getAsString().replaceAll("\"", "");;
                                String price = obj_data.get("price").getAsString().replaceAll("\"", "");;
                                String prepare_time = obj_data.get("prepare_time").getAsString().replaceAll("\"", "");;
                                // String menu_image = obj_data.get("menu_image").getAsString().replaceAll("\"", "");;


                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("menu_id", menu_id);
                                hashMap.put("menu_name", menu_name);
                                hashMap.put("category_id", category_id);
                                //    hashMap.put("category_name", category_name);
                                hashMap.put("description", description);
                                hashMap.put("veg_nonveg", veg_nonveg);
                                hashMap.put("price", price);
                                hashMap.put("prepare_time", prepare_time);
                                //  hashMap.put("menu_image", menu_image);


                                non_veg_list.add(hashMap);
                            }




                            rv_item_list.setLayoutManager(new LinearLayoutManager(AddItemActivity.this, LinearLayoutManager.VERTICAL, false));
                            ItemsAdapter itemsAdapter = new ItemsAdapter(AddItemActivity.this, menu_list,tv_item_count);
                            rv_item_list.setAdapter(itemsAdapter);
                            itemsAdapter.notifyDataSetChanged();

                            rv_veg_list.setLayoutManager(new LinearLayoutManager(AddItemActivity.this, LinearLayoutManager.VERTICAL, false));
                            ItemsAdapter vegAdapter = new ItemsAdapter(AddItemActivity.this, veg_list,tv_item_count);
                            rv_veg_list.setAdapter(vegAdapter);
                            vegAdapter.notifyDataSetChanged();

                            rv_non_veg_list.setLayoutManager(new LinearLayoutManager(AddItemActivity.this, LinearLayoutManager.VERTICAL, false));
                            ItemsAdapter nonvegAdapter = new ItemsAdapter(AddItemActivity.this, non_veg_list,tv_item_count);
                            rv_non_veg_list.setAdapter(nonvegAdapter);
                            nonvegAdapter.notifyDataSetChanged();




                        }else{

                            Toasty.info(AddItemActivity.this, message, Toast.LENGTH_LONG).show();
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

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(AddItemActivity.this, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search_list(String search_text) {
        //  mView.showDialog();
        ll_select_orders.setVisibility(View.GONE);
        rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "search_item";
        String url = ApplicationConstants.search_item;
        Log.d(TAG, "search_list url: "+url);

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "search_item Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        if(status.equals("1")) {
                            menu_list.clear();


                            JsonArray data = jobj.getAsJsonArray("response");
                            Log.d(TAG, "onResponse menu_list_arr: "+data);

                            for( int i = 0 ; i < data.size() ; i++ ) {

                                JsonObject obj_data = data.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String menu_id = obj_data.get("menu_id").getAsString().replaceAll("\"", "");;
                                String menu_name = obj_data.get("menu_name").getAsString().replaceAll("\"", "");;
                                String category_id = obj_data.get("category_id").getAsString().replaceAll("\"", "");;
                                //String category_name = obj_data.get("category_name").getAsString().replaceAll("\"", "");;
                                String description = obj_data.get("description").getAsString().replaceAll("\"", "");;
                                String veg_nonveg = obj_data.get("veg_nonveg").getAsString().replaceAll("\"", "");;
                                String price = obj_data.get("price").getAsString().replaceAll("\"", "");;
                                String prepare_time = obj_data.get("prepare_time").getAsString().replaceAll("\"", "");;
                                String menu_image = obj_data.get("menu_image").getAsString().replaceAll("\"", "");;


                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("menu_id", menu_id);
                                hashMap.put("menu_name", menu_name);
                                hashMap.put("category_id", category_id);
                             //   hashMap.put("category_name", category_name);
                                hashMap.put("description", description);
                                hashMap.put("veg_nonveg", veg_nonveg);
                                hashMap.put("price", price);
                                hashMap.put("prepare_time", prepare_time);
                                hashMap.put("menu_image", menu_image);

                                menu_list.add(hashMap);
                            }




                            rv_item_list.setLayoutManager(new LinearLayoutManager(AddItemActivity.this, LinearLayoutManager.VERTICAL, false));
                            ItemsAdapter itemsAdapter = new ItemsAdapter(AddItemActivity.this, menu_list,tv_item_count);
                            rv_item_list.setAdapter(itemsAdapter);
                            itemsAdapter.notifyDataSetChanged();





                        }else{

                            Toasty.info(AddItemActivity.this, message, Toast.LENGTH_LONG).show();
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
                    Log.e(TAG, "search_item Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    rl_progress.setVisibility(View.GONE);

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("search_text",search_text);

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(AddItemActivity.this, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
