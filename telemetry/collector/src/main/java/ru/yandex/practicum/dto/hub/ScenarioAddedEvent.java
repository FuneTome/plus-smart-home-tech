package ru.yandex.practicum.dto.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    private String name;

    private List<ScenarioCondition> conditions;

    private List<DeviceAction> actions;

    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
