package ru.yandex.practicum.service.handler;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    private final KafkaProducer producer;

    @Value("${collector.topics.sensors}")
    private String sensorsTopic;

    @Override
    public void handle(SensorEventProto event) {
        if (!event.getPayloadCase().equals(getMessageType())) {
            throw new IllegalArgumentException("Неизветсный тип события: " + event.getPayloadCase());
        }

        T payload = mapToAvro(event);

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(Instant.ofEpochSecond(
                        event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
        producer.send(eventAvro, event.getHubId(), eventAvro.getTimestamp(), sensorsTopic);
    }

    public abstract T mapToAvro(SensorEventProto event);
}
