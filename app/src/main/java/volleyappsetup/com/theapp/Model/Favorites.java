package volleyappsetup.com.theapp.Model;

public class Favorites {
    private String ItemId;
    private String ItemName;
    private String ItemPrice;
    private String ItemMenuId;
    private String ItemImage;
    private String ItemDiscount;
    private String ItemDescription;
    private String UserPhone;

    public Favorites() {
    }

    public Favorites(String itemId, String itemName, String itemPrice, String itemMenuId, String itemImage, String itemDiscount, String itemDescription, String userPhone) {
        ItemId = itemId;
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemMenuId = itemMenuId;
        ItemImage = itemImage;
        ItemDiscount = itemDiscount;
        ItemDescription = itemDescription;
        UserPhone = userPhone;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemMenuId() {
        return ItemMenuId;
    }

    public void setItemMenuId(String itemMenuId) {
        ItemMenuId = itemMenuId;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getItemDiscount() {
        return ItemDiscount;
    }

    public void setItemDiscount(String itemDiscount) {
        ItemDiscount = itemDiscount;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

}
