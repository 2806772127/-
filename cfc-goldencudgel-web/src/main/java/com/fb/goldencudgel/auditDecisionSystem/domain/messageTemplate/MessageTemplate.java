package com.fb.goldencudgel.auditDecisionSystem.domain.messageTemplate;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "MESSAGE_TEMPLATE")
public class MessageTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TEMPLATE_ID")
	private String templateId;
	@Column(name = "TEMPLATE_THEME")
	private String templateTheme;
	@Column(name = "TEMPLATE_CONTENT")
	private String templateContent;
	@Column(name = "REMARK")
	private String remark;
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTemplateTheme() {
		return templateTheme;
	}
	public void setTemplateTheme(String templateTheme) {
		this.templateTheme = templateTheme;
	}
	public String getTemplateContent() {
		return templateContent;
	}
	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	

}
