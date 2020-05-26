package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.user.UserList;
import com.fb.goldencudgel.auditDecisionSystem.domain.user.UserListPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserListRepository extends JpaRepository<UserList, UserListPK> {

    @Query("from UserList where userGroup=:userGroup and userType = 'C' and leaveFlag <> 1")
    List<UserList> findTeamUser(@Param("userGroup")String userGroup);

    @Query("from UserList where userCode=:userCode and leaveFlag <> 1")
    List<UserList> findByUserCode(@Param("userCode")String userCode);
}
