package ru.yandex.practicum.service.handler;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    private final KafkaProducer producer;

    protected BaseHubEventHandler(KafkaProducer producer) {
        this.producer = producer;
    }

    @Value("${collector.topics.hubs}")
    private String hubsTopic;

    @Override
    public void handle(HubEventProto event) {
        // Проверка соответсвия типа события ожидаемому типу обрботчика
        if (!event.getPayloadCase().equals(getMessageType())) {
            throw new IllegalArgumentException("Неизветсный тип события: " + event.getPayloadCase());
        }

        // Преобразование событие в Avro-запись
        T payload = mapToAvro(event);

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
        producer.send(eventAvro, event.getHubId(), eventAvro.getTimestamp(), hubsTopic);
    }

    public abstract T mapToAvro(HubEventProto event);
}
