package volleyappsetup.com.theapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import volleyappsetup.com.theapp.Database.Database;
import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.Model.Favorites;
import volleyappsetup.com.theapp.Model.Item;
import volleyappsetup.com.theapp.Model.Order;
import volleyappsetup.com.theapp.ViewHolder.ItemViewHolder;
import volleyappsetup.com.theapp.ViewHolder.SearchAdp;
import volleyappsetup.com.theapp.commen.Common;

public class ItemList extends AppCompatActivity {

    RecyclerView recyc_item;
    RecyclerView.LayoutManager layoutManagers;

    FirebaseDatabase database;
    DatabaseReference itemlist;

    MaterialSearchBar search_edit_text;
    ArrayList<String> namesearch;
    ArrayList<String> pricesearch;
    ArrayList<String> imagesearch;
    ArrayList<String> values;

    SearchAdp searchAdp;


    Database locaolDB;


    String categoryId = "";
    FirebaseRecyclerAdapter<Item,ItemViewHolder> adapter;



    // Ads View
    private static final String TAG = "ItemList";
    private AdView mAdView;

    //FB Share
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    // Targe Share API
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // Create photo from BitMap
            SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
            if (ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);


        // INIT FB
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        // Ads View
        MobileAds.initialize(this, "ca-app-pub-1316548528485611/4625145765");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        database = FirebaseDatabase.getInstance();
        itemlist = database.getReference("items");

        // Local DB
        locaolDB = new Database(this);

        namesearch = new ArrayList <>();
        pricesearch = new ArrayList <>();
        imagesearch = new ArrayList <>();
        values = new ArrayList <>();

        search_edit_text = (MaterialSearchBar) findViewById(R.id.searchedit);
        search_edit_text.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty())
                {
                    setAdapter(s.toString());
                }
                else
                    {
                        namesearch.clear();
                        pricesearch.clear();
                        imagesearch.clear();
                        values.clear();
                        recyc_item.removeAllViews();
                    }
            }
        });



        recyc_item = (RecyclerView) findViewById(R.id.item_recycler);
        recyc_item.setHasFixedSize(true);
        layoutManagers = new GridLayoutManager(this,2);
        recyc_item.setLayoutManager(layoutManagers);
        // Set Dividers
        recyc_item.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL|DividerItemDecoration.HORIZONTAL));
        recyc_item.setItemAnimator(new DefaultItemAnimator());




        // Get Intent of CatagoryId
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
            if (!categoryId.isEmpty() && categoryId != null){
                if (Common.isConnectingToNet(getBaseContext())) {
                    loadListItem(categoryId);
                }
                else
                    {
                        Toast.makeText(this, "Check InterNet Connection", Toast.LENGTH_SHORT).show();
                        return;
                    }
            }

    }

    private void setAdapter(final String searchedString) {

        itemlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                namesearch.clear();
                pricesearch.clear();
                imagesearch.clear();
                values.clear();
                recyc_item.removeAllViews();
                int counters = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren() )
                {
                    String uid = snapshot.getKey();
                    String name = snapshot.child("Name").getValue(String.class);
                    String price = snapshot.child("Price").getValue(String.class);
                    String image = snapshot.child("Image").getValue(String.class);
                    String value = snapshot.child("ItemId").getValue(String.class);

                    if (name.toLowerCase().contains(searchedString.toLowerCase()))
                    {
                        namesearch.add(name);
                        pricesearch.add(price);
                        imagesearch.add(image);
                        values.add(value);
                        counters++;
                    }
                    else
                        {

                        }
                  }

                searchAdp = new SearchAdp(ItemList.this,namesearch,pricesearch,imagesearch,values);
                recyc_item.setAdapter(searchAdp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadListItem(String categoryId) {
        adapter = new FirebaseRecyclerAdapter <Item, ItemViewHolder>(Item.class,
                R.layout.item,ItemViewHolder.class,itemlist.orderByChild("MenuId").equalTo(categoryId)){
            @Override
            protected void populateViewHolder(final ItemViewHolder viewHolder, final Item model, final int position) {

                viewHolder.txtItemName.setText(model.getName());
                viewHolder.txtItemPrice.setText(model.getPrice()+" LE");
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.ItemImage);

                final Favorites favorites = new Favorites();
                favorites.setItemId(adapter.getRef(position).getKey());
                favorites.setItemName(model.getName());
                favorites.setItemDescription(model.getDescription());
                favorites.setItemDiscount(model.getDiscount());
                favorites.setItemImage(model.getImage());
                favorites.setItemMenuId(model.getMenuId());
                favorites.setUserPhone(Common.currentuser.getPhone());
                favorites.setItemPrice(model.getPrice());
                // Add Fav
                if (locaolDB.isFav(adapter.getRef(position).getKey()))
                    viewHolder.favimg.setImageResource(R.drawable.ic_favorite_black_24dp);
                viewHolder.favimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!locaolDB.isFav(adapter.getRef(position).getKey()))
                        {
                            locaolDB.addToFav(favorites);
                            viewHolder.favimg.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(ItemList.this, model.getName()+" Added To Fav", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                                locaolDB.removeFav(adapter.getRef(position).getKey(), Common.currentuser.getPhone());
                                viewHolder.favimg.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                Toast.makeText(ItemList.this, " "+model.getName()+" Removed From Fav", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
                final Item local = model;
                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int postion, boolean isLongClick) {
                        Toast.makeText(ItemList.this,""+local.getName()+"",Toast.LENGTH_SHORT).show();
                        Intent itemDetails = new Intent(ItemList.this,ItemDetail.class);
                        itemDetails.putExtra("ItemId",adapter.getRef(position).getKey());
                        startActivity(itemDetails);

                    }
                });

                viewHolder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Picasso.with(getApplicationContext()).load(model.getImage()).into(target);
                    }
                });
                viewHolder.qcart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Database(getBaseContext()).addcart(new Order(
                                adapter.getRef(position).getKey(),
                                model.getName(),
                                "1",
                                model.getPrice()
                                ,model.getDiscount(),model.getImage()));
                        Toast.makeText(ItemList.this, "Added To Cart", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        };
        recyc_item.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.getItemCount();
    }
}


