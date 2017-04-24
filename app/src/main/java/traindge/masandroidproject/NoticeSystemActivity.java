package traindge.masandroidproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class NoticeSystemActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spiClass;
    private Spinner spiSubject;
    private TextView tvMsgByTeacher;
    private Button btnNoticeSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_system);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spiClass = (Spinner) findViewById(R.id.spiClass);
        spiSubject = (Spinner) findViewById(R.id.spiSubject);
        tvMsgByTeacher = (TextView) findViewById(R.id.tvMsgByTeacher);
        btnNoticeSend = (Button) findViewById(R.id.btnNoticeSend);

        btnNoticeSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
