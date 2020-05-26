package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.fbProduct.FbProduct;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FbProductRepository extends JpaRepository<FbProduct, String> {

    @Query(value="select distinct PROD_NAME from FB_PRODUCT",nativeQuery=true)
    List<String> queryProdName();
    
    /*@Query(value="SELECT QUESTION_ID FROM FB_PROD_QUESTION WHERE 1=1 AND VERSION_NUM=:productVersion ",nativeQuery=true)
    List<String> queryProductVersion(@Param("productVersion")String productVersion);*/
    
    /*@Query(value="SELECT QUESTION_TYPE FROM FB_PROD_QUESTION WHERE 1=1 AND VERSION_NUM=:productVersion",nativeQuery=true)
    List<String> queryQuestionType(@Param("productVersion")String productVersion);*/
    
    @Query("from FbProduct where current_date >= estimatedLaunchTime and auditState = '01' and onlineState <> '01'")
    List<FbProduct> findOnlineProd();
    
    @Query("from FbProduct where PROD_ID=:prodId ")
    FbProduct findProductByProdId(@Param("prodId")String prodId);
    
    @Query(value="SELECT AUDIT_STATE FROM FB_PRODUCT WHERE 1=1 AND CREATE_USER=:agentUserCode ",nativeQuery=true)
    String queryAuditState(@Param("agentUserCode")String agentUserCode);
   
}
