package booklion.client.login;

import booklion.client.utils.StandardReturn;

/**
 * @author Blake McBride
 * Date: 2/1/14
 */
public class UserData extends StandardReturn {
    private String uuid;
    private String supervisor;

    public UserData() {
    }

    public UserData(String msg) {
        super(msg);
    }

    public UserData(int ec, String msg) {
        super(ec, msg);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    static String part3() {
        return "od6";
    }
}
