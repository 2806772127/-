package com.fb.goldencudgel.auditDecisionSystem.repository;


import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface ViewDictItemRepository extends JpaRepository<ViewDataDictItem, String> {

    @Modifying
    @Transactional
    @Query(" delete from ViewDataDictItem where dictId=:dictId and itemCode=:itemCode")
    public void deleteByIdAndCode(@Param("dictId")String dictId,@Param("itemCode")String itemCode);

    @Query(" from ViewDataDictItem where dictId=:dictId and itemCode=:itemCode and itemName=:itemName")
    public ViewDataDictItem findViewDataDictItem(@Param("dictId")String dictId,@Param("itemCode")String itemCode,@Param("itemName")String itemName);

    @Query(" from ViewDataDictItem where dictId=:dictId order by itemRemark")
    public List<ViewDataDictItem> findByDictId(@Param("dictId")String dictId);

    @Query("select itemName from ViewDataDictItem where dictId=:dictId and itemCode=:itemCode")
    public String findItemName(@Param("dictId")String dictId,@Param("itemCode")String itemCode);

    @Query("from ViewDataDictItem where dictId=:dictId and itemCode like :itemCode")
    public List<ViewDataDictItem> findItemCode(@Param("dictId")String dictId,@Param("itemCode")String itemCode);

    @Query("from ViewDataDictItem where dictId=:dictId and itemCode not like :itemCode")
    public List<ViewDataDictItem> findNotItemCode(@Param("dictId")String dictId,@Param("itemCode")String itemCode);
    
    @Query(" from ViewDataDictItem where dictId=:dictId and itemCode=:itemCode")
    public ViewDataDictItem findViewDataDictItemCode(@Param("dictId") String dictId, @Param("itemCode") String itemCode);

    @Modifying
    @Transactional
    @Query(" delete from ViewDataDictItem where dictId=:dictId")
    public void deleteByDictId(@Param("dictId")String dictId);

}
