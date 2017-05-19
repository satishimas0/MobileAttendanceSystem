package traindge.masandroidproject.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import traindge.masandroidproject.attendance.ClassCreationActivity;
import traindge.masandroidproject.notice.NoticeMessageActivity;
import traindge.masandroidproject.reports.GraphReportActivity;
import traindge.masandroidproject.ui.FeedbackActivity;
import traindge.masandroidproject.notice.NoticeSystemActivity;
import traindge.masandroidproject.R;
import traindge.masandroidproject.reports.ReportDaysActivity;
import traindge.masandroidproject.attendance.StudentAttendanceActivity;
import traindge.masandroidproject.models.CollegeClass;

public class TeacherDashboard extends AppCompatActivity implements View.OnClickListener {

    public static final String TEACHER = "teacher";
    public static final String CLASS_UNIQUE_KEY = "traindge.masandroidproject.classkey";
    public static final String CLASS_NAME_KEY ="traindge.masandroidproject.class";
    public static final String SUBJECT_NAME_KEY ="traindge.masandroidproject.subject";

    private FirebaseRecyclerAdapter<CollegeClass, CollegeClassHolder> mAdapter;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddClass);
        fab.setOnClickListener(this);
        RecyclerView rvClassList = (RecyclerView) findViewById(R.id.rvClassList);
        rvClassList.setHasFixedSize(false);
        rvClassList.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("classes").child(uid);
        mAdapter = new FirebaseRecyclerAdapter<CollegeClass, CollegeClassHolder>(CollegeClass.class, R.layout.simple_class_item, CollegeClassHolder.class, mRef) {
            @Override
            protected void populateViewHolder(CollegeClassHolder viewHolder, final CollegeClass model, final int position) {
                viewHolder.setClassName(model.getName());
                viewHolder.row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TeacherDashboard.this, StudentAttendanceActivity.class);
                        intent.putExtra(CLASS_UNIQUE_KEY, getRef(position).getKey());
                        startActivity(intent);

                    }
                });
                viewHolder.row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Toast.makeText(TeacherDashboard.this, "add a Notice", Toast.LENGTH_SHORT).show();
                        AlertDialog dialog= new AlertDialog.Builder(TeacherDashboard.this)
                                .setAdapter(new ArrayAdapter<String>(TeacherDashboard.this, android.R.layout.simple_list_item_1, new String[]{"view report", "create notice"}), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                      switch (which){
                                          case 0:
                                              showReport(position, model);
                                              break;
                                          case 1:
                                              showNotices(position, model);
                                              break;
                                      }
                                    }
                                }).setTitle("Available options").create();
                        dialog.show();


                        return true;
                    }
                });
            }

            private void showReport(int position, CollegeClass model) {
                Intent intent = new Intent(TeacherDashboard.this, GraphReportActivity.class);
                intent.putExtra(CLASS_UNIQUE_KEY, getRef(position).getKey());
                intent.putExtra(CLASS_NAME_KEY, model.getName());
                intent.putExtra(SUBJECT_NAME_KEY, model.getSubject());
                startActivity(intent);
            }

            private void showNotices(int position, CollegeClass model) {
                Intent intent = new Intent(TeacherDashboard.this, NoticeSystemActivity.class);
                intent.putExtra(CLASS_UNIQUE_KEY, getRef(position).getKey());
                intent.putExtra(CLASS_NAME_KEY, model.getName());
                intent.putExtra(SUBJECT_NAME_KEY, model.getSubject());
                startActivity(intent);
            }

        };
        rvClassList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, ClassCreationActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notice_menu:
                Intent IntentHom = new Intent(TeacherDashboard.this, NoticeMessageActivity.class);
                startActivity(IntentHom);
                break;
            case R.id.about_menu:
                break;
            case R.id.feedback_menu:
                Intent i = new Intent(TeacherDashboard.this, FeedbackActivity.class);
                startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public static class CollegeClassHolder extends RecyclerView.ViewHolder {

        private final TextView mClassName;
        private View row;

        public CollegeClassHolder(View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.llwrapper);
            mClassName = (TextView) itemView.findViewById(R.id.tvClass);
        }

        public void setClassName(String name) {
            mClassName.setText(name);
        }

    }

}
