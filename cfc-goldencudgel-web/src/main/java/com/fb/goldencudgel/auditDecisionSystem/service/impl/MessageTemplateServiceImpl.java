package com.fb.goldencudgel.auditDecisionSystem.service.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.comwave.core.jpa.BaseJpaDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Service
@Transactional
public class MessageTemplateServiceImpl  extends  BaseJpaDAO{

    private Logger logger = LoggerFactory.getLogger(MessageTemplateServiceImpl.class);
   
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getMessageTemplate(String templateId) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("SELECT TEMPLATE_THEME,TEMPLATE_CONTENT FROM MESSAGE_TEMPLATE WHERE 1=1 ");
		sql.append(" AND TEMPLATE_ID = :templateId");
		params.put("templateId", templateId);
		List<Object[]> context = findBySQL(sql, params).getContent();
		for (Object[] ob : context) {
			String templateTheme = ob[0] == null ? "" : ob[0].toString();
			String templateContent = ob[1] == null ? "" : ob[1].toString();
			result.put(templateTheme, templateContent);
		}
		return result;
	}
    
}
