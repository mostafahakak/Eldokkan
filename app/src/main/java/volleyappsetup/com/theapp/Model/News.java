package volleyappsetup.com.theapp.Model;

public class News {

    private String Image;
    private String Title;
    private String Body;

    public News() {
    }

    public News(String image, String title, String body) {
        Image = image;
        Title = title;
        Body = body;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }
}
