import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Topology class
 * Consists of a string representing the id, and a list of components
 * User is not allowed to manipulate it directly. User can only go through the api to query info
 */
public class Topology {
    private String id;
    private List<Component> components;

    protected Topology(String id, ArrayList<Component> components) {
        this.id = id;
        this.components = components;
    }

    protected String getId() {
        return id;
    }

    protected List<Component> getComponents() {
        return components;
    }

    protected List<Component> getComponentsConnectedToNetlistNode(String netlist) {
        List<Component> connectedComponents = new ArrayList<>();
        for (Component component: this.components) {
            if (component.isConnectedToNetlistNode(netlist)) connectedComponents.add(component);
        }
        return connectedComponents;
    }

    protected JsonArray getComponentsAsJsonArray() {
        JsonArray jsonArray = new JsonArray();
        for (Component component : this.components) {
            jsonArray.add(component.getAsJsonElement());
        }
        return jsonArray;
    }
}
