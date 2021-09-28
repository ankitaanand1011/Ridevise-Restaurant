package com.restaurant.ridewise.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.restaurant.ridewise.R;

import java.util.ArrayList;
import java.util.HashMap;

public class VegAdapter extends RecyclerView.Adapter<VegAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    Context context;
    ArrayList<HashMap<String, String>> item_list;


    public VegAdapter(Context c, ArrayList<HashMap<String, String>> item_list) {
            this.mInflater = LayoutInflater.from(c);
            context = c;
            this.item_list = item_list;

        //    globalClass = (GlobalClass)context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.row_item, parent, false);
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

        holder.tv_item_name.setText(menu_name);
        holder.tv_price.setText("₹  "+price);

        Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface light = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-LIGHT.TTF");
        holder.tv_item_name.setTypeface(regular);
        holder.tv_price.setTypeface(regular);
        holder.tv_count.setTypeface(regular);
        holder.tv_add.setTypeface(regular);

        assert veg_nonveg != null;
        if(veg_nonveg.equals("veg")){
            holder.iv_veg_sign.setImageDrawable(context.getResources().getDrawable(R.mipmap.veg));
        }else {
            holder.iv_veg_sign.setImageDrawable(context.getResources().getDrawable(R.mipmap.nonveg));
        }


        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    @Override
    public int getItemCount() {
            return item_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        TextView tv_item_name,tv_price,tv_count,tv_add;
        ImageView iv_veg_sign;

        ViewHolder(View itemView) {
            super(itemView);


            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_count = itemView.findViewById(R.id.tv_count);
            iv_veg_sign = itemView.findViewById(R.id.iv_veg_sign);
            tv_add = itemView.findViewById(R.id.tv_add);




        }


    }


}