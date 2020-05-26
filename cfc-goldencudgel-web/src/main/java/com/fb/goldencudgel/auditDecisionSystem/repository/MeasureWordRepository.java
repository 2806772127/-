package com.fb.goldencudgel.auditDecisionSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbMeasureWord;

import java.util.List;

@Repository
public interface MeasureWordRepository extends JpaRepository<FbMeasureWord, String> {

	@Query("from FbMeasureWord where measureId =:measureId")
	FbMeasureWord findByMeasureId(@Param("measureId") String measureId);

	@Query(value = "SELECT\n" +
			"\tFD.SORT_NO,FQ.QUESTUIB_ANSWER\n"+",FM.SCHEDULER_ID "+
			"FROM\n" +
			"\tFB_MEASURE_WORD FM\n" +
			"LEFT JOIN FB_QUESTIONRECORD FQ ON FM.MEASURE_ID = FQ.MEASURE_ID\n" +
			"LEFT JOIN FB_QUESTIONNAIRE_DETAIL  FD ON FQ.QUESTION_ID=FD.ID WHERE FQ.QUESTION_TYPE='01'AND FD.SORT_NO in ('1','2','3','4')  AND FM.MEASURE_ID=:ids ORDER BY FQ.CREATE_TIME",nativeQuery=true)
    List findQuest(@Param("ids") String ids);

	@Query(value = "SELECT " +
					"far.COMPILATION_NAME, " +
					"far.APPOINTMENT_DATE, " +
					"far.APPOINTMENT_USER_NAME  " +
					"FROM " +
					"FB_MEASURE_WORD fmw " +
					"LEFT JOIN FB_APPOINTMENT_RECORD far ON fmw.SCHEDULER_ID = far.APPOINTMENT_ID  " +
					"WHERE " +
					"MEASURE_ID = :measureId ",nativeQuery = true)
	List findSchedulerByMeasureId(@Param("measureId") String measureId);

	@Query("from FbMeasureWord where zjxId =:zjxId")
	List<FbMeasureWord> findByZjxId(@Param("zjxId")String zjxId);

	@Query("select measureId from FbMeasureWord where measureDate = (select max(measureDate) from FbMeasureWord where compilationNo=:compilationNo)")
	public String getMaxMeasureId(@Param("compilationNo")String compilationNo);
}
