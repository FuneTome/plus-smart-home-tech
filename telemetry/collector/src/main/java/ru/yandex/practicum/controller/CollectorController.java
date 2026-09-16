package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.service.CollectorService;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/hubs")
    public void collectHubs(@Valid @RequestBody HubEvent event) {
        collectorService.sendHubEvent(event);
    }

    @PostMapping("/sensors")
    public void collectSensors(@Valid @RequestBody SensorEvent event) {
        collectorService.sendSensorEvent(event);
    }
}
