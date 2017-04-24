package traindge.masandroidproject;

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

import java.util.ArrayList;
import java.util.List;

public abstract class ClassManagementSystemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etClass;
    private Spinner spiYear;
    private EditText etAddStudent;
    private EditText etSubject;
    private EditText etPeriod;
    private Button btnClassSystmContinue;
    private EditText etYearSem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create object
        etClass = (EditText) findViewById(R.id.etClass);
        spiYear = (Spinner) findViewById(R.id.spiYear);
        etAddStudent = (EditText) findViewById(R.id.etAddStudent);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etPeriod = (EditText) findViewById(R.id.etPeriod);
        btnClassSystmContinue = (Button) findViewById(R.id.btnClassSystmContinue);


        List<String> list = new ArrayList<String>();
        list.add("I SEMESTER");
        list.add("II SEMESTER");
        list.add("III SEMESTER");
        list.add("IV SENESTER");
        list.add("V SEMESTER");
        list.add("VI SEMESTER");
        list.add("VII SEMESTER");
        list.add("VIII SEMESTER");
        list.add("1 YEAR");
        list.add("2 YEAR");
        list.add("3 YEAR");
        list.add("None");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spiYear.setAdapter(dataAdapter);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        // Button click Listener
        addListenerOnButton();


    }

    // Add spinner data

    public void addListenerOnSpinnerItemSelection() {

        spiYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //get the selected dropdown list value

    public void addListenerOnButton() {

        spiYear = (Spinner) findViewById(R.id.spiYear);

        etYearSem = (EditText) findViewById(R.id.etYearSem);

        etYearSem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ClassManagementSystemActivity.this,
                        "On Button Click : " +
                                "\n" + String.valueOf(spiYear.getSelectedItem()),
                        Toast.LENGTH_LONG).show();


            }

        });
    }
}
