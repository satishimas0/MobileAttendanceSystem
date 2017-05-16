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

public class TeacherRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "TeacherRegistration";
    private EditText etTchrName;
    private EditText etTchrMobile;
    private EditText etTchrEmail;
    private EditText etClgName;
    private EditText etHqualification;
    private EditText etTchrPassword;
    private Button btnTchrSubmit;


    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create object
        etTchrName = (EditText) findViewById(R.id.etTchrName);
        etTchrMobile = (EditText) findViewById(R.id.etTchrMobile);
        etTchrEmail = (EditText) findViewById(R.id.etTchrEmail);
        etClgName = (EditText) findViewById(R.id.etClgName);
        etHqualification = (EditText) findViewById(R.id.etHqualification);
        etTchrPassword = (EditText) findViewById(R.id.etTchrPassword);
        btnTchrSubmit = (Button) findViewById(R.id.btnTchrSubmit);

        mAuth = FirebaseAuth.getInstance();

        btnTchrSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnTchrSubmit:
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("wait...");
                dialog.show();
                final String TeacherName = etTchrName.getText().toString();
                final String mobileNumber = etTchrMobile.getText().toString();
                final String email = etTchrEmail.getText().toString().trim();
                final String college = etClgName.getText().toString();
                final String HQualification = etHqualification.getText().toString();
                final String password = etTchrPassword.getText().toString();
                Log.e(TAG, email);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                dialog.setMessage("updating database");

                                if (!task.isSuccessful()) {
                                    Toast.makeText(TeacherRegistrationActivity.this, R.string.auth_failed + task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                if (task.isSuccessful()) {
                                    dialog.setMessage("it may take a while...");
                                    String uid = task.getResult().getUser().getUid();
                                    HashMap<String, String> usermap = new HashMap<String, String>();
                                    usermap.put("name", TeacherName);
                                    usermap.put("mobile", mobileNumber);
                                    usermap.put("email", email);
                                    usermap.put("college", college);
                                    usermap.put("qualification", HQualification);
                                    usermap.put("password", password);
                                    FirebaseDatabase.getInstance().getReference("users").child("teachers").child(uid).setValue(usermap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            Toast.makeText(TeacherRegistrationActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            mAuth.signOut();
                                            startActivity(new Intent(TeacherRegistrationActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    });
                                }

                            }
                        });
                break;
        }

    }
}
