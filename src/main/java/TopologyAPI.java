import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

public class TopologyAPI {
    private Topology[] topologies;

    public static class TopologyDeserilizer implements JsonDeserializer<Topology> {
        @Override
        public Topology deserialize(JsonElement json, Type topology, JsonDeserializationContext context) {
            System.out.println(json.toString());
            ArrayList<Component> components = new ArrayList<>();
            return new Topology("id", components);
        }
    }

    public void readJSON(String fileName) {

    }

    public void writeJSON(String topologyID) {

    }

    public Topology[] queryTopologies() {
        return null;
    }

    public void deleteToppology(String topologyID) {

    }

    public Component[] queryDevices(String topologyID) {
        return null;
    }

    public Component[] queryDevicesWithNetlistNode(String topologyID, String netlistNodeID) {
        return null;
    }

}

