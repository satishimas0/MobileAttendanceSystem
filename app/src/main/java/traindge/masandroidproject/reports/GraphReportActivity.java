package traindge.masandroidproject.reports;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import traindge.masandroidproject.R;

import static traindge.masandroidproject.attendance.ViewAttendanceActivity.ATTENDANCE;
import static traindge.masandroidproject.attendance.ViewAttendanceActivity.CLASSES;
import static traindge.masandroidproject.dashboard.StudentDashboard.STUDENTS;
import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_NAME_KEY;
import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_UNIQUE_KEY;
import static traindge.masandroidproject.dashboard.TeacherDashboard.SUBJECT_NAME_KEY;

public class GraphReportActivity extends AppCompatActivity {



    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private String uid;
    private HashMap<String, Integer> monthList;
    private BarChart chart;
    private Toolbar toolbar;
    private ProgressBar pbLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        chart = (BarChart) findViewById(R.id.chart);
        pbLoader = (ProgressBar) findViewById(R.id.pbLoader);
        setSupportActionBar(toolbar);


        if (getIntent().hasExtra(CLASS_UNIQUE_KEY)) {
            final String classId = getIntent().getStringExtra(CLASS_UNIQUE_KEY);
            String className = getIntent().getStringExtra(CLASS_NAME_KEY);
            String subject = getIntent().getStringExtra(SUBJECT_NAME_KEY);
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference(CLASSES).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot teacherClasse : dataSnapshot.getChildren()) {
                            if (teacherClasse.hasChild(classId)) {
                                handleAttendanceRecords(FirebaseDatabase.getInstance().getReference(ATTENDANCE).child(classId));
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(GraphReportActivity.this, "not found any class", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void handleAttendanceRecords(DatabaseReference classRef) {
        final int[] present = {0};
        final int[] absent = {0};
        final int[] leave = {0};
        monthList = new HashMap<String, Integer>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                monthList.clear();
                pbLoader.setVisibility(View.GONE);
                if (dataSnapshot.hasChildren()) {
                    long totalClass = dataSnapshot.getChildrenCount();

                    //tvTotalClasses.setText("Total Classes | " + String.valueOf(totalClass));

                    for (DataSnapshot day : dataSnapshot.getChildren()) {
                        String daykey = day.getKey();
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(Long.parseLong(daykey));
                        int month = cal.get(Calendar.MONTH);
                        present[0]=0;
                        for (DataSnapshot students : day.getChildren()) {
                            String sid = students.getKey();
                            Integer status = students.getValue(Integer.class);
                            if (status == 1) {
                                monthList.put(sid, present[0]++);
                            }
                        }
                    }
                    if (monthList.size() > 0) {
                        displayReportData();
                    } else {
                        Toast.makeText(GraphReportActivity.this, "No record for this class yet", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void displayReportData() {

        final ArrayList<Integer> presents = Collections.list(Collections.enumeration(monthList.values()));
        final ArrayList<String> studentId = Collections.list(Collections.enumeration(monthList.keySet()));
        final ArrayList<String> labels = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("users").child(STUDENTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (String id : studentId) {
                        if (dataSnapshot.hasChild(id)){
                            String name = dataSnapshot.child(id).child("name").getValue(String.class);
                            labels.add(name);
                        }
                    }
                    int count = 0;
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    for (Integer present : presents) {
                        entries.add(new BarEntry(present, count));
                        count++;
                    }


                    BarDataSet dataset = new BarDataSet(entries, "Number of Classes attended");
                    BarData data = new BarData(labels,dataset);
                    chart.setData(data);

                    chart.animateXY(2000, 2000);
                    chart.invalidate();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
