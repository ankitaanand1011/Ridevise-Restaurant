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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    Context context;
    ArrayList<HashMap<String, String>> item_list;


    public NotificationAdapter(Context c, ArrayList<HashMap<String, String>> item_list) {
            this.mInflater = LayoutInflater.from(c);
            context = c;
            this.item_list = item_list;

        //    globalClass = (GlobalClass)context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.row_notify, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

         String title= item_list.get(position).get("title");
         String description= item_list.get(position).get("description");
         String created_at= item_list.get(position).get("created_at");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM");
        Date testDate = null;
        try {
            testDate = sdf.parse(created_at);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        String newFormat = formatter.format(testDate);
       // System.out.println(".....Date..."+newFormat);

         holder.tv_notification_name.setText(title);
         holder.tv_description.setText(description);
         holder.tv_date_time.setText(newFormat);

        Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface light = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-LIGHT.TTF");
        holder.tv_notification_name.setTypeface(regular);
        holder.tv_description.setTypeface(regular);
        holder.tv_date_time.setTypeface(regular);




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



    }

    @Override
    public int getItemCount() {
            return item_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        TextView tv_notification_name,tv_description,tv_date_time;

        ImageView iv_notify;

        ViewHolder(View itemView) {
            super(itemView);


            iv_notify = itemView.findViewById(R.id.iv_notify);
            tv_notification_name = itemView.findViewById(R.id.tv_notification_name);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);





        }


    }


}