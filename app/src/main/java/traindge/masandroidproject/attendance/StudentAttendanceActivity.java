package traindge.masandroidproject.attendance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import traindge.masandroidproject.R;
import traindge.masandroidproject.dashboard.TeacherDashboard;
import traindge.masandroidproject.models.Attendance;
import traindge.masandroidproject.models.CollegeClass;

import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_UNIQUE_KEY;

public class StudentAttendanceActivity extends AppCompatActivity {
    public static final String ATTENDANCE = "attendance";
    ArraySet<Attendance> attendanceArraySet;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView tvClassNameAtte;
    private TextView tvDateAtte;
    private String uid;
    private DatabaseReference dayRef;
    private RecyclerView rvAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        tvClassNameAtte = (TextView) findViewById(R.id.tvClassNameAtte);
        tvDateAtte = (TextView) findViewById(R.id.tvDateAtte);
        rvAttendance = (RecyclerView) findViewById(R.id.rvAttendance);
        rvAttendance.setHasFixedSize(false);
        rvAttendance.setLayoutManager(new LinearLayoutManager(this));


        if (getIntent().hasExtra(CLASS_UNIQUE_KEY)) {
            String classId = getIntent().getStringExtra(CLASS_UNIQUE_KEY);
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Date date = DateUtil.removeTime(new Date());

            dayRef = FirebaseDatabase.getInstance().getReference(ATTENDANCE).child(classId).child(String.valueOf(date.getTime()));
            DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("classes").child(uid).child(classId);
            loadAttendance(classRef);
        }
    }

    public void loadAttendance(DatabaseReference classRef) {
        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CollegeClass cClass = dataSnapshot.getValue(CollegeClass.class);
                tvClassNameAtte.setText(cClass.getName());
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                tvDateAtte.setText("" + date + "/" + month+1 + "/" + year);
                setUpStudentList(cClass.getStudents());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StudentAttendanceActivity.this, "Could not Load not Data. " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpStudentList(HashMap<String, String> students) {
        final ArrayList<String> items = Collections.list(Collections.enumeration(students.values()));
        final ArrayList<String> keys = Collections.list(Collections.enumeration(students.keySet()));
        AttendanceAdapter adapter = new AttendanceAdapter(this, items, keys);
        rvAttendance.setAdapter(adapter);
    }

    public void saveAttendance() {
        if (attendanceArraySet.isEmpty() && attendanceArraySet.size() < rvAttendance.getAdapter().getItemCount()) {
            Toast.makeText(this, "take attendance of all student", Toast.LENGTH_SHORT).show();
        } else {

            HashMap<String, Integer> data = new HashMap<>();
            for (Attendance attendance : attendanceArraySet) {
                data.put(attendance.getStudentId(), attendance.getStatus());
            }

            dayRef.setValue(data, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Toast.makeText(StudentAttendanceActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(StudentAttendanceActivity.this).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(StudentAttendanceActivity.this, TeacherDashboard.class));
                                finish();
                            }
                        }).setTitle("Completed").setMessage("Todays attendance has been taken! have a nice day").create();
                        dialog.show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attendance_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveAttendance();
                break;
        }
        return true;
    }
    /*recycler adapter*/

    public static class AttendanceHolder extends RecyclerView.ViewHolder {

        private final CheckBox cLeave;
        private final CheckBox cPresent;
        private final CheckBox cAbsent;
        private final TextView tvStudent;

        public AttendanceHolder(View itemView) {
            super(itemView);
            cPresent = (CheckBox) itemView.findViewById(R.id.cPresent);
            cAbsent = (CheckBox) itemView.findViewById(R.id.cAbsent);
            cLeave = (CheckBox) itemView.findViewById(R.id.cLeave);
            tvStudent = (TextView) itemView.findViewById(R.id.tvStudent);
        }
    }

    public static class DateUtil {
        public static Date removeTime(Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }

    }

    class AttendanceAdapter extends RecyclerView.Adapter<AttendanceHolder> {

        private final Context context;
        private final ArrayList<String> studentIds;
        private final ArrayList<String> students;

        public AttendanceAdapter(Context context, ArrayList<String> students, ArrayList<String> studentIds) {
            this.context = context;
            this.studentIds = studentIds;
            this.students = students;
            attendanceArraySet = new ArraySet<>();
        }

        @Override
        public AttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.simple_attendance_layout, parent, false);
            return new AttendanceHolder(view);
        }

        @Override
        public void onBindViewHolder(final AttendanceHolder holder, final int position) {
            final int[] status = new int[1];
            holder.tvStudent.setText(students.get(position));
            holder.cLeave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.cPresent.setChecked(false);
                        holder.cAbsent.setChecked(false);
                    }
                    status[0] = getStatus(holder);
                    attendanceArraySet.add(new Attendance(studentIds.get(position), status[0]));

                }
            });
            holder.cAbsent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.cPresent.setChecked(false);
                        holder.cLeave.setChecked(false);
                    }
                    status[0] = getStatus(holder);
                    attendanceArraySet.add(new Attendance(studentIds.get(position), status[0]));

                }
            });
            holder.cPresent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.cLeave.setChecked(false);
                        holder.cAbsent.setChecked(false);
                    }
                    status[0] = getStatus(holder);
                    attendanceArraySet.add(new Attendance(studentIds.get(position), status[0]));

                }
            });
        }

        private int getStatus(AttendanceHolder holder) {
            int status;
            if (holder.cPresent.isChecked()) {
                status = 1;
            } else if (holder.cAbsent.isChecked()) {
                status = 2;
            } else {
                status = 3;
            }
            return status;
        }

        @Override
        public int getItemCount() {
            return studentIds.size();
        }
    }
}
