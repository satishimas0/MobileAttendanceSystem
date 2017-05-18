package traindge.masandroidproject.attendance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import traindge.masandroidproject.R;
import traindge.masandroidproject.dashboard.StudentDashboard;
import traindge.masandroidproject.models.Attendance;
import traindge.masandroidproject.models.CollegeClass;
import traindge.masandroidproject.models.Day;
import traindge.masandroidproject.reports.ReportDaysActivity;

import static traindge.masandroidproject.dashboard.StudentDashboard.STUDENTS;
import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_UNIQUE_KEY;

public class ViewAttendanceActivity extends AppCompatActivity implements View.OnClickListener {

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
    private Button btnReport;
    private RecyclerView rvDayList;
    ArrayList<Day> daysList;

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
        btnReport = (Button) findViewById(R.id.btnViewReport);
        rvDayList = (RecyclerView) findViewById(R.id.rvDayList);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvDayList.setHasFixedSize(false);
        rvDayList.setLayoutManager(new GridLayoutManager(this, 6));
        btnReport.setOnClickListener(this);

        daysList = new ArrayList<>();
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
                daysList.clear();
                if (dataSnapshot.hasChildren()) {
                    long totalClass = dataSnapshot.getChildrenCount();
                    tvTotalClasses.setText("Total Classes | " + String.valueOf(totalClass));

                    for (DataSnapshot day : dataSnapshot.getChildren()) {
                        String daykey = day.getKey();
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(Long.parseLong(daykey));
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
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
                            daysList.add(new Day(dayOfMonth, status));
                        }
                    }
                    if (daysList.size() > 0) {
                        rvDayList.setAdapter(new AttendanceAdapter(daysList));
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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ViewAttendanceActivity.this, ReportDaysActivity.class);
        intent.putExtra(CLASS_UNIQUE_KEY, getIntent().getStringExtra(CLASS_UNIQUE_KEY));
        startActivity(intent);

    }

    public static class AttendanceHolder extends RecyclerView.ViewHolder {

        CheckBox cbDate;

        public AttendanceHolder(View itemView) {
            super(itemView);
            cbDate = (CheckBox) itemView.findViewById(R.id.cbDate);
        }

        public void setClassName(int name) {
            cbDate.setText(name+"");
        }

        public void setChecked(boolean check) {
            cbDate.setChecked(check);
        }


    }


    private class AttendanceAdapter extends RecyclerView.Adapter<AttendanceHolder> {

        private final ArrayList<Day> daysList;

        public AttendanceAdapter(ArrayList<Day> daysList) {
            this.daysList = daysList;
        }

        @Override
        public AttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ViewAttendanceActivity.this).inflate(R.layout.simple_calendar_view, parent, false);
            return new AttendanceHolder(view);
        }

        @Override
        public void onBindViewHolder(AttendanceHolder holder, int position) {
            Day day = daysList.get(position);
            holder.cbDate.setClickable(false);
            holder.setClassName(day.getDay());
            switch (day.getState()) {
                case 1:
                    holder.setChecked(true);
                    break;
                case 2:
                    holder.setChecked(true);
                    break;
                case 3:
                    holder.cbDate.setBackgroundColor(Color.GRAY);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return daysList.size();
        }
    }
}
