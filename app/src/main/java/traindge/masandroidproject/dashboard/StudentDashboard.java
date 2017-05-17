package traindge.masandroidproject.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import traindge.masandroidproject.R;
import traindge.masandroidproject.attendance.StudentAttendanceActivity;
import traindge.masandroidproject.attendance.ViewAttendanceActivity;
import traindge.masandroidproject.models.CollegeClass;

import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_UNIQUE_KEY;

public class StudentDashboard extends AppCompatActivity {

    public static final String STUDENTS = "students";
    private String uid;
    HashMap<String,CollegeClass> stdClasses;
    private FirebaseRecyclerAdapter<CollegeClass, StudentDashboard.CollegeClassHolder> mAdapter;
    private RecyclerView rvClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        stdClasses = new HashMap<>();
        rvClassList = (RecyclerView) findViewById(R.id.rvClassList);
        rvClassList.setHasFixedSize(false);
        rvClassList.setLayoutManager(new LinearLayoutManager(this));
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("wait...");
        dialog.show();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("classes");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();
                handleLoadedClasses(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(StudentDashboard.this, databaseError != null ? databaseError.getMessage() : "cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        rvClassList.setAdapter(mAdapter);
    }

    private void handleLoadedClasses(DataSnapshot dataSnapshot) {
        for (DataSnapshot teacherClasses : dataSnapshot.getChildren()) {
            for (DataSnapshot classSnap : teacherClasses.getChildren()) {
                if (classSnap.child(STUDENTS).hasChild(uid)) {
                    stdClasses.put(classSnap.getKey(),classSnap.getValue(CollegeClass.class));
                }
            }
        }
        if (stdClasses.size() > 0) {
            setupAdapter(stdClasses);
        }
    }

    private void setupAdapter(HashMap<String, CollegeClass> stdClasses) {
        StudentClassAdapter adapter = new StudentClassAdapter(stdClasses);
        rvClassList.setAdapter(adapter);
    }

    class StudentClassAdapter extends RecyclerView.Adapter<CollegeClassHolder> {


        private final ArrayList<CollegeClass> list;
        private final ArrayList<String> keys;

        public StudentClassAdapter(HashMap<String, CollegeClass> stdClasses) {
            list = Collections.list(Collections.enumeration(stdClasses.values()));
            keys = Collections.list(Collections.enumeration(stdClasses.keySet()));


        }

        @Override
        public CollegeClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CollegeClassHolder(LayoutInflater.from(StudentDashboard.this).inflate(R.layout.simple_student_class_item,parent,false));
        }

        @Override
        public void onBindViewHolder(CollegeClassHolder holder, final int position) {
            final CollegeClass model = list.get(position);
            holder.setClassName(model.getName());
            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudentDashboard.this, ViewAttendanceActivity.class);
                    intent.putExtra(CLASS_UNIQUE_KEY, keys.get(position));
                    startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public static class CollegeClassHolder extends RecyclerView.ViewHolder {

        public final TextView mClassName;
        public View row;

        public CollegeClassHolder(View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.llwrapper);
            mClassName = (TextView) itemView.findViewById(R.id.tvClass);
        }

        public void setClassName(String name) {
            mClassName.setText(name);
        }

    }
}
