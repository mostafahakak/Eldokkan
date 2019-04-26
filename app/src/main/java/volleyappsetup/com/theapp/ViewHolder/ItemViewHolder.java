package volleyappsetup.com.theapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.R;




public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtItemName,txtItemPrice;
    public ImageView ItemImage,favimg,share,qcart;
    private ItemClickListner itemClickListner;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        txtItemName = (TextView) itemView.findViewById(R.id.item_name);
        txtItemPrice = (TextView) itemView.findViewById(R.id.item_price);
        ItemImage = (ImageView) itemView.findViewById(R.id.item_image);
        favimg = (ImageView) itemView.findViewById(R.id.fav);
        share = (ImageView) itemView.findViewById(R.id.share);
        qcart = (ImageView) itemView.findViewById(R.id.qcart);


        itemView.setOnClickListener(this);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v , getAdapterPosition(),false);
    }
}
