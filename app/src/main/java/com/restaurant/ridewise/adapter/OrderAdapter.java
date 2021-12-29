package com.restaurant.ridewise.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.restaurant.ridewise.R;
import com.restaurant.ridewise.activities.MenuActivity;
import com.restaurant.ridewise.activities.OrderActivity;
import com.restaurant.ridewise.util.ApplicationConstants;
import com.restaurant.ridewise.util.GlobalClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    String TAG = "menu_adapter";

    private LayoutInflater mInflater;
    Context context;
    private ArrayList<HashMap<String, String>>  item_list;
    GlobalClass globalClass;

    public OrderAdapter(Context c, ArrayList<HashMap<String, String>> item_list) {
            this.mInflater = LayoutInflater.from(c);
            context = c;
            this.item_list = item_list;

        globalClass = (GlobalClass)context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.row_order, parent, false);

            return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

         String order_id= item_list.get(position).get("order_id");
         String user_id= item_list.get(position).get("user_id");
         String address= item_list.get(position).get("address");
         String total_price= item_list.get(position).get("total_price");
         String order_status= item_list.get(position).get("order_status");
         String order_date= item_list.get(position).get("order_date");
         String order_time= item_list.get(position).get("order_time");
         String customer_name= item_list.get(position).get("user_name");
         String payment_status= item_list.get(position).get("payment_status");


         double price= Double.parseDouble(total_price);
        holder.tv_order_name.setText(address);
        holder.tv_order_no.setText("#"+order_id);

        holder.tv_customer_name.setText(customer_name+",");
        holder.tv_price.setText("₹  "+String.format("%.2f", price));

        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
        Date newDate= null;
        try {
            newDate = spf.parse(order_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd-MM-yyyy");
        String date = spf.format(newDate);
        System.out.println(date);


        SimpleDateFormat spf1=new SimpleDateFormat("hh:mm:ss");
        Date newDate1= null;
        try {
            newDate1 = spf1.parse(order_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("hh:mm aaa");
        String time = spf.format(newDate1);
        System.out.println(time);



        holder.tv_order_date.setText(date+"  "+time);


        if (payment_status != null) {
            switch (payment_status) {
                case "0":
                    holder.tv_payment_mode.setText("COD");
                    break;
                case "1":
                    holder.tv_payment_mode.setText("Online Mode");
                    break;

            }
        }

        if (order_status != null) {
            switch (order_status) {
                case "1":
                    holder.tv_order_status.setText("Order Placed");
                    break;
                case "2":
                    holder.tv_order_status.setText("Out for Collect");
                    break;
                case "3":
                    holder.tv_order_status.setText("Reached to Restaurant");
                    break;
                case "4":
                    holder.tv_order_status.setText("Start");
                    break;
                case "5":
                    holder.tv_order_status.setText("Reached to Customer");
                    break;
                case "6":
                    holder.tv_order_status.setText("Delivered");
                    break;
                case "7":
                    holder.tv_order_status.setText("Cancelled");
                    break;
            }
        }


        Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface light = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-LIGHT.TTF");
        Typeface semi_bold = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-SEMIBOLD.TTF");

        holder.tv_order_no.setTypeface(semi_bold);
        holder.tv_customer_name.setTypeface(semi_bold);
        holder.tv_order_name.setTypeface(regular);
        holder.tv_price.setTypeface(regular);
        holder.tv_order_date.setTypeface(regular);
        holder.tv_payment_mode.setTypeface(regular);
        holder.tv_order_status.setTypeface(regular);





        RequestOptions options = new RequestOptions()
                .centerInside()
                .placeholder(R.drawable.no_image)
                .centerCrop()
                .error(R.drawable.no_image);


        switch (Objects.requireNonNull(order_status)) {
            case "1":
              /*  Glide.with(context).load(context.getResources().getDrawable(R.mipmap.order_inprogress))
                        .apply(options).into(holder.iv_order);*/

                holder.iv_order.setImageResource(R.mipmap.order_inprogress);

                break;
            case "2":

             /*   Glide.with(context).load(context.getResources().getDrawable(R.mipmap.ord_picked))
                        .apply(options).into(holder.iv_order);*/
                holder.iv_order.setImageResource(R.mipmap.ord_picked);
                break;
            case "3":

            /*    Glide.with(context).load(context.getResources().getDrawable(R.mipmap.order_delivered))
                        .apply(options).into(holder.iv_order);*/

                holder.iv_order.setImageResource(R.mipmap.order_delivered);
                break;
            case "4":

               /* Glide.with(context).load(context.getResources().getDrawable(R.mipmap.order_new))
                        .apply(options).into(holder.iv_order);*/
                holder.iv_order.setImageResource(R.mipmap.order_new);


                break;
            case "0":

               /* Glide.with(context).load(context.getResources().getDrawable(R.mipmap.order_new))
                        .apply(options).into(holder.iv_order);
*/
                holder.iv_order.setImageResource(R.mipmap.order_new);

                break;
        }








        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("id",item_list.get(position).get("id"));

                context.startActivity(intent);

            }
        });





    }

    @Override
    public int getItemCount() {
            return item_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        TextView tv_order_name,tv_price,tv_order_no,tv_order_date,
                tv_payment_mode,tv_order_status,tv_customer_name;
        RelativeLayout rl_main;

        ImageView iv_order;

        ViewHolder(View itemView) {
            super(itemView);


            iv_order = itemView.findViewById(R.id.iv_order);
            tv_order_no = itemView.findViewById(R.id.tv_order_no);
            tv_order_name = itemView.findViewById(R.id.tv_order_name);
            tv_order_date = itemView.findViewById(R.id.tv_order_date);
            tv_payment_mode = itemView.findViewById(R.id.tv_payment_mode);
            tv_order_status = itemView.findViewById(R.id.tv_order_status);
            tv_customer_name = itemView.findViewById(R.id.tv_customer_name);

            tv_price = itemView.findViewById(R.id.tv_price);

            rl_main = itemView.findViewById(R.id.rl_main);





        }


    }
  /*  public void delete_alert(String menu_id){
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
    }*/


}