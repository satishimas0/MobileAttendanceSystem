package traindge.masandroidproject.notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import traindge.masandroidproject.R;
import traindge.masandroidproject.attendance.ViewAttendanceActivity;
import traindge.masandroidproject.dashboard.StudentDashboard;
import traindge.masandroidproject.models.CollegeClass;
import traindge.masandroidproject.models.Notice;

import static traindge.masandroidproject.dashboard.TeacherDashboard.CLASS_UNIQUE_KEY;

public class NoticeMessageActivity extends AppCompatActivity {

    private RecyclerView rvNotices;
    private DatabaseReference notices;
    List<Notice> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notices = FirebaseDatabase.getInstance().getReference("notices");
        rvNotices = (RecyclerView) findViewById(R.id.rvNotices);
        rvNotices.setHasFixedSize(false);
        rvNotices.setLayoutManager(new LinearLayoutManager(this));
        notices.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        list.add(dataSnapshot.getValue(Notice.class));

                    }
                    if (list.size() > 0) {
                        rvNotices.setAdapter(new StudentClassAdapter(list));
                    } else {
                        Toast.makeText(NoticeMessageActivity.this, "no notice available now", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class StudentClassAdapter extends RecyclerView.Adapter<CollegeClassHolder> {


        private final List<Notice> list;

        public StudentClassAdapter(List<Notice> list) {
            this.list = list;
        }

        @Override
        public CollegeClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CollegeClassHolder(LayoutInflater.from(NoticeMessageActivity.this).inflate(R.layout.simple_notice_view, parent, false));
        }

        @Override
        public void onBindViewHolder(CollegeClassHolder holder, final int position) {
            holder.tvMsg.setText(NoticeMessageActivity.this.list.get(position).getText());
            holder.mClassName.setText(NoticeMessageActivity.this.list.get(position).getClazz());
            holder.tvSubject.setText(NoticeMessageActivity.this.list.get(position).getSubject());

        }

        @Override
        public int getItemCount() {
            return NoticeMessageActivity.this.list.size();
        }
    }

    public static class CollegeClassHolder extends RecyclerView.ViewHolder {

        public final TextView mClassName;
        TextView tvMsg;
        TextView tvSubject;
        public View row;

        public CollegeClassHolder(View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.llwrapper);
            mClassName = (TextView) itemView.findViewById(R.id.tvClass);
            tvMsg = (TextView) itemView.findViewById(R.id.tvMsg);
            tvSubject = (TextView) itemView.findViewById(R.id.tvSubject);
        }

        public void setClassName(String name) {
            mClassName.setText(name);
        }

    }
}
