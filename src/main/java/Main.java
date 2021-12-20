import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        GsonBuilder gsonBuilder   = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Topology.class, new TopologyAPI.TopologyDeserilizer());
        Gson gson = gsonBuilder.create();
        System.out.println("Current dir is: " + System.getProperty("user.dir"));
        Path path = Path.of("./target/classes/topology.json");
        try {
            String content = Files.readString(path, StandardCharsets.US_ASCII);
            System.out.println(content);
            Topology topology = gson.fromJson(content, Topology.class);
            System.out.println(topology.getId());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't open file!");
            return;
        }

    }
}
