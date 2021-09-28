package com.restaurant.ridewise.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.restaurant.ridewise.R;

public class SetPasswordActivity extends AppCompatActivity {
    TextView tv_forgot_text,tv_submit;
    EditText et_mobile,et_new_pass,et_confirm_password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        initialization();
        function();
    }

    private void initialization() {
        tv_forgot_text = findViewById(R.id. tv_forgot_text);
        et_new_pass = findViewById(R.id. et_new_pass);
     //   et_mobile = findViewById(R.id. et_mobile);
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
     //   et_mobile.setTypeface(regular);
        et_confirm_password.setTypeface(regular);
        tv_submit.setTypeface(regular);


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(SetPasswordActivity.this, SetPasswordActivity.class);
                startActivity(intent);*/
            }
        });

    }
}
