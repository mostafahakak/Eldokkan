package volleyappsetup.com.theapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import volleyappsetup.com.theapp.Database.Database;
import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.Model.Catagory;
import volleyappsetup.com.theapp.Model.Favorites;
import volleyappsetup.com.theapp.Model.Item;
import volleyappsetup.com.theapp.Model.Order;
import volleyappsetup.com.theapp.Model.Request;
import volleyappsetup.com.theapp.Model.itemcat;
import volleyappsetup.com.theapp.ViewHolder.CartAdaptar;
import volleyappsetup.com.theapp.ViewHolder.ItemViewHolder;
import volleyappsetup.com.theapp.ViewHolder.MenuViewHolder;
import volleyappsetup.com.theapp.ViewHolder.OrderReViewHolder;
import volleyappsetup.com.theapp.ViewHolder.OrderViewHolder;
import volleyappsetup.com.theapp.commen.Common;

public class OrderView extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference requests;

    String orr = "";


    RecyclerView recyclerViewOrder;
    RecyclerView.LayoutManager layman;

    FirebaseRecyclerAdapter<itemcat,OrderReViewHolder> adp;


    TextView txtTotal;
    List<Order> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        database = FirebaseDatabase.getInstance();
        orr = getIntent().getStringExtra("or");
        requests = database.getReference("Requests").child(orr).child("items");

        txtTotal = (TextView) findViewById(R.id.total);


        recyclerViewOrder = (RecyclerView) findViewById(R.id.listCarts);
        recyclerViewOrder.setHasFixedSize(true);
        layman = new LinearLayoutManager(this);
        recyclerViewOrder.setLayoutManager(layman);

        if (getIntent() != null)
            orr = getIntent().getStringExtra("or");
        if (!orr.isEmpty() && orr != null) {
            if (Common.isConnectingToNet(getBaseContext())) {
                loadListItem();
            } else {
                Toast.makeText(this, "Check InterNet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }


    }

    private void loadListItem() {
        adp = new FirebaseRecyclerAdapter <itemcat, OrderReViewHolder>(itemcat.class,
                                                                     R.layout.orderview_item, OrderReViewHolder.class,
                                                                      requests){
            @Override
            protected void populateViewHolder(final OrderReViewHolder viewHolder, itemcat model, int position) {

                viewHolder.txt_cart_name.setText(model.getProductName());
                viewHolder.img_cart_count.setText(model.getQuantity());
                viewHolder.size.setText(model.getDiscount());
                viewHolder.txt_price.setText(model.getPrice());
                Picasso.with(getApplicationContext()).load(model.getImage()).resize(70,70).centerCrop().into(viewHolder.cart_image);


            }
        };
        recyclerViewOrder.setAdapter(adp);
    }
}
