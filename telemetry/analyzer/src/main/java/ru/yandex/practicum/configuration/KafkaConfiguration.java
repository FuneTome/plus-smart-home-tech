package ru.yandex.practicum.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaConfiguration {

    private String bootstrapServers;

    private Consumer consumer = new Consumer();

    @Data
    public static class Consumer {
        private Snapshot snapshot = new Snapshot();
        private Hub hub = new Hub();
    }

    @Data
    public static class Snapshot {
        private String groupId;
        private String keyDeserializer;
        private String valueDeserializer;
        private boolean enableAutoCommit;
    }

    @Data
    public static class Hub {
        private String groupId;
        private String keyDeserializer;
        private String valueDeserializer;
        private boolean enableAutoCommit;
        private String autoCommitIntervalMs;
    }
}