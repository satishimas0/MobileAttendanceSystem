package traindge.masandroidproject.models;

import java.util.HashMap;

/**
 * Created by DELL on 5/14/2017.
 */

public class CollegeClass {
    HashMap<String, String> students;
    private String teacher;
    private String subject;
    private String name;
    private String batch;
    private String time;
    private String year;


    public CollegeClass() {
    }

    public CollegeClass(String teacher, HashMap<String, String> students, String subject, String name, String batch, String time, String yearorsem) {
        this.teacher = teacher;
        this.students = students;
        this.subject = subject;
        this.name = name;
        this.batch = batch;
        this.time = time;
        this.year = yearorsem;
    }


    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public HashMap<String, String> getStudents() {
        return students;
    }

    public void setStudents(HashMap<String, String> students) {
        this.students = students;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


}
