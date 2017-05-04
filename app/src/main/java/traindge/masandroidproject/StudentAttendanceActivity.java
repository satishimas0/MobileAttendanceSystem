package traindge.masandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class StudentAttendanceActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth= FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.report_menu:
                Intent IntentHome = new Intent(StudentAttendanceActivity.this,ReportDaysActivity.class);
                startActivity(IntentHome);
                break;
            case R.id.notice_menu:
                Intent IntentHom = new Intent(StudentAttendanceActivity.this, NoticeSystemActivity.class);
                startActivity(IntentHom);
                break;
            case R.id.about_menu:
                break;
            case R.id.feedback_menu:
                IntentHome = new Intent(StudentAttendanceActivity.this, FeedbackActivity.class);
                startActivity(IntentHome);
                break;

         //Logout coding
            case R.id.logout_menu:
                    mAuth.signOut();
                    try {
                    }catch (Exception ignored) {
                    }
                    Intent lgtIntent=new Intent(this, LoginActivity.class);
                    startActivity(lgtIntent);
                    finish();
                    break;


        }
        return super.onOptionsItemSelected(item);
    }
}
