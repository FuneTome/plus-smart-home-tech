package ru.yandex.practicum.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TemperatureSensorEvent extends SensorEvent {
    @NotNull
    private int temperatureC;

    @NotNull
    private int temperatureF;

    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
