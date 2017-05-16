package traindge.masandroidproject.models;

/**
 * Created by DELL on 5/15/2017.
 */

public class Attendance {
    private String studentId;
    private int status;

    public Attendance() {
    }

    public Attendance(String studentId, int status) {
        this.studentId = studentId;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


}
