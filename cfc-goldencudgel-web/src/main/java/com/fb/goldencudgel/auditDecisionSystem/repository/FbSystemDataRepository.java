package com.fb.goldencudgel.auditDecisionSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.systemData.FbSystemData;

/**
 * @author mazongjian
 * @createdDate 2019年8月17日 - 下午2:37:44 
 */
@Repository
public interface FbSystemDataRepository extends JpaRepository<FbSystemData, String>  {

}
