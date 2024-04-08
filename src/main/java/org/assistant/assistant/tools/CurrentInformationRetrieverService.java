package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;
import org.assistant.api.news.News;
import org.assistant.api.news.NewsRetriever;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrentInformationRetrieverService {

    @Tool("Retrieves the latest news.")
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
