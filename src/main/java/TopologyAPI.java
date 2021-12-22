import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gson.*;

public class TopologyAPI {

    private ArrayList<Topology> topologyArrayList = new ArrayList<>();

    private class TopologyDeserilizer implements JsonDeserializer<Topology> {
        @Override
        public Topology deserialize(JsonElement json, Type topology, JsonDeserializationContext context) {

            // Initialize Topology object parameters
            String topologyID = "";
            ArrayList<Component> components = new ArrayList<>();

            // get the top level contents of the json string (Id and components array)
            // as a set of map entries
            JsonObject object = json.getAsJsonObject();
            Set<Map.Entry<String,JsonElement>> entrySet = object.entrySet();

            // Iterate over the map, get id and components and assign to predefined variables
            Iterator entrySetIterator = entrySet.iterator();
            while (entrySetIterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) entrySetIterator.next();
                String key = entry.getKey();
                if (key.equals("id")) {
                    topologyID = entry.getValue().getAsString();
                }
                else if (key.equals("components")) {
                    // get all components as an array
                    JsonArray array = entry.getValue().getAsJsonArray();

                    // Of-load component creation from json to Component class
                    array.forEach(jsonElement -> {
                        components.add(new Component(jsonElement));
                    });
                }
            }

            if (topologyID.equals("")) {
                throw new java.lang.RuntimeException("TopologyID not assigned!");
            }

            Topology newTopology = new Topology(topologyID, components);
            return newTopology;
        }
    }

    public void readJSON(String fileName) throws IOException{

        // Assign the custom deserializer to gsonBuilder to get a gson object using
        // that deserializer
        GsonBuilder gsonBuilder   = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Topology.class, new TopologyAPI.TopologyDeserilizer());
        Gson gson = gsonBuilder.create();

        // Read the contents of the json file
        Path path = Path.of(fileName);
        String content = Files.readString(path, StandardCharsets.US_ASCII);

        // Create a new topology from the json string and store it in list
        Topology topology = gson.fromJson(content, Topology.class);
        topologyArrayList.add(topology);
    }

    public void writeJSON(String topologyID) {

    }

    /**
     * Query about which topologies are currently in the memory.
     * @return The array containing all topologies currently stored in memory
     */
    public ArrayList<Topology> queryTopologies() {
        return this.topologyArrayList;
    }


    public boolean deleteToppology(String topologyID) {
        // First, find the topology with the given ID
        int indexTopologyToDelete = -1;

        for (int i = 0; i < this.topologyArrayList.size(); i++) {
            if (this.topologyArrayList.get(i).getId().equals(topologyID)) {
                indexTopologyToDelete = i;
                break;
            }
        }

        // If element to delete doesn't exist in list, return false and have the api user
        // deal with the situation
        if (indexTopologyToDelete == -1) return false;

        this.topologyArrayList.remove(indexTopologyToDelete);
        return true;
    }

    public Component[] queryDevices(String topologyID) {
        return null;
    }

    public Component[] queryDevicesWithNetlistNode(String topologyID, String netlistNodeID) {
        return null;
    }

    public ArrayList<Topology> getTopologyArrayList() {
        return topologyArrayList;
    }
}

