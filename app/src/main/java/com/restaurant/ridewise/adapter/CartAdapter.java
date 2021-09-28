package com.restaurant.ridewise.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.activities.CartActivity;
import com.restaurant.ridewise.activities.MenuActivity;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    String TAG = "cart_adapter";
    GlobalClass globalClass;

    private LayoutInflater mInflater;
    Context context;
    ArrayList<HashMap<String, String>> item_list;


    public CartAdapter(Context c, ArrayList<HashMap<String, String>> item_list) {
            this.mInflater = LayoutInflater.from(c);
            context = c;
            this.item_list = item_list;

            globalClass = (GlobalClass)context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.row_cart, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

         String menu_name= item_list.get(position).get("menu_name");
         String price= item_list.get(position).get("price");
         String veg_nonveg= item_list.get(position).get("veg_nonveg");
         String qty= item_list.get(position).get("qty");

         holder.tv_item_name.setText(menu_name);
        holder.tv_price.setText("₹  "+price);
        holder.tv_count.setText(qty);

        Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface light = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface medium = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-MEDIUM.TTF");
        holder.tv_item_name.setTypeface(regular);
        holder.tv_price.setTypeface(regular);
        holder.tv_count.setTypeface(medium);

        assert veg_nonveg != null;
        if(veg_nonveg.equals("veg")){
            holder.iv_veg_sign.setImageDrawable(context.getResources().getDrawable(R.mipmap.veg));
        }else {
            holder.iv_veg_sign.setImageDrawable(context.getResources().getDrawable(R.mipmap.nonveg));
        }

        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count= Integer.parseInt(String.valueOf(holder.tv_count.getText()));

                if(count>1) {

                    //2. enter code here

                    count--;

                    holder.tv_count.setText(String.valueOf(count));
                    String cart_id = item_list.get(position).get("cart_id");
                    cart_update(cart_id,String.valueOf(count));
                }
            }
        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count= Integer.parseInt(String.valueOf(holder.tv_count.getText()));

              //  int count= Integer.parseInt(String.valueOf(holder.tv_count.getText()));
                count++;
                holder.tv_count.setText(String.valueOf(count));
                String cart_id = item_list.get(position).get("cart_id");

                cart_update(cart_id,String.valueOf(count));
            }
        });



        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cart_id = item_list.get(position).get("cart_id");

                delete_alert(cart_id);

            }
        });



    }

    @Override
    public int getItemCount() {
            return item_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        TextView tv_item_name,tv_price,tv_count;
        ImageView iv_veg_sign,iv_delete,iv_minus,iv_plus;

        ViewHolder(View itemView) {
            super(itemView);


            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_count = itemView.findViewById(R.id.tv_count);
            iv_veg_sign = itemView.findViewById(R.id.iv_veg_sign);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_minus = itemView.findViewById(R.id.iv_minus);
            iv_plus = itemView.findViewById(R.id.iv_plus);




        }


    }

    public void delete_alert(String cart_id){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure you want to delete this item?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        dialog.cancel();
                        cart_delete(cart_id);
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

    private void cart_delete(String cart_id) {
        //  mView.showDialog();

        //  rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "cart_delete";
        String url = ApplicationConstants.cart_delete;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "cart_delete Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        ((CartActivity)context).cart_details();


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
                    params.put("cart_id",cart_id);

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(context, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cart_update(String cart_id,String count) {
        //  mView.showDialog();

        //  rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "cart_update";
        String url = ApplicationConstants.cart_update;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "cart_update Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        ((CartActivity)context).cart_details();


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
                    params.put("cart_id",cart_id);
                    params.put("qty",count);

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