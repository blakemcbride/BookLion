package utils;

/**
 * Author: Blake McBride
 * Date: 2/22/23
 */
public class EmailAddress {
    private String name;
    private String address;

    public EmailAddress(String address, String name) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
