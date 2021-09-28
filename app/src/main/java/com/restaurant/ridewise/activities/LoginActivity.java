package com.restaurant.ridewise.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    String TAG = "login";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    TextView tv_login,tv_login_text,tv_forgot_password;
    EditText et_mobile,et_password;
    SpinKitView spinKitView;
    String device_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();
        function();
    }

    private void initialization() {

        UpdateManager mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.FLEXIBLE);
        mUpdateManager.start();

        globalClass = (GlobalClass)getApplicationContext();
        shared_preference = new Shared_Preference(this);
        shared_preference.loadPrefrence();


        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
        Sprite fadingCircle = new FadingCircle();
        spinKitView.setIndeterminateDrawable(fadingCircle);
        spinKitView.setVisibility(View.GONE);

        tv_login = findViewById(R.id.tv_login);
        tv_login_text = findViewById(R.id.tv_login_text);
        et_mobile = findViewById(R.id.et_mobile);
        et_password = findViewById(R.id.et_password);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
    }

    @SuppressLint("HardwareIds")
    private void function() {
        String login_text = "Please Login to \\nmanage your activities";
        device_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        if(globalClass.getLogin_status()){

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        tv_login_text.setTypeface(regular);
        et_mobile.setTypeface(regular);
        et_password.setTypeface(regular);
        tv_forgot_password.setTypeface(regular);
        tv_login.setTypeface(medium);

    //    SpannableString spannableString = new SpannableString(login_text);




        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            
                if(globalClass.isNetworkAvailable()) {
                    if (!et_mobile.getText().toString().trim().isEmpty()) {
                        if (!et_password.getText().toString().trim().isEmpty()) {
                            login_using_password(et_mobile.getText().toString(),et_password.getText().toString());
                        } else {
                            Toasty.warning(LoginActivity.this, "Please enter password.", Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.warning(LoginActivity.this, "Please enter the mobile number.", Toast.LENGTH_SHORT, true).show();
                    }
                }else{
                    Toasty.info(LoginActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login_using_password(final String mobile,final String password) {
        // Tag used to cancel the request
        final String tag_string_req = "req_login";
        tv_login.setEnabled(false);
        spinKitView.setVisibility(View.VISIBLE);
        String url = ApplicationConstants.login;
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
                            String id = data.get("id").getAsString().replaceAll("\"", "");
                            String res_name = data.get("res_name").getAsString().replaceAll("\"", "");
                          //  String res_email = data.get("res_email").getAsString().replaceAll("\"", "");
                            String res_phone = data.get("res_phone").getAsString().replaceAll("\"", "");
                            String res_address = data.get("res_address").getAsString().replaceAll("\"", "");
                            String res_address2 = data.get("res_address2").getAsString().replaceAll("\"", "");
                            String postal_code = data.get("postal_code").getAsString().replaceAll("\"", "");
                            String res_image = data.get("res_image").getAsString().replaceAll("\"", "");
                            String open_time = data.get("open_time").getAsString().replaceAll("\"", "");
                            String close_time = data.get("close_time").getAsString().replaceAll("\"", "");

                            Log.d(TAG, "onResponse:id>>>> " + id);
                            Log.d(TAG, "onResponse:res_name>>> " + res_name);
                            Log.d(TAG, "onResponse:res_phone>>> " + res_phone);
                            //Log.d(TAG, "onResponse:res_email>>> " + res_email);


                            globalClass.setId(id);
                            globalClass.setRes_phone(res_phone);
                           /* globalClass.setRes_name(res_name);
                            globalClass.setRes_email(res_email);
                            globalClass.setRes_address(res_address);
                            globalClass.setRes_address2(res_address2);
                            globalClass.setPostal_code(postal_code);
                            globalClass.setRes_image(res_image);
                            globalClass.setOpen_time(open_time);
                            globalClass.setClose_time(close_time);*/
                            globalClass.setLogin_status(true);
                            shared_preference.savePrefrence();


                            tv_login.setEnabled(true);
                            Toasty.success(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT, true).show();
                         //   Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onSuccess:id "+message);
                            spinKitView.setVisibility(View.GONE);
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);

                        }else{
                            spinKitView.setVisibility(View.GONE);

                            Toasty.error(LoginActivity.this, message, Toast.LENGTH_SHORT, true).show();

                            //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
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


                    params.put("phone", mobile);
                    params.put("password", password);
                    params.put("device_id", device_id);
                    params.put("device_type", "android");


                    Log.d(TAG, "getParams: "+params);

                    return params;
                }

            };

            globalClass.addToRequestQueue(LoginActivity.this, strReq, tag_string_req);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
