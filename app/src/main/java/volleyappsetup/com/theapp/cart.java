package volleyappsetup.com.theapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import volleyappsetup.com.theapp.Database.Database;
import volleyappsetup.com.theapp.Helper.ReceyclerItemTouchHelper;
import volleyappsetup.com.theapp.Interface.ReceyclerItemTouchHelperListner;
import volleyappsetup.com.theapp.Model.Item;
import volleyappsetup.com.theapp.Model.Order;
import volleyappsetup.com.theapp.Model.Request;
import volleyappsetup.com.theapp.Model.promo;
import volleyappsetup.com.theapp.Remote.IGoogleService;
import volleyappsetup.com.theapp.ViewHolder.CartAdaptar;
import volleyappsetup.com.theapp.ViewHolder.CartViewHolder;
import volleyappsetup.com.theapp.commen.Common;

public class cart extends AppCompatActivity implements ReceyclerItemTouchHelperListner,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.
OnConnectionFailedListener,LocationListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests,get;

    TextView txtTotal;
    Button btnPlace;
    CartAdaptar adaptar;
    RelativeLayout rootlayout;
    List<Order> cart = new ArrayList <>();

    Place shipingAddress;
    String address;
    promo promos;
    // Location
    private LocationRequest locationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;



    private static final int UPDATE_iNTERVAL = 5000;
    private static final int FATEST_iNTERVAL = 3000;
    private static final int DISPLACMENT = 10;
    private static final int LOCATION_REQUEST_CODE = 9999;
    private static final int PLAY_SERVICES_REQUEST = 9997;

    IGoogleService mGoogleMapService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        get = database.getReference("promo");

        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotal = (TextView) findViewById(R.id.total);
        btnPlace = (Button) findViewById(R.id.submit);

        rootlayout = (RelativeLayout) findViewById(R.id.rootlayout);

        // Swipe To Delete
        ItemTouchHelper.SimpleCallback itemTouchHelper = new ReceyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);



        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cart.size() > 0)
                    showAlartDialog();
                else
                    Toast.makeText(cart.this, "Empty Cart", Toast.LENGTH_SHORT).show();


            }
        });

        loadlistItem();



    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_iNTERVAL);
        locationRequest.setFastestInterval(FATEST_iNTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACMENT);
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }

    private boolean CheckPlaayService() {
        int resultcode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultcode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultcode))
                GooglePlayServicesUtil.getErrorDialog(resultcode,this,PLAY_SERVICES_REQUEST).show();
        else
            {
                Toast.makeText(this, "This Device is not Supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void showAlartDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cart.this);
        alertDialog.setTitle(R.string.alert_dialogTitle);
        alertDialog.setMessage(R.string.alert_dialog);

        LayoutInflater inflater = this.getLayoutInflater();
        View order_comment = inflater.inflate(R.layout.order_comment,null);

        final MaterialEditText edtAddr = (MaterialEditText) order_comment.findViewById(R.id.edtadd);
        final MaterialEditText promo = (MaterialEditText) order_comment.findViewById(R.id.promo);
        final Button pcode = (Button) order_comment.findViewById(R.id.pcode);



        final MaterialEditText edtcomm = (MaterialEditText) order_comment.findViewById(R.id.edtcomment);

        alertDialog.setView(order_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

            // Check weather the code exist or not //if exist 20% off
        pcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cod = promo.getText().toString();
                get.child(cod).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        promos = dataSnapshot.getValue(promo.class);
                            //  Code Is Wrong
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(cart.this, "Wrong Code or Expired", Toast.LENGTH_SHORT).show();
                            promo.setText("");
                            promo.setTextColor(Color.BLACK);
                        }
                        // Code Exist
                        else if (cod.equals(promos.getName()))
                        {
                            // Calc Total Price
                            int total = 0;
                            int di =  Integer.parseInt(promos.getDis());
                            for (Order order:cart)
                                total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
                            int fi = ((total-di) + 30);


                            Locale locale = new Locale("en","Eg");
                            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                            txtTotal.setText(fmt.format(fi));
                            promo.setTextColor(Color.GREEN);

                            Toast.makeText(cart.this, "Promo Applied", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

     /*   shareloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  final PlaceAutocompleteFragment edtAddr = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        // Hide Search Icon Before Fragment
        edtAddr.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
        // Set Hint for AutoComplete
        ((EditText)edtAddr.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Enter your Add.");
        // Set TextSize
        ((EditText)edtAddr.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(14);
        // Get Add from Place AutoComplet
        edtAddr.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
            shipingAddress = place;
                mGoogleMapService.getAdressName(String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=false",
                                                              mLastLocation.getLatitude(),
                                                              mLastLocation.getLongitude())).enqueue(new Callback <String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        // If fetch API ok
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray resultArray = new JSONObject().getJSONArray("results");
                            JSONObject firstObject = resultArray.getJSONObject(0);
                            address = firstObject.getString("formatted_address");
                            // set this add to edtAddr
                            ((EditText)edtAddr.getView().findViewById(R.id.place_autocomplete_search_input)).setText(address);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call <String> call, Throwable t) {
                        Toast.makeText(cart.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Status status) {
                Log.e("ERROR",status.getStatusMessage());
            }
        });

                // Event Listner
                address = shipingAddress.getAddress().toString();

            }
        }); */
            // Alert Dialog Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentuser.getPhone(),
                        Common.currentuser.getName(),
                        edtAddr.getText().toString(),
                        txtTotal.getText().toString(),
                        "0",
                        edtcomm.getText().toString(),
                        promo.getText().toString(),
                        cart);
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                // Delete Cart
                new Database(getBaseContext()).cleancart();
                Toast.makeText(cart.this, "Thank You,Order being Prepared", Toast.LENGTH_SHORT).show();
                finish();

                // Remove Fragment
                //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment)).commit();
            }
        });
        alertDialog.show();

    }

    private void loadlistItem() {

        cart = new Database(this).getCarts();
        adaptar = new CartAdaptar(cart,this);
        adaptar.notifyDataSetChanged();
        recyclerView.setAdapter(adaptar);

        // Calc Total Price
        int total = 0;
        for (Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        int fi = total + 30;


        Locale locale = new Locale("en","Eg");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotal.setText(fmt.format(fi));

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int directions, int postion) {

        if (viewHolder instanceof CartViewHolder)
        {
            String name = ((CartAdaptar)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
            final Order deleteItem = ((CartAdaptar)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            adaptar.removeItem(deleteIndex);
            new Database(getBaseContext()).removefromcart(deleteItem.getProductId(),Common.currentuser.getPhone());


            int total = 0;
            List<Order>orders = new Database(getBaseContext()).getCarts();
            for (Order item : orders)
                total+=(Integer.parseInt(item.getPrice()))*(Integer.parseInt(item.getQuantity()));
            int fi = total + 30;


            Locale locale = new Locale("en","Eg");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
            txtTotal.setText(fmt.format(fi));

            // SnackBar
            Snackbar snackbar = Snackbar.make(rootlayout,name+" Removed from Cart",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptar.restoreItem(deleteItem,deleteIndex);
                    new Database(getBaseContext()).addcart(deleteItem);

                    int total = 0;
                    List<Order>orders = new Database(getBaseContext()).getCarts();
                    for (Order item : orders)
                        total+=(Integer.parseInt(item.getPrice()))*(Integer.parseInt(item.getQuantity()));
                    int fi = total + 30;

                    Locale locale = new Locale("en","Eg");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                    txtTotal.setText(fmt.format(fi));
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deletecart(item.getOrder());
        return true;
    }

    private void deletecart(int order) {
        cart.remove(order);
        new Database(this).cleancart();
        for (Order item:cart)
            new Database(this).addcart(item);
        loadlistItem();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    displayLocation();
    startLocationUpdate();
    }

    private void startLocationUpdate() {
     if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
     {
        return;
     }
     LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,locationRequest,this);
        }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation !=null)
        {
            Log.d("LOCATION","YOUR LOCATION :"+mLastLocation.getLatitude()+","+mLastLocation.getLatitude());
        }
        else
            {
                Log.d("LOCATION","Could not get Location");

            }
    }

    @Override
    public void onConnectionSuspended(int i) {
    mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case LOCATION_REQUEST_CODE:
            {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (CheckPlaayService()) // if have play service on Device
                    {
                        buildGoogleApiClient();
                        createLocationRequest();
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    mLastLocation = location;
    displayLocation();
    }
}
