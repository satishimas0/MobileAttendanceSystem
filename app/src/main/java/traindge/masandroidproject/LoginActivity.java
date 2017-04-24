package traindge.masandroidproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView etUsername;
    private TextView etPassword;
    private Button btnLogin;
    private Button btnForgtPassword;
    private Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //create object
        etUsername = (TextView) findViewById(R.id.etUsername);
        etPassword = (TextView) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnForgtPassword = (Button) findViewById(R.id.btnForgtPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //mAuth = FirebaseAuth.getInstance();

        // important part
       //mAuthListener = new FirebaseAuth.AuthStateListener() {
         //   @Override
           // public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
             //   FirebaseUser user = firebaseAuth.getCurrentUser();
               // if (user != null) {
                    // User is signed in
                 //   Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                   // Intent homeIntent = new Intent(LoginActivity.this, FeedbackActivity.class);
                    //startActivity(homeIntent);
                    //finish();
               // } else {
                    // User is signed out
               //     Log.d(TAG, "onAuthStateChanged:signed_out");
                //}

         //   }
        //};

        //final line
        btnSignUp.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                showOptions();
                break;
        }

    }
// option users coding

    private void showOptions() {
        String[] options = new String[]{"Student", "Teacher", "Parent"};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select")
                .setAdapter(new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, options), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //// TODO: 4/5/2017 Student registration
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


