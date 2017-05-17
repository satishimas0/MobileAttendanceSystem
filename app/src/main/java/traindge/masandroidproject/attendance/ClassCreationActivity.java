package traindge.masandroidproject.attendance;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import traindge.masandroidproject.R;
import traindge.masandroidproject.models.CollegeClass;
import traindge.masandroidproject.models.Student;

public class ClassCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etClass;
    private EditText etSubject;
    private EditText etBatch;
    private EditText etClassDur;
    private EditText etYearSem;
    private Button btnClassSystmContinue;

    private Button btnChooseStudents;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private HashMap<String, String> allStudents;
    private SpinnerDialog spinnerDialog;
    private HashMap<String, String> selectedStudentList;
    private EditText getTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        //create object
        etClass = (EditText) findViewById(R.id.etClass);
        etYearSem = (EditText) findViewById(R.id.etYearSem);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etBatch = (EditText) findViewById(R.id.etBatch);
        getTime = (EditText) findViewById(R.id.etTime);

        etClassDur = (EditText) findViewById(R.id.etClassDuration);
        btnClassSystmContinue = (Button) findViewById(R.id.btnClassSystmContinue);

        btnChooseStudents = (Button) findViewById(R.id.btnChooseStudents);
        btnClassSystmContinue.setOnClickListener(this);
        btnChooseStudents.setOnClickListener(this);

        loadListOfStudents();

        selectedStudentList = new HashMap<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClassSystmContinue:
                saveClassToFirebase();
                /*Intent subIntent = new Intent(ClassCreationActivity.this, StudentAttendanceActivity.class);
                startActivity(subIntent);*/
                break;
            case R.id.btnChooseStudents:
                spinnerDialog.showSpinerDialog();
                break;
        }
    }

    private void saveClassToFirebase() {
        String batch = etBatch.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String Class = etClass.getText().toString().trim();
        String classdur = etClassDur.getText().toString().trim();
        String yearsem = etYearSem.getText().toString().trim();
        String time = getTime.getText().toString().trim();
        if (selectedStudentList.isEmpty()) {
            Toast.makeText(this, "add student to class", Toast.LENGTH_SHORT).show();
        } else {
            if (batch.isEmpty() || subject.isEmpty() || Class.isEmpty() || classdur.isEmpty() || yearsem.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("classes").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            CollegeClass newClass = new CollegeClass(
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    selectedStudentList,
                    subject,
                    Class,
                    batch,
                    time,
                    yearsem
            );
            mRef.push().setValue(newClass, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        Toast.makeText(ClassCreationActivity.this, "success fully added new class", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }

    private void loadListOfStudents() {
        allStudents = new HashMap<>();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("loading student list");
        dialog.setCancelable(false);
        dialog.show();
        FirebaseDatabase.getInstance().getReference("users").child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allStudents.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Student student = snapshot.getValue(Student.class);
                        allStudents.put(snapshot.getKey(), student.getName());
                    }

                    final ArrayList<String> items = Collections.list(Collections.enumeration(allStudents.values()));
                    final ArrayList<String> keys = Collections.list(Collections.enumeration(allStudents.keySet()));
                    spinnerDialog = new SpinnerDialog(ClassCreationActivity.this, items, "Select or Search student");// With 	Animation

                    spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                            selectedStudentList.put(keys.get(position), items.get(position));
                        }
                    });

                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ClassCreationActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
