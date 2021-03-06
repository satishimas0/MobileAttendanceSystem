package traindge.masandroidproject.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import traindge.masandroidproject.attendance.ViewAttendanceActivity;
import traindge.masandroidproject.models.CollegeClass;
import traindge.masandroidproject.notice.NoticeMessageActivity;

import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_UNIQUE_KEY;

public class ParentDashboard extends AppCompatActivity {

    public static final String STUDENTS = "students";
    private String uid;
    HashMap<String,CollegeClass> stdClasses;
    private FirebaseRecyclerAdapter<CollegeClass, StudentDashboard.CollegeClassHolder> mAdapter;
    private RecyclerView rvClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        stdClasses = new HashMap<>();
        rvClassList = (RecyclerView) findViewById(R.id.rvClassList);
        rvClassList.setHasFixedSize(false);
        rvClassList.setLayoutManager(new LinearLayoutManager(this));
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("wait...");
        dialog.show();
        //loadClassData(dialog);
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    String studentName = dataSnapshot.child("parent").child(uid).child("student").getValue(String.class);
                    for (DataSnapshot snapshot : dataSnapshot.child(STUDENTS).getChildren()) {
                        if (snapshot.child("name").getValue(String.class).equals(studentName)){
                            String studentID = snapshot.getKey();
                            loadClassData(dialog,studentID);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadClassData(final ProgressDialog dialog, final String studentID) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("classes");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();
                handleLoadedClasses(dataSnapshot,studentID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(ParentDashboard.this, databaseError != null ? databaseError.getMessage() : "cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        rvClassList.setAdapter(mAdapter);
    }

    private void handleLoadedClasses(DataSnapshot dataSnapshot, String studentID) {
        for (DataSnapshot teacherClasses : dataSnapshot.getChildren()) {
            for (DataSnapshot classSnap : teacherClasses.getChildren()) {
                if (classSnap.child(STUDENTS).hasChild(studentID)) {
                    stdClasses.put(classSnap.getKey(),classSnap.getValue(CollegeClass.class));
                }
            }
        }
        if (stdClasses.size() > 0) {
            setupAdapter(stdClasses);
        }
    }

    private void setupAdapter(HashMap<String, CollegeClass> stdClasses) {
        ParentDashboard.StudentClassAdapter adapter = new ParentDashboard.StudentClassAdapter(stdClasses);
        rvClassList.setAdapter(adapter);
    }

    class StudentClassAdapter extends RecyclerView.Adapter<StudentDashboard.CollegeClassHolder> {


        private final ArrayList<CollegeClass> list;
        private final ArrayList<String> keys;

        public StudentClassAdapter(HashMap<String, CollegeClass> stdClasses) {
            list = Collections.list(Collections.enumeration(stdClasses.values()));
            keys = Collections.list(Collections.enumeration(stdClasses.keySet()));


        }

        @Override
        public StudentDashboard.CollegeClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StudentDashboard.CollegeClassHolder(LayoutInflater.from(ParentDashboard.this).inflate(R.layout.simple_student_class_item,parent,false));
        }

        @Override
        public void onBindViewHolder(StudentDashboard.CollegeClassHolder holder, final int position) {
            final CollegeClass model = list.get(position);
            holder.setClassName(model.getName());
            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ParentDashboard.this, ViewAttendanceActivity.class);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.notices:
                startActivity(new Intent(this, NoticeMessageActivity.class));
        }
        return true;
    }

}
