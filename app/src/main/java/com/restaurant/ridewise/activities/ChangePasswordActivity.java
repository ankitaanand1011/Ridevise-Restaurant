package com.restaurant.ridewise.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ChangePasswordActivity extends AppCompatActivity {
    String TAG="change_pass";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    TextView tv_forgot_text,tv_submit;
    EditText et_old_pass,et_new_pass,et_confirm_password;
    SpinKitView spinKitView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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

        tv_forgot_text = findViewById(R.id. tv_forgot_text);
        et_new_pass = findViewById(R.id. et_new_pass);
        et_old_pass = findViewById(R.id. et_old_pass);
        et_confirm_password = findViewById(R.id. et_confirm_password);
        tv_submit = findViewById(R.id. tv_submit);
    }

    private void function() {
        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        tv_forgot_text.setTypeface(medium);
        et_new_pass.setTypeface(regular);
        et_old_pass.setTypeface(regular);
        et_confirm_password.setTypeface(regular);
        tv_submit.setTypeface(regular);


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(SetPasswordActivity.this, SetPasswordActivity.class);
                startActivity(intent);*/

                if(globalClass.isNetworkAvailable()) {
                    if (!et_old_pass.getText().toString().trim().isEmpty()) {
                            if (!et_new_pass.getText().toString().trim().isEmpty()) {
                                if (!et_confirm_password.getText().toString().trim().isEmpty()) {
                                    if(et_confirm_password.getText().toString().matches(et_new_pass.getText().toString())){
                                        change_password(et_old_pass.getText().toString(),et_new_pass.getText().toString(),et_confirm_password.getText().toString());
                                    } else {
                                        Toasty.warning(ChangePasswordActivity.this, "Password mismatch.", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toasty.warning(ChangePasswordActivity.this, "Please enter confirm password.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toasty.warning(ChangePasswordActivity.this, "Please enter new password.", Toast.LENGTH_LONG).show();
                            }

                    } else {
                        Toasty.warning(ChangePasswordActivity.this, "Please enter the mobile number.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toasty.info(ChangePasswordActivity.this,  "Check your internet connection.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void change_password(final String old_pass,final String new_pass,final String confirm_pass) {
        // Tag used to cancel the request
        final String tag_string_req = "req_login";

        spinKitView.setVisibility(View.VISIBLE);
        String url = ApplicationConstants.change_password;
        Log.d(TAG, "change_password:url>>>  "+url);
        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "change_password Response: " + response);

                    Gson gson = new Gson();

                    try {

                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message = jobj.get("message").getAsString().replaceAll("\"", "");

                        spinKitView.setVisibility(View.GONE);
                        Toasty.success(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT, true).show();


                    }catch (Exception e){
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "change_password Error: " + error.getMessage());
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
                    params.put("new_password", new_pass);
                    params.put("confirm_new_password", confirm_pass);
                    params.put("current_password", old_pass);


                    Log.d(TAG, "getParams: "+params);

                    return params;
                }
            };

            globalClass.addToRequestQueue(ChangePasswordActivity.this, strReq, tag_string_req);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
