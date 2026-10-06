package ru.yandex.practicum.service.handler.sensor;

import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.model.ConditionType;

public interface SensorEventHandler {
    String getType();

    Integer getValue(ConditionType condition, SensorStateAvro snapshot);
}
