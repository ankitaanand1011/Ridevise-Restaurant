package com.restaurant.ridewise.util;


import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;


public class GlobalClass extends Application {

    public Boolean login_status = false;
    String id,  res_name, res_email, res_phone,res_address,res_address2,postal_code,res_image,
            open_time,close_time, res_lat, res_long;
    String  cart_item_count="0";
    String user_id,user_address_id ,user_name, user_email, user_phn, user_address, user_pin, user_lat, user_long;
    String cart_item;
    double total_item_price_acc_qty = 0.0;
    private static RequestQueue mRequestQueue;

     public  <T> void addToRequestQueue(Context mContext, Request<T> request, String tag) {
         // set the default tag if tag is empty
         request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         request.setTag(TextUtils.isEmpty(tag));
         getRequestQueue(mContext).add(request);
     }

    private RequestQueue getRequestQueue(Context mContext) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }

        return mRequestQueue;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public static String getFolderDirectory(){
        final String dir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/RidewiseRestaurant";
        return dir;
    }
    public static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "PROFILE_PICTURE_" + timeStamp;
        File image, storageDir;

        if (SDK_INT < Build.VERSION_CODES.Q) {
            storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "RideWiseRES");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            image = new File(storageDir, imageFileName + ".jpg");
            image.createNewFile();
        } else {
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "RideWiseRES");
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
            String currentPhotoPath = image.getAbsolutePath();
        }

        return image;
    }
    @Override
    public void onCreate() {
        super.onCreate();

      //  TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Montserrat-Regular.ttf");
      //  TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/POPPINS-MEDIUM.ttf");
    }


    public Boolean getLogin_status() {
        return login_status;
    }

    public void setLogin_status(Boolean login_status) {
        this.login_status = login_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

    public String getRes_email() {
        return res_email;
    }

    public void setRes_email(String res_email) {
        this.res_email = res_email;
    }

    public String getRes_phone() {
        return res_phone;
    }

    public void setRes_phone(String res_phone) {
        this.res_phone = res_phone;
    }

    public String getRes_address() {
        return res_address;
    }

    public void setRes_address(String res_address) {
        this.res_address = res_address;
    }

    public String getRes_address2() {
        return res_address2;
    }

    public void setRes_address2(String res_address2) {
        this.res_address2 = res_address2;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getRes_image() {
        return res_image;
    }

    public void setRes_image(String res_image) {
        this.res_image = res_image;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getCart_item_count() {
        return cart_item_count;
    }

    public void setCart_item_count(String cart_item_count) {
        this.cart_item_count = cart_item_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phn() {
        return user_phn;
    }

    public void setUser_phn(String user_phn) {
        this.user_phn = user_phn;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_pin() {
        return user_pin;
    }

    public void setUser_pin(String user_pin) {
        this.user_pin = user_pin;
    }

    public String getUser_lat() {
        return user_lat;
    }

    public void setUser_lat(String user_lat) {
        this.user_lat = user_lat;
    }

    public String getUser_long() {
        return user_long;
    }

    public void setUser_long(String user_long) {
        this.user_long = user_long;
    }

    public String getUser_address_id() {
        return user_address_id;
    }

    public void setUser_address_id(String user_address_id) {
        this.user_address_id = user_address_id;
    }

    public String getCart_item() {
        return cart_item;
    }

    public void setCart_item(String cart_item) {
        this.cart_item = cart_item;
    }

    public double getTotal_item_price_acc_qty() {
        return total_item_price_acc_qty;
    }

    public void setTotal_item_price_acc_qty(double total_item_price_acc_qty) {
        this.total_item_price_acc_qty = total_item_price_acc_qty;
    }

    public String getRes_lat() {
        return res_lat;
    }

    public void setRes_lat(String res_lat) {
        this.res_lat = res_lat;
    }

    public String getRes_long() {
        return res_long;
    }

    public void setRes_long(String res_long) {
        this.res_long = res_long;
    }
}