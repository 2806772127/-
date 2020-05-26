package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysRoleFunc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RoleFuncRepository extends JpaRepository<SysRoleFunc,String> {


    /**根据角色D和权限类别查找*/
    @Query("FROM SysRoleFunc WHERE  roleId=?1 and authType=?2")
    public List<SysRoleFunc> findByIdAndAuth( @Param("roleId")String roleId,@Param("authType")String authType);
    /**根据角色D查找*/
    @Query("FROM SysRoleFunc WHERE  roleId=?1")
    public List<SysRoleFunc> findByRoleID( @Param("roleId")String roleId);
    /**根据角色和权限类别删除*/
    @Modifying
    @Transactional
    @Query(" delete from SysRoleFunc where roleId=?1 and authType=?2")
    public void deleteByIdAndAuth(@Param("roleId")String roleId,@Param("authType")String authType);

    /**根据角色id删除*/
    @Modifying
    @Transactional
    @Query(" delete from SysRoleFunc where roleId=?1")
    public void deleteByRoleId(@Param("roleId")String roleId);


}
