package traindge.masandroidproject.notice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import traindge.masandroidproject.R;

import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_NAME_KEY;
import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_UNIQUE_KEY;
import static traindge.masandroidproject.dashboard.TeacherDashboard.SUBJECT_NAME_KEY;

public class NoticeSystemActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TEXT = "text";
    public static final String CLASS = "class";
    public static final String SUBJECT = "subject";
    private Spinner spiClass;
    private Spinner spiSubject;
    private TextView tvMsgByTeacher;
    private Button btnNoticeSend;
    private EditText etClass;
    private EditText etSubject;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_system);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvMsgByTeacher = (EditText) findViewById(R.id.tvMsgByTeacher);
        btnNoticeSend = (Button) findViewById(R.id.btnNoticeSend);
        etClass = (EditText) findViewById(R.id.etClass);
        etSubject = (EditText) findViewById(R.id.etSubject);

        btnNoticeSend.setOnClickListener(this);
        if (getIntent().hasExtra(CLASS_UNIQUE_KEY)) {
            String classId = getIntent().getStringExtra(CLASS_UNIQUE_KEY);
            String className = getIntent().getStringExtra(CLASS_NAME_KEY);
            String subject = getIntent().getStringExtra(SUBJECT_NAME_KEY);
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            etClass.setText(className);
            etSubject.setText(subject);
        }
    }

    @Override
    public void onClick(View v) {
        if (tvMsgByTeacher.getText().toString().isEmpty()){
            return;
        }
        HashMap<String, Object> o = new HashMap<>();
        o.put(TEXT,tvMsgByTeacher.getText().toString());
        o.put(CLASS,etClass.getText().toString());
        o.put(SUBJECT,etSubject.getText().toString());
        FirebaseDatabase.getInstance().getReference("notices").push().setValue(o, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError==null){
                    Toast.makeText(NoticeSystemActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
