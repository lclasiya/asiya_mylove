package li.changlin.user.entity;



import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    @NotNull
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}