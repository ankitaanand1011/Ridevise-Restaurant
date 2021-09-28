package com.restaurant.ridewise.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.restaurant.ridewise.R;
import com.restaurant.ridewise.activities.OrderActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    Context context;
    private ArrayList<HashMap<String, String>> order_list;


    public OrderHistoryAdapter(Context c, ArrayList<HashMap<String, String>> order_list) {
            this.mInflater = LayoutInflater.from(c);
            context = c;
            this.order_list = order_list;

        //    globalClass = (GlobalClass)context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.row_history, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface light = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-LIGHT.TTF");
        holder.tv_order_no.setTypeface(regular);
        holder.tv_from.setTypeface(regular);
        holder.tv_branch_name.setTypeface(regular);
        holder.tv_date_time.setTypeface(regular);
        holder.tv_price.setTypeface(regular);
        holder.tv_payment_method.setTypeface(regular);
        holder.tv_view_details.setTypeface(regular);


        String order_no= order_list.get(position).get("order_id");
        holder.tv_order_no.setText("#"+order_no);
        String order_date= order_list.get(position).get("order_date");
        String order_time= order_list.get(position).get("order_time");

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt;
        try {
            dt = sdf.parse(order_time);
            String date_time = order_date+" at "+ sdfs.format(dt);
            holder.tv_date_time.setText(date_time);
          //  System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date d=dateFormat.parse(order_date);
            System.out.println("DATE"+d);
            System.out.println("Formated"+dateFormat.format(d));
        }
        catch(Exception e) {
            //java.text.ParseException: Unparseable date: Geting error
            System.out.println("Excep"+e);
        }



        String total_price= order_list.get(position).get("total_price");
        float price = Float.parseFloat(total_price);
        String t_price = String.format("%.2f",price);
        holder.tv_price.setText("₹ "+t_price);
        String payment_type= order_list.get(position).get("payment_type");
        holder.tv_payment_method.setText(payment_type);





        holder.tv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("address",order_list.get(position).get("address"));
                intent.putExtra("total_price",order_list.get(position).get("total_price"));
                intent.putExtra("sub_total",order_list.get(position).get("sub_total"));
                intent.putExtra("delivery_charge",order_list.get(position).get("delivery_charge"));
                intent.putExtra("tax_charge",order_list.get(position).get("tax_charge"));
                intent.putExtra("delivery_date",order_list.get(position).get("delivery_date"));
                intent.putExtra("delivery_time",order_list.get(position).get("delivery_time"));
                intent.putExtra("status",order_list.get(position).get("status"));
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
            return order_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        TextView tv_order_no,tv_from,tv_branch_name,tv_date_time,tv_price,tv_payment_method,tv_view_details;


        ViewHolder(View itemView) {
            super(itemView);


            tv_order_no = itemView.findViewById(R.id.tv_order_no);
            tv_from = itemView.findViewById(R.id.tv_from);
            tv_branch_name = itemView.findViewById(R.id.tv_branch_name);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_payment_method = itemView.findViewById(R.id.tv_payment_method);
            tv_view_details = itemView.findViewById(R.id.tv_view_details);




        }


    }


}