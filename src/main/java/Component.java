import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Component {
    private String type;
    private String id;
    private Attributes attributes;
    private Map<String, String> netlist = new HashMap<>();

    /**
     * @param jsonElement: the component in json format
     */
    public Component(JsonElement jsonElement) {

        // get the top level contents of the json element (type, id, netlist, and attribute)
        // as a set of map entries
        JsonObject object = jsonElement.getAsJsonObject();
        Set<Map.Entry<String,JsonElement>> entrySet = object.entrySet();

        // Iterate over the map, and assign to appropriate properties
        Iterator entrySetIterator = entrySet.iterator();
        while (entrySetIterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) entrySetIterator.next();
            String key = entry.getKey();
            switch (key) {
                case "type":
                    this.type = entry.getValue().getAsString();
                    break;
                case "id":
                    this.id = entry.getValue().getAsString();
                    break;
                case "netlist":
                    JsonObject netlistJsonObject = entry.getValue().getAsJsonObject();
                    Set<Map.Entry<String,JsonElement>> netlistEntrySet = netlistJsonObject.entrySet();

                    // Add all entries to map
                    Iterator netlistIterator = netlistEntrySet.iterator();
                    while (netlistIterator.hasNext()) {
                        Map.Entry<String, JsonElement> netlistEntry = (Map.Entry<String, JsonElement>) netlistIterator.next();
                        this.netlist.put(netlistEntry.getKey(), netlistEntry.getValue().getAsString());
                    }
                    break;
                default: // this is the attribute case
                    String attributeName = entry.getKey();

                    // The attributesObject only contains jsonPrimitives. We can't call
                    // getAsObject on any of them.
                    JsonObject attributesObject = entry.getValue().getAsJsonObject();
                    double defaultvalue = attributesObject.get("default").getAsDouble();
                    double minVale = attributesObject.get("min").getAsDouble();
                    double maxValue = attributesObject.get("max").getAsDouble();
                    this.attributes = new Attributes(attributeName, defaultvalue, minVale, maxValue);

            }
        }
    }

    public boolean isConnectedToNetlistNode(String node) {
        return false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getNetlist() {
        return netlist;
    }

    public void setNetlist(Map<String, String> netlist) {
        this.netlist = netlist;
    }
}
