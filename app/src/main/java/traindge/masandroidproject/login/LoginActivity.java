package traindge.masandroidproject.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import traindge.masandroidproject.ui.ProfileActivity;
import traindge.masandroidproject.R;
import traindge.masandroidproject.registration.ParentRegistrationActivity;
import traindge.masandroidproject.registration.StudentRegistrationActivity;
import traindge.masandroidproject.registration.TeacherRegistrationActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Login";
    public static final String TYPE = "type";
    public static final String EXTRA_TYPE = "usertype";
    public static final String APP_PREF = "app_pref";

    String type;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etUsername;
    private EditText etPassword;
    private SharedPreferences app_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //create object
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnForgtPassword = (Button) findViewById(R.id.btnForgtPassword);
        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        //load user type from pref
        app_pref = getSharedPreferences(APP_PREF, MODE_PRIVATE);
        type = app_pref.getString(TYPE, "students");

        //firebase instance
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    intent.putExtra(EXTRA_TYPE, type);
                    startActivity(intent);
                    finish();
                }
            }
        };
        //final line
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgtPassword.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                showSignUpOptions();
                break;
            case R.id.btnLogin:
                showSignInOptions();
                break;
            case R.id.btnForgtPassword:

                break;


        }

    }

    private void signIn(final String type) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("authenticating...");
        dialog.show();
        String name = etUsername.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        if (name.isEmpty()) {
            etUsername.setError("cant be empty");
            dialog.dismiss();
            return;

        }
        if (pass.isEmpty()) {
            dialog.dismiss();
            etPassword.setError("cant be empty");
            return;
        }
        app_pref.edit().putString(TYPE, type).apply();
        mAuth.signInWithEmailAndPassword(name, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }
// option users coding

    private void showSignInOptions() {
        String[] options = new String[]{"Student Login", "Teacher Login", "Parent Login"};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select a Login")
                .setAdapter(new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, options), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                signIn("students");
                                break;
                            case 1:
                                signIn("teachers");
                                break;
                            case 2:
                                signIn("parent");
                                break;
                        }
                    }
                }).create();
        dialog.show();
    }

    private void showSignUpOptions() {
        String[] options = new String[]{"Student Signup", "Teacher Signup", "Parent Signup"};

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select a Signup")
                .setAdapter(new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, options), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent subIntent = new Intent(LoginActivity.this, StudentRegistrationActivity.class);
                                startActivity(subIntent);
                                break;
                            case 1:
                                subIntent = new Intent(LoginActivity.this, TeacherRegistrationActivity.class);
                                startActivity(subIntent);
                                break;
                            case 2:
                                subIntent = new Intent(LoginActivity.this, ParentRegistrationActivity.class);
                                startActivity(subIntent);
                                break;
                        }
                    }
                }).create();
        dialog.show();
    }


}


