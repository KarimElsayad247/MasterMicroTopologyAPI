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

    public List<Component> getComponents() {
        return components;
    }

    public List<Component> getComponentsConnectedToNetlistNode(String netlist) {
        List<Component> connectedComponents = new ArrayList<>();
        for (Component component: this.components) {
            if (component.isConnectedToNetlistNode(netlist)) connectedComponents.add(component);
        }
        return connectedComponents;
    }

    public JsonArray getComponentsAsJsonArray() {
        JsonArray jsonArray = new JsonArray();
        for (Component component : this.components) {
            jsonArray.add(component.getAsJsonElement());
        }
        return jsonArray;
    }
}
