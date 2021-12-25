import java.io.IOException;
import java.util.List;

/**
 * API interface, intended as the entry point of the api user
 * @author Karim M. Elsayad
 */
public interface ITopologyAPI {
    /**
     * This method reads an object from a file and stores it in topologyArrayList
     *
     * @param fileName The name of the file from which to read a string
     *                 provided as an absolute or relative path
     * @throws IOException If the file doesn't exist. API user should provide an existing file.
     * @throws NullPointerException if json file is bad (wrong format, missing params, etc...)
     */
    void readJSON(String fileName) throws IOException, NullPointerException;

    /**
     * Writes a given topology to a Json file
     *
     * @param topologyID ID of the topology to write to a file
     * @param fileName   name of the file to which we write the topology
     * @throws IllegalArgumentException if there is no topology in memory with provided ID
     * @throws IOException              If any other errors with files occur
     */
    void writeJSON(String topologyID, String fileName) throws IllegalArgumentException, IOException;

    /**
     * Query about which topologies are currently in the memory.
     *
     * @return The array containing all topologies currently stored in memory
     */
    List<Topology> queryTopologies();

    /**
     * Delete a topology from memory. doesn't affect any file.
     *
     * @param topologyID ID of topology to delete
     * @return A boolean signifying whether a topology was deleted.
     * returns false if topology doesn't exist
     */
    boolean deleteTopology(String topologyID);

    /**
     * Get a list of the components on a given topology
     *
     * @param topologyID ID of a topology we wish to find its components
     * @return a list of components in a given topology. If a topology doesn't exist,
     * null is returned
     */
    List<Component> queryDevices(String topologyID);

    /**
     * Given a topology and a netlist node, find all components on the topology
     * that are connected to the given node.
     *
     * @param topologyID    ID of the topology whose components we want to check
     * @param netlistNodeID The netlist node to which components are connected
     * @return A list of components on the topology that are connected to the given netlist not.
     * Returns null if topology doesn't exist.
     * Returns an empty list if topology exists but no components connected to given node
     */
    List<Component> queryDevicesWithNetlistNode(String topologyID, String netlistNodeID);
}
