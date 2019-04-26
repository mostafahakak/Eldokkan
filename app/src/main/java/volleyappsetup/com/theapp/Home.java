package volleyappsetup.com.theapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.accountkit.AccountKit;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;

import io.paperdb.Paper;
import volleyappsetup.com.theapp.Database.Database;
import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.Model.Banner;
import volleyappsetup.com.theapp.Model.Catagory;
import volleyappsetup.com.theapp.ViewHolder.MenuViewHolder;
import volleyappsetup.com.theapp.commen.Common;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        FirebaseDatabase database;
        DatabaseReference category;
        TextView txtFullName;

        RecyclerView recyc_menu;
        RecyclerView.LayoutManager layoutManager;

        SliderLayout ItemImage;
        HashMap<String,String> image_list;

        FirebaseDatabase databases;
        DatabaseReference mRef;


        CounterFab fab;

        FirebaseRecyclerAdapter<Catagory,MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu);
        setSupportActionBar(toolbar);

        databases = FirebaseDatabase.getInstance();
        mRef = databases.getReference("Ads");





        database = FirebaseDatabase.getInstance();
        category = database.getReference("Catogry");

        Paper.init(this);

        fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, cart.class));
            }
        });
        fab.setCount(new Database(this).getcountcart());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //set Name For User
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView) findViewById(R.id.txtfullname);

        //  txtFullName.setText(Common.currentuser.getName());

        // Load Menu
        recyc_menu = (RecyclerView) findViewById(R.id.recycler_menue);
        recyc_menu.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        recyc_menu.setLayoutManager(layoutManager);
        recyc_menu.setNestedScrollingEnabled(false);
        recyc_menu.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL|DividerItemDecoration.HORIZONTAL));
        recyc_menu.setItemAnimator(new DefaultItemAnimator());

        if (Common.isConnectingToNet(getBaseContext()))
        {
            loadmenu();
            setupslider();
        }
        else
            {
                Toast.makeText(this, "Check InterNet Connection", Toast.LENGTH_SHORT).show();
                return;
        }

    }

    private void loadmenu() {
        adapter = new FirebaseRecyclerAdapter <Catagory, MenuViewHolder>(Catagory.class,
                R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Catagory model, final int position) {

                viewHolder.txtMenueName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageVie);
                viewHolder.progressBar.setVisibility(View.GONE);

                final Catagory clickItem = model;

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int postion, boolean isLongClick) {
                        Toast.makeText(Home.this,""+clickItem.getName(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Home.this,ItemList.class);
                        //CatagorID is a key we need to get;
                        intent.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyc_menu.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getcountcart());
       if (adapter != null)
            adapter.getItemCount();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh)
            startActivity(new Intent(Home.this, Main2Activity.class));
            //loadmenu();
        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Order) {
            startActivity(new Intent(Home.this,OrderStatus.class));

        } else if (id == R.id.nav_cart) {
            startActivity(new Intent(Home.this, cart.class));
        }
        else if (id == R.id.fav) {
            startActivity(new Intent(Home.this,Favorite.class));
        }
        else if (id == R.id.contact) {
                startActivity(new Intent(Home.this,ContactUs.class));
                }
            else if (id == R.id.Log_out) {
            Paper.book().destroy();
            Intent siginout = new Intent(Home.this,MainActivity.class);
            siginout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(siginout);
            AccountKit.logOut();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setupslider() {

        ItemImage = (SliderLayout) findViewById(R.id.adin);
        image_list = new HashMap <>();
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
                    TextSliderView textSliderView = new TextSliderView(getBaseContext());
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

}
