package ru.yandex.practicum.dto.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceAddedEvent extends HubEvent {
    @NotBlank
    private String id;

    @NotNull
    private DeviceType deviceType;

    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }

    public enum DeviceType {
        MOTION_SENSOR,
        TEMPERATURE_SENSOR,
        LIGHT_SENSOR,
        CLIMATE_SENSOR,
        SWITCH_SENSOR
    }
}
