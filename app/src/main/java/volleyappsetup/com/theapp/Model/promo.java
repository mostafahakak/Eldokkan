package volleyappsetup.com.theapp.Model;

public class promo {
    private String name;
    private String dis;

    public promo() {
    }

    public promo(String name, String dis) {
        this.name = name;
        this.dis = dis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }
}
