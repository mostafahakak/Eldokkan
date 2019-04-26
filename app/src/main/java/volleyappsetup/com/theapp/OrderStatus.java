package volleyappsetup.com.theapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import volleyappsetup.com.theapp.Model.Request;
import volleyappsetup.com.theapp.ViewHolder.OrderViewHolder;
import volleyappsetup.com.theapp.commen.Common;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManagers;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        // FireBase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listorders);
        recyclerView.setHasFixedSize(true);
        layoutManagers = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagers);

        loadordes(Common.currentuser.getPhone());

    }

    private void loadordes(String phone) {
        adapter = new FirebaseRecyclerAdapter <Request, OrderViewHolder>(
                Request.class,R.layout.order_layout,
                OrderViewHolder.class,requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(final OrderViewHolder viewHolder, Request model, final int position) {

                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAdd.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtTotal.setText(model.getTotal());

                viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String or = viewHolder.txtOrderId.getText().toString();
                        Intent intent = new Intent(OrderStatus.this,OrderView.class);
                        //or is a key we need to get;
                        intent.putExtra("or",or);
                        startActivity(intent);
                    }
                });

                viewHolder.dltorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (adapter.getItem(position).getStatus().equals("0"))
                            DeletOrder(adapter.getRef(position).getKey());
                        else
                            Toast.makeText(OrderStatus.this, "Sorry ur Order Can't be deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                if (viewHolder.txtOrderStatus.getText().toString() == "On My Way" )
                {
                    viewHolder.txtOrderStatus.setTextColor(Color.GREEN);
                }
                else if (viewHolder.txtOrderStatus.getText().toString() == "Delieverd")
                {
                    viewHolder.txtOrderStatus.setTextColor(Color.RED);

                }
                else if (viewHolder.txtOrderStatus.getText().toString() == "Shiped")
                {
                    viewHolder.txtOrderStatus.setTextColor(Color.GREEN);

                } else if (viewHolder.txtOrderStatus.getText().toString() == "Reject")
                {
                    viewHolder.txtOrderStatus.setTextColor(Color.RED);

                }else
                    {
                        viewHolder.txtOrderStatus.setTextColor(Color.DKGRAY);
                    }
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void DeletOrder(final String key) {
        requests.child(key).removeValue().addOnSuccessListener(new OnSuccessListener <Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(OrderStatus.this,new StringBuilder("Order").append(key).append("has been deleted").toString(), Toast.LENGTH_SHORT).show();
                
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String convertCodeToStatus(String status) {

        if (status.equals("0"))
            return "Placed";

        if (status.equals("1"))
            return "On My Way";

        if (status.equals("2"))
            return "Delieverd";

        if (status.equals("9"))
            return "Reject";

        else
            return "Shiped";

    }
}
