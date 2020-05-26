package com.fb.goldencudgel.auditDecisionSystem.controller.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDictItem;
import com.fb.goldencudgel.auditDecisionSystem.domain.message.Message;
import com.fb.goldencudgel.auditDecisionSystem.domain.user.FbUser;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage.WebSendMessageRs;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.MessageServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.VideoConferenceServiceImpl;
import com.fb.goldencudgel.auditDecisionSystem.utils.ExcelUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.NumberFormatUtils;
import com.fb.goldencudgel.auditDecisionSystem.utils.ObjectUtil;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;

@Controller
@RequestMapping("/message")
public class MessageController {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private MessageServiceImpl messageService;
	
	@Autowired
	private VideoConferenceServiceImpl videoConferenceService;
	
	@Autowired
    private IInterfaceService interfaceService;
	
	private static final Integer PAGE_SIZE = 15;
	
	@RequestMapping("viewMessage")
	public String viewMessage(Model model, String backFlag) {
	    List<ViewDataDictItem> messageTypes = messageService.getMessageType();
	    model.addAttribute("messageTypes", messageTypes);
	    model.addAttribute("backFlag", backFlag);
		return "message/viewMessage";
	}
	@RequestMapping("/queryMessage")
	public String queryVideoConferences(Model model,String messageType,String startDate,String endDate,
			@RequestParam(required = false, defaultValue = "1") Integer curPage) {
		try { 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date sDate = null;
			Date eDate = null;
			if(StringUtils.isNoneBlank(startDate)) {
				sDate = sdf.parse(startDate);
			}
			if(StringUtils.isNoneBlank(endDate)) {
				eDate = sdf.parse(endDate);
				// 结束日期设置为所选日期最后一秒
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(eDate);
				endCalendar.add(Calendar.DAY_OF_MONTH, 1);
				endCalendar.add(Calendar.SECOND, -1);
				eDate = endCalendar.getTime();
			}
		    QueryPage<Object[]> page = messageService.findByConditions(messageType,sDate,
		    		eDate,new QueryPage<Object[]>(curPage, PAGE_SIZE));
		    model.addAttribute("page", page);
			model.addAttribute("curPage", curPage);
			model.addAttribute("messageList", page.getContent());
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "message/viewMessageList";
	}
	
	
	@RequestMapping("/viewAddMessage")
	public String viewAddMessage(Model model) {
	    Map<String, String> areaList = messageService.getAreaList();
        model.addAttribute("areaList", areaList);
        
        List<ViewDataDictItem> messageTypes = messageService.getMessageType();
        model.addAttribute("messageTypes", messageTypes);
		return "message/viewAddMessage";
	}
	
	@RequestMapping("/getSendUser")
	@ResponseBody
	public Map<String,Object> getSendUser(String type,String pType,String userArea){
		Map<String,Object>  result = new HashMap<String,Object>();
		try {
			Map<String,String> participantList = videoConferenceService.getParticipant(type, pType,userArea);
			List<Object> list = new ArrayList<>();
			for(Map.Entry<String,String> participant : participantList.entrySet()) {
				list.add(participant);
			}
			result.put("success",true);
			result.put("data",list);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success",false);
			result.put("message","获取信息失败,請稍後重試");
		}
		return result;
	}
	@RequestMapping("/getUserInfo")
	@ResponseBody
	public Map<String,Object> getUserInfo(String userAreas,String userGroups,String users){
		Map<String,Object>  result = new HashMap<String,Object>();
		try {
			List<Object[]> infos = messageService.getUserInfo(userAreas, userGroups, users);
			String accepts = "";
			for (int i = 0; i < infos.size(); i++) {
				Object[] obj = infos.get(i);
				if (StringUtils.isBlank(userGroups) && StringUtils.isBlank(users)) {
					if (i > 0) {
						if (!obj[0].equals(infos.get(i-1)[0])){
							accepts += " " + obj[0] + " ";
						}
					} else {
						accepts += obj[0];
					}
				} else if (StringUtils.isBlank(users)) {
					accepts += " " + obj[0] + "-" + obj[1] + " ";
				} else {
					accepts += " " + obj[0]+ "-" + obj[ 1]+ "-" + obj[2] + " ";
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			result.put("success",true);
			result.put("data", accepts);
			result.put("sendTime", sdf.format(new Date()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success",false);
			result.put("message","获取信息失败,請稍後重試");
		}
		return result;
	}

	@RequestMapping("/addMessage")
	@ResponseBody
	public Map<String,Object> addMessage(HttpServletRequest request,Message msg,String userAreas,String userGroups,String users){
		Map<String,Object>  result = new HashMap<String,Object>();
		try {
		    
		    if (StringUtils.isBlank(userGroups)) {
		        userGroups = messageService.getUsersizeArea(userAreas);
            }
            
            if (StringUtils.isBlank(users)) {
                users = messageService.getUsersizeFromArea(userAreas, userGroups);
            }
		    
			Integer messageCount = messageService.addMessage(msg, userAreas, userGroups, users);
			result.put("success",true);
			result.put("message","提交成功");
			result.put("messageCount", messageCount);

			setMessageCount(request, messageCount);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success",false);
			result.put("message","提交失敗,請稍後重試");
		}
		return result;
	}

	@RequestMapping("/deleteMessage")
	@ResponseBody
	public Map<String,Object>  deleteMessage(HttpServletRequest request,String messageId,String receiveId){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			Integer messageCount = messageService.deleteMessage(messageId,receiveId);
			result.put("success",true);
			result.put("message","刪除成功");
			
			QueryPage<Object[]> pageContent = messageService.findByIsHasReadTime(new QueryPage<Object[]>(1, PAGE_SIZE));
			   Object contentCount;
			   if(pageContent.getContent().isEmpty()) {
				    contentCount =0;
			   }else{
			    contentCount = pageContent.getContent().get(0);
			   }
			   result.put("contentCount", contentCount);
			   setMessageCount(request, contentCount);
			   
		       //setMessageCount(request, messageCount);
			
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success",false);
			result.put("message","刪除失敗,請稍後重試");
		}
		return result;
	}
	
	
/*	@RequestMapping("/updateReadTime")
	@ResponseBody
	public Map<String,Object>  updateReadTime(HttpServletRequest request,String receiveId){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			Integer messageCount = messageService.updateReadTime(receiveId);
			result.put("success",true);
			result.put("messageCount", messageCount);
			
			setMessageCount(request, messageCount);
	        
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success",false);
			result.put("message","請稍後重試");
		}
		return result;
	}*/
	@RequestMapping("/updateReadTime")
	@ResponseBody
	public Map<String,Object>  updateReadTime(HttpServletRequest request,String receiveId){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			Integer messageCount = messageService.updateReadTime(receiveId);
			result.put("success",true);
			result.put("messageCount", messageCount);
			
		   QueryPage<Object[]> pageContent = messageService.findByIsHasReadTime(new QueryPage<Object[]>(1, PAGE_SIZE));
		   Object contentCount;
		   if(pageContent.getContent().isEmpty()) {
			    contentCount =0;
		   }else{
		    contentCount = pageContent.getContent().get(0);
		   }
		   result.put("contentCount", contentCount);
			
		   setMessageCount(request, contentCount);
		   //setMessageCount(request, messageCount);
			
	        
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			result.put("success",false);
			result.put("message","請稍後重試");
		}
		return result;
	}
	
	/*private void setMessageCount(HttpServletRequest request, String messageCount) {
		HttpSession session = request.getSession(true);
		session.setAttribute("messageCount", messageCount);
	}*/
	private void setMessageCount(HttpServletRequest request, Object contentCount) {
	HttpSession session = request.getSession(true);
	session.setAttribute("contentCount", contentCount);
    }
	
	//数值格式化
	 public static  String formatString(String data) {
         DecimalFormat df = new DecimalFormat("#,###"); 
         return df.format(data);
     }
	
    // 导入方法
    @RequestMapping("/ImportExcle")
    @ResponseBody
    @Transactional
    public Map<String, Object> saveUpload(MultipartHttpServletRequest request) {
        FbUser curUser = UserUtil.getCurrUser();
        Map<String, Object> result = null;
        List<MultipartFile> files = request.getFiles("file");
        if (files != null && files.size() > 0) {
            MultipartFile file = null;
            for (int i = 0; i < files.size(); i++) {
                file = files.get(i);
                if (!file.isEmpty()) {
                    try {
                        List<List<Object>> dataList = new ArrayList<List<Object>>();
                        List<Object> dataLists = new ArrayList<>();
                        List<List<Object>> lists = new ArrayList<>();
                        // 从excel读取数据
                        dataList = ExcelUtil.readFromExcelFile(file);
                        
                        // 校驗訊息內容
                        result = validateMessageContent(dataList);
                        if (result != null) {
                            return result;
                        } else {
                            result = new HashMap<String, Object>();
                        }
                        
                        // 第0行为信息主旨，第1行为标题，第2行开始为信息内容
                        for (int j = 2; j < dataList.size(); j++) {
                            // 判断是否已读取完excle数据
                            if (dataList.get(j).get(0).equals("") && dataList.get(j).get(1).equals("") 
                                    && dataList.get(j).get(2).equals("") && dataList.get(j).get(3).equals("")
                                    && dataList.get(j).get(4).equals("") && dataList.get(j).get(5).equals("")) {
                                break;
                            }

                            if (dataList.get(j).get(0).equals("") || dataList.get(j).get(0) == null) {
                                result.put("message", "姓名不可爲空");
                                result.put("success", false);
                                return result;
                            }
                            
                            if (dataList.get(j).get(1).equals("") || dataList.get(j).get(1) == null) {
                                result.put("message", "員工編號不可為空");
                                result.put("success", false);
                                return result;
                            }

                            // 咨询类型
                            String consultationType = "03"; 
                            // 咨询名称
                            String consultationName = "系統通知";
                            // 咨询主旨，第0行第0列
                            String consultKeynote = dataList.get(0).get(0).toString();
                            // 员工编号
                            String acceptsNum = dataList.get(j).get(1).toString().split("\\.")[0];
                            // 接收人
                            List<Object[]> infos = messageService.getAllUserInfoFromUserList(acceptsNum);

                            if (infos == null || infos.isEmpty()) {
                                result.put("message", "接收者[" + acceptsNum + "]信息有誤");
                                result.put("success", false);
                                return result;
                            }

                            // 拼接接收者信息，区-组-人员
                            String acceptsHr = "";
                            for (int y = 0; y < infos.size(); y++) {
                                Object[] obj = infos.get(y);
                                if ("M".equals(obj[7])) {
                                    acceptsHr += obj[1] + ";";
                                } else if ("A".equals(obj[7])) {
                                    acceptsHr += obj[3] + "-" + obj[1] + ";";
                                } else {
                                    acceptsHr += obj[3] + "-" + obj[5] + "-" + obj[1] + ";";
                                }
                            }
                            if (StringUtils.isNoneBlank(acceptsHr) && acceptsHr.length() > 0) {
                                acceptsHr = acceptsHr.substring(0, acceptsHr.length() - 1);
                            }
                            StringBuilder consultDetails = new StringBuilder();
                            
                            // 信息标题
                            List<Object> titleList = new ArrayList<>();
                            titleList = dataList.get(1);
                            
                            List<Object> partConList = new ArrayList<>();

                            String consultDetail;
                            for (int s = 0; s < titleList.size(); s++) {
                                if (s >= dataList.get(j).size()) {
                                    continue;
                                }
                                // 如果信息内容为空，不拼接
                                if (StringUtils.isEmpty(ObjectUtil.obj2String(dataList.get(j).get(s)).trim())) {
                                    continue;
                                }
                                
                                // 替換掉字符串里的第一個“.”和所有“,”，如果保留下來的是純數字，則說明是金額
                                String excelTd = ObjectUtil.obj2String(dataList.get(j).get(s)).replaceFirst("\\.", "").replaceAll(",", "");
                                
                                if (NumberFormatUtils.isDigital(excelTd) && s != 1) {
                                    // 拼接数值型信息
                                    Double numberVal = (Double) dataList.get(j).get(s);
                                    // 读取excle数值太大，会出现科学计数法，解决处理
                                    BigDecimal bigDecimal = new BigDecimal(numberVal.toString());
                                    // 判断是否是最后一个，要是最后一个就不加";"结束
                                    if (s == titleList.size() - 1) {
                                        if (bigDecimal.toPlainString().split("\\.")[bigDecimal.toPlainString().split("\\.").length - 1].equals("0")) {
                                            consultDetail = titleList.get(s).toString() + ":" + NumberFormatUtils.addThousands(bigDecimal.toPlainString().split("\\.")[0]);
                                        } else {
                                            consultDetail = titleList.get(s).toString() + ":" + NumberFormatUtils.addThousands(bigDecimal.toPlainString());
                                        }
                                    } else {
                                        if (bigDecimal.toPlainString().split("\\.")[bigDecimal.toPlainString().split("\\.").length - 1].equals("0")) {
                                            consultDetail = titleList.get(s).toString() + ":" + NumberFormatUtils.addThousands(bigDecimal.toPlainString().split("\\.")[0]) + "; ";
                                        } else {
                                            consultDetail = titleList.get(s).toString() + ":" + NumberFormatUtils.addThousands(bigDecimal.toPlainString()) + "; ";
                                        }
                                    }
                                } else {
                                    // 拼接非数值型信息
                                    if (s == titleList.size() - 1) {
                                        consultDetail = titleList.get(s).toString() + ":" + ObjectUtil.obj2String(dataList.get(j).get(s));
                                    } else {
                                        consultDetail = titleList.get(s).toString() + ":" + ObjectUtil.obj2String(dataList.get(j).get(s)) + "; ";
                                    }
                                }
                                partConList.add(consultDetail);
                            }

                            for (int q = 0; q < partConList.size(); q++) {
                                consultDetails.append(partConList.get(q));
                            }

                            String mergeConsultDetails = consultDetails.toString();

                            dataLists.add(consultationType);
                            dataLists.add(consultationName);
                            dataLists.add(consultKeynote);
                            dataLists.add(mergeConsultDetails);
                            dataLists.add(acceptsHr);// 接收人
                            dataLists.add(acceptsNum);

                            List<Object> minList = new ArrayList<>();
                            for (int a = 0; a < dataLists.size(); a++) {
                                minList.add(dataLists.get(a));
                            }
                            lists.add(minList);
                            dataLists.clear();
                        }
                        
                        for (int h = 0; h < lists.size(); h++) {
                            StringBuffer acceptUser = new StringBuffer();
                            
                            String hrConsultationType = (String) lists.get(h).get(0);
                            String hrConsultationName = (String) lists.get(h).get(1);
                            String hrConsultKeynote = (String) lists.get(h).get(2);
                            String hrConsultDetails = (String) lists.get(h).get(3);
                            String hrAccepts = (String) lists.get(h).get(4);
                            String users = (String) lists.get(h).get(5);// 员工编号

                            Message msg = new Message();
                            msg.setMessageContext(hrConsultDetails);// 訊息內容
                            msg.setMessageKeyNote(hrConsultKeynote);// 訊息主旨
                            msg.setMessageType(hrConsultationType);// 訊息類型
                            msg.setMessageTypeName(hrConsultationName);// 訊息名称
                            msg.setAccpectUser(hrAccepts);// 接收者
                            msg.setSendTime((new Date()));// 时间
                            Integer messageCount = messageService.addMessageByHr(msg, users);
                            
                            // 將發起人也加入到接收者中
                            acceptUser.append(curUser.getUserCode() + "," + users);
                            // 調用BWCE接口推送消息
                            sendMessage(msg, curUser.getUserCode(), acceptUser.toString());
                            
                            result.put("success", true);
                            result.put("message", "提交成功");
                            result.put("messageCount", messageCount);
                            setMessageCount(request, messageCount);
                            
                        }
                        
                        
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        result.put("success", false);
                        result.put("message", "提交失敗,請稍後重試");
                    }
                }
            }
        } else {
            result.put("message", "文件導入失敗");
            result.put("success", false);
        }
        return result;
    }
	
	
	
	
	//下载模板
	  @RequestMapping("/excleDownload")
	  public String excleDownload(HttpServletRequest request,HttpServletResponse response) {
	     
	      response.setCharacterEncoding("utf-8");
	      String name = "獎金通知模板";
	      response.setContentType("text/html");
	      String codedFileName = null;//编码处理后文件名
	      try {
	          
	          //String path="D:";//把訊息導入模板.xlsx模板复制到D盘下
	    	  String path="/app/cache/excel/";//docker容器
	    	  
	          String fileName = "ZXHR.xlsx";
	          InputStream inputStream = new FileInputStream(new File(path+ File.separator + fileName));
	          response.reset();
	          
	          String agent = request.getHeader("USER-AGENT");  
	          if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie  
	               codedFileName = java.net.URLEncoder.encode(name, "UTF8");  
	               
	          } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等  
	        	  
	        	  codedFileName = new String(name.getBytes("UTF-8"), "iso-8859-1");  
	          }  
	          response.setHeader("Content-disposition", "attachment; filename=" + codedFileName + ".xlsx");
	          response.setContentType("application/x-download");
	          //response.setHeader("Content-disposition", "attachment; filename=" + new String(name.getBytes(),"iso-8859-1") + ".xlsx");
	         
	          OutputStream os = response.getOutputStream();
	          byte[] b = new byte[2048];
	          int length;
	          while ((length = inputStream.read(b)) > 0) {
	              os.write(b, 0, length);
	          }
	          os.close();

	          inputStream.close();
	      } catch (FileNotFoundException e) {
	          e.printStackTrace();
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	      return null;
	  }
	  
	  /**
	   * @description: 校驗格式內容
	   * @author: mazongjian
	   * @param dataList
	   * @return  
	   * @date 2019年5月23日
	   */
	  private Map<String, Object> validateMessageContent(List<List<Object>> dataList) {
	      Map<String, Object> result = new HashMap<>();
	      if (CollectionUtils.isEmpty(dataList)) {
	          result.put("message", "匯入內容不能為空");
	          result.put("success", false);
	          return result;
	      }
	      
	      // 判斷訊息主旨是否為空
	      List<Object> purpose = dataList.get(0);
	      if (StringUtils.isEmpty(ObjectUtil.obj2String(purpose.get(0)))) {
	          result.put("message", "請填寫訊息主旨");
              result.put("success", false);
              return result;
	      }
	      
	      // 判斷訊息標題是否為空
	      if (dataList.size() < 2) {
	          result.put("message", "請填寫訊息標題和訊息內容");
              result.put("success", false);
              return result;
	      }
	      
	      // 判斷是否有空標題
	      List<Object> title = dataList.get(1);
	      int lastTitleIndex = 0;
	      for (int i = title.size() - 1; i >= 0; i--) {
	          if (title.get(i) != null && !"".equals(title.get(i))) {
	              lastTitleIndex = i;
	              break;
	          }
	      }
	      for (int i = 0; i <= lastTitleIndex; i++) {
	          if (lastTitleIndex < 3 || title.get(i) == null || "".equals(title.get(i))) {
	              result.put("message", "請正確填寫訊息標題");
	              result.put("success", false);
	              return result;
	          }
	      }
	      
	      // 判斷訊息內容是否為空
          if (dataList.size() < 3) {
              result.put("message", "請填寫訊息內容");
              result.put("success", false);
              return result;
          }
	      return null;
	  }
	  
	/**
	 * @description: 調用BWCE接口進行消息推送
	 * @author: mazongjian
	 * @param msg  
	 * @date 2019年5月30日
	 */
    public void sendMessage(Message msg, String sendUser, String acceptUser) {
        WebSendMessageRq webSendMessageRq = new WebSendMessageRq();
        webSendMessageRq.setMessageTitle(msg.getMessageKeyNote());
        webSendMessageRq.setMessageContext(msg.getMessageContext());
        webSendMessageRq.setMessageType(msg.getMessageType());
        webSendMessageRq.setSendUser(sendUser);
        webSendMessageRq.setAcceptUser(acceptUser);
        webSendMessageRq.setIsPush("1");
        logger.info("獎金消息推送請求報文：" + JSON.toJSONString(webSendMessageRq));
//        WebSendMessageRs webSendMessageRs = interfaceService.webSendMessage(webSendMessageRq);
        WebSendMessageRs webSendMessageRs = interfaceService.webPushMessage(webSendMessageRq);
        if (webSendMessageRs != null) {
            String returnCode = webSendMessageRs.getReturnCode();
            String returnMessage = webSendMessageRs.getReturnMessage();
            logger.info("獎金消息推送響應編碼：" + returnCode + "，消息ID：" + msg.getMessageId());
            logger.info("獎金消息推送響應內容：" + returnMessage + "，消息ID：" + msg.getMessageId());
        }
    }
    
}
