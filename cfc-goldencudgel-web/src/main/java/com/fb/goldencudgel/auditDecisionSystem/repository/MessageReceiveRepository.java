package com.fb.goldencudgel.auditDecisionSystem.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.message.MessageReceive;

@Repository
public interface MessageReceiveRepository extends JpaRepository<MessageReceive, String> {
   
	@Modifying
    @Transactional
	@Query("delete from MessageReceive where messageId =:messageId and id =:id")
	void deleteByMessageId(@Param("messageId")String messageId, @Param("id")String receiveId);

	@Modifying
    @Transactional
	@Query("update MessageReceive set readTime=:readTime where id =:receiveId")
	void updateReadTime(@Param("readTime")Date date, @Param("receiveId")String receiveId);

	@Query("from MessageReceive where messageId =:messageId")
	List<MessageReceive> getMessages(@Param("messageId")String messageId);

	@Query("from MessageReceive where id =:receiveId")
	MessageReceive findByReceviceId(@Param("receiveId")String receiveId);

	@Query("from MessageReceive where readTime is null and acceptUser =:acceptUser")
	List<MessageReceive> getUnreadMessage(@Param("acceptUser")String acceptUser);
}
