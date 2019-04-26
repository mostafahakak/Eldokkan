package volleyappsetup.com.theapp.Model;

public class newsmodel {
    private String image;
    private String news;
    private String title;

    public newsmodel() {
    }

    public newsmodel(String image, String news, String title) {
        this.image = image;
        this.news = news;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
