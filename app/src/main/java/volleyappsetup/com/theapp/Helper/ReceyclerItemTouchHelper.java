package volleyappsetup.com.theapp.Helper;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import volleyappsetup.com.theapp.Interface.ReceyclerItemTouchHelperListner;
import volleyappsetup.com.theapp.ViewHolder.CartViewHolder;
import volleyappsetup.com.theapp.ViewHolder.FavViewHolder;

public class ReceyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ReceyclerItemTouchHelperListner listener;

    public ReceyclerItemTouchHelper(int dragDirs, int swipeDirs, ReceyclerItemTouchHelperListner listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (listener != null)
            listener.onSwiped(viewHolder,i,viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        if(viewHolder instanceof CartViewHolder) {
            View forgroundView = ((CartViewHolder) viewHolder).view_forg;
            getDefaultUIUtil().clearView(forgroundView);
        }
        else if (viewHolder instanceof FavViewHolder)
        {
            View forgroundView = ((FavViewHolder) viewHolder).view_forge;
            getDefaultUIUtil().clearView(forgroundView);
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
        if(viewHolder instanceof CartViewHolder) {

            View forgroundView = ((CartViewHolder) viewHolder).view_forg;
            getDefaultUIUtil().onDraw(c, recyclerView, forgroundView, dX, dY, actionState, isCurrentlyActive);
        }
        else if (viewHolder instanceof FavViewHolder)
        {
            View forgroundView = ((FavViewHolder) viewHolder).view_forge;
            getDefaultUIUtil().onDraw(c, recyclerView, forgroundView, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null){

            if(viewHolder instanceof CartViewHolder) {


                View forgroundView = ((CartViewHolder) viewHolder).view_forg;
                getDefaultUIUtil().onSelected(forgroundView);
            }
            else if (viewHolder instanceof FavViewHolder)
            {
                View forgroundView = ((FavViewHolder) viewHolder).view_forge;
                getDefaultUIUtil().onSelected(forgroundView);
            }

        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if(viewHolder instanceof CartViewHolder) {

            View forgroundView = ((CartViewHolder) viewHolder).view_forg;
            getDefaultUIUtil().onDrawOver(c, recyclerView, forgroundView, dX, dY, actionState, isCurrentlyActive);
        }
        else if (viewHolder instanceof FavViewHolder){
            View forgroundView = ((FavViewHolder) viewHolder).view_forge;
        getDefaultUIUtil().onDrawOver(c, recyclerView, forgroundView, dX, dY, actionState, isCurrentlyActive);

    }
    }
}


