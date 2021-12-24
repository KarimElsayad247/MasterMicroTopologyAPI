import com.google.gson.JsonArray;

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

    public JsonArray getComponentsAsJsonArray() {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < this.components.size(); i++) {
            jsonArray.add(this.components.get(i).getAsJsonElement());
        }
        return jsonArray;
    }
}
