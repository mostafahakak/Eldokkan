package volleyappsetup.com.theapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.R;
import volleyappsetup.com.theapp.commen.Common;

public class OrderReViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_cart_name,txt_price,img_cart_count,size;
    public ImageView cart_image;


    public OrderReViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_cart_name = (TextView) itemView.findViewById(R.id.cartItemNames);
        txt_price = (TextView) itemView.findViewById(R.id.cartItemPrices);
        img_cart_count = (TextView) itemView.findViewById(R.id.cart_item_counts);
        cart_image = (ImageView) itemView.findViewById(R.id.cart_images);
        size = (TextView) itemView.findViewById(R.id.size);



    }


}
