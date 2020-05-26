package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDictItem;
import com.fb.goldencudgel.auditDecisionSystem.domain.message.Message;
import com.fb.goldencudgel.auditDecisionSystem.domain.message.MessageReceive;
import com.fb.goldencudgel.auditDecisionSystem.domain.user.FbUser;
import com.fb.goldencudgel.auditDecisionSystem.repository.MessageReceiveRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.MessageRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.UserRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.ViewDictItemRepository;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.ServiceUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Service
@Transactional
public class MessageServiceImpl extends BaseJpaDAO{
	
	@Autowired
	private ViewDictItemRepository viewDictItemRepository;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private MessageReceiveRepository messageReceiveRepository;
	
	@Autowired
	private UserRepository userRepository;

	@SuppressWarnings("unchecked")
	public QueryPage<Object[]> findByConditions(String messageType, Date startDate, Date endDate,
			QueryPage<Object[]> queryPage) {
		StringBuffer sb = new StringBuffer("SELECT DISTINCT FM.MESSAGE_TYPE_NAME,FM.MESSAGE_KEYNOTE,FM.MESSAGE_CONTEXT,FM.ACCPECT_USER,FM.SEND_TIME,FR.READ_TIME,FM.MESSAGE_ID,FR.ID FROM FB_MESSAGE FM ");
		sb.append("LEFT JOIN FB_MESSAGE_RECEIVE FR ON FM.MESSAGE_ID = FR.MESSAGE_ID  WHERE 1=1");
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNoneBlank(messageType)) {
			sb.append(" AND FM.MESSAGE_TYPE=:messageType");
			params.put("messageType", messageType);
		}
		if(startDate!=null) {
			sb.append(" and FM.SEND_TIME >=:startDate");
			params.put("startDate", startDate);
		}
		if(endDate!=null) {
			sb.append(" and FM.SEND_TIME <=:endDate");
			params.put("endDate", endDate);
		}
		sb.append(" AND FR.ACCPECT_USER =:userCode");
		params.put("userCode", UserUtil.getCurrUser().getUserCode());
		
		sb.append(" ORDER BY FM.SEND_TIME DESC,FR.MESSAGE_ID ASC");
		queryPage = findBySQL(sb, queryPage, params);
		return queryPage;
	}

	//用来查询列表结果数据是否有数据是否没有readtime
	@SuppressWarnings("unchecked")
	public QueryPage<Object[]> findByIsHasReadTime(
			QueryPage<Object[]> queryPage) {
		StringBuffer sb = new StringBuffer("SELECT count(*) AS count FROM FB_MESSAGE FM ");
		sb.append("LEFT JOIN FB_MESSAGE_RECEIVE FR ON FM.MESSAGE_ID = FR.MESSAGE_ID  WHERE 1=1");
		Map<String,Object> params = new HashMap<String,Object>();
		sb.append(" AND FR.ACCPECT_USER =:userCode");
		sb.append(" AND ISNULL(FR.READ_TIME)");
		params.put("userCode", UserUtil.getCurrUser().getUserCode());
		
		sb.append(" ORDER BY FR.MESSAGE_ID ASC");
		queryPage = findBySQL(sb, queryPage, params);
		return queryPage;
	}

	public List<ViewDataDictItem> getMessageType() {
		return viewDictItemRepository.findByDictId("MESSAGE_TYPE");
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getUserInfo(String userAreas, String userGroups, String users) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer("SELECT ");
		sql.append(" CASE WHEN (ISNULL(USER_AREA_NAME) = 1) || (LENGTH(trim(USER_AREA_NAME)) = 0) THEN '新興客群處' ELSE USER_AREA_NAME END USER_AREA_NAME, ");
        sql.append(" USER_GROUP_NAME, ");
        sql.append(" GROUP_CONCAT(USER_NAME) AS USER_NAME ");
        sql.append(" from USER_LIST WHERE 1=1 ");
		if(StringUtils.isNoneBlank(userAreas)) {
			sql.append(" AND IFNULL(USER_AREA,'') IN :areas");
			List<String> areaList = ServiceUtil.setParamList(userAreas);
			// 如果区域里面包含188AA时，将处长也查出来(处长的区域为空)
            if (userAreas.contains("188AA")) {
                areaList.add("");
            }
			params.put("areas", areaList);
		}
		if(StringUtils.isNoneBlank(userGroups)) {
			sql.append(" AND IFNULL(USER_GROUP,'') IN :groups");
			List<String> groupList = ServiceUtil.setParamList(userGroups);
			groupList.add("");//添加空的区防止处长查询不出来
			params.put("groups", groupList);
		}
		if(StringUtils.isNoneBlank(users)) {
			sql.append(" AND USER_CODE IN :users");
			List<String> userList = ServiceUtil.setParamList(users);
			params.put("users", userList);
		}
		sql.append("  GROUP BY USER_AREA_NAME,USER_GROUP_NAME ");
		return findBySQL(sql, params).getContent();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getUserInfoHr(String users) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer("SELECT USER_AREA_NAME,USER_GROUP_NAME,GROUP_CONCAT(USER_NAME) AS USER_NAME from FB_USER WHERE 1=1 ");
		if(StringUtils.isNoneBlank(users)) {
			sql.append(" AND USER_CODE IN :users");
			List<String> userList = ServiceUtil.setParamList(users);
			params.put("users", userList);
		}
		sql.append("  GROUP BY USER_AREA_NAME,USER_GROUP_NAME ");
		return findBySQL(sql, params).getContent();
	}
	
	@SuppressWarnings("unchecked")
    public List<Object[]> getUserCodeAndName(String userAreas, String userGroups, String users) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT USER_CODE, USER_NAME from USER_LIST WHERE 1=1 ");
		if(StringUtils.isNoneBlank(userAreas)) {
			sql.append(" AND IFNULL(USER_AREA,'') IN :areas");
			List<String> areaList = ServiceUtil.setParamList(userAreas);
			// 如果区域里面包含188AA时，将处长也查出来(处长的区域为空)
			if (userAreas.contains("188AA")) {
			    areaList.add("");
			}
			params.put("areas", areaList);
		}
		if(StringUtils.isNoneBlank(userGroups)) {
			sql.append(" AND IFNULL(USER_GROUP,'') IN :groups");
			List<String> groupList = ServiceUtil.setParamList(userGroups);
			groupList.add("");//添加空的区防止处长查询不出来
			params.put("groups", groupList);
		}
        if(StringUtils.isNoneBlank(users)) {
            sql.append(" AND USER_CODE IN :users");
            List<String> userList = ServiceUtil.setParamList(users);
            params.put("users", userList);
        }
        return findBySQL(sql, params).getContent();
    }

	/**
	 * @description: 
	 * @author: mazongjian
	 * @param users
	 * @return  
	 * @date 2019年5月29日
	 */
	@SuppressWarnings("unchecked")
    private List<Object[]> getAllUserInfoFromOrgList(String users) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT USER_CODE, USER_NAME, USER_AREA, USER_AREA_NAME, USER_GROUP, USER_GROUP_NAME FROM USER_LIST WHERE 1=1 ");
        sql.append(" AND USER_CODE IN :users");
        List<String> userList = ServiceUtil.setParamList(users);
        params.put("users", userList);
        return findBySQL(sql, params).getContent();
    }
    
    /**
     * @description: 从USER_LIST中获取人员信息，包括处长、区长、组长、业务人员、征信人员、审查人员的信息
     * @author: mazongjian
     * @param users
     * @return  
     * @date 2019年6月3日
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getAllUserInfoFromUserList(String users) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" USER_CODE "); // 0
        sql.append(", USER_NAME"); // 1
        sql.append(", USER_AREA"); // 2
        sql.append(", USER_AREA_NAME"); // 3
        sql.append(", USER_GROUP"); // 4
        sql.append(", USER_GROUP_NAME"); // 5
        sql.append(", ROLE_ID"); // 6
        sql.append(", USER_TYPE"); // 7
        sql.append(" FROM USER_LIST WHERE 1=1 ");
        sql.append(" AND USER_CODE IN :users");
        List<String> userList = ServiceUtil.setParamList(users);
        params.put("users", userList);
        return findBySQL(sql, params).getContent();
    }
	
	public Integer addMessage(Message msg, String userAreas, String userGroups, String users) {
		FbUser curUser = UserUtil.getCurrUser();
		
		msg.setSendUser(curUser.getUserName());
		Message back = messageRepository.saveAndFlush(msg);
		if (back != null) {
		   String msgId = back.getMessageId();
		   
		   // 保存發起人的消息
           MessageReceive sendMsgr = new MessageReceive();
           sendMsgr.setAcceptUser(curUser.getUserCode());
           sendMsgr.setAcceptUserName(curUser.getUserName());
           sendMsgr.setMessageId(msgId);
           messageReceiveRepository.saveAndFlush(sendMsgr);
		   
		   List<Object[]> allReceives = getAllUserInfoFromOrgList(users);
		   List<FbUser> receivces  = getAcceptUsers(userAreas,userGroups, users);
		   Map<String, FbUser> fbUserListMap = new HashMap<>();
		   
		   if (!CollectionUtils.isEmpty(receivces)) {
		       for (FbUser fbUser : receivces) {
		           fbUserListMap.put(fbUser.getUserCode(), fbUser);
		       }
		   }
		   
		   if (!CollectionUtils.isEmpty(allReceives)) {
			   List<MessageReceive> saveList = new ArrayList<MessageReceive>();
			   for (int i=0; i < allReceives.size(); i++) {
				   MessageReceive msgr = new MessageReceive();
				   Object[] user = allReceives.get(i);
				   // 上面有发送发起人的消息，这里不再重复发送
				   if (curUser.getUserCode().equals(user[0])) {
				       continue;
				   }
				   msgr.setAcceptUser(ObjectUtil.obj2String(user[0]));
				   msgr.setAcceptUserName(ObjectUtil.obj2String(user[1]));
				   msgr.setMessageId(msgId);
				   saveList.add(msgr);
				   
				   FbUser currReceiveUser = fbUserListMap.get(ObjectUtil.obj2String(user[0]));
				   if (currReceiveUser != null) {
				       currReceiveUser.setMessageCount(currReceiveUser.getMessageCount() == null ? 1 : currReceiveUser.getMessageCount() + 1);
				   }
			   }
			   if(saveList!=null && saveList.size()>0) {
				   messageReceiveRepository.saveAll(saveList);
			   }
			   //更新消息数量
               userRepository.saveAll(receivces);
		   }
		}
		Integer messageCount = (curUser.getMessageCount() == null || curUser.getMessageCount() == 0) ? 0 : curUser.getMessageCount();
		
		return messageCount;
	}
	
	
	public Integer addMessageByHr(Message msg,String users) {
		FbUser curUser = UserUtil.getCurrUser();
		
		// 保存消息
		msg.setSendUser(curUser.getUserName());
		Message back = messageRepository.saveAndFlush(msg);
		
        if (back != null) {
            String msgId = back.getMessageId();
            // 發起人也要接受到消息
	        MessageReceive sendMsgr = new MessageReceive();
	        sendMsgr.setAcceptUser(curUser.getUserCode());
	        sendMsgr.setAcceptUserName(curUser.getUserName());
	        sendMsgr.setMessageId(msgId);
	        messageReceiveRepository.saveAndFlush(sendMsgr);
	        
	        // 獲取所有接收人信息
	        List<Object[]> allReceives = getAllUserInfoFromUserList(users);
	        List<FbUser> receivces  = getAcceptByUser(users);
	        Map<String, FbUser> fbUserListMap = new HashMap<>();
	        if (!CollectionUtils.isEmpty(receivces)) {
	            for (FbUser fbUser : receivces) {
                   fbUserListMap.put(fbUser.getUserCode(), fbUser);
	            }
	        }
		   
		   if (!CollectionUtils.isEmpty(allReceives)) {
			   List<MessageReceive> saveList = new ArrayList<MessageReceive>();
			   for (int i = 0; i < allReceives.size(); i++) {
				   MessageReceive msgr = new MessageReceive();
				   Object[] user = allReceives.get(i);
				   msgr.setAcceptUser(ObjectUtil.obj2String(user[0]));
				   msgr.setAcceptUserName(ObjectUtil.obj2String(user[1]));
				   msgr.setMessageId(msgId);
				   saveList.add(msgr);
				   
				   FbUser currReceiveUser = fbUserListMap.get(ObjectUtil.obj2String(user[0]));
                   if (currReceiveUser != null) {
                       currReceiveUser.setMessageCount(currReceiveUser.getMessageCount() == null ? 1 : currReceiveUser.getMessageCount() + 1);
                   }
			   }
			   if (saveList != null && saveList.size() > 0) {
				   messageReceiveRepository.saveAll(saveList);
			   }
			   //更新消息数量
               userRepository.saveAll(receivces);
		   }
		}
		Integer messageCount = (curUser.getMessageCount() == null || curUser.getMessageCount() == 0) ? 0 : curUser.getMessageCount();
		
		return messageCount;
	}
	
	@SuppressWarnings("unchecked")
	private List<FbUser> getAcceptUsers(String userAreas, String userGroups, String users) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer jql = new StringBuffer("from FbUser where 1=1 ");
		if(StringUtils.isNoneBlank(users)) {
			jql.append(" and userCode in :users");
			List<String> userList = ServiceUtil.setParamList(users);
			params.put("users", userList);
		}else {
			if(StringUtils.isNoneBlank(userGroups)) {
				jql.append(" and userGroup in :groups");
				List<String> groupList = ServiceUtil.setParamList(userGroups);
				params.put("groups", groupList);
			}else {
				jql.append(" and userArea in :areas");
				List<String> areaList = ServiceUtil.setParamList(userAreas);
				params.put("areas", areaList);
			}
		}
		return findByJQL(jql, params).getContent();
	}

	@SuppressWarnings("unchecked")
	private List<FbUser> getAcceptByUser(String users) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer jql = new StringBuffer("from FbUser where 1=1 ");
		if(StringUtils.isNoneBlank(users)) {
			jql.append(" and userCode in :users");
			List<String> userList = ServiceUtil.setParamList(users);
			params.put("users", userList);
		}
		return findByJQL(jql, params).getContent();
	}

	//删除
	public Integer deleteMessage(String messageId, String receiveId) {
		List<MessageReceive> _exists = messageReceiveRepository.getMessages(messageId);
		MessageReceive rObj = messageReceiveRepository.findByReceviceId(receiveId);
		
		//刪除未讀消息時才将未读消息数减一
		FbUser user = UserUtil.getCurrUser();
		String curUserCode = user.getUserCode();
		String userAreas = "";
		String userGroups = "";
		Integer count = 0;
	    List<FbUser> receivcees  = getAcceptUsers(userAreas,userGroups,curUserCode);
	    count = (receivcees.get(0).getMessageCount()==null||receivcees.get(0).getMessageCount()== 0)? 0 : receivcees.get(0).getMessageCount();
		if(rObj!=null && rObj.getReadTime()==null && count > 0) {
			count = count-1;
		}
		messageReceiveRepository.deleteByMessageId(messageId,receiveId);
		Integer _size = _exists == null ? 0 : _exists.size();
		if(_size <= 1) {
			messageRepository.deleteById(messageId);
		}
		userRepository.updateMessageCount(count,receivcees.get(0).getUserCode());
		return count;
	}

	//更新
	public Integer updateReadTime(String receiveId) {
	    MessageReceive mr = messageReceiveRepository.findByReceviceId(receiveId);
		messageReceiveRepository.updateReadTime(new Date(),receiveId);
		FbUser user = UserUtil.getCurrUser();
		String curUserCode = user.getUserCode();
		String userAreas = "";
		String userGroups = "";
		Integer count = 0;
	    List<FbUser> receivcees  = getAcceptUsers(userAreas,userGroups,curUserCode);
	    if(receivcees!=null && receivcees.size()>0) {
	        count = (receivcees.get(0).getMessageCount()==null||receivcees.get(0).getMessageCount()== 0)? 0 : receivcees.get(0).getMessageCount();
			if(mr.getReadTime()==null) {
			  count = count-1;
			  userRepository.updateMessageCount(count,receivcees.get(0).getUserCode());
			  return count;
			}
	    }
		return count;
	}
	
	//根据咨询类型获取咨询名称
	@SuppressWarnings("unchecked")
	public List getConsultationNameByConType(String consultationType) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer("SELECT FBM.MESSAGE_TYPE_NAME FROM FB_MESSAGE FBM WHERE 1=1 ");
		if(StringUtils.isNoneBlank(consultationType)) {
			sql.append(" AND FBM.MESSAGE_TYPE =:consultationType");
			params.put("consultationType", consultationType);
		}
		/*return findBySQL(sql, params).getContent().get(0);*/
		return findBySQL(sql, params).getContent();
	}
	//计算组里有多少角色
	public String getUsersize(String groups) {
		StringBuffer sql = new StringBuffer();
		Map<String,Object> params = new HashMap<String,Object>();
		sql.append("SELECT  GROUP_CONCAT(DISTINCT USER_CODE) from USER_LIST where USER_GROUP IN :groups");
		List<String> areaList = ServiceUtil.setParamList(groups);
		params.put("groups", areaList);
		QueryPage bySQL = findBySQL(sql, params);
		
		if (bySQL == null || bySQL.getContent() == null || bySQL.getContent().get(0) == null) {
		    return "";
		}
		String usernames= bySQL.getContent().get(0).toString();

		return usernames;
	}
	
	public String getUsersizeFromArea(String areas, String groups) {
        StringBuffer sql = new StringBuffer();
        Map<String,Object> params = new HashMap<String,Object>();
        sql.append("SELECT  GROUP_CONCAT(DISTINCT USER_CODE) from USER_LIST where USER_AREA IN :areas and USER_GROUP IN :groups");
        List<String> areaList = ServiceUtil.setParamList(areas);
        List<String> groupList = ServiceUtil.setParamList(groups);
        params.put("areas", areaList);
        // 如果区域里面包含188AA时，将处长也查出来(处长的区域为空)
        if (areas.contains("188AA")) {
            areaList.add("");
        }
        params.put("groups", groupList);
        groupList.add("");
        QueryPage bySQL = findBySQL(sql, params);
        
        if (bySQL == null || bySQL.getContent() == null || bySQL.getContent().get(0) == null) {
            return "";
        }
        String usernames= bySQL.getContent().get(0).toString();

        return usernames;
    }
	
	//计算地区里有多少组
	public String getUsersizeArea(String areas) {
		StringBuffer sql = new StringBuffer();
		Map<String,Object> params = new HashMap<String,Object>();
		sql.append("SELECT  GROUP_CONCAT(DISTINCT USER_GROUP) from USER_LIST where USER_AREA IN :areas");
		List<String> areaList = ServiceUtil.setParamList(areas);
		params.put("areas", areaList);
		QueryPage bySQL = findBySQL(sql, params);
		
		if (bySQL == null || bySQL.getContent() == null || bySQL.getContent().get(0) == null) {
		    return "";
		}
		String groups= bySQL.getContent().get(0).toString();

		return groups;
	}

	//获取所有的区
	public Map<String, String> getAreaList() {
		Map<String, String> result = new LinkedHashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct ");
		sql.append(" CASE WHEN (ISNULL(ul.USER_AREA) = 1) || (LENGTH(TRIM(ul.USER_AREA)) = 0) THEN '188AA' ELSE ul.USER_AREA END AS USER_AREA, ");
		sql.append(" CASE WHEN (ISNULL(ul.USER_AREA_NAME) = 1) || (LENGTH(TRIM(ul.USER_AREA_NAME)) = 0) THEN '新興客群處' ELSE ul.USER_AREA_NAME END USER_AREA_NAME ");
		sql.append("   from USER_LIST ul");
		sql.append("   left join VIEW_DATA_DICT_ITEM ddi on ddi.ITEM_CODE = ul.USER_AREA and ddi.DICT_ID = 'ARRAY_SCHEMA_SORT_AREA'");
		sql.append("  where IFNULL(ul.USER_AREA, '') not in ('188J', '188K', '188L') ");
		sql.append("  order by cast(ifnull(ddi.ITEM_REMARK,999) as signed integer)");
		List<Object[]> context = findBySQL(sql).getContent();
		for(Object[] ob : context) {
			String areaCode = ob[0] == null ? "" : ob[0].toString();
			String areaName = ob[1] == null ? "" : ob[1].toString();
			result.put(areaCode,areaName);
		}
		return result;
	}
	
}
