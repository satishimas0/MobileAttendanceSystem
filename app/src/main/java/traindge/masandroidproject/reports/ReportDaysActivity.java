package traindge.masandroidproject.reports;

import android.os.Bundle;
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
import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_UNIQUE_KEY;

public class ReportDaysActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_report_days);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        chart = (BarChart) findViewById(R.id.chart);
        pbLoader = (ProgressBar) findViewById(R.id.pbLoader);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        if (getIntent().hasExtra(CLASS_UNIQUE_KEY)) {
            final String classId = getIntent().getStringExtra(CLASS_UNIQUE_KEY);
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
                        Toast.makeText(ReportDaysActivity.this, "not found any class", Toast.LENGTH_SHORT).show();
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
        monthList = new HashMap<String, Integer>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            //monthList.add(new Day(dayOfMonth, status));
                            switch (month) {
                                case 0:
                                    monthList.put("jan", present[0]);
                                    break;
                                case 1:
                                    monthList.put("feb", present[0]);
                                    break;
                                case 2:
                                    monthList.put("mar", present[0]);
                                    break;
                                case 3:
                                    monthList.put("apr", present[0]);
                                    break;
                                case 4:
                                    monthList.put("may", present[0]);
                                    break;
                                case 5:
                                    monthList.put("jun", present[0]);
                                    break;
                                case 6:
                                    monthList.put("jul", present[0]);
                                    break;
                                case 7:
                                    monthList.put("aug", present[0]);
                                    break;
                                case 8:
                                    monthList.put("sep", present[0]);
                                    break;
                                case 9:
                                    monthList.put("oct", present[0]);
                                    break;
                                case 10:
                                    monthList.put("nov", present[0]);
                                    break;
                                case 11:
                                    monthList.put("dec", present[0]);
                                    break;

                            }
                        }
                    }
                    if (monthList.size() > 0) {
                        displayReportData();
                    } else {
                        Toast.makeText(ReportDaysActivity.this, "Student have no attendance to be recorded", Toast.LENGTH_SHORT).show();
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
        ArrayList<BarEntry> entries = new ArrayList<>();
        final ArrayList<Integer> presents = Collections.list(Collections.enumeration(monthList.values()));
        final ArrayList<String> labels = Collections.list(Collections.enumeration(monthList.keySet()));
        int count = 0;
        for (Integer present : presents) {
            entries.add(new BarEntry(present, count));
            count++;
        }


        BarDataSet dataset = new BarDataSet(entries, "Number of Classes attended");
        BarData data = new BarData(labels,dataset);
        chart.setData(data);
        chart.setDescription("attendance of "+mAuth.getCurrentUser().getEmail());
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

}

