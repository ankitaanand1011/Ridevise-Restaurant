package com.restaurant.ridewise.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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

public class NotificationActivity extends AppCompatActivity {
    String TAG = "notify";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    RelativeLayout rl_progress;

    ImageView iv_back;
    TextView tv_header;
    private ArrayList<HashMap<String, String>> notification_list;
    RecyclerView rv_notification;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initialization();
        function();
    }


    private void initialization() {
        globalClass = (GlobalClass) getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();
        rl_progress = findViewById(R.id.rl_progress);

        iv_back = findViewById(R.id.iv_back);
        tv_header = findViewById(R.id.tv_header);
        rv_notification = findViewById(R.id.rv_notification);
    }


    private void function() {
        notification_list = new ArrayList<>();

      /*  notification_list.add("Notification Title 1");
        notification_list.add("Notification Title 2");
        notification_list.add("Notification Title 3");
        notification_list.add("Notification Title 4");
        notification_list.add("Notification Title 5");*/

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


        res_notifications();
    }

    public void res_notifications() {
        //  mView.showDialog();

        rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "res_notifications";
        String url = ApplicationConstants.res_notifications;

        try {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {


                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "menu_list Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message = jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: " + message);


                        if (status.equals("1")) {
                            notification_list.clear();
                            JsonArray data = jobj.getAsJsonArray("response");
                            Log.d(TAG, "onResponse menu_list_arr: " + data);

                            for (int i = 0; i < data.size(); i++) {

                                JsonObject obj_data = data.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  " + obj_data);
                                String title = obj_data.get("title").getAsString().replaceAll("\"", "");
                                ;
                                String description = obj_data.get("description").getAsString().replaceAll("\"", "");
                                ;
                                String created_at = obj_data.get("created_at").getAsString().replaceAll("\"", "");
                                ;


                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("title", title);
                                hashMap.put("description", description);
                                hashMap.put("created_at", created_at);


                                notification_list.add(hashMap);
                            }


                            rv_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false));
                            NotificationAdapter notificationAdapter = new NotificationAdapter(NotificationActivity.this, notification_list);
                            rv_notification.setAdapter(notificationAdapter);
                            notificationAdapter.notifyDataSetChanged();


                        } else {

                            Toasty.info(NotificationActivity.this, message, Toast.LENGTH_LONG).show();
                        }

                        rl_progress.setVisibility(View.GONE);

                    } catch (Exception e) {
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
                    params.put("res_id", globalClass.getId());

                    Log.d(TAG, "getParams: " + params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(NotificationActivity.this, strReq, tag_string_req);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ///////
            String s = intent.getStringExtra("message_body");
            String p = intent.getStringExtra("message");
            Log.d("MH", "onReceive: chat " + s + " " + p);


            res_notifications();
        }
    };

    @Override
    public void onResume() {

        //  show_chat();
        super.onResume();
        // check_device();
        NotificationActivity.this.registerReceiver(mMessageReceiver, new IntentFilter("order_broadcast"));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                res_notifications();
                handler.postDelayed(this, 60000);
            }
        }, 60000);

    }

    @Override
    public void onPause() {
        super.onPause();
        NotificationActivity.this.unregisterReceiver(mMessageReceiver);


    }

}