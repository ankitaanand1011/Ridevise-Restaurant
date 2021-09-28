package com.restaurant.ridewise.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.restaurant.ridewise.adapter.MenuAdapter;
import com.restaurant.ridewise.adapter.NotificationAdapter;
import com.restaurant.ridewise.adapter.OrderHistoryAdapter;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MenuActivity extends AppCompatActivity {

    String TAG = "menu_list";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    RelativeLayout rl_progress;

    ImageView iv_back;
    TextView tv_header,tv_add;
    private ArrayList<HashMap<String, String>>  menu_list;
    RecyclerView rv_menu;
    LinearLayout ll_add_order;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
        rv_menu = findViewById(R.id.rv_menu);
        ll_add_order = findViewById(R.id.ll_add_order);
        tv_add = findViewById(R.id.tv_add);
    }


    private void function() {
        menu_list = new ArrayList<>();


        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        tv_header.setTypeface(medium);
        tv_add.setTypeface(medium);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MenuActivity.this, AddMenuActivity.class);
                startActivity(intent);
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

                            Log.d(TAG, "onResponse: me > "+menu_list);

                            rv_menu.setLayoutManager(new LinearLayoutManager(MenuActivity.this, LinearLayoutManager.VERTICAL, false));
                            MenuAdapter menuAdapter  = new MenuAdapter(MenuActivity.this, menu_list);
                            rv_menu.setAdapter(menuAdapter);
                            menuAdapter.notifyDataSetChanged();




                        }else{

                            Toasty.info(MenuActivity.this, message, Toast.LENGTH_LONG).show();
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

            globalClass.addToRequestQueue(MenuActivity.this, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        menu_list();
    }
}
