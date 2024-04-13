package org.assistant.assistant.core.assistants;

import dev.langchain4j.model.output.structured.Description;

public enum Intent {

    @Description("An intent of conversing or asking questions.")
    CONVERSATIONAL_INTENT,

    @Description("An intent of toggling a smart outlet either on or off.")
    TOGGLE_OUTLET_INTENT,

    @Description("An intent of inquiry regarding today's weather.")
    INQUIRE_WEATHER_INTENT,

    @Description("An intent of inquiry regarding today's date and/or time.")
    INQUIRE_DATETIME_INTENT,

    @Description("An intent of inquiry regarding today's news.")
    INQUIRE_NEWS_INTENT,

    @Description("An intent of seeing via a camera captured still image as of the moment.")
    SEE_INTENT
}