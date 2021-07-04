package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id = :id and m.user.id = :userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.id = :id AND m.user.id = :userId")
    Meal getMealByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId")
    List<Meal> getAllSortedByUserId(@Param("userId") int userId, Sort sort);

    @Query("""
            SELECT m FROM Meal m WHERE m.user.id=:userId
            AND m.dateTime >= :startDateTime AND m.dateTime < :endDateTime
            """)
    List<Meal> getMealsBetweenHalfOpen(@Param("startDateTime") LocalDateTime startDateTime,
                                       @Param("endDateTime") LocalDateTime endDateTime,
                                       @Param("userId") int userId,
                                       Sort sort);
    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id = :id AND m.user.id = :userId")
    Meal getMealWithUser(@Param("id") int id, @Param("userId") int userId);
}
