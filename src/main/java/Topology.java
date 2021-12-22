import java.util.ArrayList;
import java.util.List;

public class Topology {
    private String id;
    private List<Component> components;

    public Topology(String id, ArrayList<Component> components) {
        this.id = id;
        this.components = components;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }
}
