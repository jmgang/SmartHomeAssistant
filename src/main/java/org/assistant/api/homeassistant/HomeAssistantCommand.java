package org.assistant.api.homeassistant;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record HomeAssistantCommand(@JsonProperty("entity_id") String entityId) {
}
