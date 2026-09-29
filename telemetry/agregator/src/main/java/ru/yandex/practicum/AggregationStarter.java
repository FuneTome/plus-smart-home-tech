package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConfiguration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.AggregatorService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final Consumer<String, SensorEventAvro> consumer;
    private final Producer<String, SensorsSnapshotAvro> producer;

    private final AggregatorService aggregatorService;

    private final KafkaConfiguration configuration;

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    public void start() {
        List<String> topics = List.of(configuration.getTopics().getSensorEvents());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Получен сигнал завершения, инициируем остановку...");
            consumer.wakeup();
        }));
        try {
            consumer.subscribe(topics);
            while (true) {
                log.debug("Ожидание новых сообщений...");
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    log.debug("Обработка записи: topic={}, partition={}, offset={}, key={}",
                            record.topic(), record.partition(), record.offset(), record.key());
                    handleRecord(record);
                    manageOffsets(record, count, consumer);
                    count++;
                }
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
                producer.flush();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
                log.info("AggregationStarter завершил работу");
            }
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorEventAvro> record) {
        Optional<SensorsSnapshotAvro> snapshotOpt = aggregatorService.updateState(record.value());
        snapshotOpt.ifPresent(this::sendSnapshot);
    }

    private void sendSnapshot(SensorsSnapshotAvro snapshot) {
        log.info("Отправка снепшота для хаба {} в топик {}", snapshot.getHubId(), configuration.getTopics().getSnapshots());
        log.debug("Детали снепшота: timestamp={}, количество датчиков={}",
                snapshot.getTimestamp(), snapshot.getSensorsState().size());

        ProducerRecord<String, SensorsSnapshotAvro> record = new ProducerRecord<>(
                configuration.getTopics().getSnapshots(),
                null,
                snapshot.getTimestamp().toEpochMilli(),
                snapshot.getHubId(),
                snapshot);
        producer.send(record);
    }

    private void manageOffsets(ConsumerRecord<String, SensorEventAvro> record, int count, Consumer<String, SensorEventAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if(count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if(exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }

}