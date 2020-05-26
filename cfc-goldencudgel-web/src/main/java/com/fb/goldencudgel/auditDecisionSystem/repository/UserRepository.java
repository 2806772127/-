package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.user.FbUser;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<FbUser, String> {

    @Query("from FbUser where userCode =:userCode")
    public FbUser findByUserCode(@Param("userCode") String userCode);

    @Modifying
    @Transactional
    @Query("update FbUser set messageCount =:messageCount where userCode=:userCode")
	public void updateMessageCount(@Param("messageCount")Integer count,@Param("userCode") String userCode);

}
