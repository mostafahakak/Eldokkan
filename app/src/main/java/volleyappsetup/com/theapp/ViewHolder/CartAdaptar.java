package volleyappsetup.com.theapp.ViewHolder;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.ItemDetail;
import volleyappsetup.com.theapp.Model.Order;
import volleyappsetup.com.theapp.R;

public class CartAdaptar extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList <>();
    private Context context;


    public CartAdaptar(List <Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_item,viewGroup,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {

        Picasso.with(context).load(listData.get(i).getImage()).resize(70,70).centerCrop().into(cartViewHolder.cart_image);
        TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(i).getQuantity(), Color.RED);
        cartViewHolder.img_cart_count.setImageDrawable(drawable);

        Locale locale = new Locale("en","Eg");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(i).getPrice()))*(Integer.parseInt(listData.get(i).getQuantity()));
        cartViewHolder.txt_price.setText(fmt.format(price));
        cartViewHolder.txt_cart_name.setText(listData.get(i).getProductName());

        cartViewHolder.setItemClickListner(new ItemClickListner() {
            @Override
            public void onClick(View view, int postion, boolean isLongClick) {
                Intent itemDetails = new Intent(context,ItemDetail.class);
                itemDetails.putExtra("ItemId",listData.get(postion).getProductId());
                context.startActivity(itemDetails);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Order getItem(int postion){
        return listData.get(postion);
    }


    public void removeItem(int postion){
        listData.remove(postion);
        notifyItemRemoved(postion);
    }
    public void restoreItem(Order item,int postion){
        listData.add(postion,item);
        notifyItemInserted(postion);
    }
}
