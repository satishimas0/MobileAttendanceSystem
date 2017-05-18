package traindge.masandroidproject.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import traindge.masandroidproject.R;

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
        tvfeedbackMsg = (EditText) findViewById(R.id.tvfeedbackMsg);
        btnFeedbackSend = (Button) findViewById(R.id.btnFeedbackSend);

        //create onclicklistener
    }

    @Override
    public void onClick(View v) {
        String subject = etSubjectFeedback.getText().toString();
        String msg = tvfeedbackMsg.getText().toString();
        if (subject.isEmpty() || msg.isEmpty() ){
            return;
        }
        else{
            composeEmail(new String[]{"xaidmeta@gmail.com"},subject,msg);
        }
    }

    public void composeEmail(String[] addresses, String subject, String msg) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}






