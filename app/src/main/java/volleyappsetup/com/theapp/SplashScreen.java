package volleyappsetup.com.theapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import volleyappsetup.com.theapp.Model.spalshmodel;

public class SplashScreen extends AppCompatActivity {

    Context context;
    TextView splash;
    ImageView splashimage;
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

      /*  database = FirebaseDatabase.getInstance();
        ref = database.getReference("splash");

        splash = (TextView) findViewById(R.id.splash);
        splashimage = (ImageView) findViewById(R.id.splashimage);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                spalshmodel splashmodel = dataSnapshot.getValue(spalshmodel.class);
                splash.setText(splashmodel.getName());
                Picasso.with(getBaseContext()).load(splashmodel.getImage()).into(splashimage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        Thread myThread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
