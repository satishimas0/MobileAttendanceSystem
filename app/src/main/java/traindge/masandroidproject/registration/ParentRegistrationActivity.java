package traindge.masandroidproject.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import traindge.masandroidproject.R;
import traindge.masandroidproject.models.Student;
import traindge.masandroidproject.reports.ReportDaysActivity;


public class ParentRegistrationActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, View.OnClickListener {

    public static final String TAG = "parentRegistration";
    private EditText etParentName;
    private EditText etStudentName;
    private EditText etParentEmail;
    private EditText etClgName;
    private EditText etParentPassword;
    private Button btnParentSubmit;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Task<AuthResult> authResultTask;
    private OnCompleteListener<AuthResult> task;
    private EditText etParentMobile;
    private ArrayList<Student> studentlist;
    private int found = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //create object
        etParentName = (EditText) findViewById(R.id.etParentName);
        etStudentName = (EditText) findViewById(R.id.etStudentName);
        etParentMobile = (EditText) findViewById(R.id.etParentMobile);
        etParentEmail = (EditText) findViewById(R.id.etParentEmail);
        etClgName = (EditText) findViewById(R.id.etClgName);
        etParentPassword = (EditText) findViewById(R.id.etParentPassword);
        btnParentSubmit = (Button) findViewById(R.id.btnParentSubmit);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = this;


        btnParentSubmit.setOnClickListener(this);
        loadStudentsList();
    }

    private void loadStudentsList() {
        studentlist = new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("loading student list");
        dialog.setCancelable(false);
        dialog.show();
        FirebaseDatabase.getInstance().getReference("users").child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentlist.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        studentlist.add(dataSnapshot.getValue(Student.class));
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ParentRegistrationActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //user is signed in
            Intent subIntent = new Intent(ParentRegistrationActivity.this, ReportDaysActivity.class);
            startActivity(subIntent);
            finish();


        } else {
            //user is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
        final String parentname = etParentName.getText().toString();
        final String email = etParentEmail.getText().toString();
        final String mobile = etParentMobile.getText().toString();
        final String studentname = etStudentName.getText().toString();
        final String password = etParentPassword.getText().toString();
        final String submit = btnParentSubmit.getText().toString();
        final String college = etClgName.getText().toString();
        // TODO: 5/14/2017 find student
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("please wait while we update");
        dialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        dialog.setMessage("updating database");

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(ParentRegistrationActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        if (task.isSuccessful()) {
                            dialog.dismiss();


                            String uid = task.getResult().getUser().getUid();
                            HashMap<String, String> usermap = new HashMap<String, String>();
                            usermap.put("parent", parentname);
                            usermap.put("student", studentname);
                            usermap.put("mobile", mobile);
                            usermap.put("email", email);
                            usermap.put("college", college);
                            usermap.put("password", password);
                            FirebaseDatabase.getInstance().getReference("users").child("parent").child(uid).
                                    setValue(usermap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            Toast.makeText(ParentRegistrationActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }


                });

    }
}