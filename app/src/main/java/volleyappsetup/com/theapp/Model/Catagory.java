package volleyappsetup.com.theapp.Model;

public class Catagory {
    private String Name;
    private String Image;

    public Catagory() {
    }

    public Catagory(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
