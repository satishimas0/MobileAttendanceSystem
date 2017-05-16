package traindge.masandroidproject.models;

/**
 * Created by DELL on 5/14/2017.
 */
public class Teacher {

    private String name;
    private String college;
    private String email;
    private String mobile;
    private String qualification;
    private String password;

    public Teacher() {
        // Needed for Firebase
    }

    public Teacher(String name, String college, String email, String mobile, String qualification, String password) {
        this.name = name;
        this.college = college;
        this.email = email;
        this.mobile = mobile;
        this.qualification = qualification;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
