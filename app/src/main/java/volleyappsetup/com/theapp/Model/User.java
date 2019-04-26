package volleyappsetup.com.theapp.Model;

public class User {
    private String name;
    private String phone;

    public User() {
    }

    public User(String name, String password,String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
