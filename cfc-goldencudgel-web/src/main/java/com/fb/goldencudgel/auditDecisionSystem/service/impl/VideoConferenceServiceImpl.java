package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.videoConference.FbVideoAttendees;
import com.fb.goldencudgel.auditDecisionSystem.domain.videoConference.VideoConference;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbVideoAttendeesRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.VideoConferenceRepository;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.ServiceUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

import javax.lang.model.util.ElementScanner6;

@Service
@Transactional
public class VideoConferenceServiceImpl extends BaseJpaDAO{
	
	@Autowired
	private VideoConferenceRepository  videoConferenceRepository;
	
	@Autowired
	private FbVideoAttendeesRepository fbVideoAttendeesRepository;
	
	@Autowired
	private MessageServiceImpl messageService;
	
	public LinkedHashMap<String,String> getParticipant(String type, String pType, String userArea) {
		LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
		if(StringUtils.isBlank(userArea))
			return result;
		String [] _split = pType.split(",");
		if ("area".equals(type)) {
			if("all".equals(pType)) {
			    result = getGroupList("");
			} else {
			    String areaParam = "'" + StringUtils.join(_split, "','") + "'";
			    result = getGroupList(areaParam);
			}
		} else if ("group".equals(type)) {
		    String [] area_split = userArea.split(",");
		    String areaParam = "'" + StringUtils.join(area_split, "','") + "'";
			if ("all".equals(pType)) {
			    result = getUserList(areaParam, "");
			} else {
			    String groupParam = "'" + StringUtils.join(_split, "','") + "'";
			    if (groupParam.equals("''")) {
			        groupParam = "";
			    }
			    result = getUserList(areaParam, groupParam);
			}
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getAreaList() {
		Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT DISTINCT USER_AREA,USER_AREA_NAME FROM FB_USER");
		return findBySQL(sql, params).getContent();
	}

	public String addConference(VideoConference conf, String areaStr, String groupStr, String userStr, String webex) {
		User user = UserUtil.getCurrUser();

		List<Object[]> infos = getUserInfoFromUserList(areaStr, groupStr, userStr);
		String accepts = "";
		for (int i = 0; i < infos.size(); i++) {
			Object[] obj = infos.get(i);
			if ((StringUtils.isBlank(groupStr) && StringUtils.isBlank(userStr))) {
				//区不重复才累加（去重）
				if(!accepts.contains(ObjectUtil.obj2String(infos.get(i)[0]))) {
					accepts += " "+obj[0]+" ";
				}
			} else if (StringUtils.isBlank(userStr)) {
				accepts += " " + obj[0] + "-" + obj[1] + " ";
			} else {
				accepts += " " + obj[0]+ (StringUtils.isNoneBlank(ObjectUtil.obj2String(obj[0])) ? "-" : "") + obj[1]+ (StringUtils.isNoneBlank(ObjectUtil.obj2String(obj[1])) ? "-" : "") + obj[2] + " ";
			}
		}
		conf.setWebexAccount(webex);
		conf.setParticipants(accepts);
		conf.setCreateUser(UserUtil.getCurrUser().getUserCode());
		conf.setCreatTime(new Date());
		videoConferenceRepository.saveAndFlush(conf);

		List<Object[]> userList = messageService.getUserCodeAndName(areaStr, groupStr, userStr);
		List<FbVideoAttendees> fbVideoAttendeesList = new ArrayList<>();
		for (int i = 0; i < userList.size(); i++) {
			Object[] userObj = userList.get(i);
			FbVideoAttendees fbVideoAttendees = new FbVideoAttendees();
			fbVideoAttendees.setConferenceId(conf.getConferenceId());
			fbVideoAttendees.setParticipantsCode(ObjectUtil.obj2String(userObj[0]));
			fbVideoAttendees.setParticipants(ObjectUtil.obj2String(userObj[1]));
			fbVideoAttendees.setVideoStatu("0");
			fbVideoAttendees.setCreateUser(user.getUserCode());
			fbVideoAttendees.setCreateTime(new Date());
			fbVideoAttendeesList.add(fbVideoAttendees);
			fbVideoAttendeesRepository.saveAndFlush(fbVideoAttendees);
		}
		return accepts;
	}

	@SuppressWarnings("unchecked")
	public QueryPage<VideoConference> findByConditions(String keyNote, Date startDate, Date endDate,
													   QueryPage<VideoConference> queryPage) {
		StringBuffer sb = new StringBuffer("select distinct video from VideoConference video left join FbVideoAttendees attendes on attendes.conferenceId = video.conferenceId  where 1=1");
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNoneBlank(keyNote)) {
			sb.append(" and video.conferenceKeyNote like :conferenceKeyNote");
			params.put("conferenceKeyNote", "%" + keyNote + "%");
		}
		if(startDate!=null) {
			sb.append(" and video.startDate >=:startDate");
			params.put("startDate", startDate);
		}
		if(endDate!=null) {
			sb.append(" and video.startDate <=:endDate");
			params.put("endDate", endDate);
		}
		sb.append(" and (attendes.participantsCode =:userCode or  video.createUser=:userCode)");

		params.put("userCode", UserUtil.getCurrUser().getUserCode());
		sb.append(" order by str_to_date(CONCAT(video.startDate,' ', video.startTime),'%Y-%m-%d %H:%i') desc");
			int total = findByCount(keyNote,startDate,
				endDate, queryPage);
		queryPage.setTotal(total);
		queryPage = findByJQL(sb, queryPage, params);
		return queryPage;
	}

	@SuppressWarnings("unchecked")
	public int findByCount(String keyNote, Date startDate, Date endDate,
													   QueryPage<VideoConference> queryPage) {
		StringBuffer sb = new StringBuffer("select count(distinct video) from VideoConference video left join FbVideoAttendees attendes on attendes.conferenceId = video.conferenceId  where 1=1");
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNoneBlank(keyNote)) {
			sb.append(" and video.conferenceKeyNote like :conferenceKeyNote");
			params.put("conferenceKeyNote", "%" + keyNote + "%");
		}
		if(startDate!=null) {
			sb.append(" and video.startDate >=:startDate");
			params.put("startDate", startDate);
		}
		if(endDate!=null) {
			sb.append(" and video.startDate <=:endDate");
			params.put("endDate", endDate);
		}
		sb.append(" and (attendes.participantsCode =:userCode or  video.createUser=:userCode)");

		params.put("userCode", UserUtil.getCurrUser().getUserCode());
		//sb.append(" ORDER BY CREATE_TIME ASC");
		sb.append(" ORDER BY video.startDate DESC, str_to_date(video.startTime, '%k:%i') DESC");


		queryPage = findByJQL(sb, queryPage, params);
		long totalElements = queryPage.getTotalElements();
		int counts=(int)totalElements;
		return counts;
	}

	public void deleteVideoConference(String conferenceId) {
		videoConferenceRepository.deleteById(conferenceId);
	}

	public LinkedHashMap<String,String> getGroupList(String areaCode) {
		LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct ul.USER_GROUP, ul.USER_GROUP_NAME ");
		sql.append("   from USER_LIST ul");
		sql.append("   left join VIEW_DATA_DICT_ITEM ddi on ddi.ITEM_CODE = ul.USER_GROUP and ddi.DICT_ID = ul.USER_AREA");
		sql.append("  where ifnull(ul.USER_AREA, '') not in ('188J', '188K', '188L') ");
		sql.append("    and ul.USER_TYPE not in('A','Z') ");
		sql.append("    and ifnull(ul.USER_GROUP,'') != '' ");
		if (StringUtils.isNoneBlank(areaCode)) {
			sql.append(" and ul.USER_AREA in (" + areaCode + ")");
		}
		sql.append("  order by cast(ifnull(ddi.ITEM_REMARK,999) as signed integer) ");
		List<Object[]> context = findBySQL(sql).getContent();
		for(Object[] ob : context) {
			String groupCode = ob[0] == null ? "" : ob[0].toString();
			String groupName = ob[1] == null ? "" : ob[1].toString();
			result.put(groupCode,groupName);
		}
		return result;
	}

	public LinkedHashMap<String,String> getUserList(String areaCode, String groupCode) {
		LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct ul.USER_CODE,ul.USER_NAME,ul.USER_TYPE, ");
		sql.append("   case ul.USER_TYPE when 'M' then '0' when 'N' then '1' ");
		sql.append("   when 'A' then '2' when 'C' then '3' when 'Z' then '4' ");
		sql.append("   when 'S' then '5' when 'B' then '6' when 'O' then '7' else '9' end showOrder  ");
		sql.append("   from USER_LIST ul");
		sql.append("   left join VIEW_DATA_DICT_ITEM ddi on ddi.ITEM_CODE = ul.USER_GROUP and ddi.DICT_ID = ul.USER_AREA");
		sql.append("  where IFNULL(ul.USER_AREA, '') not in ('188J', '188K', '188L') ");
		if(StringUtils.isNoneBlank(areaCode)) {
		    // 如果区域里面包含188AA时，将处长也查出来(处长的区域为空)
		    if (areaCode.contains("188AA")) {
		        sql.append(" AND IFNULL(ul.USER_AREA, '') in ('', " + areaCode + ")");
		    } else {
		        sql.append(" AND IFNULL(ul.USER_AREA, '') in (" + areaCode + ")");
		    }
		    
		}
		if(StringUtils.isNoneBlank(groupCode)) {
		    sql.append(" AND IFNULL(ul.USER_GROUP, '') in ('', " + groupCode + ")");
		}
		sql.append("  order by showOrder,cast(ifnull(ddi.ITEM_REMARK,999) as signed integer), ul.USER_CODE ");
		List<Object[]> context = findBySQL(sql).getContent();
		for(Object[] ob : context) {
			String userCode = ob[0] == null ? "" : ob[0].toString();
			String userName ="";
			if (!ob[2].toString().equals("M")){
			userName = ob[1] == null ? "" : ob[1].toString() + "[" + getUserTypeName(ob[2].toString()) + "]";
			}else{
			 userName = ob[1] == null ? "" : ob[1].toString();
			}

			if(result.get(userCode) == null)
				result.put(userCode,userName);
		}
		return result;
	}

	public String getUserTypeName(String userType) {
		String userTypeName = "";
		switch (userType) {
			case "M" : userTypeName = "處長"; break;
			case "N" : userTypeName = "管理員"; break;
			case "A" : userTypeName = "區長"; break;
			case "C" : userTypeName = "組長"; break;
			case "S" : userTypeName = "業務員"; break;
			case "Z" : userTypeName = "徵信員"; break;
			case "B" : userTypeName = "行秘"; break;
			case "O" : userTypeName = "審查員"; break;
			default: userTypeName = "";
		}
		return userTypeName;
	}

	public int getUserPower(String userType) {
		int userPower = 0;
		switch (userType) {
			case "處長" : userPower = 1; break;
			case "管理員" : userPower = 2; break;
			case "區長" : userPower = 3; break;
			case "組長" : userPower = 4; break;
			case "業務員" : userPower = 5; break;
			case "徵信員" : userPower = 5; break;
			case "行秘" : userPower = 6; break;
			case "審查員" : userPower = 7; break;
			default: userPower = 0;
		}
		return userPower;
	}
	
	/**
     * @description: 從USER_LIST獲取員工信息
     * @author: mazongjian
     * @param userAreas
     * @param userGroups
     * @param users
     * @return  
     * @date 2019年5月29日
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getUserInfoFromUserList(String userAreas, String userGroups, String users) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" CASE WHEN (ISNULL(USER_AREA_NAME) = 1) || (LENGTH(trim(USER_AREA_NAME)) = 0) THEN '新興客群處' ELSE USER_AREA_NAME END USER_AREA_NAME, ");
        sql.append(" USER_GROUP_NAME, ");
        sql.append(" GROUP_CONCAT(USER_NAME) AS USER_NAME ");
        sql.append(" from USER_LIST WHERE 1=1 ");
        if (StringUtils.isNoneBlank(userAreas)) {
            sql.append(" AND USER_AREA IN :areas");
            List<String> areaList = ServiceUtil.setParamList(userAreas);
            // 如果区域里面包含188AA时，将处长也查出来(处长的区域为空)
            if (userAreas.contains("188AA")) {
                areaList.add("");
            }
            params.put("areas", areaList);
        }
        if (StringUtils.isNoneBlank(userGroups)) {
            sql.append(" AND USER_GROUP IN :groups");
            List<String> groupList = ServiceUtil.setParamList(userGroups);
            groupList.add("");//添加空的区防止处长查询不出来
            params.put("groups", groupList);
        }
        if (StringUtils.isNoneBlank(users)) {
            sql.append(" AND USER_CODE IN :users");
            List<String> userList = ServiceUtil.setParamList(users);
            params.put("users", userList);
        }
        sql.append("  GROUP BY USER_AREA_NAME, USER_GROUP_NAME ");
        return findBySQL(sql, params).getContent();
    }
}
