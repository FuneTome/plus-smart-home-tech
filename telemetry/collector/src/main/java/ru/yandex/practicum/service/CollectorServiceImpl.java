package ru.yandex.practicum.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {

    private final HubEventMapper hubEventMapper;
    private final SensorEventMapper sensorEventMapper;
    private final Producer<String, SpecificRecordBase> producer;

    @Value("${collector.topics.sensors}")
    private String sensorsTopic;

    @Value("${collector.topics.hubs}")
    private String hubsTopic;

    @Override
    public void sendHubEvent(HubEvent event) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                hubsTopic,
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                hubEventMapper.toAvro(event)
        );
        producer.send(record);
    }

    @Override
    public void sendSensorEvent(SensorEvent event) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                sensorsTopic,
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                sensorEventMapper.toAvro(event)
        );
        producer.send(record);
    }

    @PreDestroy
    public void shutdown() {
        producer.flush();
        producer.close(Duration.ofSeconds(10));
    }
}
