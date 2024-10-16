package org.example.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class EndpointConfig {

    private static final String CONFIG_FILE = "endpoints.json";
    private static JsonNode jsonNode;

    static {
        try(InputStream inputstream = EndpointConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE))
        {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(inputstream);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static String getEndpoint(String key, String property)
    {
        JsonNode endpointNode = jsonNode.path(key).path(property);
        return endpointNode.asText();
    }
}
