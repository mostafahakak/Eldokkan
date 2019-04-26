package volleyappsetup.com.theapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.R;

public class MoreNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListner itemClickListner;
    public TextView titlemorenews,bodymorenews;
    public ImageView imagemorenews;


    public MoreNewsViewHolder(@NonNull View itemView) {
        super(itemView);

        titlemorenews = (TextView) itemView.findViewById(R.id.readmoretitle);
        imagemorenews = (ImageView) itemView.findViewById(R.id.readmoreimage);
        bodymorenews = (TextView) itemView.findViewById(R.id.readmorebody);
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
