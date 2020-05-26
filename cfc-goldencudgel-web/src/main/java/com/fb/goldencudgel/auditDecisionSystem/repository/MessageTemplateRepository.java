package com.fb.goldencudgel.auditDecisionSystem.repository;
import com.fb.goldencudgel.auditDecisionSystem.domain.messageTemplate.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, String> {
	
	@Query("from MessageTemplate where templateId=:templateId")
    MessageTemplate findByTemplateId(@Param("templateId") String templateId);
}
