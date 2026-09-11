package ru.yandex.practicum.dto.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScenarioCondition {
    @NotBlank
    private String sensorId;

    @NotBlank
    private ConditionType type;

    @NotBlank
    private ConditionOperation operation;

    private Integer value;

    public enum ConditionType {
        MOTION,
        LUMINOSITY,
        SWITCH,
        TEMPERATURE,
        CO2LEVEL,
        HUMIDITY
    }

    public enum ConditionOperation {
        EQUALS,
        GREATER_THAN,
        LOWER_THAN
    }
}
