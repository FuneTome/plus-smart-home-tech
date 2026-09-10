package ru.yandex.practicum.dto.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceRemovedEvent extends HubEvent {
    @NotBlank
    private String id;

    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
