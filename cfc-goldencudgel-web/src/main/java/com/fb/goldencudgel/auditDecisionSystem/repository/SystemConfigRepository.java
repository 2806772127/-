package com.fb.goldencudgel.auditDecisionSystem.repository;

 import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
  import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.query.Param;
 import org.springframework.stereotype.Repository;


@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {

    @Query("from SystemConfig where id=:id")
    SystemConfig findByID(@Param("id")String id);

}
