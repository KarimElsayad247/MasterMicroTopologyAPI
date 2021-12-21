import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {

        TopologyAPI topologyAPI = new TopologyAPI();
        System.out.println("Current dir is: " + System.getProperty("user.dir"));
        String fileName1 = "./target/classes/topology.json";
        try {
            topologyAPI.readJSON(fileName1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in readJSON; check if provided file exists");
        }

        topologyAPI.readJSON("data");

        System.out.println("end");
    }
}
