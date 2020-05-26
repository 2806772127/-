package com.fb.goldencudgel.auditDecisionSystem.repository;


import com.fb.goldencudgel.auditDecisionSystem.domain.user.FbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FbUserRepository extends JpaRepository<FbUser, String> {

    @Query("select distinct userCode from FbUser where userArea=:userArea and userGroup=:userGroup")
    public List<String> getUserCodeByGroup(@Param("userArea")String userArea, @Param("userGroup")String userGroup);

    @Query("select distinct userCode from FbUser where userArea=:userArea")
    public List<String> getUserCodeByArea(@Param("userArea")String userArea);


    @Query("select distinct userCode from FbUser")
    public List<String> getUserCodeByAll();
}
