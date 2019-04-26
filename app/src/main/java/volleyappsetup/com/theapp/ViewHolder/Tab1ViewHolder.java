package volleyappsetup.com.theapp.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.R;

public class Tab1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textView;
    public ImageView imm;
    private ItemClickListner itemClickListner;

    public Tab1ViewHolder(@NonNull View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(R.id.menu_name);
        imm = (ImageView) itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);

    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
    public void onClick(View v) {
        itemClickListner.onClick(v , getAdapterPosition(),false);
    }

}
