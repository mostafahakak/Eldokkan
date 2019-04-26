package volleyappsetup.com.theapp.Model;

public class Populerty {
    private String Image;
    private String Name;
    private String Price;
    private String Populerty;

    public Populerty() {
    }

    public Populerty(String image, String name, String price, String populerty) {
        Image = image;
        Name = name;
        Price = price;
        Populerty = populerty;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPopulerty() {
        return Populerty;
    }

    public void setPopulerty(String populerty) {
        Populerty = populerty;
    }
}
