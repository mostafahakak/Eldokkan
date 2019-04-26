package volleyappsetup.com.theapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import volleyappsetup.com.theapp.Interface.ItemClickListner;
import volleyappsetup.com.theapp.Model.News;
import volleyappsetup.com.theapp.ViewHolder.MoreNewsViewHolder;
import volleyappsetup.com.theapp.commen.Common;

public class ReadMore extends AppCompatActivity {

    FirebaseDatabase newsdatabase;
    DatabaseReference newsRef;

    RecyclerView morenewsrec;
    RecyclerView.LayoutManager newslayoutmanager;

    FirebaseRecyclerAdapter<News,MoreNewsViewHolder> newsadp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more);

        newsdatabase = FirebaseDatabase.getInstance();
        newsRef = newsdatabase.getReference("WomensNews");
        morenewsrec = (RecyclerView) findViewById(R.id.morenewsrecyc);
        morenewsrec.setHasFixedSize(true);
        newslayoutmanager = new LinearLayoutManager(this);
        morenewsrec.setLayoutManager(newslayoutmanager);

        if (Common.isConnectingToNet(getBaseContext()))
        {
            loadmenunews();

        }
        else
        {
            Toast.makeText(this, "Check InterNet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void loadmenunews() {

        newsadp = new FirebaseRecyclerAdapter <News, MoreNewsViewHolder>(News.class,
                                                                         R.layout.morenewsitem,MoreNewsViewHolder.class,newsRef) {
            @Override
            protected void populateViewHolder(MoreNewsViewHolder viewHolder, News model, final int position) {

                viewHolder.titlemorenews.setText(model.getTitle());
                viewHolder.bodymorenews.setText(model.getBody());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imagemorenews);

                final News clickItem = model;

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int postion, boolean isLongClick) {
                        Intent intent = new Intent(ReadMore.this, News_activity.class);
                        //CatagorID is a key we need to get;
                        intent.putExtra("newsid",newsadp.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        morenewsrec.setAdapter(newsadp);

    }
}
