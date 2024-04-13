package org.assistant.assistant.core.assistants;

import dev.langchain4j.model.output.structured.Description;

public enum Intent {

    @Description("An intent of conversing with someone as a chatbot.")
    CONVERSATIONAL_INTENT,

    @Description("An intent of toggling a smart outlet either on or off.")
    TOGGLE_OUTLET_INTENT,

    @Description("An intent of inquiry regarding today's weather such as temperature and humidity.")
    INQUIRE_WEATHER_INTENT,

    @Description("An intent of inquiry regarding today's date and/or time.")
    INQUIRE_DATETIME_INTENT,

    @Description("An intent of inquiry regarding today's news.")
    INQUIRE_NEWS_INTENT,

    @Description("An intent of seeing via a camera captured still image as of the moment. Thus, you can describe things around you.")
    SEE_INTENT;

    public boolean isConversational() {
        return CONVERSATIONAL_INTENT.equals(this);
    }
}