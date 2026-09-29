package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.model.ScenarioCondition;

import java.util.List;

public interface ScenarioConditionsRepository extends JpaRepository<ScenarioCondition, Long> {
    List<ScenarioCondition> findByScenarioId(Long scenarioId);

    @Modifying
    @Query("DELETE FROM ScenarioCondition sc WHERE sc.scenario.id = :scenarioId")
    void deleteByScenarioId(@Param("scenarioId") Long scenarioId);
}
