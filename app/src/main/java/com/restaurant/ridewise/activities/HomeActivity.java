package com.restaurant.ridewise.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.adapter.MenuAdapter;
import com.restaurant.ridewise.adapter.OrderAdapter;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity{
    String TAG = "dashboard";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    RelativeLayout rl_progress;
    
    LinearLayout ll_add_order;
    TextView tv_text,tv_today_order,tv_add;
    ImageView iv_profile,iv_notify,iv_menu,iv_add,iv_no_order;
    RelativeLayout rl_no_order;
    RecyclerView rv_order;
    ImageView fab_add;

    private ArrayList<HashMap<String, String>> order_list_arr;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialization();
        function();
    }



    private void initialization() {

        UpdateManager mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.FLEXIBLE);
        mUpdateManager.start();

        globalClass = (GlobalClass)getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();
        rl_progress = findViewById(R.id.rl_progress);

        ll_add_order = findViewById(R.id.ll_add_order);
        tv_text = findViewById(R.id.tv_text);
        tv_today_order = findViewById(R.id.tv_today_order);
        tv_add = findViewById(R.id.tv_add);
        iv_profile = findViewById(R.id.iv_profile);
        iv_notify = findViewById(R.id.iv_notify);
        iv_menu = findViewById(R.id.iv_menu);
        iv_add = findViewById(R.id.iv_add);
        rl_no_order = findViewById(R.id.rl_no_order);
        iv_no_order = findViewById(R.id.iv_no_order);
        rv_order = findViewById(R.id.rv_order);
        fab_add = findViewById(R.id.fab_add);
    }

    private void function() {
        order_list_arr = new ArrayList<>();
        
        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");


     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                       String token = task.getResult();

                        // Log and toast
                        //   String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
                        // Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });*/


        tv_text.setTypeface(light);
        tv_today_order.setTypeface(medium);
        tv_add.setTypeface(medium);

        ll_add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchCustomerActivity.class);
                startActivity(intent);
            }
        });



        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchCustomerActivity.class);
                startActivity(intent);
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchCustomerActivity.class);
                startActivity(intent);
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        iv_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        order_list();
    }

    public void order_list() {
        //  mView.showDialog();

        rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "order_list";
        String url = ApplicationConstants.order_list;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "order_list Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        if(status.equals("1")) {
                            order_list_arr.clear();
                          //  JsonObject data = jobj.getAsJsonObject("response");
                            // Log.d(TAG, "onResponse data: "+data);

                            JsonArray data = jobj.getAsJsonArray("response");

                            if(data.size()>0) {



                                for (int i = 0; i < data.size(); i++) {

                                    JsonObject obj_data = data.get(i).getAsJsonObject();

                                    String id = obj_data.get("id").getAsString().replaceAll("\"", "");
                                    String order_id = obj_data.get("order_id").getAsString().replaceAll("\"", "");


                                    String user_id = obj_data.get("user_id").getAsString().replaceAll("\"", "");


                                    String address = obj_data.get("address").getAsString().replaceAll("\"", "");


                                    String user_lat = obj_data.get("user_lat").getAsString().replaceAll("\"", "");

                                    String user_long = obj_data.get("user_long").getAsString().replaceAll("\"", "");

                                    String order_date = obj_data.get("order_date").getAsString().replaceAll("\"", "");

                                    String order_time = obj_data.get("order_time").getAsString().replaceAll("\"", "");


                                    String sub_total = obj_data.get("sub_total").getAsString().replaceAll("\"", "");

                                    String total_price = obj_data.get("total_price").getAsString().replaceAll("\"", "");

                                    String delivery_charge = obj_data.get("delivery_charge").getAsString().replaceAll("\"", "");


                                    String order_status = obj_data.get("status").getAsString().replaceAll("\"", "");
                                    String user_name = obj_data.get("user_name").getAsString().replaceAll("\"", "");
                                    String payment_status = obj_data.get("payment_status").getAsString().replaceAll("\"", "");



                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("id", id);
                                    hashMap.put("order_id", order_id);
                                    hashMap.put("user_id", user_id);
                                    hashMap.put("address", address);

                                    hashMap.put("user_lat", user_lat);
                                    hashMap.put("user_long", user_long);
                                    hashMap.put("order_date", order_date);
                                    hashMap.put("order_time", order_time);

                                    hashMap.put("sub_total", sub_total);
                                    hashMap.put("total_price", total_price);
                                    hashMap.put("delivery_charge", delivery_charge);
                                    hashMap.put("order_status", order_status);
                                    hashMap.put("user_name", user_name);
                                    hashMap.put("payment_status", payment_status);

                                    order_list_arr.add(hashMap);
                                }


                                Log.d(TAG, "onResponse: order_list > "+order_list_arr);
                                rl_no_order.setVisibility(View.GONE);
                                iv_no_order.setVisibility(View.GONE);

                                rv_order.setVisibility(View.VISIBLE);
                                fab_add.setVisibility(View.VISIBLE);

                                rv_order.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
                                OrderAdapter orderAdapter = new OrderAdapter(HomeActivity.this, order_list_arr);
                                rv_order.setAdapter(orderAdapter);
                                orderAdapter.notifyDataSetChanged();

                            }
                            rl_progress.setVisibility(View.GONE);


                        }else{


                                rl_no_order.setVisibility(View.VISIBLE);
                                iv_no_order.setVisibility(View.VISIBLE);

                                rv_order.setVisibility(View.GONE);
                                fab_add.setVisibility(View.GONE);


                           // Toasty.info(HomeActivity.this, message, Toast.LENGTH_LONG).show();
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
                    params.put("res_id",globalClass.getId());

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(HomeActivity.this, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {

        //  show_chat();
        super.onResume();
        //  startService(new  Intent(this, Service_class.class));
        HomeActivity.this.registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
        HomeActivity.this.registerReceiver(mMessageReceiver, new IntentFilter("logout_status"));
        HomeActivity.this.registerReceiver(mMessageReceiver, new IntentFilter("group_chat"));
        HomeActivity.this.registerReceiver(mMessageReceiver, new IntentFilter("grp_chat_broadcast"));
        //Log.d("kite", "onResume: ");



   /*     tooltext.setText(global_class.title);
        toolbar_image.setVisibility(View.INVISIBLE);
        if (global_class.title.equals("Chat")){
            toolbar_image.setVisibility(View.VISIBLE);
        }else {
            toolbar_image.setVisibility(View.INVISIBLE);
        }

        Log.d("vary", "aasdfghjk ");
        shared_prefrence.loadPrefrence();
        show_chat();
        setupDrawerContent(navigationView);*/

        // Log.d("ORRRR", "Call Onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
       // toolbar_image.setVisibility(View.GONE);
        //  stopService(new Intent(this,Service_class.class));
        HomeActivity.this.unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("kite", "ev_unique_name " + intent);
            Log.d("MH", "intent_drwer" + intent.getStringExtra("message"));

            String j = intent.getStringExtra("message");
            Log.d("MH", "intent_drwer_j >>>>" + j );
        /*    if(j.equals("logout")){
                shared_prefrence.clearPrefrence();
                Intent intent_1 = new Intent(Drawer_Activity.this, Login_Screen.class);
                intent_1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_1);
                finish();
            }else if(j.equals("chat_add_remove")) {
                // show_chat();
                String k = intent.getStringExtra("message_body");
                if(k.equals("Admin removed you from the group chat.")){
                    target.setVisible(false);
                }else if(k.equals("Admin added you in the group chat.")){
                    target.setVisible(true);
                }

            }else {
                get_ad_video();
            }*/



        }
    };
}
