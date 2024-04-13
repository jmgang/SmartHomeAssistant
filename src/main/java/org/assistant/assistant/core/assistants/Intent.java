package org.assistant.assistant.core.assistants;

import dev.langchain4j.model.output.structured.Description;

public enum Intent {

    @Description("An intent of solely conversing or asking questions.")
    CONVERSATIONAL,

    @Description("An intent of solely performing an action or command.")
    ACTION
}