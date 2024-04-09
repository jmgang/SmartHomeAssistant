package org.assistant.api.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Weather(Location location, Current current) {

    public record Location(
            @JsonProperty("name") String name,
            @JsonProperty("region") String region,
            @JsonProperty("country") String country,
            @JsonProperty("lat") double lat,
            @JsonProperty("lon") double lon,
            @JsonProperty("tz_id") String tzId,
            @JsonProperty("localtime_epoch") long localtimeEpoch,
            @JsonProperty("localtime") String localtime) {}

    public record Current(
            @JsonProperty("last_updated") String lastUpdated,
            @JsonProperty("temp_c") double tempC,
            @JsonProperty("temp_f") double tempF,
            @JsonProperty("is_day") int isDay,
            @JsonProperty("condition") Condition condition,
            @JsonProperty("wind_kph") double windKph,
            @JsonProperty("humidity") int humidity,
            @JsonProperty("cloud") int cloud,
            @JsonProperty("feelslike_c") double feelslikeC) {}

    public record Condition(@JsonProperty("text") String text) {}

}




