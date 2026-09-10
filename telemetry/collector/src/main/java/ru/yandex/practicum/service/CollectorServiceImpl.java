package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;
import ru.yandex.practicum.serialize.AvroSerializer;

@Service
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {

    private final HubEventMapper hubEventMapper;
    private final SensorEventMapper sensorEventMapper;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value("${collector.topics.sensors}")
    private String sensorsTopic;

    @Value("${collector.topics.hubs}")
    private String hubsTopic;

    @Override
    public void sendHubEvent(HubEvent event) {
        byte[] payload = AvroSerializer.serialize(hubEventMapper.toAvro(event));
        kafkaTemplate.send(hubsTopic, event.getHubId(), payload);
    }

    @Override
    public void sendSensorEvent(SensorEvent event) {
        byte[] payload = AvroSerializer.serialize(sensorEventMapper.toAvro(event));
        kafkaTemplate.send(sensorsTopic, event.getId(), payload);
    }
}
