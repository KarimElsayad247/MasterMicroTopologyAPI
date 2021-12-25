import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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
    protected Component(JsonElement jsonElement) {

        // get the top level contents of the json element (type, id, netlist, and attribute)
        // as a set of map entries
        JsonObject object = jsonElement.getAsJsonObject();
        Set<Map.Entry<String,JsonElement>> entrySet = object.entrySet();

        // Iterate over the map, and assign to appropriate properties
        Iterator<Map.Entry<String, JsonElement>> entrySetIterator = entrySet.iterator();
        while (entrySetIterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = entrySetIterator.next();
            String key = entry.getKey();
            switch (key) {
                case "type" -> this.type = entry.getValue().getAsString();
                case "id" -> this.id = entry.getValue().getAsString();
                case "netlist" -> {
                    JsonObject netlistJsonObject = entry.getValue().getAsJsonObject();
                    Set<Map.Entry<String, JsonElement>> netlistEntrySet = netlistJsonObject.entrySet();

                    // Add all entries to map
                    Iterator<Map.Entry<String, JsonElement>> netlistIterator = netlistEntrySet.iterator();
                    while (netlistIterator.hasNext()) {
                        Map.Entry<String, JsonElement> netlistEntry = netlistIterator.next();
                        this.netlist.put(netlistEntry.getKey(), netlistEntry.getValue().getAsString());
                    }
                }
                default -> { // this is the attribute case
                    String attributeName = entry.getKey();

                    // The attributesObject only contains jsonPrimitives. We can't call
                    // getAsObject on any of them.
                    JsonObject attributesObject = entry.getValue().getAsJsonObject();
                    double defaultValue = attributesObject.get("default").getAsDouble();
                    double minVale = attributesObject.get("min").getAsDouble();
                    double maxValue = attributesObject.get("max").getAsDouble();
                    this.attributes = new Attributes(attributeName, defaultValue, minVale, maxValue);
                }
            }
        }
    }

    protected boolean isConnectedToNetlistNode(String node) {
        for (String value: this.netlist.values()) {
            if(value.equals(node)) return true;
        }
        return false;
    }

    protected JsonElement getAsJsonElement() {
        JsonObject element = new JsonObject();
        element.add("type", new JsonPrimitive(this.type));
        element.add("id", new JsonPrimitive(this.id));

        JsonObject attributes = new JsonObject();
        attributes.add("default", new JsonPrimitive(this.attributes.defaultValue()));
        attributes.add("min", new JsonPrimitive(this.attributes.minValue()));
        attributes.add("max", new JsonPrimitive(this.attributes.maxValue()));

        element.add(this.attributes.name(), attributes);

        JsonObject netlist = new JsonObject();
        this.netlist.forEach((s, s2) -> netlist.add(s, new JsonPrimitive(s2)));

        element.add("netlist", netlist);

        return element;
    }

    protected String getType() {
        return type;
    }

    protected String getId() {
        return id;
    }
}
