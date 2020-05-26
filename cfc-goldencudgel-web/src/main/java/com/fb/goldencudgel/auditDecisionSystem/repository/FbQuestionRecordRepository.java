package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbQuestionRecord;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaire;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireAnswer;
import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FbQuestionRecordRepository extends JpaRepository<FbQuestionRecord, String> {

    @Query(" from FbQuestionRecord t where t.measureId=:measureId ")
    List<FbQuestionRecord> findByMeasureId(@Param("measureId")String measureId);

    @Query("from FbQuestionRecord where id=:id")
    FbQuestionRecord findByrecordId(@Param("id")String recordId);

    @Query("select qr from FbQuestionRecord qr inner join FbQuestionnaireDetail qd on qr.questionId = qd.id" +
            " where qr.measureId=:measureId and qr.createTime = (select max(createTime) from FbQuestionRecord where measureId=:measureId1) " +
            " and qr.questionType=:questionType order by qd.sortNo asc")
    List<FbQuestionRecord> findRecord(@Param("measureId")String measureId,@Param("questionType")String questionType,@Param("measureId1")String measureId1);

    @Modifying
    @Transactional
    @Query("delete from FbQuestionRecord where measureId =:measureId")
    void deleteAllByMeasureId(@Param("measureId")String measureId);

    @Query(value="SELECT\n" +
            "\tt.id,\n" +
            "\tt.q question,\n" +
            "\tgroup_concat(DISTINCT t.ANSWER) answer,\n" +
            "\tgroup_concat(t.c_q, '：', t.c_a, '' ORDER BY SORT_NO_B2) child,\n" +
            "SORT_NO_B1,SORT_NO_B2\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\ta1.ID id,\n" +
            "\t\t\ta1.QUESTION_NAME q,\n" +
//            "\t\t\ta1.QUESTUIB_ANSWER ANSWER,\n" +
            "CASE WHEN INSTR(a1.QUESTION_NAME,'仟元')>0 THEN FORMAT(a1.QUESTUIB_ANSWER,0) ELSE a1.QUESTUIB_ANSWER END ANSWER,"+
            "\t\t\ta1.QUESTION_LEVEL,\n" +
            "\t\t\ta2.ID c_id,\n" +
            "\t\t\ta2.QUESTION_NAME c_q,\n" +
//            "\t\t\ta2.QUESTUIB_ANSWER c_a,\n" +
            "CASE WHEN INSTR(a2.QUESTION_NAME,'仟元')>0 THEN FORMAT(a2.QUESTUIB_ANSWER,0) ELSE a2.QUESTUIB_ANSWER END c_a,"+
            "\t\t\tCONVERT(b1.SORT_NO,SIGNED) AS SORT_NO_B1,\n" +
            "\t\t\tCONVERT(b2.SORT_NO,SIGNED) AS SORT_NO_B2\n" +
            "\t\tFROM\n" +
            "\t\t\tFB_QUESTIONRECORD a1\n" +
            "\t\tLEFT JOIN FB_QUESTIONRECORD a2 ON a1.QUESTION_ID = a2.FATHER_QUERTION\n" +
            "\t\tAND a2.QUESTION_LEVEL = '2'\n" +
            "\t\tAND a2.MEASURE_ID =:measureId\n" +
            "\t\tAND a2.QUESTION_TYPE = :type \n" +
            "\t\tLEFT JOIN FB_QUESTIONNAIRE_DETAIL b1 ON a1.QUESTION_ID = b1.ID\n" +
            "\t\tLEFT JOIN FB_QUESTIONNAIRE_DETAIL b2 ON a2.QUESTION_ID = b2.ID\n" +
            "\t\tWHERE\n" +
            "\t\t\ta1.QUESTION_LEVEL = '1'\n" +
            "\t\tAND a1.MEASURE_ID =:measureId\n" +
            "\t\tAND a1.QUESTION_TYPE =:type \n" +
            "\t\tORDER BY\n" +
            "\t\t\tb1.SORT_NO,\n" +
            "\t\t\tb2.SORT_NO\n" +
            "\t) t\n" +
            "GROUP BY\n" +
            "\tt.id" +
            "\tORDER BY SORT_NO_B1;", nativeQuery = true)
    List findByQuestion(@Param("measureId")String measureId,@Param("type") String type);

    /*@Query(value = "select " +
            "c.q question, " +
            "c.ANSWER answer, " +
            "CONCAT_WS('。',c2_qa,c4_qa) child, " +
            "CONCAT_WS('。',c3_qa,c5_qa) child3, " +
            "f.count questionCount, " +
            "0 answerCount, " +
            "0 isAnd, " +
            "c.TYPE type  " +
            "from " +
            "(SELECT " +
            "   d.id id, " +
            "   d.q q, " +
            "   d.ANSWER ANSWER, " +
            "   d.QUESTION_LEVEL, " +
            "   d.c_id c_id, " +
            "   d.c_q c_q, " +
            "   d.c_a c_a, " +
            "   CONCAT(d.c_q,'：',d.c_a) c2_qa, " +
            "   d.TYPE, "+
            "   d.c3_id c3_id, " +
            "   d.c3_q c3_q,   " +
            "   d.c3_a c3_a, " +
            "   CONCAT(d.c3_q,'：',d.c3_a) c3_qa, " +
            "   d.c4_q c4_q, " +
            "   d.c4_a c4_a, " +
            "   CONCAT(d.c4_q,'：',d.c4_a) c4_qa, " +
            "   d.c5_q c5_q, " +
            "   d.c5_a c5_a, " +
            "   CONCAT(d.c5_q,'：',d.c5_a) c5_qa, " +
            "   d.SORT_NO_B1 SORT_NO_B1, " +
            "   d.SORT_NO_B2 SORT_NO_B2, " +
            "   d.SORT_NO_B3 SORT_NO_B3, " +
            "   d.SORT_NO_B4 SORT_NO_B4, " +
            "   d.SORT_NO_B5 SORT_NO_B5, " +
            "   d.QUESTION_ID " +
            "   FROM " +
            "   (SELECT " +
            "            a1.ID id, " +
            "            a1.QUESTION_NAME q, " +
            "            CASE WHEN INSTR(a1.QUESTION_NAME,'仟元')>0 THEN FORMAT(a1.QUESTUIB_ANSWER,0) ELSE a1.QUESTUIB_ANSWER END ANSWER, " +
            "            a1.QUESTION_LEVEL, " +
            "            a2.ID c_id, " +
            "            a2.QUESTION_NAME c_q, " +
            "            CASE WHEN INSTR(a2.QUESTION_NAME,'仟元')>0 THEN FORMAT(a2.QUESTUIB_ANSWER,0) ELSE a2.QUESTUIB_ANSWER END c_a, " +
            "            a2.TYPE, "+
            "            a3.ID c3_id, " +
            "            a3.QUESTION_NAME c3_q,   " +
            "            CASE WHEN INSTR(a3.QUESTION_NAME,'仟元')>0 THEN FORMAT(a3.QUESTUIB_ANSWER,0) ELSE a3.QUESTUIB_ANSWER END c3_a, " +
            "            a4.QUESTION_NAME c4_q, " +
            "            CASE WHEN INSTR(a4.QUESTION_NAME,'仟元')>0 THEN FORMAT(a4.QUESTUIB_ANSWER,0) ELSE a4.QUESTUIB_ANSWER END c4_a, " +
            "            a5.QUESTION_NAME c5_q, " +
            "            CASE WHEN INSTR(a5.QUESTION_NAME,'仟元')>0 THEN FORMAT(a5.QUESTUIB_ANSWER,0) ELSE a5.QUESTUIB_ANSWER END c5_a,  " +
            "            CONVERT(b1.SORT_NO,SIGNED) AS SORT_NO_B1, " +
            "            CONVERT(b2.SORT_NO,SIGNED) AS SORT_NO_B2, " +
            "            CONVERT(b3.SORT_NO,SIGNED) AS SORT_NO_B3, " +
            "            CONVERT(b4.SORT_NO,SIGNED) AS SORT_NO_B4, " +
            "            CONVERT(b5.SORT_NO,SIGNED) AS SORT_NO_B5, " +
            "            a1.QUESTION_ID " +
            "            FROM " +
            "            FB_QUESTIONRECORD a1 " +
            "            LEFT JOIN FB_QUESTIONRECORD a2 ON a1.QUESTION_ID = a2.FATHER_QUERTION " +
            "            AND a2.QUESTION_LEVEL = '2' " +
            "            AND a2.MEASURE_ID =:measureId " +
            "            AND a2.QUESTION_TYPE = :type " +
            "            AND a2.QUESTUIB_ANSWER !='' " +
            "            LEFT JOIN FB_QUESTIONRECORD a4 ON a2.QUESTION_ID = a4.FATHER_QUERTION " +
            "            AND a4.QUESTION_LEVEL = '2' " +
            "            AND a4.MEASURE_ID =:measureId " +
            "            AND a4.QUESTION_TYPE = :type " +
            "            AND a4.QUESTUIB_ANSWER !='' " +
            "            LEFT JOIN FB_QUESTIONRECORD a3 ON (a2.QUESTION_ID = a3.FATHER_QUERTION or  a1.QUESTION_ID = a3.FATHER_QUERTION)  " +
            "            AND a3.QUESTION_LEVEL = '3'  " +
            "            AND a3.MEASURE_ID =:measureId " +
            "            AND a3.QUESTION_TYPE = :type " +
            "            AND a3.QUESTUIB_ANSWER !='' " +
            "            LEFT JOIN FB_QUESTIONRECORD a5 ON a3.QUESTION_ID = a5.FATHER_QUERTION " +
            "            AND a5.QUESTION_LEVEL = '3' " +
            "            AND a5.MEASURE_ID =:measureId " +
            "            AND a5.QUESTION_TYPE = :type " +
            "            AND a5.QUESTUIB_ANSWER !='' " +
            "            LEFT JOIN FB_QUESTIONNAIRE_DETAIL b1 ON a1.QUESTION_ID = b1.ID " +
            "            LEFT JOIN FB_QUESTIONNAIRE_DETAIL b2 ON a2.QUESTION_ID = b2.ID " +
            "            LEFT JOIN FB_QUESTIONNAIRE_DETAIL b3 ON a3.QUESTION_ID = b3.ID " +
            "            LEFT JOIN FB_QUESTIONNAIRE_DETAIL b4 ON a4.QUESTION_ID = b4.ID " +
            "            LEFT JOIN FB_QUESTIONNAIRE_DETAIL b5 ON a5.QUESTION_ID = b5.ID " +
            "            WHERE " +
            "            a1.QUESTION_LEVEL = '1' " +
            "            AND a1.MEASURE_ID =:measureId " +
            "            AND a1.QUESTION_TYPE =:type " +
            "            AND a1.QUESTUIB_ANSWER !='' " +
            "            ORDER BY " +
            "            CONVERT(b1.SORT_NO,SIGNED), " +
            "            CONVERT(b2.SORT_NO,SIGNED)," +
            "            CONVERT(b3.SORT_NO,SIGNED) )d )c " +
            "LEFT JOIN " +
            "(select d.id,count(id)  count from (SELECT " +
            "            a1.ID id, " +
            "            a1.QUESTION_NAME q, " +
            "            a1.QUESTUIB_ANSWER ANSWER, " +
            "            a1.QUESTION_LEVEL, " +
            "            a2.ID c_id, " +
            "            a2.QUESTION_NAME c_q, " +
            "            a2.QUESTUIB_ANSWER c_a, " +
            "            CONVERT(b1.SORT_NO,SIGNED) AS SORT_NO_B1, " +
            "            CONVERT(b2.SORT_NO,SIGNED) AS SORT_NO_B2," +
            "            CONVERT(b3.SORT_NO,SIGNED) AS SORT_NO_B3," +
            "            a1.QUESTION_ID " +
            "            FROM " +
            "            FB_QUESTIONRECORD a1 " +
            "            LEFT JOIN FB_QUESTIONRECORD a2 ON a1.QUESTION_ID = a2.FATHER_QUERTION " +
            "            AND a2.QUESTION_LEVEL = '2' " +
            "            AND a2.MEASURE_ID =:measureId " +
            "            AND a2.QUESTION_TYPE = :type " +
            "            AND a2.QUESTUIB_ANSWER !='' " +
            "            LEFT JOIN FB_QUESTIONRECORD a4 ON a2.QUESTION_ID = a4.FATHER_QUERTION " +
            "            AND a4.QUESTION_LEVEL = '2' " +
            "            AND a4.MEASURE_ID =:measureId " +
            "            AND a4.QUESTION_TYPE = :type " +
            "            AND a4.QUESTUIB_ANSWER !='' " +
            "            LEFT JOIN FB_QUESTIONRECORD a3 ON (a2.QUESTION_ID = a3.FATHER_QUERTION or  a1.QUESTION_ID = a3.FATHER_QUERTION)  " +
            "            AND a3.QUESTION_LEVEL = '3'  " +
            "            AND a3.MEASURE_ID =:measureId " +
            "            AND a3.QUESTION_TYPE = :type " +
            "            AND a3.QUESTUIB_ANSWER !='' " +
            "            LEFT JOIN FB_QUESTIONRECORD a5 ON a3.QUESTION_ID = a5.FATHER_QUERTION " +
            "            AND a5.QUESTION_LEVEL = '3' " +
            "            AND a5.MEASURE_ID =:measureId " +
            "            AND a5.QUESTION_TYPE = :type " +
            "            AND a5.QUESTUIB_ANSWER !='' " +
            "            LEFT JOIN FB_QUESTIONNAIRE_DETAIL b1 ON a1.QUESTION_ID = b1.ID " +
            "            LEFT JOIN FB_QUESTIONNAIRE_DETAIL b2 ON a2.QUESTION_ID = b2.ID " +
            "            LEFT JOIN FB_QUESTIONNAIRE_DETAIL b3 ON a3.QUESTION_ID = b3.ID  " +
            "            WHERE " +
            "            a1.QUESTION_LEVEL = '1' " +
            "            AND a1.MEASURE_ID =:measureId " +
            "            AND a1.QUESTION_TYPE =:type " +
            "            AND a1.QUESTUIB_ANSWER !='' " +
            "            ORDER BY " +
            "            CONVERT(b1.SORT_NO,SIGNED), " +
            "            CONVERT(b2.SORT_NO,SIGNED)," +
            "            CONVERT(b3.SORT_NO,SIGNED))d " +
            "            GROUP BY d.id) f on " +
            "f.id = c.id " +
            "ORDER BY SORT_NO_B1,SORT_NO_B2,SORT_NO_B3,SORT_NO_B4,SORT_NO_B5 ",nativeQuery = true)*/
    @Query(value = "select  " +
            "            c.q question,  " +
            "            c.ANSWER answer,  " +
            "            c2_qa child,  " +
            "            CONCAT_WS('。',c3_qa,c4_qa,c5_qa) child3,  " +
            "            f.count questionCount,  " +
            "            0 answerCount,  " +
            "            0 isAnd " +
            "           from " +
            "          (SELECT " +
            "               d.id id, " +
            "               d.q q,  " +
            "               d.ANSWER ANSWER,  " +
            "               CONCAT(d.c_q,'：',d.c_a) c2_qa,  " +
            "               CONCAT(d.c3_q,'：',d.c3_a) c3_qa, " +
            "               CONCAT(d.c4_q,'：',d.c4_a) c4_qa, " +
            "               CONCAT(d.c5_q,'：',d.c5_a) c5_qa, " +
            "               d.SORT_NO_B1 SORT_NO_B1,  " +
            "               d.SORT_NO_B2 SORT_NO_B2,  " +
            "               d.SORT_NO_B3 SORT_NO_B3,  " +
            "               d.QUESTION_ID  " +
            "               FROM  " +
            "               (SELECT  " +
            "                        a1.ID id,  " +
            "                        a1.QUESTION_NAME q,  " +
            "                        a1.QUESTUIB_ANSWER  ANSWER,  " +
            "                        a1.QUESTION_LEVEL,  " +
            "                        a2.ID c_id,  " +
            "                        a2.QUESTION_NAME c_q,  " +
            "                        a2.QUESTUIB_ANSWER  c_a,  " +
            "                        a2.TYPE, " +
            "                        a3.ID c3_id,  " +
            "                        a3.QUESTION_NAME c3_q,    " +
            "                        a3.QUESTUIB_ANSWER  c3_a,                        " +
            "                        a4.QUESTION_NAME c4_q,  " +
            "                        a4.QUESTUIB_ANSWER  c4_a, " +
            "                        a5.QUESTION_NAME c5_q,  " +
            "                        a5.QUESTUIB_ANSWER  c5_a,   " +
            "                        CONVERT(b1.SORT_NO,SIGNED) AS SORT_NO_B1,  " +
            "                        CONVERT(b2.SORT_NO,SIGNED) AS SORT_NO_B2,  " +
            "                        CONVERT(b3.SORT_NO,SIGNED) AS SORT_NO_B3,  " +
            "                        CONVERT(b4.SORT_NO,SIGNED) AS SORT_NO_B4,  " +
            "                        a1.QUESTION_ID  " +
            "                        FROM  " +
            "                        FB_QUESTIONRECORD a1  " +
            "                        LEFT JOIN FB_QUESTIONRECORD a2 ON a1.QUESTION_ID = a2.FATHER_QUERTION  " +
            "                        AND a2.QUESTION_LEVEL = '2'  " +
            "                        AND a2.MEASURE_ID =:measureId " +
            "                        AND a2.QUESTION_TYPE = :type " +
            "                        AND a2.QUESTUIB_ANSWER !=''   " +
            "                        AND locate('是否繼續添加',a2.QUESTION_NAME )=0 " +
            "                        AND locate('是否添加',a2.QUESTION_NAME )=0 " +
            "                        LEFT JOIN FB_QUESTIONRECORD a3 on a2.QUESTION_ID = a3.FATHER_QUERTION " +
            "                        AND a3.QUESTION_LEVEL = '3'    " +
            "                        AND a3.MEASURE_ID =:measureId " +
            "                        AND a3.QUESTION_TYPE = :type " +
            "                        AND a3.QUESTUIB_ANSWER !=''     " +
            "                        AND locate('是否繼續添加',a3.QUESTION_NAME )=0 " +
            "                        LEFT JOIN FB_QUESTIONRECORD a5 on a1.QUESTION_ID = a5.FATHER_QUERTION " +
            "                        AND a5.QUESTION_LEVEL = '3'   " +
            "                        AND a5.MEASURE_ID =:measureId " +
            "                        AND a5.QUESTION_TYPE = :type " +
            "                        AND a5.QUESTUIB_ANSWER !=''     " +
            "                        AND locate('是否繼續添加',a5.QUESTION_NAME )=0 " +
            "                        LEFT JOIN FB_QUESTIONRECORD a4 ON a3.QUESTION_ID = a4.FATHER_QUERTION " +
            "                        AND a4.QUESTION_LEVEL = '3'    " +
            "                        AND a4.MEASURE_ID =:measureId " +
            "                        AND a4.QUESTION_TYPE = :type " +
            "                        AND a4.QUESTUIB_ANSWER !=''     " +
            "                        AND locate('是否繼續添加',a4.QUESTION_NAME )=0 " +
            "                        LEFT JOIN FB_QUESTIONNAIRE_DETAIL b1 ON a1.QUESTION_ID = b1.ID   " +
            "                        LEFT JOIN FB_QUESTIONNAIRE_DETAIL b2 ON a2.QUESTION_ID = b2.ID   " +
            "                        LEFT JOIN FB_QUESTIONNAIRE_DETAIL b3 ON a3.QUESTION_ID = b3.ID " +
            "                        LEFT JOIN FB_QUESTIONNAIRE_DETAIL b4 ON a4.QUESTION_ID = b4.ID  " +
            "                        LEFT JOIN FB_QUESTIONNAIRE_DETAIL b5 ON a5.QUESTION_ID = b5.ID " +
            "                        WHERE   " +
            "                        a1.QUESTION_LEVEL = '1'  " +
            "                        AND a1.MEASURE_ID =:measureId " +
            "                        AND a1.QUESTION_TYPE =:type " +
            "                        AND a1.QUESTUIB_ANSWER !='' " +
            "                        AND locate('是否繼續添加',a1.QUESTION_NAME )=0 " +
            "                        ORDER BY   " +
            "                        CONVERT(b1.SORT_NO,SIGNED),   " +
            "                        CONVERT(b2.SORT_NO,SIGNED),  " +
            "                        CONVERT(b3.SORT_NO,SIGNED), " +
            "                        CONVERT(b4.SORT_NO,SIGNED) )d)c " +
            "               LEFT JOIN   " +
            "            (select d.id,count(id)  count from (SELECT   " +
            "                        a1.ID id,   " +
            "                        CONVERT(b1.SORT_NO,SIGNED) AS SORT_NO_B1,  " +
            "                        CONVERT(b2.SORT_NO,SIGNED) AS SORT_NO_B2,  " +
            "                        CONVERT(b3.SORT_NO,SIGNED) AS SORT_NO_B3,  " +
            "                        a1.QUESTION_ID   " +
            "                        FROM   " +
            "                        FB_QUESTIONRECORD a1   " +
            "                        LEFT JOIN FB_QUESTIONRECORD a2 ON a1.QUESTION_ID = a2.FATHER_QUERTION   " +
            "                        AND a2.QUESTION_LEVEL = '2'   " +
            "                        AND a2.MEASURE_ID =:measureId " +
            "                        AND a2.QUESTION_TYPE = :type  " +
            "                        AND a2.QUESTUIB_ANSWER !=''   " +
            "                        AND locate('是否繼續添加',a2.QUESTION_NAME )=0 " +
            "                        AND locate('是否添加',a2.QUESTION_NAME )=0 " +
            "                        LEFT JOIN FB_QUESTIONRECORD a3 ON a2.QUESTION_ID = a3.FATHER_QUERTION " +
            "                        AND a3.QUESTION_LEVEL = '3'    " +
            "                        AND a3.MEASURE_ID =:measureId " +
            "                        AND a3.QUESTION_TYPE = :type " +
            "                        AND a3.QUESTUIB_ANSWER !=''     " +
            "                        AND locate('是否繼續添加',a3.QUESTION_NAME )=0 " +
            "                        LEFT JOIN FB_QUESTIONRECORD a5 on a1.QUESTION_ID = a5.FATHER_QUERTION " +
            "                        AND a5.QUESTION_LEVEL = '3'    " +
            "                        AND a5.MEASURE_ID =:measureId " +
            "                        AND a5.QUESTION_TYPE = :type " +
            "                        AND a5.QUESTUIB_ANSWER !=''     " +
            "                        AND locate('是否繼續添加',a5.QUESTION_NAME )=0 " +
            "                        LEFT JOIN FB_QUESTIONRECORD a4 ON a3.QUESTION_ID = a4.FATHER_QUERTION " +
            "                        AND a4.QUESTION_LEVEL = '3'    " +
            "                        AND a4.MEASURE_ID =:measureId " +
            "                        AND a4.QUESTION_TYPE = :type " +
            "                        AND a4.QUESTUIB_ANSWER !=''     " +
            "                        AND locate('是否繼續添加',a4.QUESTION_NAME )=0 " +
            "                        LEFT JOIN FB_QUESTIONNAIRE_DETAIL b1 ON a1.QUESTION_ID = b1.ID   " +
            "                        LEFT JOIN FB_QUESTIONNAIRE_DETAIL b2 ON a2.QUESTION_ID = b2.ID   " +
            "                        LEFT JOIN FB_QUESTIONNAIRE_DETAIL b3 ON a3.QUESTION_ID = b3.ID " +
            "                        LEFT JOIN FB_QUESTIONNAIRE_DETAIL b4 ON a4.QUESTION_ID = b4.ID      " +
            "                        WHERE   " +
            "                        a1.QUESTION_LEVEL = '1'   " +
            "                        AND a1.MEASURE_ID =:measureId " +
            "                        AND a1.QUESTION_TYPE =:type " +
            "                        AND a1.QUESTUIB_ANSWER !='' " +
            "                        AND locate('是否繼續添加',a1.QUESTION_NAME )=0 " +
            "                        ORDER BY   " +
            "                        CONVERT(b1.SORT_NO,SIGNED),   " +
            "                        CONVERT(b2.SORT_NO,SIGNED),  " +
            "                        CONVERT(b3.SORT_NO,SIGNED))d   " +
            "                        GROUP BY d.id) f on   " +
            "            f.id = c.id   " +
            "            ORDER BY SORT_NO_B1,SORT_NO_B2,SORT_NO_B3" ,nativeQuery = true)
    List findQuestionByMeasureId(@Param("measureId")String measureId,@Param("type") String type);

    @Query(value = "select * from (select name from FB_QUESTIONNAIRE where QUESTIONNAIRE_TYPE in ('04') and  IS_ENABLE='1' ORDER BY QUESTIONNAIRE_TYPE limit 9999) a " +
            "union all " +
            "select * from (select name from FB_QUESTIONNAIRE where QUESTIONNAIRE_TYPE = :type and IS_ENABLE='1' limit 9999) b " +
            "union all " +
            "select * from (select name from FB_QUESTIONNAIRE where QUESTIONNAIRE_TYPE in ('05','06','13') and  IS_ENABLE='1' ORDER BY QUESTIONNAIRE_TYPE limit 9999) a ",nativeQuery = true)
    List findQuestionName(@Param("type") String type);

    @Query(value = "select DISTINCT QUESTION_TYPE   " +
                   "from FB_QUESTIONRECORD  where MEASURE_ID = :measureId ",nativeQuery = true)
    List findTypeByMeasureId (@Param("measureId")String measureId);

    @Query(value="SELECT QUESTUIB_ANSWER from FB_QUESTIONRECORD where QUESTION_ID=:id AND MEASURE_ID=:measureId",nativeQuery = true)
    String findByIds(@Param("id")String id,@Param("measureId")String measureId);
    
    @Query(" from FbQuestionnaire t where t.questionniareType=:questionType ")
    FbQuestionnaire findByQuestionType(@Param("questionType")String questionType);

//    @Query(value="select DISTINCT QUESTION_TYPE  from FB_QUESTIONRECORD  where MEASURE_ID =:measureId and QUESTION_TYPE  not in ('04','05','06','13') ORDER BY CREATE_TIME DESC",nativeQuery = true)
//    List<String> findByTypes(@Param("measureId")String measureId);
    
   
    @Query(value = "select NAME from FB_QUESTIONNAIRE t where t.NAME=:questionnaireType ",nativeQuery=true)
    String findQuestionTypeByType(@Param("questionnaireType")String questionnaireType);
    
    @Query(value = "select COM_CREDIT_INDUSTRY_CODE from FB_LOAN_COMPANY t where t.COMPILATION_NO=:compilationNo ",nativeQuery=true)
    String findQuestionTypeByCompilationNo(@Param("compilationNo")String compilationNo);
    
     @Query(value = "select NAME from FB_QUESTIONNAIRE t where t.QUESTIONNAIRE_TYPE=:questionnaireType and IS_ENABLE='1' ",nativeQuery=true)
    String findIndustryNameByType(@Param("questionnaireType")String questionnaireType);

    @Query(value = "select DISTINCT QUESTION_TYPE from FB_QUESTIONRECORD where MEASURE_ID = :measureId  and QUESTION_TYPE not in ('04','05','06','13') ORDER BY CREATE_TIME DESC ",nativeQuery=true)
    List<String> findQuestionType(@Param("measureId")String measureId);

    @Query(value = "SELECT " +
            "a1.QUESTION_TYPE type," +
            "a1.QUESTION_ID id, " +
            "a1.QUESTION_NAME name, " +
            "a1.QUESTUIB_ANSWER answer, " +
            "a1.QUESTION_LEVEL level, "+
            "a1.TYPE types "+
            "FROM " +
            "FB_QUESTIONRECORD a1  " +
            "LEFT JOIN FB_QUESTIONNAIRE_DETAIL b1 ON a1.QUESTION_ID = b1.ID " +
            "WHERE " +
            "MEASURE_ID = :measureId   " +
            "AND a1.FATHER_QUERTION = :fatherQuestion "+
            "AND locate('是否繼續添加',a1.QUESTION_NAME )=0 "+
            "AND locate('是否添加',a1.QUESTION_NAME )=0 "+
            "ORDER BY " +
            "a1.QUESTION_TYPE,- -a1.SORT_NO",nativeQuery = true)
    List findFatherQuestionByMeasureId (@Param("measureId")String measureId,@Param("fatherQuestion")String fatherQuestion);

    @Query(value="select A21500,A20100,CREDITGRADE,A20300,AA0304, " +
            "AA5000,AA5200,AA0503,AA5100,AA5300, " +
            "AL0101,AL0201,AL0301,AB0803,AB0903, " +
            "AB1003,AB1103,AA1103,AA1804,AA2902, " +
            "AA3703,B10000,B10140,B20000,B20140, " +
            "B30000,B30140,AD0204  " +
            "from ZYJ_PROC_OUT_RECORD zor " +
            "left join FB_APPOINTMENT_RECORD far on zor.CASE_NO = far.ZJ_CREDIT_NUM  " +
            "where far.APPOINTMENT_ID = :appointmentId ",nativeQuery = true)
    List findRecordAnswer(@Param("appointmentId")String appointmentId);
}


