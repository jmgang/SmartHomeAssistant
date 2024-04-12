package org.assistant.api.homeassistant;

import java.util.List;

public record HomeAssistantCommand(List<String> entity_id) {
    public HomeAssistantCommand(String entityId) {
        this(List.of(entityId));
    }
}
