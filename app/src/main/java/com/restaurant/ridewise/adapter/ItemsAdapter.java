package com.restaurant.ridewise.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {


    String TAG = "item_adapter";
    private LayoutInflater mInflater;
    Context context;
    ArrayList<HashMap<String, String>> item_list;
    GlobalClass globalClass;
    TextView tv_item_count;

    double total_amount;


    public ItemsAdapter(Context c, ArrayList<HashMap<String, String>> item_list, TextView tv_item_count) {
            this.mInflater = LayoutInflater.from(c);
            context = c;
            this.item_list = item_list;
            this.tv_item_count = tv_item_count;

            globalClass = (GlobalClass)context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.row_item, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

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

                holder.ll_count.setVisibility(View.VISIBLE);
                holder.tv_add.setVisibility(View.GONE);

               String qty = holder.tv_count.getText().toString();
               String menu_id = item_list.get(position).get("menu_id");
               String price= item_list.get(position).get("price");

                assert price != null;
                double total_price_acc_qty = Integer.parseInt(qty) * Double.parseDouble(price);
                Log.d(TAG, "onClick: total_price_acc_qty >" + total_price_acc_qty);



                add_cart(qty,menu_id,total_price_acc_qty,holder.ll_count);

            }
        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count= Integer.parseInt(String.valueOf(holder.tv_count.getText()));

                count++;
                holder.tv_count.setText(String.valueOf(count));


                String menu_id = item_list.get(position).get("menu_id");
                String price= item_list.get(position).get("price");

                assert price != null;
                double total_price_acc_qty = count * Double.parseDouble(price);
                Log.d(TAG, "onClick: total_price_acc_qty >" + total_price_acc_qty);



                add_cart(String.valueOf(count),menu_id,total_price_acc_qty,holder.ll_count);
            }
        });


        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Integer.parseInt(holder.tv_count.getText().toString()) > 1){

                    holder.ll_count.setVisibility(View.GONE);
                    holder.tv_add.setVisibility(View.VISIBLE);
                }

                int count= Integer.parseInt(String.valueOf(holder.tv_count.getText()));

                if(count>1) {

                    //2. enter code here

                    count--;

                    holder.tv_count.setText(String.valueOf(count));
                    String menu_id = item_list.get(position).get("menu_id");
                    String price= item_list.get(position).get("price");

                    assert price != null;
                    double total_price_acc_qty = count * Double.parseDouble(price);
                    Log.d(TAG, "onClick: total_price_acc_qty >" + total_price_acc_qty);



                    add_cart(String.valueOf(count),menu_id,total_price_acc_qty,holder.ll_count);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
            return item_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        TextView tv_item_name,tv_price,tv_count,tv_add;
        ImageView iv_veg_sign,iv_minus,iv_plus;
        LinearLayout ll_count;

        ViewHolder(View itemView) {
            super(itemView);


            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_count = itemView.findViewById(R.id.tv_count);
            iv_veg_sign = itemView.findViewById(R.id.iv_veg_sign);
            tv_add = itemView.findViewById(R.id.tv_add);
            ll_count = itemView.findViewById(R.id.ll_count);
            iv_minus = itemView.findViewById(R.id.iv_minus);
            iv_plus = itemView.findViewById(R.id.iv_plus);




        }


    }

    private void add_cart(String qty,String menu_id,double total_price_acc_qty ,LinearLayout ll_count) {
        //  mView.showDialog();

        //  rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "add_cart";
        String url = ApplicationConstants.add_cart;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "add_cart Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        JsonArray data = jobj.getAsJsonArray("response");
                        Log.d(TAG, "onResponse data: "+data);

                        for( int i = 0 ; i < data.size() ; i++ ) {

                            JsonObject obj_data = data.get(i).getAsJsonObject();
                            Log.d(TAG, "onResponse obj_data:  " + obj_data);
                            String id = obj_data.get("id").getAsString().replaceAll("\"", "");
                            String food_id = obj_data.get("food_id").getAsString().replaceAll("\"", "");
                            String qty = obj_data.get("qty").getAsString().replaceAll("\"", "");



                        }



                      //  ((MenuActivity)context).menu_list();
                        if(data.size()==1) {
                            total_amount= total_price_acc_qty;
                            Log.d(TAG, "onResponse: size1 >"+total_amount);
                            globalClass.setTotal_item_price_acc_qty(total_price_acc_qty);
                        }else{
                            Log.d(TAG, "onClick: total_price_acc_qty >" + total_price_acc_qty);
                            Log.d(TAG, "onClick: global_var >" + globalClass.getTotal_item_price_acc_qty());
                            total_amount = globalClass.getTotal_item_price_acc_qty() + total_price_acc_qty;
                            globalClass.setTotal_item_price_acc_qty(total_amount);
                            Log.d(TAG, "onResponse: size2 >"+total_amount);

                        }

                      /*  String item_count = data.size() +" ITEMS ₹ "+total_amount;
                        tv_item_count.setText(item_count);*/
                        Toasty.success(context, message, Toast.LENGTH_LONG).show();



                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    //  mView.hideDialog();

                }
            }, new Response.ErrorListener() {

                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "add_cart Error: " + error.getMessage());
                    Toast.makeText(context.getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    //rl_progress.setVisibility(View.GONE);

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id",globalClass.getUser_id());
                    params.put("user_lat",globalClass.getUser_lat());
                    params.put("user_long",globalClass.getUser_long());
                    params.put("menu_id",menu_id);
                    params.put("qty",qty);
                    params.put("res_id",globalClass.getId());

                    Log.d(TAG, "getParams: "+params);
                    return params;
                }

            };

            globalClass.addToRequestQueue(context, strReq, tag_string_req);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cart_details() {
        //  mView.showDialog();

     //   rl_progress.setVisibility(View.VISIBLE);
        final String tag_string_req = "cart_details";
        String url = ApplicationConstants.cart_details;

        try{
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>(){


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "menu_list Response: " + response);


                    Gson gson = new Gson();

                    try {


                        JsonObject jobj = gson.fromJson(response, JsonObject.class);
                        String status = jobj.get("status").getAsString().replaceAll("\"", "");
                        String message =jobj.get("message").getAsString().replaceAll("\"", "");


                        Log.d(TAG, "Message: "+message);


                        if(status.equals("1")) {
                            item_list.clear();
                            JsonObject data = jobj.getAsJsonObject("response");
                            String sub_total = data.get("sub_total").getAsString().replaceAll("\"", "");;
                            String delivery_charge = data.get("delivery_charge").getAsString().replaceAll("\"", "");;
                            String tax_charge = data.get("tax_charge").getAsString().replaceAll("\"", "");;
                            String total_payable = data.get("total_payable").getAsString().replaceAll("\"", "");;




                            JsonArray cart_details = data.getAsJsonArray("cart_details");
                            Log.d(TAG, "onResponse menu_list_arr: "+cart_details);

                     /*       for( int i = 0 ; i < cart_details.size() ; i++ ) {

                                JsonObject obj_data = cart_details.get(i).getAsJsonObject();
                                Log.d(TAG, "onResponse obj_data:  "+obj_data);
                                String qty = obj_data.get("qty").getAsString().replaceAll("\"", "");;
                                String cart_id = obj_data.get("cart_id").getAsString().replaceAll("\"", "");;
                                String menu_name = obj_data.get("menu_name").getAsString().replaceAll("\"", "");;
                                String menu_id = obj_data.get("menu_id").getAsString().replaceAll("\"", "");;
                                String category_id = obj_data.get("category_id").getAsString().replaceAll("\"", "");;
                                String description = obj_data.get("description").getAsString().replaceAll("\"", "");;
                                String price = obj_data.get("price").getAsString().replaceAll("\"", "");;
                                String veg_nonveg = obj_data.get("veg_nonveg").getAsString().replaceAll("\"", "");;
                                String prepare_time = obj_data.get("prepare_time").getAsString().replaceAll("\"", "");;
                                String menu_image = obj_data.get("menu_image").getAsString().replaceAll("\"", "");;


                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("menu_id", menu_id);
                                hashMap.put("menu_name", menu_name);
                                hashMap.put("category_id", category_id);
                                hashMap.put("cart_id", cart_id);
                                hashMap.put("description", description);
                                hashMap.put("veg_nonveg", veg_nonveg);
                                hashMap.put("price", price);
                                hashMap.put("prepare_time", prepare_time);
                                hashMap.put("menu_image", menu_image);
                                hashMap.put("qty", qty);

                                item_list.add(hashMap);
                            }
                            double s_total = Double.parseDouble(sub_total);
                            @SuppressLint("DefaultLocale")
                            String su_total = String.format("%.2f", s_total);

                            double s_delivery_charge = Double.parseDouble(delivery_charge);
                            @SuppressLint("DefaultLocale")
                            String su_delivery_charge = String.format("%.2f", s_delivery_charge);

                            double s_tax_charge = Double.parseDouble(tax_charge);
                            @SuppressLint("DefaultLocale")
                            String su_tax_charge = String.format("%.2f", s_tax_charge);

                            double s_total_payable = Double.parseDouble(total_payable);
                            @SuppressLint("DefaultLocale")
                            String su_total_payable = String.format("%.2f", s_total_payable);
*/

                           /* tv_subtotal_value.setText("₹  "+su_total);
                            tv_delivery_value.setText("₹  "+su_delivery_charge);
                            tv_tax_value.setText("₹  "+su_tax_charge);
                            tv_total_value.setText("₹  "+su_total_payable);*/

                            globalClass.setTotal_item_price_acc_qty(Double.parseDouble(sub_total));

                            String item_count = cart_details.size() +" ITEMS  ₹ "+sub_total;
                            tv_item_count.setText(item_count);



                        /*    rv_cart.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                            CartAdapter cartAdapter = new CartAdapter(CartActivity.this, item_list);
                            rv_cart.setAdapter(cartAdapter);
                            cartAdapter.notifyDataSetChanged();*/




                        }else{

                            Toasty.info(context, message, Toast.LENGTH_LONG).show();
                        }

                       // rl_progress.setVisibility(View.GONE);

                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    //  mView.hideDialog();

                }
            }, new Response.ErrorListener() {

                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "menu_list Error: " + error.getMessage());
                    Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                  //  rl_progress.setVisibility(View.GONE);

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("res_id",globalClass.getId());
                    params.put("user_id",globalClass.getUser_id());
                    params.put("user_lat",globalClass.getUser_lat());
                    params.put("user_long",globalClass.getUser_long());

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