package com.fb.goldencudgel.auditDecisionSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.message.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

}
