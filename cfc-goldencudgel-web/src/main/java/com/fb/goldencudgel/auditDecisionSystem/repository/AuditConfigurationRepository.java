package com.fb.goldencudgel.auditDecisionSystem.repository;
import com.fb.goldencudgel.auditDecisionSystem.domain.auditConfiguration.AuditConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;


@Repository
public interface AuditConfigurationRepository extends JpaRepository<AuditConfiguration, String> {
	
   @Query(value="SELECT FAC.APPROVE_ID,FAC.FUNCTION_CODE,FAC.FUNCTION_NAME,FAC.AGENT_USER_CODE,FAC.AGENT_USER_NAME,FAC.APPROVE_USER_CODE,FAC.APPROVE_USER_NAME,FAC.CREATE_TIME,FAC.CREATE_USER,FAC.UPDATE_TIME,FAC.UPDATE_USER,FAC.AGENT_USER_AREA,FAC.AGENT_USER_GROUP,FAC.APPROVE_USER_AREA,FAC.APPROVE_USER_GROUP FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.FUNCTION_CODE=:functionCode AND FAC.AGENT_USER_CODE=:agentUserCode AND FAC.APPROVE_USER_CODE=:approveUserCode",nativeQuery=true)
   AuditConfiguration findByCondition(@Param("functionCode")String functionCode,@Param("agentUserCode")String agentUserCode,@Param("approveUserCode")String approveUserCode);

   @Modifying
   @Transactional
   @Query(" delete from AuditConfiguration where APPROVE_ID=:approveId")
   public void deleteByApproveId(@Param("approveId")String approveId);
   
   @Query("from AuditConfiguration where approveId=?1")
   AuditConfiguration querybyApproveId(@Param("approveId")String approveId);
   
   @Query(value="SELECT AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.AGENT_USER_CODE=:agentUserCode OR FAC.APPROVE_USER_CODE=:approveUserCode",nativeQuery=true)
   public List<String> queryByCodes(@Param("agentUserCode")String agentUserCode,@Param("approveUserCode")String approveUserCode);
   
   @Query(value="SELECT APPROVE_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.APPROVE_USER_CODE=:approveUserCode",nativeQuery=true)
   public List<String> queryByApproveUserCode(@Param("approveUserCode")String approveUserCode);
   
   @Query(value="SELECT AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.AGENT_USER_CODE=:agentUserCode",nativeQuery=true)
   public List<String> queryByAgentCode(@Param("agentUserCode")String agentUserCode);
   
   @Query(value="SELECT AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.APPROVE_USER_CODE=:approveUserCode",nativeQuery=true)
   public List<String> queryAgentByApproveUserCode(@Param("approveUserCode")String approveUserCode);
   
   @Query(value="SELECT FAC.AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND (FAC.AGENT_USER_CODE=:agentUserCode AND FAC.APPROVE_USER_CODE=:approveUserCode) OR (FAC.AGENT_USER_CODE=:agentUserCodeTwo AND FAC.APPROVE_USER_CODE=:approveUserCodeTwo)",nativeQuery=true)
   public List<String> findHasedAgentUserCode(@Param("agentUserCode")String agentUserCode,@Param("approveUserCode")String approveUserCode,@Param("agentUserCodeTwo")String agentUserCodeTwo,@Param("approveUserCodeTwo")String approveUserCodeTwo);
 
   
   @Query(value="SELECT FAC.AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND (FAC.AGENT_USER_CODE=:agentUserCode OR FAC.APPROVE_USER_CODE=:approveUserCode) OR (FAC.AGENT_USER_CODE=:agentUserCodeTwo OR FAC.APPROVE_USER_CODE=:approveUserCodeTwo)",nativeQuery=true)
   public List<String> findByAgentAndApprove(@Param("agentUserCode")String agentUserCode,@Param("approveUserCode")String approveUserCode,@Param("agentUserCodeTwo")String agentUserCodeTwo,@Param("approveUserCodeTwo")String approveUserCodeTwo);
  
   
   @Query(value="SELECT FAC.AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND ((FAC.AGENT_USER_CODE=:agentUserCode OR FAC.APPROVE_USER_CODE=:approveUserCode) AND FAC.FUNCTION_CODE=:functionCode) OR ((FAC.AGENT_USER_CODE=:agentUserCodeTwo OR FAC.APPROVE_USER_CODE=:approveUserCodeTwo) AND FAC.FUNCTION_CODE=:functionCodeTwo)",nativeQuery=true)
   public List<String> findByAgentAndApproveFunction(@Param("agentUserCode")String agentUserCode,@Param("approveUserCode")String approveUserCode,@Param("functionCode")String functionCode,@Param("agentUserCodeTwo")String agentUserCodeTwo,@Param("approveUserCodeTwo")String approveUserCodeTwo,@Param("functionCodeTwo")String functionCodeTwo);
  
   
   @Query(value="SELECT AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1  AND FAC.AGENT_USER_CODE=:agentUserCode",nativeQuery=true)
   public List<String> queryByAgentUserCode(@Param("agentUserCode")String agentUserCode);
   
   @Query(value="SELECT AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1  AND FAC.APPROVE_USER_CODE=:agentUserCode",nativeQuery=true)
   public List<String> queryByApproUserCode(@Param("agentUserCode")String agentUserCode);
   
   @Query(value="SELECT AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.AGENT_USER_CODE=:approveUserCode",nativeQuery=true)
   public String queryAgentByApproveCode(@Param("approveUserCode")String approveUserCode);
   
   @Query(value="SELECT APPROVE_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.APPROVE_USER_CODE=:approveUserCode",nativeQuery=true)
   public List<String> queryApproveByApprove(@Param("approveUserCode")String approveUserCode);
   
   @Query(value="SELECT AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.APPROVE_USER_CODE=:approveUserCode",nativeQuery=true)
   public String queryAgentUserByApproveUser(@Param("approveUserCode")String approveUserCode);
   
   @Query(value="SELECT APPROVE_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.AGENT_USER_CODE=:agentUserCode AND FAC.FUNCTION_CODE=:functionCode ",nativeQuery=true)
   public String queryApproveUserByAgentUser(@Param("agentUserCode")String agentUserCode,@Param("functionCode")String functionCode);
   
   @Query(value="SELECT APPROVE_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.AGENT_USER_CODE=:agentUserCode ",nativeQuery=true)
   public String queryApproveUserByAgentUserForProduct(@Param("agentUserCode")String agentUserCode);
   
   @Query(value="SELECT USER_CODE FROM FB_USER FAC WHERE 1=1 AND FAC.USER_TYPE='CA' ",nativeQuery=true)
   public List<String> queryUserType();
   
   @Query(value="SELECT USER_TYPE FROM FB_USER FAC WHERE 1=1 AND FAC.USER_CODE=:userCode ",nativeQuery=true)
   public String queryUserTypeByUserCode(@Param("userCode")String userCode);
   
   @Query(value="SELECT AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 AND FAC.AGENT_USER_CODE=:agentUserCode AND FAC.APPROVE_USER_CODE=:approveUserCode",nativeQuery=true)
   public List<String> queryByAgentAndApprove(@Param("agentUserCode")String agentUserCode,@Param("approveUserCode")String approveUserCode);
   
   @Query(value="SELECT COUNT(*) FROM FB_APPROVE_CONFIG A WHERE  A.FUNCTION_CODE=:functionCode AND A.AGENT_USER_CODE=:agentUserCode ",nativeQuery=true)
   public String queryCount(@Param("functionCode")String functionCode,@Param("agentUserCode")String agentUserCode);
   
   @Query(value="SELECT AGENT_USER_CODE FROM FB_APPROVE_CONFIG FAC WHERE 1=1 ",nativeQuery=true)
   public List<String> queryByAll();
}
