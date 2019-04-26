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

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{

    public TextView txt_cart_name,txt_price;
    public ImageView img_cart_count,cart_image;
    public Button delete;

    public RelativeLayout view_backGround;
    public LinearLayout view_forg;

    private ItemClickListner itemClickListner;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_cart_name = (TextView) itemView.findViewById(R.id.cartItemName);
        txt_price = (TextView) itemView.findViewById(R.id.cartItemPrice);
        img_cart_count = (ImageView) itemView.findViewById(R.id.cart_item_count);
        delete = (Button) itemView.findViewById(R.id.remove);
        view_backGround = (RelativeLayout) itemView.findViewById(R.id.view_background);
        view_forg = (LinearLayout) itemView.findViewById(R.id.view_for);
        cart_image = (ImageView) itemView.findViewById(R.id.cart_image);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v , getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0,0,getAdapterPosition(),Common.DELETE);
    }

}
