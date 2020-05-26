package com.fb.goldencudgel.auditDecisionSystem.service;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDict;

import java.util.List;

/**
 * 数据字典service接口
 * @Auther hu
 */
public interface IDataDictService {

   public List<ViewDataDict> getAllDataDict();

   public QueryPage<Object[]> findByCodeAndPage(String dictId, QueryPage<Object[]> queryPage);

   public QueryPage<Object[]> findByPage(int curPage);

   public List<Object[]> findProdList();

}
