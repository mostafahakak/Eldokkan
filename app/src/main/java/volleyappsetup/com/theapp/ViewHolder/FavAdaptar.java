package volleyappsetup.com.theapp.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import volleyappsetup.com.theapp.Favorite;
import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.ItemDetail;
import volleyappsetup.com.theapp.Model.Favorites;
import volleyappsetup.com.theapp.Model.Order;
import volleyappsetup.com.theapp.R;

public class FavAdaptar extends RecyclerView.Adapter<FavViewHolder> {

    private Context context;
    private List <Favorites> favoritesList;

    public FavAdaptar(Context context, List <Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.favorites_item,viewGroup,false);
        return new FavViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder viewHolder, final int postion) {

        viewHolder.txtItemName.setText(favoritesList.get(postion).getItemName());
        Picasso.with(context).load(favoritesList.get(postion).getItemImage()).resize(70,70).centerCrop().into(viewHolder.ItemImages);


        final Favorites local = favoritesList.get(postion);
        viewHolder.setItemClickListner(new ItemClickListner() {
            @Override
            public void onClick(View view, int i, boolean isLongClick) {
                Intent itemDetails = new Intent(context,ItemDetail.class);
                itemDetails.putExtra("ItemId",favoritesList.get(postion).getItemId());
                context.startActivity(itemDetails);
            }
        });
        }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }
    public void removeItem(int postion){
        favoritesList.remove(postion);
        notifyItemRemoved(postion);
    }
    public void restoreItem(Favorites item, int postion){
        favoritesList.add(postion,item);
        notifyItemInserted(postion);
    }
    public  Favorites getItem (int postion)
    {
        return favoritesList.get(postion);
    }
}
