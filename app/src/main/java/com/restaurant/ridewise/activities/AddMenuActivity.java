package com.restaurant.ridewise.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.adapter.MenuAdapter;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class AddMenuActivity extends AppCompatActivity {
    String TAG = "add_menu";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    RelativeLayout rl_progress;

    ImageView iv_back,iv_profile;
    TextView tv_header,tv_save,tv_select_cat;
    EditText et_menu_name,et_description,et_prep_time,et_price;
    SwitchCompat switch_compat,switch_compat1;
    ArrayList<String> category_name,category_id;
    AlertDialog alertDialog1;
    private final int PICK_IMAGE_CAMERA = 11, PICK_IMAGE_GALLERY = 22;
    File p_image;
    String selected_cat_id,switch_state="nonveg";
    ImageLoader loader;
    DisplayImageOptions defaultOptions;
    String currentPhotoPath;
    String mCameraFileName = "";
    File destination;
    String imagePath;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

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
        switch_compat = findViewById(R.id.switch_compat);
        et_menu_name = findViewById(R.id.et_menu_name);
        tv_select_cat = findViewById(R.id.tv_select_cat);
        et_description = findViewById(R.id.et_description);
        et_prep_time = findViewById(R.id.et_prep_time);
        et_price = findViewById(R.id.et_price);
        tv_save = findViewById(R.id.tv_save);
        iv_profile = findViewById(R.id.iv_profile);
        switch_compat1 = findViewById(R.id.switch_compat1);

    }


    private void function() {

        category_name= new ArrayList<>();
        category_id= new ArrayList<>();

        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                //  .showImageOnLoading(R.mipmap.loading_black128px)
                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(AddMenuActivity.this.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();



        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        tv_header.setTypeface(medium);
        et_menu_name.setTypeface(regular);
        tv_select_cat.setTypeface(regular);
        et_description.setTypeface(regular);
        et_prep_time.setTypeface(regular);
        et_price.setTypeface(regular);
        tv_save.setTypeface(regular);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AddMenuActivity.this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(AddMenuActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(AddMenuActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            }
            else{
                if(checkForPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, 124)){

                }

            }
        }

        category_list();





       // switch_compat.setOnCheckedChangeListener(onCheckedChanged());
        switch_compat1.setOnCheckedChangeListener(onCheckedChanged());

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_select_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_listview();
            }
        });
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_save.setEnabled(false);
                menu_add();

            }
        });



    }

    private CompoundButton.OnCheckedChangeListener onCheckedChanged() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.switch_compat1:
                        setButtonState(isChecked);
                        break;

                }
            }
        };
    }

    private void setButtonState(boolean state) {
        if (state) {
            switch_state = "veg";
           // switch_compat.setText("Veg");
          //  button.setEnabled(true);
            //Toast.makeText(AddMenuActivity.this, "Button enabled!", Toast.LENGTH_SHORT).show();
        } else {
            switch_state = "nonveg";
            //switch_compat.setText("Non Veg");
            //button.setEnabled(false);
           // Toast.makeText(AddMenuActivity.this, "Button disabled!", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        try {
            PackageManager pm = AddMenuActivity.this.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, AddMenuActivity.this.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Choose From Gallery","Cancel"};
             //   final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMenuActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @SuppressLint("QueryPermissionsNeeded")
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                      if (options[item].equals("Choose From Gallery")) {
                                dialog.dismiss();
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);



                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toasty.error(AddMenuActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toasty.error(AddMenuActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: requestCode>> "+requestCode);
        Log.d(TAG, "onActivityResult: resultCode>> "+resultCode);

        if (requestCode == PICK_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            p_image = new File(getRealPathFromURI(uri));
            mCameraFileName = p_image.toString();

            Log.d(TAG, "image = " + p_image.toString());

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(AddMenuActivity.this.getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                iv_profile.setImageBitmap(bitmap);
                //  Glide.with(AddMenuActivity.this).load(p_image).apply(options).into(iv_profile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == PICK_IMAGE_CAMERA ) {

/*
            try {
                FileInputStream in = new FileInputStream(destination);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                imagePath = destination.getAbsolutePath();
                Log.d(TAG, "onActivityResult imagePath: "+imagePath);

               // tvPath.setText(imagePath);
                Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
               // picture.setImageBitmap(bmp);
                iv_profile.setImageBitmap(bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/

            Log.d(TAG , "mCameraFileName - " +mCameraFileName);
            Bitmap bitmap = BitmapFactory.decodeFile(mCameraFileName);
            iv_profile.setImageBitmap(bitmap);
            iv_profile.setScaleType(ImageView.ScaleType.FIT_XY);


          /*  Uri imageURI = getImageUri(AddMenuActivity.this,bitmap);

            Log.d(TAG, "onActivityResult:imageURI >  "+imageURI);*/
           // Bitmap photo = (Bitmap) data.getExtras().get("data");


        }
    }

    private void cameraIntent() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        String newPicFile = System.currentTimeMillis() + ".jpg";

        //String outPath = Common.getCachePath(getActivity());
       /* String outPath = GlobalClass.getFolderDirectory();
       // File file = new File(outPath);

        Log.d(TAG, "cameraIntent: outPath >>> "+outPath);

        File outFile = new File(outPath, newPicFile);
        if (!outFile.exists()) {
            outFile.mkdir();
        }*/

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
   //     File storageDir = getApplicationContext().getCacheDir();
        Log.d(TAG, "cameraIntent: storageDir >> "+storageDir);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createImageFile: image >> "+image);
        assert image != null;
        mCameraFileName = image.toString();

        Uri outuri = Uri.fromFile(image);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);

        startActivityForResult(intent, PICK_IMAGE_CAMERA);
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);


        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
       // @SuppressLint("SimpleDateFormat")
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.d(TAG, "createImageFile: image >> "+image);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "createImageFile: "+currentPhotoPath);
        return image;
    }


/*    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = AddMenuActivity.this.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkForPermission(final String[] permissions, final int permRequestCode) {

        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(AddMenuActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

                    Log.d("permisssion","not granted");


                    if (shouldShowRequestPermissionRationale(permissions[i])) {

                        Log.d("if","if");
                        permissionsNeeded.add(perm);

                    } else {
                        // add the request.
                        Log.d("else","else");
                        permissionsNeeded.add(perm);
                    }

                }
            }
        }

        if (permissionsNeeded.size() > 0) {
            // go ahead and request permissions
            requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), permRequestCode);
            return false;
        } else {
            // no permission need to be asked so all good...we have them all.
            return true;
        }

    }

    public void date_listview() {


        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_date_dialog, null);
        builderSingle.setView(dialogView);



       /* Log.d("upcoming_dates", "upcoming_dates: " + upcoming_dates);
        Log.d("upcoming_dates", "coming_event_id: " + coming_event_id);*/

        final ListView listView = (ListView) dialogView.findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter<String>(AddMenuActivity.this, R.layout.date_lv_single_row, category_name);
        listView.setAdapter(adapter);


        alertDialog1 = builderSingle.create();
        alertDialog1.show();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id_1) {
                String strName = String.valueOf(category_name.get(position));
                selected_cat_id = String.valueOf(category_id.get(position));
                Log.d(TAG, "selected_cat_id: " + selected_cat_id);
                tv_select_cat.setText(strName);

                alertDialog1.dismiss();



            }
        });

    }


    private void category_list() {
        //  mView.showDialog();

        rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "category_list";
        String url = ApplicationConstants.category_list;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "category_list Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        if(status.equals("1")) {

                            JsonArray data = jobj.getAsJsonArray("response");
                            Log.d(TAG, "onResponse data: "+data);

                            for( int i = 0 ; i < data.size() ; i++ ) {

                                JsonObject obj_data = data.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String id = obj_data.get("id").getAsString().replaceAll("\"", "");;
                                String cat_name = obj_data.get("cat_name").getAsString().replaceAll("\"", "");;


                                category_name.add(cat_name);
                                category_id.add(id);
                            }


                        }else{

                            Toasty.info(AddMenuActivity.this, message, Toast.LENGTH_LONG).show();
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

            globalClass.addToRequestQueue(AddMenuActivity.this, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* private void menu_add() {
        //  mView.showDialog();
        Log.d(TAG, "menu_add: p_image>>> "+p_image);

        rl_progress.setVisibility(View.VISIBLE);

        final String tag_string_req = "menu_add";
        String url = ApplicationConstants.menu_add;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "category_list Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message = jobj.get("message").getAsString().replaceAll("\"", "");


                        Toasty.success(AddMenuActivity.this, message, Toast.LENGTH_LONG).show();
                        rl_progress.setVisibility(View.GONE);
                        finish();
                        tv_save.setEnabled(true);

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
                    params.put("menu_name",et_menu_name.getText().toString());
                    params.put("cat_id",selected_cat_id);
                    params.put("description",et_description.getText().toString());
                    params.put("prep_time",et_prep_time.getText().toString());
                    params.put("price",et_price.getText().toString());
                    params.put("veg_nonveg",switch_state);
                    params.put("image",mCameraFileName);

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(AddMenuActivity.this, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void menu_add(){

        rl_progress.setVisibility(View.VISIBLE);
        tv_save.setEnabled(false);
        String url = ApplicationConstants.menu_add;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("res_id",globalClass.getId());
        params.put("menu_name",et_menu_name.getText().toString());
        params.put("cat_id",selected_cat_id);
        params.put("description",et_description.getText().toString());
        params.put("prep_time",et_prep_time.getText().toString());
        params.put("price",et_price.getText().toString());
        params.put("veg_nonveg",switch_state);


        try{

            params.put("image", p_image);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }



        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "frag_home- " + response.toString());
                    try {

                        //    JSONObject result = response.getJSONObject("result");

                        String status = response.getString("status");
                        String message = response.getString("message");

                        Toasty.success(AddMenuActivity.this, message, Toast.LENGTH_LONG).show();
                        rl_progress.setVisibility(View.GONE);
                        tv_save.setEnabled(true);

                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
                rl_progress.setVisibility(View.GONE);
            }
        });


    }

}
