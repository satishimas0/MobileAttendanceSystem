package traindge.masandroidproject.models;

/**
 * Created by DELL on 5/4/2017.
 */

public class Student {

    private String name;
    private String parent;
    private String className;
    private String mobile;
    private String email;
    private String college;
    private String password;

    public Student() {
    }

    public Student(String name, String parent, String className, String mobile, String email, String college, String password) {
        this.name = name;
        this.parent = parent;
        this.className = className;
        this.mobile = mobile;
        this.email = email;
        this.college = college;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
