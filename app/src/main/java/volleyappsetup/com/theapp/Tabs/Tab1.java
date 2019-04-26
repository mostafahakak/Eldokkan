package volleyappsetup.com.theapp.Tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.CallbackManager;
import com.facebook.accountkit.AccountKit;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;

import io.paperdb.Paper;
import volleyappsetup.com.theapp.ContactUs;
import volleyappsetup.com.theapp.Database.Database;
import volleyappsetup.com.theapp.Favorite;
import volleyappsetup.com.theapp.Home;
import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.ItemDetail;
import volleyappsetup.com.theapp.ItemList;
import volleyappsetup.com.theapp.Main2Activity;
import volleyappsetup.com.theapp.MainActivity;
import volleyappsetup.com.theapp.Message;
import volleyappsetup.com.theapp.Model.Banner;
import volleyappsetup.com.theapp.Model.Catagory;
import volleyappsetup.com.theapp.Model.Favorites;
import volleyappsetup.com.theapp.Model.Item;
import volleyappsetup.com.theapp.Model.News;
import volleyappsetup.com.theapp.Model.Order;
import volleyappsetup.com.theapp.Model.Populerty;
import volleyappsetup.com.theapp.News_activity;
import volleyappsetup.com.theapp.OrderStatus;
import volleyappsetup.com.theapp.R;
import volleyappsetup.com.theapp.ReadMore;
import volleyappsetup.com.theapp.ViewHolder.ItemViewHolder;
import volleyappsetup.com.theapp.ViewHolder.Tab1ViewHolder;
import volleyappsetup.com.theapp.cart;
import volleyappsetup.com.theapp.commen.Common;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.instabug.featuresrequest.FeatureRequests.show;

public class Tab1 extends Fragment
{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RecyclerView somephotos;
    RecyclerView.LayoutManager somelayout;


    FirebaseDatabase database;
    DatabaseReference category;
    DatabaseReference somedata;

    ImageView imageView;
    TextView morenews;
    TextView txtFullName;
    SliderLayout ItemImage;
    HashMap<String,String> image_list;

    FirebaseDatabase databases;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter <Catagory, Tab1ViewHolder> adapter;
    FirebaseRecyclerAdapter <News, Tab1ViewHolder> adapters;

    CounterFab fabs;
 //   CounterFab message;



    @Override
    public void onResume() {
        super.onResume();
        fabs.setCount(new Database(getContext()).getcountcart());
        if (adapter != null)
            adapter.getItemCount();

    }


    public Tab1() {
        // empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void loadmenu() {
        adapter = new FirebaseRecyclerAdapter <Catagory, Tab1ViewHolder>(Catagory.class,
                                                                         R.layout.menu_item, Tab1ViewHolder.class, category) {
            @Override
            protected void populateViewHolder(Tab1ViewHolder viewHolder, Catagory model, final int position) {

                viewHolder.textView.setText(model.getName());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imm);

                final Catagory clickItem = model;

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int postion, boolean isLongClick) {
                        Toast.makeText(getContext(), ""+clickItem.getName(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), ItemList.class);
                        //CatagorID is a key we need to get;
                        intent.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void setupslider() {

        ItemImage = (SliderLayout) getView().findViewById(R.id.adin);
        image_list = new HashMap<>();
        final DatabaseReference banners = mRef;

        banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    Banner banner = postSnapShot.getValue(Banner.class);
                    image_list.put(banner.getName()+"_"+banner.getId(),banner.getImage());
                }
                for (String key:image_list.keySet())
                {
                    String[] keySplit = key.split("_");
                    String nameofitem = keySplit[0];
                    String idofitem = keySplit[1];

                    // create Slider
                    TextSliderView textSliderView = new TextSliderView(getContext());
                    textSliderView.
                            description(nameofitem).
                            image(image_list.get(key)).
                            setScaleType(BaseSliderView.ScaleType.Fit).
                            setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {

                                }
                            });
                    // ADD Extra Bundle
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("ItemId",idofitem);

                    ItemImage.addSlider(textSliderView);

                    // Remove Event after Finish
                    banners.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ItemImage.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        ItemImage.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        ItemImage.setCustomAnimation(new DescriptionAnimation());
        ItemImage.setDuration(4000);

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = (ImageView) getView().findViewById(R.id.imageoffer);
        morenews = (TextView) getView().findViewById(R.id.readmore);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("women");
        somedata = database.getReference("WomensNews");

        databases = FirebaseDatabase.getInstance();
        mRef = databases.getReference("Ads");

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_menue);
        recyclerView.setHasFixedSize(false);

        somephotos = (RecyclerView) getView().findViewById(R.id.somephotos);
        somephotos.setHasFixedSize(false);
        somelayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        somephotos.setLayoutManager(somelayout);
        somephotos.setNestedScrollingEnabled(false);



        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

   //     message = (CounterFab) getView().findViewById(R.id.message);
     //   message.setOnClickListener(new View.OnClickListener() {
       //     @Override
         //   public void onClick(View v) {
           //     startActivity(new Intent(getContext(), Message.class));
          //  }
        //});



        fabs = (CounterFab) getView().findViewById(R.id.fabs);
        fabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), cart.class));
            }
        });
        fabs.setCount(new Database(getContext()).getcountcart());

        if (Common.isConnectingToNet(getContext()))
        {
            loadmenu();
            setupslider();
            loadphotos();

        }
        else
        {
            Toast.makeText(getContext(), "Check InterNet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        morenews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ReadMore.class);
                startActivity(intent);
            }
        });

    }



    private void loadphotos() {
        adapters = new FirebaseRecyclerAdapter <News, Tab1ViewHolder>(News.class,
                                                                      R.layout.news_item, Tab1ViewHolder.class, somedata) {
            @Override
            protected void populateViewHolder(Tab1ViewHolder viewHolder, News model, final int position) {

                viewHolder.textView.setText(model.getTitle());

                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imm);

                final News clickItem = model;
                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int postion, boolean isLongClick) {
                        Intent intent = new Intent(getContext(), News_activity.class);
                        //CatagorID is a key we need to get;
                        intent.putExtra("newsid",adapters.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        somephotos.setAdapter(adapters);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab1, container, false);


    }


}

