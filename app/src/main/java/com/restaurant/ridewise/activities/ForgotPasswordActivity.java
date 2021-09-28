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

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView tv_forgot_text,tv_text,tv_back_to,tv_submit;
    EditText et_mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        initialization();
        function();
    }

    private void initialization() {
        tv_forgot_text = findViewById(R.id. tv_forgot_text);
        tv_text = findViewById(R.id. tv_text);
        et_mobile = findViewById(R.id. et_mobile);
        tv_back_to = findViewById(R.id. tv_back_to);
        tv_submit = findViewById(R.id. tv_submit);
    }

    private void function() {
        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        tv_forgot_text.setTypeface(medium);
        tv_text.setTypeface(regular);
        et_mobile.setTypeface(regular);
        tv_back_to.setTypeface(regular);
        tv_submit.setTypeface(regular);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, OtpVerifyActivity.class);
                startActivity(intent);
            }
        });
    }
}
