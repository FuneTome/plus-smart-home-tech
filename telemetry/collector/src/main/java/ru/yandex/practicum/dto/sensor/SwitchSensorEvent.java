package ru.yandex.practicum.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SwitchSensorEvent extends SensorEvent {
    @NotNull
    private boolean state;

    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
