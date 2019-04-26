package volleyappsetup.com.theapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

import volleyappsetup.com.theapp.Database.Database;
import volleyappsetup.com.theapp.Model.Banner;
import volleyappsetup.com.theapp.Model.Catagory;
import volleyappsetup.com.theapp.Model.Item;
import volleyappsetup.com.theapp.Model.Order;
import volleyappsetup.com.theapp.Model.Rating;
import volleyappsetup.com.theapp.commen.Common;

public class ItemDetail extends AppCompatActivity implements RatingDialogListener {

        TextView Item_name,Item_desc,Item_price,ship,policy,coutstars;

        SliderLayout ItemImage;
        HashMap<String,String> image_list;

        EditText siz;

        CollapsingToolbarLayout collapsingToolbarLayout;
        CounterFab cartbtn,btnRating;
        ElegantNumberButton numberButton;
        RatingBar ratingBar;
        String ItemId="";
        FirebaseDatabase database;
        DatabaseReference mRef;
        DatabaseReference ratingtbl;
        Item item;

        FirebaseDatabase dat;
        DatabaseReference refs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        dat = FirebaseDatabase.getInstance();
        refs = dat.getReference("user").child(Common.currentuser.getPhone());

        refs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                        String it = Item_name.getText().toString();
                        refs.child("Items").child(String.valueOf(System.currentTimeMillis())).setValue(it);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("items");
        ratingtbl = database.getReference("Rating");

        coutstars = (TextView) findViewById(R.id.numbcount);

        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        cartbtn = (CounterFab) findViewById(R.id.btnCart);

        btnRating = (CounterFab) findViewById(R.id.btnRating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });



        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addcart(new Order(ItemId,item.getName(),numberButton.getNumber(),item.getPrice()
                ,siz.getText().toString(),item.getImage()));
                Toast.makeText(ItemDetail.this, "Added To Cart", Toast.LENGTH_SHORT).show();

                String getpop = getIntent().getStringExtra("popul");
                mRef.child(ItemId).child("Popularity").setValue(getpop);


                mRef.child(ItemId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Item model = dataSnapshot.getValue(Item.class);

                        String popular = model.getPrice();
                        int pops = (Integer.parseInt(popular) + 1);
                        String popu = String.valueOf(pops);
                        Intent itemDetails = new Intent(ItemDetail.this,ItemDetail.class);
                        itemDetails.putExtra("popul",popu);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        cartbtn.setCount(new Database(this).getcountcart());

        Item_desc = (TextView) findViewById(R.id.item_desc);
        ship = (TextView) findViewById(R.id.ship);
        policy = (TextView) findViewById(R.id.policy);
        Item_name = (TextView) findViewById(R.id.itemName);
        Item_price = (TextView) findViewById(R.id.itemPrice);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        siz = (EditText) findViewById(R.id.size);

        //Get Item Intent
        if (getIntent()!= null)
            ItemId = getIntent().getStringExtra("ItemId");
        if (!ItemId.isEmpty())
        {
            if (Common.isConnectingToNet(getBaseContext()))
            {
                getDetailItem(ItemId);
                setupslider(ItemId);
                getRatingItem(ItemId);

            }
            else {
                Toast.makeText(this, "Check InterNet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }


    }

    private void setupslider(String itemId) {

        ItemImage = (SliderLayout) findViewById(R.id.item_mage);
        image_list = new HashMap <>();
        final DatabaseReference banners = mRef.child(itemId).child("banner");

        banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    Banner banner = postSnapShot.getValue(Banner.class);
                    Item item = postSnapShot.getValue(Item.class);
                    image_list.put(item.getName()+"_"+banner.getId(),banner.getImage());
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

    private void getRatingItem(String ItemId) {


        ratingtbl.child(ItemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    if (dataSnapshot.child("rateValue").exists())
                    {
                        float sum = Float.parseFloat(dataSnapshot.child("rateValue").getValue().toString());
                        ratingBar.setRating(sum);
                    }
                    else
                        {

                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder().
                setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Ok","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate This Item")
                .setDescription("Select From Stars").
                setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Write ur Comment Here")
                .setHintTextColor(R.color.colorAccent)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setCommentTextColor(R.color.white)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(ItemDetail.this)
                .show();
    }

    private void getDetailItem(String itemId) {

        ratingtbl.child(ItemId).child("counter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                coutstars.setText(dataSnapshot.getValue().toString());

                else
                    {

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item = dataSnapshot.getValue(Item.class);

                // Set Details
                //Picasso.with(getBaseContext()).load(item.getImage()).into(ItemImage);
                Item_name.setText(item.getName());
                Item_price.setText(item.getPrice().toString()+" LE");
                Item_desc.setText(item.getDescription());
                ship.setText(item.getShipping());
                if (ship.getText() == null)
                {
                    ship.setText(R.string.ship);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        ItemImage.startAutoCycle();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(final int i, @NotNull final String s) {

        ratingtbl.child(ItemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // New Value
                        int d = 1;
                        String sasa = String.valueOf(d);
                        ratingtbl.child(ItemId).child("counter").setValue(sasa);
                        ratingtbl.child(ItemId).child("ItemId").setValue(ItemId);
                        ratingtbl.child(ItemId).child("sum").setValue(String.valueOf(i));
                        ratingtbl.child(ItemId).child("Name").setValue(Item_name.getText().toString());
                        ratingtbl.child(ItemId).child("Price").setValue(Item_price.getText().toString());
                        ratingtbl.child(ItemId).child("rateValue").setValue(String.valueOf(i));
                        ratingtbl.child(ItemId).child("comment").setValue(s);


                    }
                    else if (dataSnapshot.exists())
                    {
                            int sums = Integer.parseInt(dataSnapshot.child("sum").getValue().toString()) + i;
                            float count = Float.parseFloat(coutstars.getText().toString()) + 1;
                            float ra = sums / count;


                            String newcount = String.valueOf(count);
                            String newrating = String.valueOf(ra);

                            ratingtbl.child(ItemId).child("counter").setValue(newcount);
                            ratingtbl.child(ItemId).child("ItemId").setValue(ItemId);
                            ratingtbl.child(ItemId).child("sum").setValue(String.valueOf(sums));
                            ratingtbl.child(ItemId).child("rateValue").setValue(String.valueOf(newrating));
                            ratingtbl.child(ItemId).child("comment").setValue(s);

                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
