package com.fb.goldencudgel.auditDecisionSystem.repository;


import com.fb.goldencudgel.auditDecisionSystem.domain.systemLog.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog,String> {
	
	@Query(value="SELECT KEYVALUE FROM SYSTEM_CONFIG  WHERE ID='GET_REPORT_FILE_URL'",nativeQuery=true)
    public String findBwceIp();
	
	@Query(value="SELECT KEYVALUE FROM SYSTEM_CONFIG  WHERE ID='AP_SERVER_LOG_PATH'",nativeQuery=true)
    public String findWebPath();
	
	@Query(value="SELECT KEYVALUE FROM SYSTEM_CONFIG  WHERE ID='FRONT_LOG_PATH'",nativeQuery=true)
    public String findFrontPath();

}
