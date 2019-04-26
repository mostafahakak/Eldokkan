package volleyappsetup.com.theapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAdd,txtTotal;
    public ImageView dltorder;
    private ItemClickListner itemClickListner;
    public LinearLayout linear;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderId = (TextView) itemView.findViewById(R.id.orderid);
        txtOrderStatus= (TextView) itemView.findViewById(R.id.orderstatus);
        txtOrderPhone= (TextView) itemView.findViewById(R.id.orderphone);
        txtOrderAdd= (TextView) itemView.findViewById(R.id.orderAdd);
        txtTotal= (TextView) itemView.findViewById(R.id.ordertot);
        dltorder= (ImageView) itemView.findViewById(R.id.deleteorder);
        linear = (LinearLayout) itemView.findViewById(R.id.view_for);
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
