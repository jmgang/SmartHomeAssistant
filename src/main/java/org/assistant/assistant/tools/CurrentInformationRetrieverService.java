package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;
import org.assistant.api.news.News;
import org.assistant.api.news.NewsRetriever;
import org.assistant.api.weather.Weather;
import org.assistant.api.weather.WeatherRetriever;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrentInformationRetrieverService {

    @Tool("Retrieves the latest news. Summarize the highlights into 2 lines only.")
    public String retrieveNews() {
        final StringBuilder collectedNews = new StringBuilder();
        final NewsRetriever newsRetriever = new NewsRetriever();
        final Optional<News> optionalRetrievedNews = newsRetriever.retrieveTopStories();

        if( optionalRetrievedNews.isPresent() && optionalRetrievedNews.get().meta().found() > 0 ) {
            News retrievedNews = optionalRetrievedNews.get();
            collectedNews.append("Found " ).append(retrievedNews.meta().limit()).append(" news.\n");
            collectedNews.append(
                    retrievedNews.data().stream().map(news -> news.title() + ". Details: " + news.description() + ". Source: " +
                            news.source() + ". Published at: " + news.publishedAt()).collect(Collectors.joining("\n"))
            );

            return collectedNews.toString();
        }

        return "No news found at the moment.";
    }
    @Tool("Retrieves the latest weather data including the temperature, feels like, humidity, wind, etc.")
    public String retrieveWeather() {
        final WeatherRetriever weatherRetriever = new WeatherRetriever();
        Optional<Weather> currentWeather = weatherRetriever.retrieveCurrentWeather();

        if(currentWeather.isPresent()) {
            return currentWeather.get().toString();
        }

        return "No weather data available at the moment.";
    }

    @Tool("Retrieves the date and time.")
    public String retrieveTime() {
        return LocalDateTime.now().toString();
    }

}
