package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysFunctionRepository extends JpaRepository<SysFunction, String> {

    /**查询所有启用权限*/
    @Query("FROM SysFunction WHERE parent is null and enabled='1' ORDER BY sortNo")
    public List<SysFunction> viewFunctionByRole();

    /**查询所有启用权限*/
    @Query("FROM SysFunction WHERE code=?1")
    public SysFunction findByFuncode(@Param("code")String code);


    @Query("FROM SysFunction f where f.url=?1")
    public SysFunction findByUrl(@Param("url")String url);


    @Query("from SysFunction where url=?1")
    public List<SysFunction> findFunctionsByUrl(@Param("url")String url);

    @Query("FROM SysFunction WHERE parent is null and authType=?1 ORDER BY sortNo")
    public List<SysFunction> viewFunctionByRoleType(@Param("authType")String authType);
}
