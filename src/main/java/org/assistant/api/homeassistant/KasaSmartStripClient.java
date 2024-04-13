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
    private final SimpleHttpClient httpClient;
    private final String homeAssistantUrl;
    private final String longLivedAccessToken;

    public KasaSmartStripClient(String homeAssistantUrl, String longLivedAccessToken) {
        this.httpClient = new SimpleHttpClient();
        this.homeAssistantUrl = homeAssistantUrl;
        this.longLivedAccessToken = longLivedAccessToken;
    }

    public void turnOn(String entityId) throws IOException {
        sendCommand("switch/turn_on", new HomeAssistantCommand(entityId));
    }

    public void turnOff(String entityId) throws IOException {
        sendCommand("switch/turn_off", new HomeAssistantCommand(entityId));
    }

    public boolean isOn(String entityId) {
        try {
            var entityChanged = getEntityState(entityId);
            System.out.println(entityChanged);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private EntityStateChanged getEntityState(String entityId) throws IOException {
        HttpRequestBuilder builder = new HttpRequestBuilder(getProperty("assistant.api.homeassistant.url"))
                .addPathSegments("api", "states", entityId)
                .addHeader("Authorization", "Bearer " + getProperty("assistant.api.homeassistant.access-token"))
                .addHeader("Content-Type", "application/json");

        String url = builder.build();
        Map<String, String> headers = builder.getHeaders();
        return httpClient.sendGet(url, headers, EntityStateChanged.class);
    }

    private void sendCommand(String service, HomeAssistantCommand command) throws IOException {
        HttpRequestBuilder builder = new HttpRequestBuilder(homeAssistantUrl)
                .addPathSegments("api", "services", service)
                .addHeader("Authorization", "Bearer " + longLivedAccessToken)
                .addHeader("Content-Type", "application/json");
        List<EntityStateChanged> changes = httpClient.sendPost(builder.build(), builder.getHeaders(), command, new TypeReference<>() {
        });

        System.out.println("Number of entities changed: " + changes.size());
        changes.forEach(change -> System.out.println("Entity ID: " + change.entityId() + ", State: " + change.state()));
    }

    public static void main(String[] args) {
        String baseUrl = "http://192.168.1.16:8123";
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIzM2QwZTUxZWY4MmU0OTIzOWE2ZTkyMTYwZTRjN2UwYSIsImlhdCI6MTcxMjk1MDE0NCwiZXhwIjoyMDI4MzEwMTQ0fQ.ckvU_W7F2xlbUmE4-S-pF0EqUfTd4PiBWRynaTyCH80";
        String entityId = "switch.tp_link_power_strip_b69d_kasa_smart_plug_b69d_1";

        KasaSmartStripClient stripClient = new KasaSmartStripClient(baseUrl, accessToken);

        try {
            stripClient.turnOn(entityId);
            stripClient.turnOn("switch.tp_link_power_strip_b69d_kasa_smart_plug_b69d_2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
