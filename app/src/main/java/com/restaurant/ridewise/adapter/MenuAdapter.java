package com.restaurant.ridewise.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.activities.MenuActivity;
import com.restaurant.ridewise.activities.ProfileActivity;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    String TAG = "menu_adapter";

    private LayoutInflater mInflater;
    Context context;
    private ArrayList<HashMap<String, String>>  item_list;
    GlobalClass globalClass;

    public MenuAdapter(Context c,  ArrayList<HashMap<String, String>> item_list) {
            this.mInflater = LayoutInflater.from(c);
            context = c;
            this.item_list = item_list;

        globalClass = (GlobalClass)context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.row_menu, parent, false);

            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

         String menu_name= item_list.get(position).get("menu_name");
         String category_id= item_list.get(position).get("category_id");
         String category_name= item_list.get(position).get("category_name");
         String description= item_list.get(position).get("description");
         String veg_nonveg= item_list.get(position).get("veg_nonveg");
         String price= item_list.get(position).get("price");
         String prepare_time= item_list.get(position).get("prepare_time");
         String menu_image= item_list.get(position).get("menu_image");

        holder.tv_est_time.setText(prepare_time);
        holder.tv_dish_name.setText(menu_name);
        holder.tv_description.setText(description);
        holder.tv_price.setText("₹  "+price);
        holder.tv_menu_type.setText(category_name);

        Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface light = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");
        holder.tv_dish_name.setTypeface(semi_bold);
        holder.tv_menu_type.setTypeface(regular);
        holder.tv_est_time.setTypeface(regular);
        holder.tv_description.setTypeface(regular);
        holder.tv_price.setTypeface(regular);





        RequestOptions options = new RequestOptions()
                .centerInside()
                .placeholder(R.drawable.no_image)
                .centerCrop()
                .error(R.drawable.no_image);
        Glide.with(context).load(menu_image).apply(options).into(holder.iv_menu);


      /*  if(position==2){
            holder.iv_active.setImageDrawable(context.getResources().getDrawable(R.mipmap.deactive));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.rl_main.setForeground(context.getResources().getDrawable(R.drawable.image_cover));
            }
        }*/
        assert veg_nonveg != null;
        if(veg_nonveg.equals("veg")){
            holder.iv_veg_sign.setImageDrawable(context.getResources().getDrawable(R.mipmap.veg));
        }else {
            holder.iv_veg_sign.setImageDrawable(context.getResources().getDrawable(R.mipmap.nonveg));
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Intent intent = new Intent(context, OrderDetail.class);
               *//* intent.putExtra("name",product_list.get(position).get("name"));
                intent.putExtra("product_id",product_list.get(position).get("id"));*//*
                context.startActivity(intent);*/
             /*   Intent intent = new Intent(context, OrderActivity.class);
                context.startActivity(intent);*/
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menu_id = item_list.get(position).get("menu_id");

                delete_alert(menu_id);

            }
        });



    }

    @Override
    public int getItemCount() {
            return item_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        TextView tv_dish_name,tv_menu_type,tv_est_time,tv_description,tv_price;
        RelativeLayout rl_main;

        ImageView iv_menu,iv_veg_sign,iv_active,iv_delete;

        ViewHolder(View itemView) {
            super(itemView);


            iv_menu = itemView.findViewById(R.id.iv_menu);
            iv_veg_sign = itemView.findViewById(R.id.iv_veg_sign);
            tv_dish_name = itemView.findViewById(R.id.tv_dish_name);
            tv_menu_type = itemView.findViewById(R.id.tv_menu_type);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_est_time = itemView.findViewById(R.id.tv_est_time);
            iv_active = itemView.findViewById(R.id.iv_active);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            rl_main = itemView.findViewById(R.id.rl_main);





        }


    }
    public void delete_alert(String menu_id){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure you want to delete this menu item?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        dialog.cancel();
                        menu_delete(menu_id);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private void menu_delete(String menu_id) {
        //  mView.showDialog();

      //  rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "menu_delete";
        String url = ApplicationConstants.menu_delete;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "menu_delete Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        ((MenuActivity)context).menu_list();


                        Toasty.success(context, message, Toast.LENGTH_LONG).show();


                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    //  mView.hideDialog();

                }
            }, new Response.ErrorListener() {

                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "menu_delete Error: " + error.getMessage());
                    Toast.makeText(context.getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    //rl_progress.setVisibility(View.GONE);

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("menu_id",menu_id);

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(context, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}