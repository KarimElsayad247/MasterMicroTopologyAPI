import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

class TopologyAPITest {

    TopologyAPI topologyAPI;
    String fileName1 = "./target/classes/topology.json";
    String fileName2 = "./target/classes/topology2.json";

    @BeforeEach
    public void setUp() {
        topologyAPI = new TopologyAPI();
    }

    @Nested
    public class readMethodTest {
        @Test
        @DisplayName("Successfully creates one topology from valid JSON file")
        public void createsTopologyFromFile() throws IOException {
            topologyAPI.readJSON(fileName1);
            Assertions.assertFalse(topologyAPI.queryTopologies().isEmpty());
            Assertions.assertEquals(1, topologyAPI.queryTopologies().size());
        }

        @Test
        @DisplayName("Successfully adds multiple topologies from multiple valid JSON files")
        public void createsMultipleTopologiesUsingDifferentFiles() throws IOException {
            topologyAPI.readJSON(fileName1);
            topologyAPI.readJSON(fileName2);
            Assertions.assertEquals(2, topologyAPI.queryTopologies().size());
        }
    }

    @Nested
    public class deleteMethodTest {
        @Test
        @DisplayName("Successfully deletes existing topologies")
        public void deletesExistingTopologies() throws IOException{
            topologyAPI.readJSON(fileName1);
            topologyAPI.readJSON(fileName2);
            boolean deleted = topologyAPI.deleteTopology("top1");
            Assertions.assertTrue(deleted);
            Assertions.assertEquals(1, topologyAPI.queryTopologies().size());
            boolean deleted2 = topologyAPI.deleteTopology("top2");
            Assertions.assertTrue(deleted2);
            Assertions.assertEquals(0, topologyAPI.queryTopologies().size());
        }

        @Test
        @DisplayName("Doesn't delete non-existent classes and preserves state")
        public void doesntDeleteNonExistingClasses() throws IOException {
            topologyAPI.readJSON(fileName1);
            topologyAPI.readJSON(fileName2);
            boolean notDeleted = topologyAPI.deleteTopology("nonexistent");
            Assertions.assertFalse(notDeleted);
            Assertions.assertEquals(2, topologyAPI.queryTopologies().size());
        }

    }

    @Nested
    public class queryDevicesMethodTest {
        @Test
        @DisplayName("Successfully queries which devices are in a given topology")
        public void queryDevicesInTopologySuccess() throws IOException {
            topologyAPI.readJSON(fileName1);
            topologyAPI.readJSON(fileName2);

            List<Component> components1 = topologyAPI.queryDevices("top1");
            Assertions.assertEquals("resistor", components1.get(0).getType());
            Assertions.assertEquals("res1", components1.get(0).getId());
            Assertions.assertEquals("nmos", components1.get(1).getType());
            Assertions.assertEquals("m1", components1.get(1).getId());

            List<Component> components2 = topologyAPI.queryDevices("top2");
            Assertions.assertEquals("resistor", components2.get(0).getType());
            Assertions.assertEquals("res2", components2.get(0).getId());
            Assertions.assertEquals("nmos", components2.get(1).getType());
            Assertions.assertEquals("m2", components2.get(1).getId());
        }
    }
}