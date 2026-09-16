package ru.yandex.practicum.dto.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ScenarioRemovedEvent extends HubEvent {
    @NotBlank
    private String name;

    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
