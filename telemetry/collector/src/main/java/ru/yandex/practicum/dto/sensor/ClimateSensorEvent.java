package ru.yandex.practicum.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClimateSensorEvent extends SensorEvent {
    @NotNull
    private int temperatureC;

    @NotNull
    private int humidity;

    @NotNull
    private int co2Level;

    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
