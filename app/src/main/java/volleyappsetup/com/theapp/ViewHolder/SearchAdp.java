package volleyappsetup.com.theapp.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import volleyappsetup.com.theapp.ItemDetail;
import volleyappsetup.com.theapp.Model.Item;
import volleyappsetup.com.theapp.R;

public class SearchAdp extends RecyclerView.Adapter<SearchAdp.SearchViewHolder> {

    Context context;
    ArrayList<String> namesearch;
    ArrayList<String> pricesearch;
    ArrayList<String> imagesearch;
    ArrayList<String> values;


    public class SearchViewHolder extends RecyclerView.ViewHolder
    {
        ImageView searchImage,searchfavimg,searchshare,searchqcart;
        TextView searchPrice,searchName;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            searchImage = (ImageView) itemView.findViewById(R.id.item_image);
            searchName = (TextView) itemView.findViewById(R.id.item_name);
            searchPrice = (TextView) itemView.findViewById(R.id.item_price);
            searchfavimg = (ImageView) itemView.findViewById(R.id.fav);
            searchshare = (ImageView) itemView.findViewById(R.id.share);
            searchqcart = (ImageView) itemView.findViewById(R.id.qcart);

        }
    }

    public SearchAdp(Context context, ArrayList <String> namesearch, ArrayList <String> pricesearch, ArrayList <String> imagesearch, ArrayList <String> values) {
        this.context = context;
        this.namesearch = namesearch;
        this.pricesearch = pricesearch;
        this.imagesearch = imagesearch;
        this.values = values;
    }

    @NonNull
    @Override
    public SearchAdp.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false);
        return new SearchAdp.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, final int i) {

        searchViewHolder.searchName.setText(namesearch.get(i));
        searchViewHolder.searchPrice.setText(pricesearch.get(i));
        Glide.with(context).asBitmap().load(imagesearch.get(i)).into(searchViewHolder.searchImage);

        searchViewHolder.searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+namesearch.get(i)+"", Toast.LENGTH_SHORT).show();
                Intent itemDetails = new Intent(context, ItemDetail.class);
                itemDetails.putExtra("ItemId",values.get(i));
                context.startActivity(itemDetails);
            }
        });
    }



    @Override
    public int getItemCount() {
        return namesearch.size();
    }
}
