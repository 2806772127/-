package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.fb.goldencudgel.auditDecisionSystem.domain.messageTemplate.MessageTemplate;
import com.fb.goldencudgel.auditDecisionSystem.repository.MessageTemplateRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.fb.goldencudgel.auditDecisionSystem.domain.commons.SystemConfig;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.schema.callZyjService.ZYJRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.callZyjService.ZYJRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.common.ServiceBody;
import com.fb.goldencudgel.auditDecisionSystem.schema.common.ServiceHeader;
import com.fb.goldencudgel.auditDecisionSystem.schema.common.ServiceMessage;
import com.fb.goldencudgel.auditDecisionSystem.schema.companyBaseInfo.GetCompanyBaseInfoRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.companyBaseInfo.GetCompanyBaseInfoRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.compress.CompressFileRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.compress.CompressFileRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang.FileToRuiyangRequest;
import com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang.FileToRuiyangResponse;
import com.fb.goldencudgel.auditDecisionSystem.schema.getCreditCaseList.GetCreditCaseListReq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getCreditCaseList.GetCreditCaseListResp;
import com.fb.goldencudgel.auditDecisionSystem.schema.getLeaderHomeInfo.GetLeaderHomeInfoRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getLeaderHomeInfo.GetLeaderHomeInfoRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.getLogFileList.GetLogFileListRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getLogFileList.GetLogFileListRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.GetManagerHomeInfoRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo.GetManagerHomeInfoRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.getReportData.GetReportDataRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getReportData.GetReportDataRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.getSaleHomeInfo.GetSaleHomeInfoRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getSaleHomeInfo.GetSaleHomeInfoRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.getSchedulerBaseInfo.GetSchedulerBaseInfoRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getSchedulerBaseInfo.GetSchedulerBaseInfoRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.getUserProfile.GetUserProfileRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getUserProfile.GetUserProfileRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRs;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.utils.ResttemplateUtile;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

/**
 * 请求bw接口的统一服务
 *
 */
@Service
@Transactional
public class InterfaceServiceImpl implements IInterfaceService {

    private final Logger logger = LoggerFactory.getLogger(InterfaceServiceImpl.class);

    //获取公司详情
    public final static String GET_COM_INFO  = "getCompanyBaseInfo";
    // 压缩图片
    public final static String COMPRESS_FILE = "compressFile";
    
    public final static String GET_MANAGER_HONE_INFO = "getManagerHomeInfo";
    
    public final static String GET_LEADER_HOME_INFO = "getLeaderHomeInfo";
    
    public final static String GET_SALE_HOME_INFO = "getSaleHomeInfo";
    
    public final static String GET_CREDIT_CASE_LIST = "getCreditCaseList";
    
    public final static String GET_USER_PROFILE = "getUserProfile";
    
    public final static String WEB_SEND_MESSAGE = "webSendMessage";
    
    public final static String WEB_PUSH_MESSAGE = "webPushMessage";

    public final static String FILE_TO_RUIYANG = "fileToRuiyang";
    
    public final static String GET_REPORT_DATA = "getReportData";

    public final static String GET_LOG_FILE_LIST = "getLogFileList";

    public final static String GET_SCHEDULER_BASE_INFO = "getSchedulerBaseInfo";
    
    /* 展業金重測接口*/
    public final static String MEASURE_WORD_RESEND = "measureWordReSend";

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    public String BuildRequestStr(String serviceType,String reqData) {
        User user = UserUtil.getCurrUser();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        ServiceMessage serviceMessage = new ServiceMessage();
        ServiceHeader head = new ServiceHeader();
        head.setReqNo("");
        head.setServiceType(serviceType);
        head.setReqTime(sdf.format(new Date()));
        head.setUuid(user.getUserUid());
        head.setToken(user.getTokenDetial().getToken());
        head.setChannelType("web");
        head.setDeviceFingerprint("");

        ServiceBody body = new ServiceBody();
        body.setMessage(reqData);
        serviceMessage.setServiceBody(body);
        serviceMessage.setServiceHeader(head);

        String reqStr = JSON.toJSONString(serviceMessage);

        logger.info("[web --> interface] request message : " + reqStr);
        return reqStr;
    }
    
    public String BuildRequestStrNoLogin(String serviceType, String reqData) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        ServiceMessage serviceMessage = new ServiceMessage();
        ServiceHeader head = new ServiceHeader();
        head.setReqNo("");
        head.setServiceType(serviceType);
        head.setReqTime(sdf.format(new Date()));
        User currUser = UserUtil.getCurrUser();
        if(currUser != null){
            head.setUuid(currUser.getUserUid());
            head.setToken(currUser.getTokenDetial().getToken());
        }
        head.setChannelType("web");
        head.setDeviceFingerprint("");

        ServiceBody body = new ServiceBody();
        body.setMessage(reqData);
        serviceMessage.setServiceBody(body);
        serviceMessage.setServiceHeader(head);

        String reqStr = JSON.toJSONString(serviceMessage);

        logger.info("[web --> interface] request message : " + reqStr);
        return reqStr;
    }

	public String sendMessage(String reqStr) {
        String response = null;
        try {
            RestTemplate restTemplate = new ResttemplateUtile().getRestTemplate("http", "60000");
            SystemConfig systemConfig = systemConfigRepository.findByID("INTERFACE_URL");
            logger.info("[web --> interface] request url : " + systemConfig != null ? systemConfig.getKeyvalue() : "");
            if(systemConfig== null || StringUtils.isEmpty(systemConfig.getKeyvalue())) {
                return null;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=utf-8");
            HttpEntity request = new HttpEntity(reqStr, headers);
            ResponseEntity<String> responseData = restTemplate.exchange(systemConfig.getKeyvalue(), HttpMethod.POST,request,String.class);
            ServiceMessage rsMessage = com.alibaba.fastjson.JSONObject.parseObject(responseData.getBody(),ServiceMessage.class);
            if(null != rsMessage.getServiceBody())
                response = StringUtils.isEmpty(rsMessage.getServiceBody().getMessage()) ? "": rsMessage.getServiceBody().getMessage();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        logger.info("[interface --> web] response message : " + response);
        return response;
    }

    @Override
    public GetCompanyBaseInfoRs getCompanyBaseInfo(GetCompanyBaseInfoRq getCompanyBaseInfoRq) {
        GetCompanyBaseInfoRs companyBaseInfoRs = new GetCompanyBaseInfoRs();
        String reqData = JSON.toJSONString(getCompanyBaseInfoRq);
        String reqStr = BuildRequestStr(GET_COM_INFO,reqData);
        String response = sendMessage(reqStr);
        if(StringUtils.isNoneBlank(response)) {
            companyBaseInfoRs = com.alibaba.fastjson.JSONObject.parseObject(response, GetCompanyBaseInfoRs.class);
        }
        return companyBaseInfoRs;
    }
    
    /**
     * @description 压缩图片
     */
    public CompressFileRs compressFile(CompressFileRq compressFileRq) {
        CompressFileRs compressFileRs = new CompressFileRs();
        String reqData = JSON.toJSONString(compressFileRq);
        String reqStr = BuildRequestStr(COMPRESS_FILE, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
            compressFileRs = com.alibaba.fastjson.JSONObject.parseObject(response, CompressFileRs.class);
        }
        return compressFileRs;
    }
    
    public GetManagerHomeInfoRs getManagerHoneInfo(GetManagerHomeInfoRq getManagerHomeInfoRq) {
        GetManagerHomeInfoRs getManagerHomeInfoRs = new GetManagerHomeInfoRs();
        String reqData = JSON.toJSONString(getManagerHomeInfoRq);
        String reqStr = BuildRequestStr(GET_MANAGER_HONE_INFO, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
            getManagerHomeInfoRs = com.alibaba.fastjson.JSONObject.parseObject(response, GetManagerHomeInfoRs.class);
        }
        return getManagerHomeInfoRs;
    }
    
    public GetLeaderHomeInfoRs getLeaderHomeInfo(GetLeaderHomeInfoRq getLeaderHomeInfoRq) {
        GetLeaderHomeInfoRs getLeaderHomeInfoRs = new GetLeaderHomeInfoRs();
        String reqData = JSON.toJSONString(getLeaderHomeInfoRq);
        String reqStr = BuildRequestStr(GET_LEADER_HOME_INFO, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
            getLeaderHomeInfoRs = com.alibaba.fastjson.JSONObject.parseObject(response, GetLeaderHomeInfoRs.class);
        }
        return getLeaderHomeInfoRs;
    }
    
    public GetSaleHomeInfoRs getSaleHomeInfo(GetSaleHomeInfoRq getSaleHomeInfoRq) {
        GetSaleHomeInfoRs getSaleHomeInfoRs = new GetSaleHomeInfoRs();
        String reqData = JSON.toJSONString(getSaleHomeInfoRq);
        String reqStr = BuildRequestStr(GET_SALE_HOME_INFO, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
            getSaleHomeInfoRs = com.alibaba.fastjson.JSONObject.parseObject(response, GetSaleHomeInfoRs.class);
        }
        return getSaleHomeInfoRs;
    }
    
    /**
     * @description 获取征信员当前所有案件
     */
    public GetCreditCaseListResp getCreditCaseList(GetCreditCaseListReq getCreditCaseListReq) {
        GetCreditCaseListResp getCreditCaseListResp = new GetCreditCaseListResp();
        String reqData = JSON.toJSONString(getCreditCaseListReq);
        String reqStr = BuildRequestStr(GET_CREDIT_CASE_LIST, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
            getCreditCaseListResp = com.alibaba.fastjson.JSONObject.parseObject(response, GetCreditCaseListResp.class);
        }
        return getCreditCaseListResp;
    }
    
    /**
     * @description 登录根据UserCode获取用户区域、组、账号、角色ID
     */
    public GetUserProfileRs getUserProfile(GetUserProfileRq getUserProfileRq) {
        GetUserProfileRs getUserProfileRs = new GetUserProfileRs();
        String reqData = JSON.toJSONString(getUserProfileRq);
        String reqStr = BuildRequestStrNoLogin(GET_USER_PROFILE, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
            getUserProfileRs = com.alibaba.fastjson.JSONObject.parseObject(response, GetUserProfileRs.class);
        }
        return getUserProfileRs;
    }
    
    /**
     * @description: 調用BWCE接口進行消息推送，這個接口會新增FB_MESSAGE、FB_MESSAGE_RECEIVE記錄、並且更新FB_USER表
     * @author: mazongjian
     * @param webSendMessageRq
     * @return  
     * @date 2019年5月29日
     */
    public WebSendMessageRs webSendMessage(WebSendMessageRq webSendMessageRq) {
        WebSendMessageRs webSendMessageRs = new WebSendMessageRs();
        String reqData = JSON.toJSONString(webSendMessageRq);
        String reqStr = BuildRequestStr(WEB_SEND_MESSAGE, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
            webSendMessageRs = com.alibaba.fastjson.JSONObject.parseObject(response, WebSendMessageRs.class);
        }
        return webSendMessageRs;
    }
    
    /**
     * @description: 調用BWCE接口進行消息推送，這個接口只是推送消息，不會更新數據庫
     * @author: mazongjian
     * @param webSendMessageRq
     * @return  
     * @date 2019年5月29日
     */
    public WebSendMessageRs webPushMessage(WebSendMessageRq webSendMessageRq) {
        WebSendMessageRs webSendMessageRs = new WebSendMessageRs();
        String reqData = JSON.toJSONString(webSendMessageRq);
        String reqStr = BuildRequestStr(WEB_PUSH_MESSAGE, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
            webSendMessageRs = com.alibaba.fastjson.JSONObject.parseObject(response, WebSendMessageRs.class);
        }
        return webSendMessageRs;
    }

    /**
     * @description 儲存征信问卷答案后调用
     */
    public FileToRuiyangResponse fileToRuiyang(FileToRuiyangRequest fileToRuiyangRq) {
        FileToRuiyangResponse fileToRuiyangRs = new FileToRuiyangResponse();
        String reqData = JSON.toJSONString(fileToRuiyangRq);
        String reqStr = BuildRequestStrNoLogin(FILE_TO_RUIYANG, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
            fileToRuiyangRs = com.alibaba.fastjson.JSONObject.parseObject(response, FileToRuiyangResponse.class);
        }
        return fileToRuiyangRs;
    }
    
    /**
     * @description: 調用BWCE接口生成報表底層數據
     * @author: mazongjian
     * @param getReportDataRq
     * @return  
     * @date 2019年8月5日
     */
    public GetReportDataRs getReportData(GetReportDataRq getReportDataRq) {
        GetReportDataRs getReportDataRs = new GetReportDataRs();
        String reqData = JSON.toJSONString(getReportDataRq);
        String reqStr = BuildRequestStr(GET_REPORT_DATA, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
            getReportDataRs = com.alibaba.fastjson.JSONObject.parseObject(response, GetReportDataRs.class);
        }
        return getReportDataRs;
    }

    //系统日志
    public GetLogFileListRs getLogFileList(GetLogFileListRq getLogFileListRq) {
    	GetLogFileListRs getLogFileListRs = new GetLogFileListRs();
        String reqData = JSON.toJSONString(getLogFileListRq);
        String reqStr = BuildRequestStr(GET_LOG_FILE_LIST, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNoneBlank(response)) {
        	getLogFileListRs = com.alibaba.fastjson.JSONObject.parseObject(response, GetLogFileListRs.class);
        }
        return getLogFileListRs;
    }

    /**
     * 獲取行程信息，同步展業金信評信息
     * @param getSchedulerBaseInfoRq
     * @return
     */
    public GetSchedulerBaseInfoRs getSchedulerBaseInfo(GetSchedulerBaseInfoRq getSchedulerBaseInfoRq){
        GetSchedulerBaseInfoRs getSchedulerBaseInfoRs = new GetSchedulerBaseInfoRs();
        String reqData = JSON.toJSONString(getSchedulerBaseInfoRq);
        String reqStr = BuildRequestStr(GET_SCHEDULER_BASE_INFO,reqData);
        String response = sendMessage(reqStr);
        if(StringUtils.isNotBlank(response)){
            getSchedulerBaseInfoRs = com.alibaba.fastjson.JSONObject.parseObject(response, GetSchedulerBaseInfoRs.class);
        }
        return getSchedulerBaseInfoRs;
    }
    
    /**
     * @description: 展業金重測接口
     * @author: mazongjian
     * @param zyjRq
     * @return  
     * @see com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService#callZYJService(com.fb.goldencudgel.auditDecisionSystem.schema.callZyjService.ZYJRq)
     * @date 2019年11月18日
     */
    public ZYJRs measureWordReSend(ZYJRq zyjRq){
        ZYJRs zyjRs = new ZYJRs();
        String reqData = JSON.toJSONString(zyjRq);
        String reqStr = BuildRequestStr(MEASURE_WORD_RESEND, reqData);
        String response = sendMessage(reqStr);
        if (StringUtils.isNotBlank(response)) {
            zyjRs = com.alibaba.fastjson.JSONObject.parseObject(response, ZYJRs.class);
        }
        return zyjRs;
    }

    public WebSendMessageRs sendMessage(String templateId, Map<String,String> keyValue,String acceptUser,String type,String isPush){
        MessageTemplate messageTemplate = messageTemplateRepository.findByTemplateId(templateId);
        if(messageTemplate==null) {
            logger.warn("消息模板未配置["+templateId+"]");
            return null;
        }
        WebSendMessageRq webSendMessageRq = new WebSendMessageRq();
        webSendMessageRq.setMessageTitle(messageTemplate.getTemplateTheme());
        String context = messageTemplate.getTemplateContent();
        //替換消息關鍵字
        for(String key : keyValue.keySet()) {
            if(StringUtils.isNoneBlank(keyValue.get(key)))
                context = context.replace(key,keyValue.get(key));
        }
        webSendMessageRq.setMessageContext(context);
        webSendMessageRq.setMessageType(type);
        webSendMessageRq.setSendUser(UserUtil.getCurrUser().getUserCode());
        webSendMessageRq.setAcceptUser(acceptUser);
        webSendMessageRq.setIsPush(isPush);
        WebSendMessageRs webSendMessageRs = webSendMessage(webSendMessageRq);
        return webSendMessageRs;
    }
}
