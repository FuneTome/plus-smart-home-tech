package ru.yandex.practicum.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MotionSensorEvent extends SensorEvent {
    @NotNull
    private int linkQuality;

    @NotNull
    private boolean motion;

    @NotNull
    private int voltage;

    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
