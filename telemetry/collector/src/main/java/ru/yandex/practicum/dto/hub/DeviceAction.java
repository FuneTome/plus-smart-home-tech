package ru.yandex.practicum.dto.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class DeviceAction {
    @NotBlank
    private String sensorId;

    @NotBlank
    private ActionType type;

    private Integer value;

    public enum ActionType {
        ACTIVATE,
        DEACTIVATE,
        INVERSE,
        SET_VALUE
    }
}
