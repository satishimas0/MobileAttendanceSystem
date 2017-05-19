package traindge.masandroidproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import traindge.masandroidproject.R;
import traindge.masandroidproject.dashboard.StudentDashboard;
import traindge.masandroidproject.dashboard.TeacherDashboard;
import traindge.masandroidproject.login.LoginActivity;
import traindge.masandroidproject.models.Parent;
import traindge.masandroidproject.models.Student;
import traindge.masandroidproject.models.Teacher;

import static traindge.masandroidproject.login.LoginActivity.APP_PREF;
import static traindge.masandroidproject.login.LoginActivity.TYPE;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private TextView tvUidProfile;
    private TextView tvEmailProfile;
    private TextView tvNameProfile;
    private String type;
    private DatabaseReference ref;
    private TextView tvClass;
    private TextView tvCollege;
    private TextView tvStudent;
    private TextView tvQualification;
    private TextView tvParent;
    private TextView tvMobile;
    private TextView tvPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindViews();

        setupFirebase();
    }

    public void manageTypeBasedUI(View view) {
        if (type.equals("students")) {
            gotoStudentAttendanceReport();
        } else if (type.equals("teachers")) {
            gotoTeacherDashboard();
        } else {
            gotoParentDashboard();
        }
    }

    private void gotoParentDashboard() {

    }

    private void gotoTeacherDashboard() {
        startActivity(new Intent(this, TeacherDashboard.class));
    }


    private void gotoStudentAttendanceReport() {
        startActivity(new Intent(this, StudentDashboard.class));
    }

    private void setupFirebase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("users");
        type = getSharedPreferences(APP_PREF, MODE_PRIVATE).getString(TYPE, "students");
        getSupportActionBar().setTitle(type);
        switch (type) {
            case "students":
                loadStudentData();
                break;
            case "teachers":
                loadTeacherData();
                break;
            default:
                loadParentData();
                break;
        }
        tvUidProfile.setText(currentUser.getUid());
        tvEmailProfile.setText(currentUser.getEmail());


    }

    private void bindViews() {
        tvUidProfile = (TextView) findViewById(R.id.tvUidProfile);
        tvEmailProfile = (TextView) findViewById(R.id.tvEmailProfile);
        tvNameProfile = (TextView) findViewById(R.id.tvNameProfile);
        tvClass = (TextView) findViewById(R.id.tvClass);
        tvCollege = (TextView) findViewById(R.id.tvCollege);
        tvStudent = (TextView) findViewById(R.id.tvStudentName);
        tvQualification = (TextView) findViewById(R.id.tvQualification);
        tvParent = (TextView) findViewById(R.id.tvParent);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
    }


    private void loadTeacherData() {
        String uid = currentUser.getUid();
        ref.child(type).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Teacher teacher = dataSnapshot.getValue(Teacher.class);
                    tvNameProfile.setText(teacher.getName());
                    tvCollege.setText(teacher.getCollege());
                    tvMobile.setText(teacher.getMobile());
                    tvPassword.setText(teacher.getPassword());
                    tvQualification.setText(teacher.getQualification());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "could not load data from firebase", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadStudentData() {
        String uid = currentUser.getUid();
        ref.child(type).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Student student = dataSnapshot.getValue(Student.class);
                    tvNameProfile.setText(student.getName());
                    tvCollege.setText(student.getCollege());
                    tvMobile.setText(student.getMobile());
                    tvPassword.setText(student.getPassword());
                    tvClass.setText(student.getClassName());
                    tvParent.setText(student.getParent());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "could not profile details of student", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void loadParentData() {
        String uid = currentUser.getUid();
        ref.child(type).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Parent parent = dataSnapshot.getValue(Parent.class);
                    tvNameProfile.setText(parent.getParent());
                    tvCollege.setText(parent.getCollege());
                    tvMobile.setText(parent.getMobile());
                    tvPassword.setText(parent.getPassword());
                    tvStudent.setText(parent.getStudent());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "could not profile details of parent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                getSharedPreferences(APP_PREF, MODE_PRIVATE).edit().clear().apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }
        return true;
    }
}
