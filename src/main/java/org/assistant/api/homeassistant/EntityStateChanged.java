package org.assistant.api.homeassistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record EntityStateChanged(
        @JsonProperty("entity_id") String entityId,
        @JsonProperty("state") String state,
        @JsonProperty("attributes") Map<String, Object> attributes,
        @JsonProperty("last_changed") String lastChanged,
        @JsonProperty("last_updated") String lastUpdated) {
}
