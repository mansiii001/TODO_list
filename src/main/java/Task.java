import java.util.HashMap;
import java.util.Map;

public class Task {

    private int id;
    private String title;

    public Task(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public int getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
