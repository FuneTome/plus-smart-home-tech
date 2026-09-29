package ru.yandex.practicum.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaConfiguration {

    private String bootstrapServers;
    private String groupId;
    private Topics topics = new Topics();

    @Data
    public static class Topics {
        private String sensorEvents;
        private String snapshots;
    }
}