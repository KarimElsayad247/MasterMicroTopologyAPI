import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.google.gson.*;

public class TopologyAPI {

    // List containing all topologies stored in memory
    private List<Topology> topologyArrayList = new ArrayList<>();

    // Map containing references to topology index in list, based on their id.
    // I believe this will make all search operations much more efficient.
    private Map<String, Integer> idToTopologyIndexMap = new HashMap<>();

    private static class TopologyDeserializer implements JsonDeserializer<Topology> {
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
                    array.forEach(jsonElement -> components.add(new Component(jsonElement)));
                }
            }

            if (topologyID.equals("")) {
                throw new java.lang.RuntimeException("TopologyID not assigned!");
            }

            return new Topology(topologyID, components);
        }
    }

    private static class TopologySerializer implements JsonSerializer<Topology> {
        @Override
        public JsonElement serialize(Topology topology, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject element = new JsonObject();
            element.add("id", new JsonPrimitive(topology.getId()));
            element.add("components", topology.getComponentsAsJsonArray());
            return element;
        }

    }

    public void readJSON(String fileName) throws IOException{

        // Assign the custom deserializer to gsonBuilder to get a gson object using
        // that deserializer
        GsonBuilder gsonBuilder   = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Topology.class, new TopologyDeserializer());
        Gson gson = gsonBuilder.create();

        // Read the contents of the json file
        Path path = Path.of(fileName);
        String content = Files.readString(path, StandardCharsets.US_ASCII);

        // Create a new topology from the json string and store it in list, and add it
        // to the map
        Topology topology = gson.fromJson(content, Topology.class);
        this.idToTopologyIndexMap.put(topology.getId(), this.topologyArrayList.size());
        this.topologyArrayList.add(topology);
    }

    public void writeJSON(String topologyID, String fileName) throws IllegalArgumentException, IOException  {
        // Assign the custom serializer to gsonBuilder to get a gson object using
        // that serializer
        GsonBuilder gsonBuilder   = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Topology.class, new TopologySerializer());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        boolean topologyExists = this.idToTopologyIndexMap.containsKey(topologyID);
        if (!topologyExists) {
            throw new IllegalArgumentException(String.format("No topology with id %s", topologyID));
        }

        int topologyIndex = this.idToTopologyIndexMap.get(topologyID);
        Topology topology = this.topologyArrayList.get(topologyIndex);

        String output = gson.toJson(topology);
        Path path = Path.of(fileName);
        Files.writeString(path, output, StandardCharsets.US_ASCII);
    }

    /**
     * Query about which topologies are currently in the memory.
     * @return The array containing all topologies currently stored in memory
     */
    public List<Topology> queryTopologies() {
        return this.topologyArrayList;
    }


    public boolean deleteTopology(String topologyID) {
        // First, find the index of the topology with the given ID
        int indexTopologyToDelete;

        indexTopologyToDelete = this.idToTopologyIndexMap.getOrDefault(topologyID, -1);

        // If element to delete doesn't exist in list, return false and have the api user
        // deal with the situation
        if (indexTopologyToDelete == -1) return false;

        // Finally, delete the topology from memory
        this.topologyArrayList.remove(indexTopologyToDelete);

        // Correct indices in map
        this.idToTopologyIndexMap.forEach((s, i) -> {
            if (i > indexTopologyToDelete) {
                this.idToTopologyIndexMap.replace(s, i - 1);
            }
        });

        return true;
    }

    /**
     *
     * @param topologyID: Id of a topology we wish to find its components
     * @return a list of components in a given topology. If a topology doesn't exist,
     *          null is returned
     */
    public List<Component> queryDevices(String topologyID) {
        if (this.idToTopologyIndexMap.containsKey(topologyID)) {
            int topologyIndex = this.idToTopologyIndexMap.get(topologyID);
            return this.topologyArrayList.get(topologyIndex).getComponents();
        }
        else {
            return null;
        }
    }

    public List<Component> queryDevicesWithNetlistNode(String topologyID, String netlistNodeID) {
        if (this.idToTopologyIndexMap.containsKey(topologyID)) {
            int topologyIndex = this.idToTopologyIndexMap.get(topologyID);
            Topology topology = this.topologyArrayList.get(topologyIndex);
            return topology.getComponentsConnectedToNetlistNode(netlistNodeID);
        }
        else {
            return null;
        }
    }
}

