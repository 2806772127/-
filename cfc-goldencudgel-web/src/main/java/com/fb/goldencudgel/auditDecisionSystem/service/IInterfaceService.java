package com.fb.goldencudgel.auditDecisionSystem.service;

import com.fb.goldencudgel.auditDecisionSystem.schema.callZyjService.ZYJRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.callZyjService.ZYJRs;
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
import com.fb.goldencudgel.auditDecisionSystem.schema.getUserProfile.GetUserProfileRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getUserProfile.GetUserProfileRs;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRs;

import java.util.Map;

/**
 * 请求bw接口的统一服务
 */
public interface IInterfaceService {

    public GetCompanyBaseInfoRs getCompanyBaseInfo(GetCompanyBaseInfoRq getCompanyBaseInfoRq);
    
    public CompressFileRs compressFile(CompressFileRq compressFileRq);
    
    public GetManagerHomeInfoRs getManagerHoneInfo(GetManagerHomeInfoRq getManagerHomeInfoRq);
    
    public GetLeaderHomeInfoRs getLeaderHomeInfo(GetLeaderHomeInfoRq getLeaderHomeInfoRq);
    
    public GetSaleHomeInfoRs getSaleHomeInfo(GetSaleHomeInfoRq getSaleHomeInfoRq);
    
    public GetCreditCaseListResp getCreditCaseList(GetCreditCaseListReq getCreditCaseListReq);
    
    public GetUserProfileRs getUserProfile(GetUserProfileRq getUserProfileRq);
    
    public WebSendMessageRs webSendMessage(WebSendMessageRq webSendMessageRq);
    
    public WebSendMessageRs webPushMessage(WebSendMessageRq webSendMessageRq);

    public FileToRuiyangResponse fileToRuiyang(FileToRuiyangRequest fileToRuiyangRq);
    
    public GetReportDataRs getReportData (GetReportDataRq GetReportDataRq);

    public GetLogFileListRs getLogFileList(GetLogFileListRq getLogFileListRq);
    
    public ZYJRs measureWordReSend(ZYJRq zyjRq);

    public WebSendMessageRs sendMessage(String templateId, Map<String,String> keyValue, String acceptUser, String type, String isPush);
}
