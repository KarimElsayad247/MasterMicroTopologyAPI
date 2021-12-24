import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

class TopologyAPITest {

    TopologyAPI topologyAPI1;
    String fileName1 = "./target/classes/topology.json";
    String fileName2 = "./target/classes/topology2.json";

    @BeforeEach
    public void setUp() {
        topologyAPI1 = new TopologyAPI();
    }

    @Nested
    public class ReadMethodTest {
        @Test
        @DisplayName("Successfully creates one topology from valid JSON file")
        public void createsTopologyFromFile() throws IOException {
            topologyAPI1.readJSON(fileName1);
            Assertions.assertFalse(topologyAPI1.queryTopologies().isEmpty());
            Assertions.assertEquals(1, topologyAPI1.queryTopologies().size());
        }

        @Test
        @DisplayName("Successfully adds multiple topologies from multiple valid JSON files")
        public void createsMultipleTopologiesUsingDifferentFiles() throws IOException {
            topologyAPI1.readJSON(fileName1);
            topologyAPI1.readJSON(fileName2);
            Assertions.assertEquals(2, topologyAPI1.queryTopologies().size());
        }
    }

    @Nested
    public class WriteMethodTest {
        @Test
        @DisplayName("Writes Topology to to file correctly")
        public void writesTopologyToFile() throws IOException {
            String outFile1 = "./target/classes/writer_out1.json";
            String outFile2 = "./target/classes/writer_out2.json";
            topologyAPI1.readJSON(fileName1);
            topologyAPI1.readJSON(fileName2);
            topologyAPI1.writeJSON("top1", outFile1);
            topologyAPI1.writeJSON("top2", outFile2);

            TopologyAPI topologyAPI2 = new TopologyAPI();
            topologyAPI2.readJSON(outFile1);
            topologyAPI2.readJSON(outFile2);

            List<Topology> list1 = topologyAPI1.queryTopologies();
            List<Topology> list2 = topologyAPI2.queryTopologies();

            Assertions.assertEquals(list1.size(), list2.size());
            Assertions.assertEquals(list1.get(0).getId(), list2.get(0).getId());
            Assertions.assertEquals(list1.get(1).getId(), list2.get(1).getId());

            List<Component> api1top1Components = topologyAPI1.queryDevices("top1");
            List<Component> api2top1Components = topologyAPI2.queryDevices("top1");

            Assertions.assertEquals(api1top1Components.size(), api2top1Components.size());
            Assertions.assertEquals(api1top1Components.get(0).getId(), api2top1Components.get(0).getId());
            Assertions.assertEquals(api1top1Components.get(0).getType(), api2top1Components.get(0).getType());
        }
    }

    @Nested
    public class DeleteMethodTest {
        @Test
        @DisplayName("Successfully deletes existing topologies")
        public void deletesExistingTopologies() throws IOException{
            topologyAPI1.readJSON(fileName1);
            topologyAPI1.readJSON(fileName2);
            boolean deleted = topologyAPI1.deleteTopology("top1");
            Assertions.assertTrue(deleted);
            Assertions.assertEquals(1, topologyAPI1.queryTopologies().size());
            boolean deleted2 = topologyAPI1.deleteTopology("top2");
            Assertions.assertTrue(deleted2);
            Assertions.assertEquals(0, topologyAPI1.queryTopologies().size());
        }

        @Test
        @DisplayName("Doesn't delete non-existent classes and preserves state")
        public void doesntDeleteNonExistingClasses() throws IOException {
            topologyAPI1.readJSON(fileName1);
            topologyAPI1.readJSON(fileName2);
            boolean notDeleted = topologyAPI1.deleteTopology("nonexistent");
            Assertions.assertFalse(notDeleted);
            Assertions.assertEquals(2, topologyAPI1.queryTopologies().size());
        }

    }

    @Nested
    public class QueryDevicesMethodTest {
        @Test
        @DisplayName("Successfully queries which devices are in a given topology")
        public void queryDevicesInTopologySuccess() throws IOException {
            topologyAPI1.readJSON(fileName1);
            topologyAPI1.readJSON(fileName2);

            List<Component> components1 = topologyAPI1.queryDevices("top1");
            Assertions.assertEquals("resistor", components1.get(0).getType());
            Assertions.assertEquals("res1", components1.get(0).getId());
            Assertions.assertEquals("nmos", components1.get(1).getType());
            Assertions.assertEquals("m1", components1.get(1).getId());

            List<Component> components2 = topologyAPI1.queryDevices("top2");
            Assertions.assertEquals("resistor", components2.get(0).getType());
            Assertions.assertEquals("res2", components2.get(0).getId());
            Assertions.assertEquals("nmos", components2.get(1).getType());
            Assertions.assertEquals("m2", components2.get(1).getId());
        }
    }

    @Nested
    public class QueryDevicesConnectedToNodeTest {

        @Test
        @DisplayName("Successfully queries which devices are connected to which nodes")
        public void queryDevicesConnectedToNodes() throws IOException {

            topologyAPI1.readJSON(fileName1);

            List<Component> componentsN1 = topologyAPI1
                    .queryDevicesWithNetlistNode("top1", "n1");
            Assertions.assertEquals(2, componentsN1.size());

            List<Component> componentsVin = topologyAPI1
                    .queryDevicesWithNetlistNode("top1", "vin");
            Assertions.assertEquals(1, componentsVin.size());

            List<Component> componentsNonExistent = topologyAPI1
                    .queryDevicesWithNetlistNode("top1", "none_existent");
            Assertions.assertEquals(0, componentsNonExistent.size());
        }

        @Test
        @DisplayName("Returns null when id doesn't belong to any existing topology")
        public void returnsNullWhenTopologyDoesntExist() throws IOException {

            topologyAPI1.readJSON(fileName1);

            List<Component> componentsBadTopologyID = topologyAPI1
                    .queryDevicesWithNetlistNode("bad_topology_id", "ni");
            Assertions.assertNull(componentsBadTopologyID);
        }

        @Test
        @DisplayName("Returns null when id doesn't belong to any existing topology")
        public void returnsNullWhenNoTopologyInMemory() {
            List<Component> componentsBadTopologyID = topologyAPI1
                    .queryDevicesWithNetlistNode("bad_topology_id", "ni");
            Assertions.assertNull(componentsBadTopologyID);
        }
    }
}