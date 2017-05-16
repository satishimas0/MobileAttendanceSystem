package traindge.masandroidproject.models;

/**
 * Created by DELL on 5/14/2017.
 */

public class Parent {

    String college;
    String email;
    String mobile;
    String parent;
    String password;
    String student;

    public Parent() {
    }

    public Parent(String college, String email, String mobile, String parent, String password, String student) {
        this.college = college;
        this.email = email;
        this.mobile = mobile;
        this.parent = parent;
        this.password = password;
        this.student = student;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
}
