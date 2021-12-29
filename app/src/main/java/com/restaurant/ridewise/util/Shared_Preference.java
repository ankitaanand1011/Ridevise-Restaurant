package com.restaurant.ridewise.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Developer on 7/19/17.
 */

public class Shared_Preference {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;

    SharedPreferences sharedPreferences3;
    SharedPreferences.Editor editor3;

    GlobalClass global_class;
    private boolean pref_logInStatus;
    String id,  res_name, res_email, res_phone,res_address,res_address2,postal_code,res_image,
            open_time,close_time,res_lat, res_long;

    private static final String PREFS_NAME = "preferences";

    private static final String PREF_logInStatus = "logInStatus";
    private static final String PREF_id="id";
    private static final String PREF_res_name="res_name";
    private static final String PREF_res_email="res_email";
    private static final String PREF_res_phone="res_phone";
    private static final String PREF_res_address="res_address";
    private static final String PREF_res_address2="res_address2";
    private static final String PREF_postal_code="postal_code";
    private static final String PREF_res_image="res_image";
    private static final String PREF_open_time="open_time";
    private static final String PREF_close_time="close_time";
    private static final String PREF_res_lat="res_lat";
    private static final String PREF_res_long="res_long";





    @SuppressLint("CommitPrefEdits")
    public Shared_Preference(Context context) {
        this.context = context;

        this.global_class = (GlobalClass) context.getApplicationContext();
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();





    }

    public void savePrefrence() {
        if (global_class.getLogin_status()) {

            pref_logInStatus = global_class.getLogin_status();
            editor.putBoolean(PREF_logInStatus, pref_logInStatus);



            id = global_class.getId();
            editor.putString(PREF_id, id);

            res_name = global_class.getRes_name();
            editor.putString(PREF_res_name, res_name);

            res_email= global_class.getRes_email();
            editor.putString(PREF_res_email,res_email);

            res_phone= global_class.getRes_phone();
            editor.putString(PREF_res_phone,res_phone);

            res_address= global_class.getRes_address();
            editor.putString(PREF_res_address,res_address);

            res_address2= global_class.getRes_address2();
            editor.putString(PREF_res_address2,res_address2);

            postal_code= global_class.getPostal_code();
            editor.putString(PREF_postal_code,postal_code);


            res_image= global_class.getRes_image();
            editor.putString(PREF_res_image,res_image);

            open_time= global_class.getOpen_time();
            editor.putString(PREF_open_time,open_time);

            close_time= global_class.getClose_time();
            editor.putString(PREF_close_time,close_time);

            res_lat= global_class.getRes_lat();
            editor.putString(PREF_res_lat,res_lat);

            res_long= global_class.getRes_long();
            editor.putString(PREF_res_long,res_long);



            editor.commit();

        }else{
            // dont save anything, if user is logged out
            pref_logInStatus = global_class.getLogin_status();
            editor.putBoolean(PREF_logInStatus, pref_logInStatus);
            editor.commit();
        }

    }

    public void loadPrefrence() {
        pref_logInStatus = sharedPreferences.getBoolean(PREF_logInStatus, false);
        global_class.setLogin_status(pref_logInStatus);

        Log.d("TV", global_class.getLogin_status() + "");
        if (global_class.getLogin_status()) {


          //res_name, res_email, res_phone,res_address,res_address2,postal_code,res_image,open_time,close_time;
            id = sharedPreferences.getString(PREF_id, "");
            global_class.setId(id);

            res_name = sharedPreferences.getString(PREF_res_name, "");
            global_class.setRes_name(res_name);

            res_email= sharedPreferences.getString(PREF_res_email,"");
            global_class.setRes_email(res_email);

            res_phone= sharedPreferences.getString(PREF_res_phone,"");
            global_class.setRes_phone(res_phone);

            res_address= sharedPreferences.getString(PREF_res_address,"");
            global_class.setRes_address(res_address);

            res_address2= sharedPreferences.getString(PREF_res_address2,"");
            global_class.setRes_address2(res_address2);


            postal_code= sharedPreferences.getString(PREF_postal_code,"");
            global_class.setPostal_code(postal_code);

            res_image=sharedPreferences.getString(PREF_res_image,"");
            global_class.setRes_image(res_image);

            open_time=sharedPreferences.getString(PREF_open_time,"");
            global_class.setOpen_time(open_time);

            close_time=sharedPreferences.getString(PREF_close_time,"");
            global_class.setClose_time(close_time);

            res_lat=sharedPreferences.getString(PREF_res_lat,"");
            global_class.setRes_lat(res_lat);

            res_long=sharedPreferences.getString(PREF_res_long,"");
            global_class.setRes_long(res_long);





          /*  customer_token=sharedPreferences.getString(PREFcustomer_token,"");
            global_class.setCustomer_token(customer_token);*/

    /*        jsonData=sharedPreferences.getString(JSON_data,"");
            global_class.setJsondata(jsonData);

            slider_pics=sharedPreferences.getString(Slider_pics,"");
            global_class.setSliderpics(slider_pics);

            comments=sharedPreferences.getString(Comments,"");
            global_class.setComments_1(comments);

            chat_fetch=sharedPreferences.getString(Chat_fetch,"");
            global_class.setChatfetch(chat_fetch);

            media_fetch=sharedPreferences.getString(Media_fetch,"");
            global_class.setMediafetch(media_fetch);

            event_fetch=sharedPreferences.getString(Event_fetch,"");
            global_class.setEventfetch(event_fetch);

            fcm=sharedPreferences.getString(f_cm,"");
            global_class.setFCM(fcm);

            p_agenda=sharedPreferences.getString(P_agenda,"");
            global_class.setImg_agenda(p_agenda);


            survey_link= sharedPreferences.getString(K_survey_link,"");
            global_class.setSurvey_link(survey_link);

            survey_event_id = sharedPreferences.getString(K_survey_eid , "");
            global_class.setSurvey_event_id(survey_event_id);*/

     /*       pref_cart_no=sharedPreferences.getString(PREF_cart_no,"");
            global_class.setCart_item(pref_cart_no);
*/

        //    Log.d("price", "loadPref: "+pref_name);
//            Log.d("rom", "slider_pics: "+slider_pics.toString());

        }
    }


  /*  public void setFirstlogin(boolean b){
        editor2.putBoolean(is_first,b);
        editor2.commit();
    }
    public boolean IS_First_Time(){
        return sharedPreferences2.getBoolean(is_first, false);

    }*/

    public  void clear_notification(){
        editor3.clear();
        editor3.commit();

    }

 /*   public  void  save_noti(){
        key_status = global_class.getKeyStatus();
        editor3.putString(Key_status, key_status);

        editor3.commit();

    }

    public  void load_noti(){

        key_status=sharedPreferences3.getString(Key_status,"");
        global_class.setKeyStatus(key_status);
    }*/



    public void clearPrefrence(){

        editor.clear();
        editor.commit();


    }













}