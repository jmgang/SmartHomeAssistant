package org.assistant.api.homeassistant;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assistant.api.common.HttpRequestBuilder;
import org.assistant.api.common.SimpleHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.assistant.config.ConfigLoader.getProperty;

public class KasaSmartStripClient {
    private static final SimpleHttpClient httpClient = new SimpleHttpClient();

    public static void turnOn(String entityId) throws IOException {
        sendCommand("turn_on", new HomeAssistantCommand(entityId));
    }

    public static void turnOff(String entityId) throws IOException {
        sendCommand("turn_off", new HomeAssistantCommand(entityId));
    }

    public static boolean isOn(String entityId) {
        try {
            var entityChanged = getEntityState(entityId);
            System.out.println(entityChanged);
            return "on".equalsIgnoreCase(entityChanged.state());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static EntityStateChanged getEntityState(String entityId) throws IOException {
        HttpRequestBuilder builder = new HttpRequestBuilder(getProperty("assistant.api.homeassistant.url"))
                .addPathSegments("api", "states", entityId)
                .addHeader("Authorization", "Bearer " + getProperty("assistant.api.homeassistant.access-token"))
                .addHeader("Content-Type", "application/json");

        String url = builder.build();
        Map<String, String> headers = builder.getHeaders();
        return httpClient.sendGet(url, headers, EntityStateChanged.class);
    }

    private static void sendCommand(String service, HomeAssistantCommand command) throws IOException {
        HttpRequestBuilder builder = new HttpRequestBuilder(getProperty("assistant.api.homeassistant.url"))
                .addPathSegments("api", "services", "switch", service)
                .addHeader("Authorization", "Bearer " + getProperty("assistant.api.homeassistant.access-token"))
                .addHeader("Content-Type", "application/json");
        List<EntityStateChanged> changes = httpClient.sendPost(builder.build(), builder.getHeaders(), command, new TypeReference<>() {
        });

        System.out.println("Number of entities changed: " + changes.size());
        changes.forEach(change -> System.out.println("Entity ID: " + change.entityId() + ", State: " + change.state()));
    }

}
