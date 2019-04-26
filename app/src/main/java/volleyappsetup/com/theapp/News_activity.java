package volleyappsetup.com.theapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import volleyappsetup.com.theapp.Model.News;
import volleyappsetup.com.theapp.Model.newsmodel;
import volleyappsetup.com.theapp.commen.Common;

public class News_activity extends AppCompatActivity {

    TextView titlenews,body;
    ImageView imagenews;
    FirebaseDatabase data;
    DatabaseReference newsref;
    String newsids = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_activity);

        titlenews = (TextView) findViewById(R.id.newstitle);
        body = (TextView) findViewById(R.id.newsbody);
        imagenews = (ImageView) findViewById(R.id.newsimaged);

        newsids = getIntent().getStringExtra("newsid");
        data = FirebaseDatabase.getInstance();
        newsref = data.getReference("WomensNews").child(newsids);

        // Get Intent of CatagoryId
        if (getIntent() != null)
            newsids = getIntent().getStringExtra("newsid");
        if (!newsids.isEmpty() && newsids != null){
            if (Common.isConnectingToNet(getBaseContext())) {
                loadnews();
            }
            else
            {
                Toast.makeText(this, "Check InterNet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void loadnews() {
        newsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                News model = dataSnapshot.getValue(News.class);
                titlenews.setText(model.getTitle());
                Picasso.with(getBaseContext()).load(model.getImage()).into(imagenews);
                body.setText(model.getBody());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
