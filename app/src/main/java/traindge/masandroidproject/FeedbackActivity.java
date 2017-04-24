package traindge.masandroidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tvfeedbackMsg;
    private EditText etSubjectFeedback;
    private Button btnFeedbackSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feddback);

        // object create
        etSubjectFeedback = (EditText) findViewById(R.id.etSubjectFeedback);
        tvfeedbackMsg = (TextView) findViewById(R.id.tvfeedbackMsg);
        btnFeedbackSend = (Button) findViewById(R.id.btnFeedbackSend);

        //create onclicklistener
    }

    @Override
    public void onClick(View v) {

    }
}






