package com.restaurant.ridewise.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.restaurant.ridewise.R;
import com.restaurant.ridewise.util.GlobalClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    Context context;
   // private ArrayList<HashMap<String, String>> address_list;
    GlobalClass globalClass;
    EditText et_pin,et_landmark,et_address,et_phn,et_customer_name;
    private ArrayList<HashMap<String, String>> address_details_list;
    String phone;

    public AddressAdapter(Context c,  ArrayList<HashMap<String, String>> address_details_list,
                          EditText et_pin, EditText et_landmark, EditText et_address, EditText et_phn,
                          EditText et_customer_name, String phone
    ) {
            this.mInflater = LayoutInflater.from(c);
            context = c;
            this.address_details_list = address_details_list;
            this.et_pin = et_pin;
            this.et_landmark = et_landmark;
            this.et_address = et_address;
            this.et_phn = et_phn;
            this.et_customer_name = et_customer_name;
            this.phone = phone;
          //  this.address_details_list = address_details_list;

           globalClass = (GlobalClass)context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.row_address, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

       //  String id= address_details_list.get(position).get("id");
         String name= address_details_list.get(position).get("name");
   //      String phone= address_details_list.get(position).get("phone");
         String address= address_details_list.get(position).get("address");
         String zip= address_details_list.get(position).get("zip");
         String add_lat= address_details_list.get(position).get("add_lat");
         String add_long= address_details_list.get(position).get("add_long");
         String landmark= address_details_list.get(position).get("landmark");

         holder.tv_address_1.setText(name+", "+phone);
         holder.tv_address_2.setText(address+", "+zip);

        Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-REGULAR.TTF");
        Typeface light = Typeface.createFromAsset(context.getAssets(), "fonts/POPPINS-LIGHT.TTF");
        holder.tv_address_1.setTypeface(regular);
        holder.tv_address_2.setTypeface(regular);
        holder.tv_select.setTypeface(regular);

        holder.tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                String address_id= address_details_list.get(position).get("address_id");
                String user_id= address_details_list.get(position).get("user_id");
                String name= address_details_list.get(position).get("name");
               // String phone= address_details_list.get(position).get("phone");
                String email= address_details_list.get(position).get("email");
                String address= address_details_list.get(position).get("address");
                String postal_code= address_details_list.get(position).get("zip");
                String add_lat= address_details_list.get(position).get("add_lat");
                String add_long= address_details_list.get(position).get("add_long");
                String landmark= address_details_list.get(position).get("landmark");

                globalClass.setUser_id(user_id);
                globalClass.setUser_address_id(address_id);
                globalClass.setUser_name(name);
                globalClass.setUser_phn(phone);
                globalClass.setUser_email(email);
                globalClass.setUser_address(address);
                globalClass.setUser_pin(postal_code);
                globalClass.setUser_lat(add_lat);
                globalClass.setUser_long(add_long);

                et_customer_name.setText(name);
                et_phn.setText(phone);
                et_address.setText(address);
                et_landmark.setText(landmark);
                et_pin.setText(postal_code);
            }
        });



    }

    @Override
    public int getItemCount() {
            return address_details_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        TextView tv_address_1,tv_address_2,tv_select;

        ViewHolder(View itemView) {
            super(itemView);


            tv_address_1 = itemView.findViewById(R.id.tv_address_1);
            tv_address_2 = itemView.findViewById(R.id.tv_address_2);
            tv_select = itemView.findViewById(R.id.tv_select);




        }


    }


}