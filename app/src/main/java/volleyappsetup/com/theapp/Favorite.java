package volleyappsetup.com.theapp;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import volleyappsetup.com.theapp.Database.Database;
import volleyappsetup.com.theapp.Helper.ReceyclerItemTouchHelper;
import volleyappsetup.com.theapp.Interface.ReceyclerItemTouchHelperListner;
import volleyappsetup.com.theapp.Model.Favorites;
import volleyappsetup.com.theapp.Model.Order;
import volleyappsetup.com.theapp.ViewHolder.FavAdaptar;
import volleyappsetup.com.theapp.ViewHolder.FavViewHolder;
import volleyappsetup.com.theapp.commen.Common;

public class Favorite extends AppCompatActivity implements ReceyclerItemTouchHelperListner {

    RecyclerView recyc_item;
    RecyclerView.LayoutManager layoutManagers;

    FavAdaptar adaptars;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);

        recyc_item = (RecyclerView) findViewById(R.id.recycler_fav);
        recyc_item.setHasFixedSize(true);
        layoutManagers = new LinearLayoutManager(this);
        recyc_item.setLayoutManager(layoutManagers);

        // Swipe To Delete
        ItemTouchHelper.SimpleCallback itemTouchHelper = new ReceyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyc_item);

        loadFav();
    }

    private void loadFav() {

        adaptars = new FavAdaptar(this,new Database(this).getAllFav(Common.currentuser.getPhone()));
        recyc_item.setAdapter(adaptars);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int directions, int postion) {

        if (viewHolder instanceof FavViewHolder)
        {
            String name = ((FavAdaptar)recyc_item.getAdapter()).getItem(postion).getItemName();

            final Favorites deletItem = ((FavAdaptar)recyc_item.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deletIndex = viewHolder.getAdapterPosition();

            adaptars.removeItem(viewHolder.getAdapterPosition());
            new Database(getBaseContext()).removeFav(deletItem.getItemId(),Common.currentuser.getPhone());


            // SnackBar
            Snackbar snackbar = Snackbar.make(rootLayout,name+" Removed from Fav",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptars.restoreItem(deletItem,deletIndex);
                    new Database(getBaseContext()).addToFav(deletItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
}
