package traindge.masandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static traindge.masandroidproject.R.id.parent;

public class ClassManagementSystemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText etClass;
    private Spinner spiYear;
    private EditText etStudentName;
    private Button btnAddStd;
    private EditText etSubject;
    private EditText etPeriod;
    private Button btnClassSystmContinue;
    private EditText etYearSem;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_management_system);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create object
        etClass = (EditText) findViewById(R.id.etClass);
        etYearSem = (EditText) findViewById(R.id.etYearSem);
        spiYear = (Spinner) findViewById(R.id.spiYear);
        etStudentName = (EditText) findViewById(R.id.etStudentName);
        btnAddStd = (Button) findViewById(R.id.btnAddStd);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etPeriod = (EditText) findViewById(R.id.etPeriod);
        btnClassSystmContinue = (Button) findViewById(R.id.btnClassSystmContinue);
        mAuth = FirebaseAuth.getInstance();



btnClassSystmContinue.setOnClickListener(this);

//dropdown list in spinner
        spiYear = (Spinner) findViewById(R.id.spiYear);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiYear.setAdapter(adapter1);
        spiYear.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClassSystmContinue:
                Intent subIntent=new Intent(ClassManagementSystemActivity.this,StudentAttendanceActivity.class);
                startActivity(subIntent);
                break;

        }
    }
}
