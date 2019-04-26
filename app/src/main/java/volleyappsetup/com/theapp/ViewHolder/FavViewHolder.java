package volleyappsetup.com.theapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.R;

public class FavViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public TextView txtItemName;
public ImageView ItemImages,favimg,share;
public TextView txt1;
public TextView txt2;
private ItemClickListner itemClickListner;

    public RelativeLayout view_backGrounds;
    public RelativeLayout view_forge;

public FavViewHolder(@NonNull View itemView) {
        super(itemView);

        txtItemName = (TextView) itemView.findViewById(R.id.item_name);
        ItemImages = (ImageView) itemView.findViewById(R.id.item_image);
        favimg = (ImageView) itemView.findViewById(R.id.fav);
        share = (ImageView) itemView.findViewById(R.id.share);
        view_backGrounds= (RelativeLayout) itemView.findViewById(R.id.view_backgrounds);
        view_forge = (RelativeLayout) itemView.findViewById(R.id.view_forge);


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

