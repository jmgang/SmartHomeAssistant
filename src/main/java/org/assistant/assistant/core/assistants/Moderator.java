package org.assistant.assistant.core.assistants;

import dev.langchain4j.service.Moderate;

public interface Moderator {
    @Moderate
    String moderate(String query);
}
