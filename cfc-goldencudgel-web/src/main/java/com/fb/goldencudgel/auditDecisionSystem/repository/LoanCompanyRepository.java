package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.LoanCompany.FbLoanCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LoanCompanyRepository extends JpaRepository<FbLoanCompany, String>  {

    @Query("from FbLoanCompany where compilationNo=:compilationNo")
    FbLoanCompany  findByComplicationNo(@Param("compilationNo")String compilationNo);

    @Query("from FbLoanCompany where compilationNo=:compilationNo ")
    List<FbLoanCompany> findByComNo(@Param("compilationNo")String compilationNo);

    @Query(value = "SELECT fu.USER_AREA_NAME AS '區域',fu.USER_GROUP_NAME AS '組',fu.USER_NAME AS '姓名',fu.USER_CODE AS '員編',(CASE fu.USER_TYPE WHEN 'Z' THEN '徵信員' ELSE '業務員' END) AS '角色名稱',(CASE fa.COM_CUSTOMER_TYPE WHEN '01' THEN '新户' ELSE '舊戶' END) AS '新戶/舊戶',fa.COMPILATION_NO AS '授信戶統編',fc.COM_NAME AS '授信戶名稱',fa.CHARGE_PERSON AS '負責人',(CASE fa.APPOINTMENT_TYPE WHEN 1 THEN di.ITEM_NAME ELSE '' END) AS '案件來源',(CASE fa.APPOINTMENT_TYPE WHEN 1 THEN fa.OTHER_INTRODUCE_ID ELSE '' END) AS '案源轉介人姓名',(CASE fa.APPOINTMENT_TYPE WHEN 1 THEN fa.OTHER_INTRODUCE_NAME ELSE '' END) AS '案源轉介人員編',(CASE fa.APPOINTMENT_TYPE WHEN 1 THEN fa.OTHER_INTRODUCE_PHONE ELSE '' END) AS '案源轉介人電話',(CASE fa.APPOINTMENT_TYPE WHEN 1 THEN fa.OTHER_INTRODUCE_ADDRESS ELSE '' END) AS '案源轉介人單位',date_format(fc.COM_ESTABDATE,'%Y-%m-%d') AS '公司設立日期',dt.ITEM_NAME AS '組織形態',fc.COM_ACTUAL_CAPITAL AS '資本額(仟元)',fc.COM_ADDRESS AS '公司登記地址',fa.BUSINESS_ADDRESS AS '實際營業地址',fa.APPOINTMENT_NAME AS '拜訪對象',(CASE fa.APPOINTMENT_POSITION WHEN 'CWRY' THEN '財務人員' WHEN 'FZR' THEN '負責人' ELSE '其他' END) AS '職稱',(CASE fa.COM_PHONE_EXTEN WHEN '' THEN CONCAT(fa.COM_PHONE_AREACODE,'-',fa.COM_PHONE_NUM) ELSE CONCAT(fa.COM_PHONE_AREACODE,'-',fa.COM_PHONE_NUM,'-',fa.COM_PHONE_EXTEN) END) AS '公司聯絡電話',date_format(fa.APPOINTMENT_DATE,'%Y/%m/%d') AS '預定拜訪日期',CONCAT(fa.APPOINTMENT_TIME_H,':',fa.APPOINTMENT_TIME_M) AS '預定拜訪時間',fa.REMARK AS '備註',fa.ZJ_CREDIT_NUM AS 'MA編號',(CASE fa.ZJ_CREDIT_NUM WHEN fa.ZJ_CREDIT_NUM IS NULL && fm.co< 1 THEN 'Y' ELSE 'N' END) AS '提交成功',date_format(fi.APPLY_DATE,'%Y/%m/%d') AS '進件申請時間',date_format(fa.CREATE_TIME,'%Y/%m/%d') AS '行程創建時間' FROM FB_APPOINTMENT_RECORD fa LEFT JOIN FB_USER fu ON fa.APPOINTMENT_USER_CODE=fu.user_code LEFT JOIN FB_LOAN_COMPANY fc ON fa.COMPILATION_NO=fc.COMPILATION_NO LEFT JOIN FB_APPLY_INCOM fi ON fa.COMPILATION_NO=fi.COMPILATION_NO LEFT JOIN VIEW_DATA_DICT_ITEM di ON di.ITEM_CODE=fa.ENTER_SOURCE AND di.DICT_ID='ENTER_SOURCE' LEFT JOIN VIEW_DATA_DICT_ITEM dt ON fc.COM_ORGANIZATION=dt.ITEM_CODE AND dt.DICT_ID='COM_ORGANIZATION' LEFT JOIN (SELECT COMPILATION_NO,COUNT(*) AS co FROM FB_MEASURE_WORD GROUP BY COMPILATION_NO) fm ON fm.COMPILATION_NO=fa.COMPILATION_NO WHERE date_format(fa.CREATE_TIME,'%Y-%m')=date_format(now(),'%Y-%m') ORDER BY fa.CREATE_TIME",nativeQuery = true)
    ArrayList<Object[]> findByATT();
}
