package volleyappsetup.com.theapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import volleyappsetup.com.theapp.Model.Favorites;
import volleyappsetup.com.theapp.Model.Order;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "ItemDB.db";
    private static final int DB_VER = 3;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    public List<Order> getCarts()
    {
      SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductName","ProductId","Quantity","Price","Discount","Image"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result = new ArrayList <>();
        if (c.moveToFirst())
        {
            do {
                result.add(new Order(
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount")),
                        c.getString(c.getColumnIndex("Image"))
                ));
            }
            while (c.moveToNext());
        }
        return result;
    }
    public void addcart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format
                ("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount,Image)VALUES('%s','%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                 order.getImage());
        db.execSQL(query);
    }
    public void cleancart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

    public void removefromcart(String productId, String phone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE ProductId='%s'",productId);
        db.execSQL(query);
    }
    //Fav
    public void addToFav(Favorites item){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites("+
                "ItemId,ItemName,ItemPrice,ItemMenuId,ItemImage,ItemDiscount,ItemDescription,UserPhone)"+
                "VALUES('%s','%s','%s','%s','%s','%s','%s','%s');",item.getItemId(),item.getItemName(),item.getItemPrice(),
                item.getItemMenuId(),item.getItemImage(),item.getItemDiscount(),item.getItemDescription(),item.getUserPhone());
                db.execSQL(query);
    }
    public void removeFav(String Itemid, String phone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE Itemid='%s';",Itemid);
        db.execSQL(query);
    }
    public boolean isFav(String Itemid){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE Itemid='%s';",Itemid);
        Cursor c = db.rawQuery(query,null);
        if (c.getCount() <= 0)
        {
            c.close();
            return false;
        }
        c.close();
        return true;

    }

    public List<Favorites> getAllFav(String userPhone)
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone","ItemId","ItemName","ItemPrice","ItemMenuId","ItemImage","ItemDiscount","ItemDescription"};
        String sqlTable = "Favorites";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,"UserPhone=?",new String[]{userPhone},null,null,null);

        final List<Favorites> result = new ArrayList <>();
        if (c.moveToFirst())
        {
            do {
                result.add(new Favorites(
                        c.getString(c.getColumnIndex("Itemid")),
                        c.getString(c.getColumnIndex("ItemName")),
                        c.getString(c.getColumnIndex("ItemImage")),
                        c.getString(c.getColumnIndex("ItemMenuId")),
                        c.getString(c.getColumnIndex("ItemPrice")),
                        c.getString(c.getColumnIndex("ItemDiscount")),
                        c.getString(c.getColumnIndex("ItemDescription")),
                        c.getString(c.getColumnIndex("UserPhone"))
                ));
            }
            while (c.moveToNext());
        }
        return result;
    }


    public int getcountcart() {
        int count=0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail");
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst())
        {
            do {
                count = c.getInt(0);
            }while (c.moveToNext());
        }
        return count;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }
}


