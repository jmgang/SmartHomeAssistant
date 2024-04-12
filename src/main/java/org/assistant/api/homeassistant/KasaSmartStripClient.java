package org.assistant.api.homeassistant;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assistant.api.common.SimpleHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

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

    private void sendCommand(String service, HomeAssistantCommand command) throws IOException {
        String url = homeAssistantUrl + "/api/services/" + service;
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + longLivedAccessToken);
        headers.put("Content-Type", "application/json");

        List<EntityStateChanged> changes = httpClient.sendPost(url, headers, command, new TypeReference<>() {});

        System.out.println(changes.size());
        changes.forEach(System.out::println);
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
