import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TopologyAPITest {

    @Test
    public void createsTopologyFromFile() throws IOException {
        TopologyAPI topologyAPI = new TopologyAPI();
        String fileName1 = "./target/classes/topology.json";
        topologyAPI.readJSON(fileName1);
        Assertions.assertFalse(topologyAPI.getTopologyArrayList().isEmpty());
    }

    @Test
    public void createsMultipleTopologiesUsingDifferentFiles() throws IOException {
        TopologyAPI topologyAPI = new TopologyAPI();
        String fileName1 = "./target/classes/topology.json";
        String fileName2 = "./target/classes/topology2.json";
        topologyAPI.readJSON(fileName1);
        topologyAPI.readJSON(fileName2);
        Assertions.assertTrue(topologyAPI.getTopologyArrayList().size() == 2);
    }

    @Test
    public void createsTopologiesAndComponentsCorrectly() throws IOException {

    }

}