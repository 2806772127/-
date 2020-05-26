package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.report.TaskComplishStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskComplishStatisticsRepository extends JpaRepository<TaskComplishStatistics,String> {

    @Query(" from TaskComplishStatistics where accountExecutiver =:accountExecutiver and statisticsYear =:statisticsYear and statisticsMonth =:statisticsMonth")
    public List<TaskComplishStatistics> findByUser(@Param("accountExecutiver")String accountExecutiver,@Param("statisticsYear")String statisticsYear,@Param("statisticsMonth")String statisticsMonth);

    @Query(" from TaskComplishStatistics where teamCode =:teamCode and statisticsYear =:statisticsYear and statisticsMonth =:statisticsMonth")
    public List<TaskComplishStatistics> findByGroup(@Param("teamCode")String teamCode,@Param("statisticsYear")String statisticsYear,@Param("statisticsMonth")String statisticsMonth);

    @Query(" from TaskComplishStatistics where areaCode =:areaCode and statisticsYear =:statisticsYear and statisticsMonth =:statisticsMonth")
    public List<TaskComplishStatistics> findByArea(@Param("areaCode")String areaCode,@Param("statisticsYear")String statisticsYear,@Param("statisticsMonth")String statisticsMonth);

    @Query(" from TaskComplishStatistics where statisticsYear =:statisticsYear and statisticsMonth =:statisticsMonth")
    public List<TaskComplishStatistics> findAll(@Param("statisticsYear")String statisticsYear,@Param("statisticsMonth")String statisticsMonth);

}
