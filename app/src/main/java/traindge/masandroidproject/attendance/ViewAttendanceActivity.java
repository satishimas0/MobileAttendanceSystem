package traindge.masandroidproject.attendance;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import traindge.masandroidproject.models.CollegeClass;

import static traindge.masandroidproject.dashboard.StudentDashboard.STUDENTS;
import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_UNIQUE_KEY;

public class ViewAttendanceActivity extends AppCompatActivity {

    public static final String CLASSES = "classes";
    public static final String ATTENDANCE = "attendance";
    private Toolbar toolbar;
    private TextView tvClassName;
    private TextView tvStudent;
    private TextView tvSubject;
    private TextView tvTotalClasses;
    private TextView tvTiming;
    private TextView tvClassesAttended;
    private TextView tvAbsent;
    private FirebaseUser currentUser;
    private String uid;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        bindViews();
        initViews();
        initFirebase();
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvClassName = (TextView) findViewById(R.id.tvClassName);
        tvStudent = (TextView) findViewById(R.id.tvStudent);
        tvSubject = (TextView) findViewById(R.id.tvSubject);
        tvTotalClasses = (TextView) findViewById(R.id.tvTotalClasses);
        tvTiming = (TextView) findViewById(R.id.tvTiming);
        tvClassesAttended = (TextView) findViewById(R.id.tvClassesAttended);
        tvAbsent = (TextView) findViewById(R.id.tvAbsent);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFirebase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        email = currentUser.getEmail();
        FirebaseDatabase.getInstance().getReference(STUDENTS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    tvStudent.setText(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (getIntent().hasExtra(CLASS_UNIQUE_KEY)) {
            final String classId = getIntent().getStringExtra(CLASS_UNIQUE_KEY);
            FirebaseDatabase.getInstance().getReference(CLASSES).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot teacherClasse : dataSnapshot.getChildren()) {
                            if (teacherClasse.hasChild(classId)) {
                                CollegeClass collegeClass = teacherClasse.child(classId).getValue(CollegeClass.class);
                                tvClassName.setText(collegeClass.getName());
                                tvTiming.setText("timings | " + collegeClass.getTime());
                                tvSubject.setText("Subject | " + collegeClass.getSubject());
                                handleAttendanceRecords(FirebaseDatabase.getInstance().getReference(ATTENDANCE).child(classId));
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(ViewAttendanceActivity.this, "no found any class", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private void handleAttendanceRecords(DatabaseReference ref) {
        final int[] present = {0};
        final int[] absent = {0};
        final int[] leave = {0};
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    long totalClass = dataSnapshot.getChildrenCount();
                    tvTotalClasses.setText("Total Classes | "+String.valueOf(totalClass));
                    for (DataSnapshot day : dataSnapshot.getChildren()) {
                        if (day.hasChild(uid)) {
                            int status = day.child(uid).getValue(Integer.class);
                            switch (status) {
                                case 1:
                                    present[0]++;
                                    break;
                                case 2:
                                    absent[0]++;
                                    break;
                                default:
                                    leave[0]++;
                                    break;
                            }
                        }

                    }
                    tvClassesAttended.setText("Attended | " + present[0]);
                    tvAbsent.setText("Absent | " + absent[0]);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
