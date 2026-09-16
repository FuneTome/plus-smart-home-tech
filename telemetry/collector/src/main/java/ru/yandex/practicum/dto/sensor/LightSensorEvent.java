package ru.yandex.practicum.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LightSensorEvent extends SensorEvent {
    @NotNull
    private int linkQuality;

    @NotNull
    private int luminosity;

    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
