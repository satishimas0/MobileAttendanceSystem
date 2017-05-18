package traindge.masandroidproject.models;

/**
 * Created by xaidi on 19-05-2017.
 */

public class Notice {
    String text;
    String clazz;
    String subject;

    public Notice(String text, String clazz, String subject) {
        this.text = text;
        this.clazz = clazz;
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public String getClazz() {
        return clazz;
    }

    public String getSubject() {
        return subject;
    }

    public Notice() {

    }
}
