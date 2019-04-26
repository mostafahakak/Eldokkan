package volleyappsetup.com.theapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenueName;
    public ImageView imageVie;
    public ProgressBar progressBar;

    private ItemClickListner itemClickListner;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMenueName = (TextView) itemView.findViewById(R.id.menu_name);
        imageVie = (ImageView) itemView.findViewById(R.id.menu_image);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
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
