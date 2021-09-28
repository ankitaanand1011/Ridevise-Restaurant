package com.restaurant.ridewise.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;
import com.restaurant.ridewise.util.Shared_Preference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class ProfileInfoActivity extends AppCompatActivity {
    String TAG ="profile_info";
    GlobalClass globalClass;
    Shared_Preference shared_preference;
    SpinKitView spinKitView;

    TextView tv_header,tv_res_name,tv_res_mobile,tv_contact_person,tv_location,tv_full_address,tv_save;
    EditText et_res_name,et_res_mobile,et_contact_person,et_location,et_full_address;
    ImageView iv_back,iv_profile,iv_add_image;
    String res_name, res_phone, res_address, res_address2, postal_code,res_image,contact_person,res_email;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    File p_image;
    String mCameraFileName = "";

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

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
        iv_add_image = findViewById(R.id.iv_add_image);
        tv_header = findViewById(R.id.tv_header);
        tv_res_name = findViewById(R.id.tv_res_name);
        et_res_name = findViewById(R.id.et_res_name);
        tv_res_mobile = findViewById(R.id.tv_res_mobile);
        et_res_mobile = findViewById(R.id.et_res_mobile);
        tv_contact_person = findViewById(R.id.tv_contact_person);
        et_contact_person = findViewById(R.id.et_contact_person);
        tv_location = findViewById(R.id.tv_location);
        et_location = findViewById(R.id.et_location);
        tv_full_address = findViewById(R.id.tv_full_address);
        et_full_address = findViewById(R.id.et_full_address);
        tv_save = findViewById(R.id.tv_save);
    }

    private void function() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ProfileInfoActivity.this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(ProfileInfoActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            }
            else{
                if(checkForPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, 124)){

                }

            }
        }


        res_name = getIntent().getStringExtra("res_name");
        res_phone = getIntent().getStringExtra("res_phone");
        res_address = getIntent().getStringExtra("res_address");
        res_address2 = getIntent().getStringExtra("res_address2");
        postal_code = getIntent().getStringExtra("postal_code");
        res_image = getIntent().getStringExtra("res_image");
        contact_person = getIntent().getStringExtra("contact_person");
        res_email = getIntent().getStringExtra("res_email");


        et_res_name.setText(res_name);
        et_res_mobile.setText(res_phone);
        et_contact_person.setText(contact_person);
        et_location.setText(res_address);
        et_full_address.setText(res_address2);

        RequestOptions options = new RequestOptions()
                .centerInside()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image);
        Glide.with(ProfileInfoActivity.this).load(res_image).apply(options).into(iv_profile);


        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface medium = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        tv_header.setTypeface(medium);
        tv_res_name.setTypeface(regular);
        et_res_name.setTypeface(regular);
        tv_res_mobile.setTypeface(regular);
        et_res_mobile.setTypeface(regular);
        tv_contact_person.setTypeface(regular);
        et_contact_person.setTypeface(regular);
        tv_location.setTypeface(regular);
        et_location.setTypeface(regular);
        tv_full_address.setTypeface(regular);
        et_full_address.setTypeface(regular);
        tv_save.setTypeface(medium);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        iv_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_add();
            }
        });
    }

    private void selectImage() {
        try {
            PackageManager pm = ProfileInfoActivity.this.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, ProfileInfoActivity.this.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Choose From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileInfoActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                      /*  if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                          //  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                            String newPicFile = System.currentTimeMillis() + ".jpg";

                            //String outPath = Common.getCachePath(getActivity());
                            String outPath = GlobalClass.getFolderDirectory();
                            File file = new File(outPath);
                            if (!file.exists()) {
                                file.mkdir();
                            }

                            File outFile = new File(outPath, newPicFile);
                            Log.d(TAG, "onClick: outFile>>> "+outFile);

                            mCameraFileName = outFile.toString();
                            Uri outuri = Uri.fromFile(outFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
                          //  startActivityForResult(intent, CAMERA_REQUEST);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else*/ if (options[item].equals("Choose From Gallery")) {
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
                Toasty.error(ProfileInfoActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toasty.error(ProfileInfoActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);






        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            p_image = new File(getRealPathFromUri(uri));


            Log.d(TAG, "image = "+p_image);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(ProfileInfoActivity.this.getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                iv_profile.setImageBitmap(bitmap);
                //  Glide.with(ProfileInfoActivity.this).load(p_image).apply(options).into(iv_profile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
/*
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {


            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }


            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);


                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);

                Log.d(TAG, "bitmap: "+bitmap);

                iv_profile.setImageBitmap(bitmap);
                // Glide.with(ProfileInfoActivity.this).load(p_image).apply(options).into(iv_profile);
                String path = Environment.getExternalStorageDirectory()+File.separator;
                // + File.separator
                //   + "Phoenix" + File.separator + "default";
                f.delete();
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {

                    p_image = file;

                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            // iv_product_image.setImageBitmap(photo);
                    }
*/


    }




    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean checkForPermission(final String[] permissions, final int permRequestCode) {

        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(ProfileInfoActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

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
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // go ahead and request permissions
                requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), permRequestCode);
            }
            return false;
        } else {
            // no permission need to be asked so all good...we have them all.
            return true;
        }

    }


    public void menu_add(){

        tv_save.setEnabled(false);
        spinKitView.setVisibility(View.VISIBLE);
        String url = ApplicationConstants.profile_update;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("res_id",globalClass.getId());
        params.put("name",et_res_name.getText().toString());
        params.put("address1",et_location.getText().toString());
        params.put("email","test@test.com");
        params.put("phone",et_res_mobile.getText().toString());
        params.put("open_time","10:00 AM");
        params.put("close_time","9:00 PM");
        params.put("res_address2",et_full_address.getText().toString());
        params.put("contact_person",et_contact_person.getText().toString());

        try{

            params.put("res_image", p_image);

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

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        Toasty.success(ProfileInfoActivity.this, message, Toast.LENGTH_LONG).show();
                        spinKitView.setVisibility(View.GONE);
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
                spinKitView.setVisibility(View.GONE);
            }
        });


    }
}
