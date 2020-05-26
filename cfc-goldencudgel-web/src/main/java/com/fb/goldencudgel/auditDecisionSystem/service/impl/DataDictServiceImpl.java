package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.DataDictItem;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDict;
import com.fb.goldencudgel.auditDecisionSystem.repository.DataDictRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.IDataDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Auther hu
 */
@Service
@Transactional
public class DataDictServiceImpl extends BaseJpaDAO implements IDataDictService {

    @Autowired
    private DataDictRepository dataDictRepository;

    @Override
    public List<ViewDataDict> getAllDataDict() {
        return dataDictRepository.findAll();
    }
    public QueryPage<Object[]> findByPage(int curPage) {
        return findBySQL("select DICT_ID,DICT_ITEM_ID from DATA_DICT_ITEM",new QueryPage<Object[]>(curPage,20) );
    };

    /**
     * Ŀǰû��
     * @param dictId
     * @param queryPage
     * @return
     */
    public QueryPage<Object[]> findByCodeAndPage(String dictId, QueryPage<Object[]> queryPage) {

        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT D.ITEM_CODE,D.ITEM_NAME,C.ITEM_CODE_C,C.ITEM_NAME_C FROM VIEW_DATA_DICT_ITEM D ");
        sql.append(" INNER JOIN (SELECT A.ITEM_CODE ITEM_CODE_C,A.ITEM_NAME ITEM_NAME_C,A.DICT_ID FROM VIEW_DATA_DICT_ITEM A  INNER JOIN VIEW_DATA_DICT B ON A.DICT_ID = B.DICT_ID WHERE 1=1 " +
                "AND B.DICT_ID ='DATA_FA') C ");//DATA_FA��Ҫ�����޸�
        sql.append(" ON D.DICT_ID = C.ITEM_CODE_C ");
        if (StringUtils.isNoneBlank(dictId) && !"nothing".equals(dictId)){
            sql.append(" AND D.DICT_ID = :dictId");
            params.put("dictId", dictId);
        }
        queryPage = findBySQL(sql,queryPage,params);

        return  queryPage;
    }

    /**
     * 查找主层下子层
     * @param dictId
     * @param queryPage
     * @return
     */
    public QueryPage<Object[]> findByDictid(String dictId, QueryPage<Object[]> queryPage) {

        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT A.ITEM_CODE,A.ITEM_NAME,B.DICT_ID,B.DICT_NAME FROM VIEW_DATA_DICT_ITEM A  INNER JOIN VIEW_DATA_DICT B ON A.DICT_ID = B.DICT_ID WHERE 1=1 ");
        if (StringUtils.isNoneBlank(dictId) && !"nothing".equals(dictId)){
            sql.append(" AND B.DICT_ID = :dictId");
            params.put("dictId", dictId);
        }
        QueryPage<Object[]> result = findBySQL(sql,queryPage,params);
        return  result;
    }

    /**
     * 查找主层
     * @param dictId
     * @return
     */
    public QueryPage<Object[]> findDateDice(String dictId) {

        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT B.DICT_ID,B.DICT_NAME FROM VIEW_DATA_DICT B  WHERE ifnull(B.CRUDFLAG,'') = '' ");
        if (StringUtils.isNoneBlank(dictId) && !"nothing".equals(dictId)){
            sql.append(" AND B.DICT_ID = :dictId");
            params.put("dictId", dictId);
        }
        QueryPage<Object[]> result = findBySQL(sql,params);
        return  result;
    }


    /**
     * 根据id查找子层
     * @param dictId
     * @param queryPage
     * @return
     */
    public QueryPage<Object[]> findByChildDictid(String dictId, QueryPage<Object[]> queryPage) {

        Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT A.ITEM_CODE,A.ITEM_NAME,B.DICT_ID,B.DICT_NAME FROM VIEW_DATA_DICT_ITEM A  INNER JOIN VIEW_DATA_DICT B ON A.DICT_ID = B.DICT_ID WHERE 1=1 ");
        sql.append(" AND B.DICT_ID = :dictId");
        params.put("dictId", dictId);
        QueryPage<Object[]> result = findBySQL(sql,queryPage,params);
        return  result;
    }
    public List<Object[]> findProdList() {
		Map<String,Object> params =  new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT D.ITEM_CODE,D.ITEM_NAME FROM VIEW_DATA_DICT_ITEM D WHERE D.DICT_ID='COM_PRODS' ORDER BY D.ITEM_CODE ASC ");
		return findBySQL(sql,params).getContent();
	}

}
