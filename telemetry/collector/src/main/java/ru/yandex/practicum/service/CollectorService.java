package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;

public interface CollectorService {
    void sendHubEvent(HubEvent event);

    void sendSensorEvent(SensorEvent event);
}
