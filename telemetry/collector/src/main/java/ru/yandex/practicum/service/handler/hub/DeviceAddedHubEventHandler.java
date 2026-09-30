package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.serialize.EnumMapper;
import ru.yandex.practicum.service.handler.BaseHubEventHandler;

@Component
public class DeviceAddedHubEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {
    public DeviceAddedHubEventHandler(KafkaProducer producer) {
        super(producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public DeviceAddedEventAvro mapToAvro(HubEventProto event) {
        DeviceAddedEventProto deviceAddedEvent = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(EnumMapper.toDeviceTypeAvro(deviceAddedEvent.getType()))
                .build();
    }
}
