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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {
    String TAG ="profile";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    SpinKitView spinKitView;
    TextView tv_header,tv_name_res,tv_add_res,tv_res_detail,tv_profile,tv_manage_address,tv_manage_menu,
            tv_order_history,tv_notification,tv_change_password,tv_logout;
    RelativeLayout rl_profile,rl_change_password,rl_order_history,rl_notification,rl_manage_menu,rl_logout;
    ImageView iv_back,iv_profile;
    String res_name, res_phone, res_address, res_address2, postal_code,res_image,contact_person,res_email;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialization();
        function();
    }

    private void initialization() {
        globalClass = (GlobalClass)getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();

        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
        Sprite fadingCircle = new FadingCircle();
        spinKitView.setIndeterminateDrawable(fadingCircle);
        spinKitView.setVisibility(View.GONE);

        iv_back = findViewById(R.id.iv_back);
        iv_profile = findViewById(R.id.iv_profile);
        tv_header = findViewById(R.id.tv_header);
        tv_name_res = findViewById(R.id.tv_name_res);
        tv_add_res = findViewById(R.id.tv_add_res);
        tv_res_detail = findViewById(R.id.tv_res_detail);
        tv_profile = findViewById(R.id.tv_profile);
        tv_manage_address = findViewById(R.id.tv_manage_address);
        tv_manage_menu = findViewById(R.id.tv_manage_menu);
        tv_order_history = findViewById(R.id.tv_order_history);
        tv_notification = findViewById(R.id.tv_notification);
        tv_change_password = findViewById(R.id.tv_change_password);
        tv_logout = findViewById(R.id.tv_logout);
        rl_profile = findViewById(R.id.rl_profile);
        rl_change_password = findViewById(R.id.rl_change_password);
        rl_order_history = findViewById(R.id.rl_order_history);
        rl_notification = findViewById(R.id.rl_notification);
        rl_manage_menu = findViewById(R.id.rl_manage_menu);
        rl_logout = findViewById(R.id.rl_logout);
    }

    private void function() {
        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        tv_header.setTypeface(medium);
        tv_name_res.setTypeface(medium);
        tv_profile.setTypeface(medium);
        tv_manage_address.setTypeface(medium);
        tv_manage_menu.setTypeface(medium);
        tv_order_history.setTypeface(medium);
        tv_notification.setTypeface(medium);
        tv_change_password.setTypeface(medium);
        tv_logout.setTypeface(medium);

        tv_add_res.setTypeface(regular);
        tv_res_detail.setTypeface(regular);

        profile();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileInfoActivity.class);

                intent.putExtra("res_name",res_name);
                intent.putExtra("res_phone",res_phone);
                intent.putExtra("res_address",res_address);
                intent.putExtra("res_address2",res_address2);
                intent.putExtra("postal_code",postal_code);
                intent.putExtra("res_image",res_image);
                intent.putExtra("postal_code",postal_code);
                intent.putExtra("contact_person",contact_person);
                //intent.putExtra("res_email",res_email);

                startActivity(intent);
            }
        });

        rl_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        rl_order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        rl_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        rl_manage_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (globalClass.isNetworkAvailable()){

                    shared_preference.clearPrefrence();
                    globalClass.setLogin_status(false);
                    Toast.makeText(ProfileActivity.this,"You are now logged out.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(ProfileActivity.this,"Check your internet connection.",Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    private void profile() {
        // Tag used to cancel the request
        final String tag_string_req = "req_login";

        spinKitView.setVisibility(View.VISIBLE);
        String url = ApplicationConstants.profile;
        Log.d(TAG, "login:url>>>  "+url);
        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "login Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message = jobj.get("message").getAsString().replaceAll("\"", "");
                        Log.d(TAG, "Message: "+message);
                        if(status.equals("1")) {
                            JsonObject data = jobj.getAsJsonObject("response");
                          //  String id = data.get("id").getAsString().replaceAll("\"", "");
                             res_name = data.get("res_name").getAsString().replaceAll("\"", "");
                            // res_email = data.get("res_email").getAsString().replaceAll("\"", "");
                             res_phone = data.get("res_phone").getAsString().replaceAll("\"", "");
                             res_address = data.get("res_address").getAsString().replaceAll("\"", "");
                             res_address2 = data.get("res_address2").getAsString().replaceAll("\"", "");
                             postal_code = data.get("postal_code").getAsString().replaceAll("\"", "");
                             res_image = data.get("res_image").getAsString().replaceAll("\"", "");
                            String res_tag_line = data.get("res_tag_line").getAsString().replaceAll("\"", "");
                             contact_person = data.get("contact_person").getAsString().replaceAll("\"", "");
//                            String open_time = data.get("open_time").getAsString().replaceAll("\"", "");
                    //        String close_time = data.get("close_time").getAsString().replaceAll("\"", "");

                        //    Log.d(TAG, "onResponse:id>>>> " + id);
                            Log.d(TAG, "onResponse:res_name>>> " + res_name);
                            Log.d(TAG, "onResponse:res_phone>>> " + res_phone);
                          //  Log.d(TAG, "onResponse:res_email>>> " + res_email);

                            tv_name_res.setText(res_name+",");
                            tv_add_res.setText(res_address+", "+res_address2);
                            tv_res_detail.setText(res_tag_line);

                            RequestOptions options = new RequestOptions()
                                    .centerInside()
                                    .placeholder(R.drawable.no_image)
                                    .error(R.drawable.no_image);
                            Glide.with(ProfileActivity.this).load(res_image).apply(options).into(iv_profile);


                          //  Toasty.success(ProfileActivity.this, "Login successful!", Toast.LENGTH_SHORT, true).show();

                            Log.d(TAG, "onSuccess:id "+message);
                            spinKitView.setVisibility(View.GONE);



                        }else{
                            spinKitView.setVisibility(View.GONE);

                            Toasty.error(ProfileActivity.this, message, Toast.LENGTH_SHORT, true).show();

                            //Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();
                        }



                    }catch (Exception e){
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "login Error: " + error.getMessage());
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


                    params.put("res_id", globalClass.getId());
                 


                    Log.d(TAG, "getParams: "+params);

                    return params;
                }

            };

            globalClass.addToRequestQueue(ProfileActivity.this, strReq, tag_string_req);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        profile();
    }
}
