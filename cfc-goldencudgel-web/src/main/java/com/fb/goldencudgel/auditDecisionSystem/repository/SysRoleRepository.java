package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, String> {
    /** 根据角色名称查找角色Role.
     *
     * @param name the name
     * @return the list
     */
    @Query("from SysRole WHERE name = ?1")
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<SysRole> findRoleByName(String name);

    @Query("from SysRole WHERE id = ?1")
    public SysRole findRoleById(String id);

}
