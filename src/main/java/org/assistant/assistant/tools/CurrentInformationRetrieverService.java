package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;

import java.time.LocalDateTime;

public class CurrentInformationRetrieverService {

    @Tool("Retrieves the latest news.")
    public String retrieveNews() {
        return "Latest News: President proclaims April 10th is a holiday.";
    }
    @Tool("Retrieves the latest weather.")
    public String retrieveWeather() {
        return "Weather today is 34°C°F\n" +
                "Precipitation: 10%\n" +
                "Humidity: 67%\n" +
                "Wind: 11 km/h";
    }

    @Tool("Retrieves the date and time.")
    public String retrieveTime() {
        return LocalDateTime.now().toString();
    }
}
