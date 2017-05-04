package traindge.masandroidproject;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by DELL on 5/4/2017.
 */

class StudentModel {

    private final String key;
    private final String name;
    private final String parent;
    private final String className;
    private final String mobile;
    private final String email;
    private final String college;
    private final String password;

    public StudentModel(DataSnapshot snapshot) {
        key = snapshot.getKey();
        name = snapshot.child("name").getValue(String.class);
        parent=snapshot.child("parentname").getValue(String.class);
        className = snapshot.child("class").getValue(String.class);
        mobile = snapshot.child("mobile").getValue(String.class);
        email = snapshot.child("email").getValue(String.class);
        college = snapshot.child("college").getValue(String.class);
        password = snapshot.child("password").getValue(String.class);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getCollege() {
        return college;
    }

    public String getPassword() {
        return password;
    }
}
