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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import traindge.masandroidproject.login.LoginActivity;
import traindge.masandroidproject.R;

public class StudentRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "StudentRegistration";
    private EditText etStdParent;
    private EditText etparentName;
    private EditText etClgName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etStudentMobile;
    private EditText etStudentName;
    private EditText etStudentClass;
    private EditText etStudentPassword;
    private Button btnStudentSubmit;
    private EditText etStudentEmail;

    public StudentRegistrationActivity() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etStudentName = (EditText) findViewById(R.id.etStudentName);
        etStudentClass = (EditText) findViewById(R.id.etStudentClass);
        etStdParent = (EditText) findViewById(R.id.etStdParent);
        etStudentMobile = (EditText) findViewById(R.id.etStudentMobile);
        etStudentEmail = (EditText) findViewById(R.id.etStudentEmail);
        etClgName = (EditText) findViewById(R.id.etClgName);
        etStudentPassword = (EditText) findViewById(R.id.etStudentPassword);
        btnStudentSubmit = (Button) findViewById(R.id.btnStudentSubmit);


        mAuth = FirebaseAuth.getInstance();


        btnStudentSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        final String name = etStudentName.getText().toString();
        final String studentclass = etStudentClass.getText().toString();
        final String parentName = etStdParent.getText().toString();
        final String mobileNumber = etStudentMobile.getText().toString();
        final String email = etStudentEmail.getText().toString();
        final String college = etClgName.getText().toString();
        final String password = etStudentPassword.getText().toString();

        Log.e(TAG, email);
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
                            Toast.makeText(StudentRegistrationActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        if (task.isSuccessful()) {
                            dialog.dismiss();

                            String uid = task.getResult().getUser().getUid();
                            HashMap<String, String> usermap = new HashMap<String, String>();
                            usermap.put("name", name);
                            usermap.put("class", studentclass);
                            usermap.put("parent", parentName);
                            usermap.put("mobile", mobileNumber);
                            usermap.put("email", email);
                            usermap.put("college", college);
                            usermap.put("password", password);

                            FirebaseDatabase.getInstance().getReference("users").child("students").child(uid).setValue(usermap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    Toast.makeText(StudentRegistrationActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    startActivity(new Intent(StudentRegistrationActivity.this, LoginActivity.class));
                                    finish();

                                }
                            });
                        }
                    }
                });
    }

}
