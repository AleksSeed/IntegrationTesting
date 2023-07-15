package ru.skypro.lessons.springboot.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.lessons.springboot.springboot.pojo.Position;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    @Query(value = "SELECT * FROM position WHERE name = :name", nativeQuery = true)
    Position getPositionByName(@Param("name") String name);
}