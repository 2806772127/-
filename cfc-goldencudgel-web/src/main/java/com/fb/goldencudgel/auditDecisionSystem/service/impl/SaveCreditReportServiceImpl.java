package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.dao.FileContextDao;
import com.fb.goldencudgel.auditDecisionSystem.domain.attachment.FbAttachment;
import com.fb.goldencudgel.auditDecisionSystem.mapper.FileContext;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbAttachmentResitory;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbQuestionRecordRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.MeasureWordRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.SystemConfigRepository;
import com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang.FileToRuiyangRequest;
import com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang.FileToRuiyangResponse;
import com.fb.goldencudgel.auditDecisionSystem.utils.DateTimeUtils;
import com.fb.goldencudgel.auditDecisionSystem.utils.ItextUtils;
import com.fb.goldencudgel.auditDecisionSystem.utils.PdfUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.model.IText;
import sun.misc.BASE64Encoder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fb.goldencudgel.auditDecisionSystem.utils.NumberUtil.formatMoney;

/**
 * @ClassName CreditReportServiceImpl
 * @Author panha
 * @Date 2019/5/17 14:37
 * @Version 1.0
 **/
@Service
@Transactional
public class SaveCreditReportServiceImpl extends BaseJpaDAO {

    @Autowired
    private FileContextDao fileContextDao;
    @Autowired
    private FbAttachmentResitory fbAttachmentResitory;
    @Autowired
    private Environment env;
    @Autowired
    private FbQuestionRecordRepository fbQuestionRecordRepository;
    @Autowired
    private MeasureWordRepository measureWordRepository;
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    @Autowired
    private InterfaceServiceImpl interfaceService;

    public Map<String,String> getCreditReports(String measureId,String type){
        Map<String,String> params = new HashMap<String,String>();
        //1.企業概況数据
        List quest1 = this.findByQuestion(measureId,"04");
        for(int i =1 ;i <= quest1.size();i++){
            Object[] object = (Object[])quest1.get(i-1);
            String value = object[1].toString()+"："+object[2].toString();
            if(null != object[3] && ""!=object[3]){
                value = value +"。"+object[3].toString()+"。";
            }
            value =  value.replaceAll("？","");
            value =  value.replaceAll("\\?","");
            params.put("{key_"+i+"}",value);
        }
        //2.不同行业别数据
        List quest2 = this.findByQuestion(measureId,type);
        for(int i =1 ;i <= quest2.size();i++){
            Object[] object = (Object[])quest2.get(i-1);
            String value = object[1].toString()+"："+object[2].toString();
            if(null != object[3] && ""!=object[3]){
                value = value +"。"+object[3].toString()+"。";
            }
            value =  value.replaceAll("？","");
            value =  value.replaceAll("\\?","");
            params.put("{key2_"+i+"}",value);
        }
        //3.資金調度及償債能力補充說明
        List quest3 = this.findByQuestion(measureId,"05");
        for(int i =1 ;i <= quest3.size();i++){
            Object[] object = (Object[])quest3.get(i-1);
            String value = object[1].toString()+"："+object[2].toString();
            if(null != object[3] && ""!=object[3]){
                value = value +"。"+object[3].toString()+"。";
            }
            value = value.replaceAll("？","");
            value =  value.replaceAll("\\?","");
            params.put("{key3_"+i+"}",value);
        }
        //4.保人資歷補充說明
        List quest4 = this.findByQuestion(measureId,"06");
        for(int i =1 ;i <= quest4.size();i++){
            Object[] object = (Object[])quest4.get(i-1);
            String value = object[1].toString()+"："+object[2].toString();
            if(null != object[3] && ""!=object[3]){
                value = value +"。"+object[3].toString()+"。";
            }
            value = value.replaceAll("？","");
            value =  value.replaceAll("\\?","");
            params.put("{key4_"+i+"}",value);
        }
        return params;
    }

    public Map<String,String> saveMongodbFile(String pdfUrl){
        String buffPath = systemConfigRepository.findByID("FILE_BUFF_PATH").getKeyvalue();
        String attachFilePath = systemConfigRepository.findByID("ATTACH_FILE_PATH").getKeyvalue();
        Map<String, String> params = new HashMap<String, String>();
        FileInputStream ips = null;
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        FileContext fileContext = new FileContext();
        fileContext.setFileId(uuid);
        String fileContextString =pdfToByte(pdfUrl).replaceAll("\r\n","");
        fileContextString =fileContextString.replaceAll("\n","");
        fileContext.setFileContext(fileContextString);
        fileContextDao.insertOne(fileContext);//儲存到Mongodb
        FbAttachment fafile = new FbAttachment();
        String filePath = buffPath+ DateTimeUtils.formatDateToStr(new Date(), "yyyy/MM/dd") + "/cache/" + uuid + ".pdf";
        fafile.setAttachId(uuid);
        fafile.setFileContextId(uuid);
        fafile.setFileName("徵信報告.pdf");
        fafile.setAttactName("徵信報告");
        fafile.setFilePath(filePath);
        fafile.setAttachUrl(attachFilePath+uuid);
        Date date = new Date();
        fafile.setCreateTime(date);
        fbAttachmentResitory.save(fafile);//儲存附件信息
        params.put("creditReportUrl",attachFilePath+uuid);
        params.put("creditReportAttachId",uuid);
        return params;
    }

    public  String pdfToByte(String path){
        byte[] data = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            data = new byte[input.available()];
            input.read(data);
            input.close();
        }
        catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        }
        catch (IOException ex1) {
            ex1.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String fileContent = encoder.encode(data);
        fileContent = "data:application/pdf;base64," + fileContent;
        return fileContent;
    }

    public List findByQuestion(String measureId, String type) {
        List list=fbQuestionRecordRepository.findByQuestion(measureId,type);
        return list;
    }

    public List findTypeByMeasureId(String measureId){
        List list=fbQuestionRecordRepository.findTypeByMeasureId(measureId);
        return list;
    }

    public Map<String,String> saveCreditReportWeb(String compCode,String measureId,String type,String schedulerId){
        Map<String,String> map = new HashMap<String,String>();
        String newPdfUrl = findQuestionByMeasureId2(measureId,type);
        map = saveMongodbFile(newPdfUrl);
        map.put("schedulerId",schedulerId);
        map.put("compCode",compCode);
        return map;
    }

    public String findQuestionByMeasureId(String measureId, String type){
        List list0=fbQuestionRecordRepository.findQuestionByMeasureId(measureId,type);
        List list1=fbQuestionRecordRepository.findQuestionByMeasureId(measureId,"04");
        List list2=fbQuestionRecordRepository.findQuestionByMeasureId(measureId,"05");
        List list3=fbQuestionRecordRepository.findQuestionByMeasureId(measureId,"06");
        List list4=fbQuestionRecordRepository.findQuestionByMeasureId(measureId,"13");
        List<List> lists  = new ArrayList<>();
        lists.add(list1);
        lists.add(list0);
        lists.add(list2);
        lists.add(list3);
        lists.add(list4);
        List nameList = fbQuestionRecordRepository.findQuestionName(type);
        /*list.addAll(list1);
        list.addAll(list2);
        list.addAll(list3);*/
        //1.将查询的数据列表的问题，判断是否有句号，然后有句号，那么问题需要截取句号前面的，句号后面的为答案，答案的值需要拼接到
        for(List list :lists){
            int x = 0 ;//获取到第一笔的下标
            for(int i =0 ;i < list.size();i++){
                Object[] object = (Object[])list.get(i);
                Object[] objectBefore = null;
                Object[] objectAfter = null;
                if(i>0){
                    objectBefore = (Object[])list.get(i-1);
                }else{
                    objectBefore = null;
                }
                if(i<list.size()-1){
                    objectAfter = (Object[])list.get(i+1);
                }else{
                    objectAfter = null;
                }
            //1.主题目中没有。的情况
                if(object[0].toString().indexOf("。")==-1) {
                    //1.1存在子题目出现。的情况，然后获取到子题目，并且获取到下一道题的子题目，如果相同，
                    // 则需要将下一道题的子题目放到该子题目的后面，并删除下一道题目
                    if (objectAfter != null && object[2] != null && object[2].toString().indexOf("。") != -1 && objectAfter[0].toString().equals(object[0].toString())) {
                        if (object[0].equals(objectAfter[0]) && object[1].equals(objectAfter[1])) {
                            String[] c = object[2].toString().split("。");
                            if (c[1].indexOf("：") != -1) {
                                String[] questionC = c[1].split("：");//获取到该题目的子题目问题
                                if (objectAfter[2].toString().indexOf("：") != -1) {
                                    String[] questionB = objectAfter[2].toString().split("：");//获取到下一道题的子题目题目
                                    if (questionC[0].equals(questionB[0])) {//
                                        object[3] = object[3] + "。" + objectAfter[2] + "。" + objectAfter[3];
                                        list.remove(i + 1);
                                        i--;
                                        for(int j =0 ;j < list.size();j++){
                                            Object[] objects = (Object[])list.get(j);
                                            if(objects[0].toString().equals(object[0].toString())){
                                                objects[4] = Integer.parseInt(objects[4].toString()) -1;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                    //主题目和子题目相同
                    if (objectAfter != null && objectAfter[0] != null && object[0] != null && objectAfter[0].toString().equals(object[0].toString())
                            && objectAfter[1] != null && object[1] != null && objectAfter[1].toString().equals(object[1].toString())
                            && objectAfter[2] != null && object[2] != null && objectAfter[2].toString().equals(object[2].toString())) {
                        if (objectAfter[3] != null && objectAfter[3].toString().indexOf("。") != -1) {
                            String[] s = objectAfter[3].toString().split("。");
                            object[3] = object[3] + "。" + s[1];
                            for(int j =0 ;j < list.size();j++){
                                Object[] objects = (Object[])list.get(j);
                                if(objects[0].toString().equals(object[0].toString())){
                                    objects[4] = Integer.parseInt(objects[4].toString()) -1;
                                }
                            }
                            list.remove(i + 1);
                            i--;
                        }
                    }
                }

            //2.主题目中有。的情况
                if(object[0].toString().indexOf("。")!=-1){
                    String[] s = object[0].toString().split("。");//将主题目通过。截取为一个数组
                //2.1主题目中有一个。的情况
                    if(s.length==2){
                        //2.1.1没有和该主题目相同的的数据，没有二级副题目(将这笔数据的主题目通过。隔开，然后分成第一列和第二列，并新增一笔数据，
                            //该数据的第一列和上面相同，然后第二列的数据为上笔数据的第三列的题目，第三列的数据为第三列的答案)
                        if((objectBefore ==null && !object[0].equals(objectAfter[0]))
                                || (objectAfter ==null && !object[0].equals(objectBefore[0]) )
                                || (objectAfter !=null && objectBefore !=null && !object[0].equals(objectAfter[0]) &&
                                    (!object[0].equals(objectBefore[0]) && !s[0].equals(objectBefore[0])))){
                            if((object[2]!=null && object[2]!="") && object[2].toString().indexOf("：")!=-1){
                                String[] c = object[2].toString().split("：");
                                object[0] = s[0];
                                object[2] = object[1];
                                object[1] = s[1];
                                object[4] = Integer.parseInt(object[4].toString())+1;
                                Object[] object2 = {object[0],c[0],c[1],object[3],object[4],object[5],object[6]};
                                //添加一笔数据
                                list.add(i+1,object2);
                            }
                        }else if(objectBefore ==null && object[0].equals(objectAfter[0])
                                && (object[3].toString().equals("") || object[3].toString().equals(null))
                                && (objectAfter[3].toString().equals("") || objectAfter[3].toString().equals(null))){
                            String[] c = object[2].toString().split("：");
                            object[0] = s[0];
                            object[2] = object[1];
                            object[1] = s[1];;
                            object[4] = Integer.parseInt(object[4].toString())+1;
                            Object[] object2 = {s[0],c[0],c[1],object[3],object[4],object[5],object[6]};
                            list.add(i+1,object2);
                            x = i;
                        }else if(objectAfter !=null && objectBefore !=null && object[0].equals(objectAfter[0]) &&
                                (!object[0].equals(objectBefore[0]) && !s[0].equals(objectBefore[0])) && (objectAfter[3].equals(object[3].toString()))){
                            String c  = object[2].toString();
                            object[0] = s[0];
                            object[2] = object[1];
                            object[1] = s[1];
                            for(x =1;x < Integer.parseInt(object[4].toString());x++){
                                Object[] objectx = (Object[])list.get(i+x);
                                if(objectx[0].toString().indexOf("。")!=-1 && objectx[2].toString().indexOf("：")!=-1){
                                    String[] sx = objectx[0].toString().split("。");
                                    String[] sc = objectx[2].toString().split("：");
                                    objectx[0] = sx[0];
                                    objectx[1] = sc[0];
                                    objectx[2] = sc[1];
                                }
                            }
                            object[4] = Integer.parseInt(object[4].toString())+1;
                            if(c.indexOf("：")!=-1){
                                String[] cs = c.split("：");
                                Object[] object2 = {object[0],cs[0],cs[1],object[3],object[4],object[5],object[6]};
                                list.add(i+1,object2);
                            }
                            for(int m = 1 ;m < Integer.parseInt(object[4].toString());m++){
                                Object[] objectm = (Object[])list.get(i+m);
                                if(objectm[3].toString().contains("是否大於50仟元")){
                                    if(objectm[3].toString().indexOf("。")!=-1){
                                        String[] ss = objectm[3].toString().split("。");
                                        objectm[3] = ss[0];
                                    }else{
                                        objectm[3] = "";
                                    }
                                }
                            }
                        }else{
                            //属于主题目重复中多笔的第一笔（第一笔，并且后面的主题目和该主题目相同 或者是和前一道题目不同，和后一道题目相同）
                            if((objectBefore == null && object[0].equals(objectAfter[0]))
                                    || (objectBefore != null && objectAfter != null &&
                                    (!object[0].equals(objectBefore[0]) && !s[0].equals(objectBefore[0])) && object[0].equals(objectAfter[0]))){
                                object[0] = s[0];
                                object[2] = object[1] +"。"+object[2];
                                object[1] = s[1];
                                x = i;
                            }
                            //前一道题不为空，
                            if(objectBefore != null && (object[0].equals(objectBefore[0])|| s[0].equals(objectBefore[0]))){
                                //获取到第一笔数据
                                Object[] objectx = (Object[])list.get(x);
                                if(s[0].equals(objectx[0]) && s[1].equals(objectx[1]) && (object[3].equals(null) || object[3].equals(""))){
                                    if(object[2].toString().indexOf("：")!=-1){
                                        String[] sc = object[2].toString().split("：");
                                        object[0] = s[0];
                                        object[1] = sc[0];
                                        object[2] = sc[1];
                                    }
                                }
                                if(objectx[2].toString().indexOf("。")!=-1){
                                    String[] cq = objectx[2].toString().split("。");
                                    //String childQuestion = objectx[1]+"。"+cq[0];
                                    //如果子题目（object[2]）和第一道题的子题目相同
                                    if(object[2].equals(cq[1])){
                                        objectx[3] = objectx[3]+"。"+object[3];
                                        objectx[4] = Integer.parseInt(objectx[4].toString()) -1;
                                        list.remove(i);
                                        i--;
                                    }else{
                                        object[0] = s[0];
                                        if(object[2].toString().indexOf("：")!=-1){
                                            String[] c = object[2].toString().split("：");
                                            object[1] = c[0];
                                            object[2] = c[1];
                                        }else{
                                            object[1] = object[2];
                                        }
                                        //判断object3是否存在‘。‘，并且在’。‘前的值相同
                                        if(object[3].toString().indexOf("。")!=-1 && objectBefore[3].toString().indexOf("。")!=-1){
                                            String[] cc = object[3].toString().split("。");
                                            String[] ccb = objectBefore[3].toString().split("。");
                                            if(cc[0].equals(ccb[0])){
                                                objectBefore[3] = objectBefore[3]+"。"+cc[1];
                                                objectx[4] = Integer.parseInt(objectx[4].toString()) -1;
                                                list.remove(i);
                                                i--;
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                //2.2主题目中有两个。的情况
                    if(s.length==3){
                        List<String> child = new ArrayList<String>();
                        List<String> child2 = new ArrayList<String>();
                        child.add(object[2].toString());
                        child2.add(object[3].toString());
                        for(int m = 1; m < Integer.parseInt(object[4].toString());m++){
                            Object[] object1 = (Object[])list.get(i+m);
                            if(!child.contains(object1[2].toString())){
                                child.add(object1[2].toString());
                            }

                            if(!child2.contains(object1[3].toString())){
                                child2.add(object1[3].toString());
                            }
                        }
                        for(int m = 1; m < Integer.parseInt(object[4].toString());m++){
                            list.remove(i+1);
                        }
                        object[0] = s[0];
                        object[2] = s[2]+"："+object[1];
                        object[1] = s[1];
                        object[3] = "";
                        object[4] = child.size()+child2.size()+1;
                        for(int m = 0; m < child2.size();m++){
                            Object[] object2 = {object[0],object[1],child2.get(m),"",object[4],object[5],object[6]};
                            //添加一笔数据
                            list.add(i+m+1,object2);
                        }
                        for(int m = 0; m < child.size();m++){
                            if(child.get(m).indexOf("：")!=-1){
                                String[] childss = child.get(m).split("：");
                                Object[] object2 = {object[0],childss[0],childss[1],"",object[4],object[5],object[6]};
                                //添加一笔数据
                                list.add(i+m+1+child2.size(),object2);
                            }
                        }

                    }
                }
            }
            for(int i =0 ;i < list.size();i++){
                Object[] object1 = (Object[])list.get(i);
                if(object1[3]!=null && object1[3] !=""){
                    object1[2] = object1[2].toString() + "。"+object1[3].toString();
                }
                object1[3] = object1[4].toString();
                object1[4]=1;
            }
            //判断没有。的副题目中重复的数据，需要删除，值需要相加
            /*for(int i =0 ;i < list.size()-1;i++){
                Object[] object1 = (Object[])list.get(i);
                Object[] object2 = (Object[])list.get(i+1);
                if(object1[0].toString().indexOf("。")==-1 && object1[0].toString().equals(object2[0].toString())){
                    int j = 0 ;
                    if(object2[2]!=null && object2[2]!="" && object2[2].toString().indexOf("。")!=-1 &&
                            object1[2]!=null && object1[2]!="" && object1[2].toString().indexOf("。")!=-1){
                        String[] s = object2[2].toString().split("。");
                        String[] s1 = object1[2].toString().split("。");
                        if(s[0].equals(s1[0])){
                            j++;
                            object1[2] = object1[2]+"。"+s[1];
                        }
                    }
                    if(object2[3]!=null && object2[3]!="" && object2[3].toString().indexOf("。")!=-1){
                        String[] s = object2[3].toString().split("。");
                        String[] s1 = object1[3].toString().split("。");
                        if(s[0].equals(s1[0])){
                            j++;
                            object1[3] = object1[3]+"。"+s[1];
                        }
                    }
                    if(j>0){
                        object1[4] =  Integer.parseInt(object1[4].toString())-1;
                        list.remove(i+1);
                        j=0;
                        i--;
                        for(int x =0 ;x < list.size();x++){ //将该个的主题目第一个的个数修改
                            Object[] objectx = (Object[])list.get(x);
                            if(objectx[0].toString().equals(object1[0].toString())){
                                objectx[4] = object1[4];
                                continue;
                            }
                        }
                    }
                }
            }*/
            //二级副题目有值，然后副题目为空，并且上一到题目不是这种情况（或者是第一笔数据），则需要加一笔数据。
            /*for(int i =0 ;i < list.size();i++){
                Object[] object = (Object[])list.get(i);
                if(i==0){
                    if(object[3] !="" && object[3] !=null && (object[2] =="" || object[2] ==null)){
                        object[4] = Integer.parseInt(object[4].toString())+1;
                        Object[] object1 = {object[0],"",object[2],object[3],object[4],object[5],object[6]};
                        list.add(i+1,object1);
                        for(int x =0 ;x < list.size();x++){ //将该个的主题目第一个的个数修改
                            Object[] objectx = (Object[])list.get(x);
                            if(objectx[0].toString().equals(object[0].toString())){
                                objectx[4] = object[4];
                                continue;
                            }
                        }
                    }
                }else{
                    Object[] object1 = (Object[])list.get(i-1);
                    //object[3] !="" && object[3] !=null && (object[2] =="" || object[2] ==null) &&
                    if(object[3] !="" && object[3] !=null && (object[2] =="" || object[2] ==null)){
                        //(object1[3] =="" || object1[3] ==null || (object1[2] !="" && object1[2] !=null))
                        if(!object1[0].toString().equals(object[0].toString())){
                            object[4] = Integer.parseInt(object[4].toString())+1;
                            Object[] object2 = {object[0],"",object[2],object[3],object[4],object[5],object[6]};
                            list.add(i+1,object2);
                            for(int x =0 ;x < list.size();x++){ //将该个的主题目第一个的个数修改
                                Object[] objectx = (Object[])list.get(x);
                                if(objectx[0].toString().equals(object[0].toString())){
                                    objectx[4] = object[4];
                                    continue;
                                }
                            }
                        }else{
                            object[1] = "";
                        }

                    }
                }

            }*/


            /*for(int i =0 ;i < list.size();i++){
                Object[] object = (Object[])list.get(i);
                Object[] object1 = null;
                if(i>0){
                    object1 = (Object[])list.get(i-1);
                }
                if(object[0].toString().indexOf("。")!=-1){//判断题目是否有。  有。的都是主题目和副题目连在一起
                    String[] s = object[0].toString().split("。");//将主题目通过。截取为一个数组
                    object[0] = s[0]; //获取到主题目
                    if((object[3]!="" && object[3]!=null)){ //如果
                        if(object1 !=null && !object[0].toString().equals(object1[0].toString())){
                            object[4] = Integer.parseInt(object[4].toString())+1;
                            Object[] object2 = {object[0],object[2],"","",object[4],object[5],object[6]};
                            list.add(i+1,object2);
                            object[1] = s[1]+"："+object[1];
                            object[2] = "";
                            object[3] = object[4];
                            object[4] = 1;
                            continue;
                        }
                        if(object[2]=="" || object[2]==null){
                            if(object[1]=="" || object[1]==null){ //代表是用二级副题目的数据
                                object[1] = object[3];
                            }else{
                                object[1] = s[1]+"："+object[1];
                            }
                        }else{
                            object[1] = object[2];
                            object[2] = object[3];
                        }

                    }else{
                        object[1] = s[1]+"："+object[1];
                    }
                }
                object[3] = object[4];
                object[4] = 1;
            }*/
            //修改千分号
            for(int j =0 ;j < list.size();j++){
                Object[] object = (Object[])list.get(j);
                object[1] = formatMoney(object[0].toString(),object[1].toString());
                if(object[2]!=null && !object[2].equals("")){
                    object[2] = formatMoney(object[1].toString(),object[2].toString());
                }
            }

            int y = 1;
            for(int j =1 ;j < list.size();j++){
                Object[] object1 = (Object[])list.get(j-1);
                Object[] object2 = (Object[])list.get(j);
                if(object1[1].toString().equals(object2[1].toString()) && object1[0].toString().equals(object2[0].toString())){
                    y++;
                }else{
                    y = 1;
                }
                if(y > 1){
                    for(int z = 0 ;z < y;z++){
                        Object[] object3 = (Object[])list.get(j-z);
                        object3[4] = y;
                    }
                }
            }
            //
            /*int z = 0;//循环多少次
            for(int j =1 ;j < list.size();j++){
                int d = 0 ;//判断是否进入判断
                Object[] object1 = (Object[])list.get(j-1);
                Object[] object2 = (Object[])list.get(j);
                if(object1[0].toString().equals(object2[0].toString())){
                    z++;
                    d=1;
                    if(Integer.parseInt(object1[3].toString()) < Integer.parseInt(object2[3].toString()) ){
                        object2[3] = object1[3];
                    }
                }
                if(d == 0 && z > 0){
                    for(int m  = 0;m < z ;m++){
                        Object[] object3 = (Object[])list.get(j-2-m);
                        object3[3] = object1[3];
                    }
                    z =0;
                }
            }*/
            //主题目是否显示（）
            for(int i =0 ;i < list.size();i++){
                Object[] object = (Object[])list.get(i);
                if(object[3]==null || object[3] == ""){
                    object[3] =1;
                }
                if(Integer.parseInt(object[3].toString())>1){
                    object[5] = 1;
                    i = i +Integer.parseInt(object[3].toString())-1;
                }else{
                    object[5] = 1;
                }
            }
            //副题目是否显示（）
            for(int i =0 ;i < list.size();i++){
                Object[] object = (Object[])list.get(i);
                if(Integer.parseInt(object[4].toString())>1){
                    object[6] = 1;
                    i = i +Integer.parseInt(object[4].toString())-1;
                }else{
                    object[6] = 1;
                }
            }
        }

        //通过测字id到行程的数据
        List schedulerList = this.measureWordRepository.findSchedulerByMeasureId(measureId);
        Object[] scheduler = (Object[])schedulerList.get(0);

        Map<String,String> schedulerMap = new HashMap<String,String>();
        schedulerMap.put("COMPILATION_NAME",scheduler[0].toString());
        schedulerMap.put("APPOINTMENT_DATE",chengeTime(stringToDate(scheduler[1].toString())));
        schedulerMap.put("APPOINTMENT_USER_NAME",scheduler[2].toString());
        schedulerMap.put("NOW_DATE",chengeTime(new Date()));
        String buffPath = systemConfigRepository.findByID("FILE_BUFF_PATH").getKeyvalue()+new Date().getTime()+".pdf";
        //ItextUtils.itexts(list,buffPath,title);
        PdfUtil.createPdf(lists,buffPath,nameList,schedulerMap);
        return buffPath;
    }

    //替换民国时间
    private String chengeTime(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String format = simpleDateFormat.format(date);
        String[] s = format.split("/");
        s[0] = String.valueOf((Integer.parseInt(s[0])-1911));
        return s[0]+"/"+s[1]+"/"+s[2];
    }

    public static Date stringToDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(date == null || "".equals(date)){
            return null;
        }else{
            try{
                Date reDate = sdf.parse(date);
                return reDate;
            }catch (Exception e){
                return null;
            }
        }
    }
/**
 * 调bwce端FileToRuiyang接口
 *
 */
    public FileToRuiyangResponse fileToRuiyang(String schedulerId, String compilationno) {

        //根据行程编号获取征信实访编号
        Map<String, Object> param = new HashMap<>();
        StringBuffer usersql = new StringBuffer("select ZJ_CREDIT_NUM from FB_APPOINTMENT_RECORD WHERE 1=1 ");
        usersql.append(" and APPOINTMENT_ID=:schedulerId");
        param.put("schedulerId", schedulerId);
        List<Object> list = findBySQL(usersql, param).getContent();
        String zjcreditnum = list.get(0).toString();

        FileToRuiyangRequest fileToRuiyangRq = new FileToRuiyangRequest();
        fileToRuiyangRq.setAppointmentid(schedulerId);
        fileToRuiyangRq.setZjcreditnum(zjcreditnum);
        fileToRuiyangRq.setCompilationno(compilationno);
        FileToRuiyangResponse fileToRuiyangRs = interfaceService.fileToRuiyang(fileToRuiyangRq);
        return fileToRuiyangRs;

    }

    public String findQuestionByMeasureId2(String measureId, String type){
/*        List list = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,"");
        setQuestionAnswerDate(list);
        List<Object[]> list1 = new ArrayList<>();
        List<Object[]> list2 = new ArrayList<>();
        List<Object[]> list3 = new ArrayList<>();
        List<Object[]> list4 = new ArrayList<>();
        List<Object[]> list5 = new ArrayList<>();
        List<List> lists = new ArrayList<>();
        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
        lists.add(list4);
        lists.add(list5);
        for(int i = 0;i < list.size();i++ ){
            Object[] s = (Object[])list.get(i);//s[0] 是问卷类型 ，s[1] 关联子题目 ，s[2] 题目，s[3] 答案 s[4] 问题层次 s[5] 题目类型
            //首先查询出该主题目的所以副题目
            String fatherQuestion = s[1].toString();//得到fatherId
            //String[] column = new String[10];//每一列的数据
            //Object[] object = new Object[7] ;//组装后的数据
            //首先判断主问题是否有"。"
            if(s[2].toString().indexOf("。")!=-1){
                //1.主问题带。
                String[] sz = s[2].toString().split("。");
                //1.1主问题带一个。
                if(sz.length==2){
                    //查询该主问题是否存在问题（包含副问题和二级副问题）
                    List listChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion);
                    setQuestionAnswerDate(listChilds);
                    if(listChilds.size()>0){
                        Object[] sco = (Object[])listChilds.get(0);//获取到主问题的第一道关联问题
                        //判断是否存在二级副问题
                        if(sco[4]!=null &&  sco[4].toString().equals("3")){//存在二级副问题
                            String fatherThQuestion = sco[1].toString();
                            String third = s[3].toString()+"。"+sco[2]+"："+sco[3];
                            List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                            setQuestionAnswerDate(listThChilds);
                            for(int y = 0 ; y < listThChilds.size();y++){
                                Object[] sthc = (Object[])listThChilds.get(y);
                                third = third +"。"+sthc[2]+"："+sthc[3];
                            }
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,sz[0].toString(),sz[1].toString(),third);
                            setList(s[0].toString(),object,type,lists);
                        }else{//不存在二级副问题
                            String third = s[3].toString();
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,sz[0].toString(),sz[1].toString(),third);
                            setList(s[0].toString(),object,type,lists);
                        }
                        for(int j = 0;j < listChilds.size();j++){//循环问题
                            Object[] sc = (Object[])listChilds.get(j);//获取到该副问题
                            //判断是否存在副问题
                            if(sc[4]!=null &&  sc[4].toString().equals("2")){//存在副问题
                                String third = sc[3].toString();
                                String fatherTwoQuestion = sc[1].toString();
                                //查询该副问题关联的二级副问题
                                List listTwoChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherTwoQuestion);
                                setQuestionAnswerDate(listTwoChilds);
                                //判断该副问题是否存在二级副问题
                                if(listTwoChilds.size()>0){
                                    for(int x = 0 ;x < listTwoChilds.size();x++){
                                        Object[] stc = (Object[])listTwoChilds.get(x);
                                        third = third +"。"+stc[2]+"："+stc[3];
                                        String fatherThQuestion = stc[1].toString();
                                        List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                                        setQuestionAnswerDate(listThChilds);
                                        for(int y = 0 ; y < listThChilds.size();y++){
                                            Object[] sthc = (Object[])listThChilds.get(y);
                                            third = third +"。"+sthc[2]+"："+sthc[3];
                                        }
                                    }
                                    Object[] object = new Object[7] ;//组装后的数据
                                    setObject(object,sz[0].toString(),sc[2].toString(),third);
                                    setList(s[0].toString(),object,type,lists);
                                }else{//没有二级副题目
                                    Object[] object = new Object[7] ;//组装后的数据
                                    setObject(object,sz[0].toString(),sc[2].toString(),sc[3].toString());
                                    setList(s[0].toString(),object,type,lists);
                                }
                            }
                        }

                    }else{//不存在关联问题
                        Object[] object = new Object[7] ;//组装后的数据
                        setObject(object,sz[0].toString(),sz[1].toString(),s[3].toString());
                        setList(s[0].toString(),object,type,lists);
                    }
                }
                if(sz.length==3){
                    //获取到所以关联问题
                    List listChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion);
                    setQuestionAnswerDate(listChilds);
                    //判断是否存在问题
                    if(listChilds.size()>0){
                        Object[] sco = (Object[])listChilds.get(0);//获取到主问题的第一道关联问题
                        //判断是否存在三级副问题
                        if(sco[4]!=null &&  sco[4].toString().equals("4")){//存在三级副问题
                            String fatherThQuestion = sco[1].toString();
                            String third = sz[2]+"："+s[3].toString()+"。"+sco[2]+"："+sco[3];
                            List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                            setQuestionAnswerDate(listThChilds);
                            for(int y = 0 ; y < listThChilds.size();y++){
                                Object[] sthc = (Object[])listThChilds.get(y);
                                third = third +"。"+sthc[2]+"："+sthc[3];
                            }
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,sz[0].toString(),sz[1].toString(),third);
                            setList(s[0].toString(),object,type,lists);
                        }else{//不存在三级副问题
                            String third = sz[2]+"："+s[3].toString();
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,sz[0].toString(),sz[1].toString(),third);
                            setList(s[0].toString(),object,type,lists);
                        }
                        //循环问题判断是否存在副题目和二级副题目
                        for(int j = 0;j < listChilds.size();j++){//循环问题
                            Object[] sc = (Object[])listChilds.get(j);//获取到该副问题
                            //判断是否存在二级副问题
                            if(sc[4]!=null &&  sc[4].toString().equals("3")){
                                Object[] stc = (Object[])listChilds.get(j);
                                String third = stc[2]+"："+stc[3];
                                String fatherThQuestion = stc[1].toString();
                                List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                                setQuestionAnswerDate(listThChilds);
                                for(int y = 0 ; y < listThChilds.size();y++){
                                    Object[] sthc = (Object[])listThChilds.get(y);
                                    third = third +"。"+sthc[2]+"："+sthc[3];
                                }
                                Object[] object = new Object[7] ;//组装后的数据
                                setObject(object,sz[0].toString(),sz[1].toString(),third);
                                setList(s[0].toString(),object,type,lists);
                            }
                            //判断是否存在副问题
                            if(sc[4]!=null &&  sc[4].toString().equals("2")){//存在副问题
                                String third = sc[3].toString();
                                String fatherTwoQuestion = sc[1].toString();
                                //查询该副问题关联的二级副问题
                                List listTwoChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherTwoQuestion);
                                setQuestionAnswerDate(listTwoChilds);
                                //判断该副问题是否存在二级副问题
                                if(listTwoChilds.size()>0){
                                    for(int x = 0 ;x < listTwoChilds.size();x++){
                                        Object[] stc = (Object[])listTwoChilds.get(x);
                                        third = third +"。"+stc[2]+"："+stc[3];
                                        String fatherThQuestion = stc[1].toString();
                                        List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                                        setQuestionAnswerDate(listThChilds);
                                        for(int y = 0 ; y < listThChilds.size();y++){
                                            Object[] sthc = (Object[])listThChilds.get(y);
                                            third = third +"。"+sthc[2]+"："+sthc[3];
                                        }
                                    }
                                    Object[] object = new Object[7] ;//组装后的数据
                                    setObject(object,sz[0].toString(),sc[2].toString(),third);
                                    setList(s[0].toString(),object,type,lists);
                                }else{//没有二级副题目
                                    Object[] object = new Object[7] ;//组装后的数据
                                    setObject(object,sz[0].toString(),sc[2].toString(),sc[3].toString());
                                    setList(s[0].toString(),object,type,lists);
                                }
                            }
                        }

                    }else{
                        Object[] object = new Object[7] ;//组装后的数据
                        setObject(object,sz[0].toString(),sz[1].toString(),sz[2]+"："+s[3].toString());
                        setList(s[0].toString(),object,type,lists);
                    }
                }
                //1.2主问题带两个。
            }else{
                //2.主问题不带。
                // 查询出该主问题关联的副问题
                List listChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion);
                setQuestionAnswerDate(listChilds);
                //判断该主问题是否存在副问题
                if(listChilds.size()>0){//存在副问题
                    for(int j = 0;j < listChilds.size();j++){//循环副问题
                        Object[] sc = (Object[])listChilds.get(j);//获取到该副问题
                        String third = sc[2]+"："+sc[3];
                        String fatherTwoQuestion = sc[1].toString();
                        //查询该副问题关联的二级副问题
                        List listTwoChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherTwoQuestion);
                        setQuestionAnswerDate(listTwoChilds);
                        //判断该副问题是否存在二级副问题
                        if(listTwoChilds.size()>0){
                            for(int x = 0 ;x < listTwoChilds.size();x++){
                                Object[] stc = (Object[])listTwoChilds.get(x);
                                third = third +"。"+stc[2]+"："+stc[3];
                                String fatherThQuestion = stc[1].toString();
                                List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                                setQuestionAnswerDate(listThChilds);
                                for(int y = 0 ; y < listThChilds.size();y++){
                                    Object[] sthc = (Object[])listThChilds.get(y);
                                    third = third +"。"+sthc[2]+"："+sthc[3];
                                }
                            }
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,s[2].toString(),s[3].toString(),third);
                            setList(s[0].toString(),object,type,lists);
                        }else{//没有二级副题目
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,s[2].toString(),s[3].toString(),sc[2]+"："+sc[3]);
                            setList(s[0].toString(),object,type,lists);
                        }
                    }

                }else{//不存在副问题
                    Object[] object = new Object[7] ;//组装后的数据
                    setObject(object,s[2].toString(),s[3].toString(),"");
                    setList(s[0].toString(),object,type,lists);
                }
            }
            //List listChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion);
            //查询关联该主题目的所以题目（副题目和二级副题目）
            *//*
            if(listChilds.size()>0){//判断是否存在题目
                for(int j = 0;j < listChilds.size() ;j++){//对所以的题目进行循环
                    Object[] c = (Object[])listChilds.get(j);//副题目数据
                    //判断是副问题还是二级副问题
                    if(c[4]!=null && c[4].toString().equals("2")){//副问题
                        String fatherQuestion2 = c[1].toString();//得到二级副题目的fatherId
                        List listChilds2 = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion2);//查询该副题目的二级副题目
                        if(listChilds2.size()>0){ //判断是否存在二级副题目 存在二级副题目
                            //判断主题目是否有。 （通过这个来确定问题的维度，如果是没有“。”，则以副问题为维度创建数据，如果有“。”则以二级副问题为维度创建数据）
                            if(column[1] == null || column[1].equals("")){//主问题没有“。”
                                //获取到该副问题下面的所有的二级副问题，以及二级副问题下面的二级副问题相加创建数据
                                column[1] = s[3].toString();
                                column[2] = c[2].toString()+"："+c[3].toString();//先将副题目加入
                                for(int z = 0;z < listChilds2.size();z++){
                                    Object[] c2 = (Object[])listChilds2.get(z);//二级副题目数据
                                    column[2] = column[2]+"。"+c2[2].toString()+"："+ c2[3].toString();//将二级副题目加入
                                    List listChilds3 = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion2);//查询该二级副题目的二级副题目
                                    for(int y = 0;y < listChilds3.size();y++){
                                        Object[] c3 = (Object[])listChilds3.get(y);//二级副题目数据
                                        column[2] = column[2]+"。"+c3[2].toString()+"："+ c3[3].toString();//将二级副题目下的二级副题目（可以理解为三级副题目）加入
                                    }
                                }
                                Object[] object = new Object[7] ;//组装后的数据
                                setObject(object,column[0],column[1],column[2]);
                                setList(s[0].toString(),object,type,lists);
                            }else{//主问题存在“。”
                                //循环该副问题下面的二级副问题，已二级副问题为维度，查询该二级副问题下所有的二级副问题创建数据
                                column[1] = c[2].toString();//将副题目加入放入第二列
                                for(int z = 0;z < listChilds2.size();z++){
                                    column[2] = c[3].toString();//将副题目答案放入第三列
                                    Object[] c2 = (Object[])listChilds2.get(z);//二级副题目数据
                                    column[2] = column[2]+"。"+c2[2].toString()+"："+ c2[3].toString();//将二级副题目加入
                                    List listChilds3 = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion2);//查询该二级副题目的二级副题目
                                    for(int y = 0;y < listChilds3.size();y++){
                                        Object[] c3 = (Object[])listChilds3.get(y);//二级副题目数据
                                        column[2] = column[2]+"。"+c3[2].toString()+"："+ c3[3].toString();//将二级副题目下的二级副题目（可以理解为三级副题目）加入
                                    }
                                    Object[] object = new Object[7] ;//组装后的数据
                                    setObject(object,column[0],column[1],column[2]);
                                    setList(s[0].toString(),object,type,lists);
                                }
                            }
                        }else{//只有主题目和副题目
                            //判断column[1] 是否问空（主问题是否有。）
                            if(column[1]==null || column[1].equals("") ){
                                column[1] = s[3].toString();
                                column[2] = c[2]+"："+c[3];
                            }else{
                                column[1]= c[2].toString();
                                column[2]= c[3].toString();
                            }
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,column[0],column[1],column[2]);
                            setList(s[0].toString(),object,type,lists);
                        }
                    }
                    if(c[4]!=null && c[4].toString().equals("3")){ //二级副问题
                        String fatherQuestion2 = c[1].toString();//得到二级副题目的fatherId（）二级副问题
                        List listChilds3 = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion2);
                        for(int y = 0;y < listChilds3.size();y++){
                            Object[] c3 = (Object[])listChilds3.get(y);//二级副题目数据
                            column[2] = column[2]+"。"+c3[2].toString()+"："+ c3[3].toString();//将二级副题目下的二级副题目（可以理解为三级副题目）加入
                        }
                        Object[] object = new Object[7] ;//组装后的数据
                        setObject(object,column[0],column[1],column[2]);
                        setList(s[0].toString(),object,type,lists);
                    }
                }
            }else{ //只有主问题
                if(column[1]!=null && !column[1].equals("") && (column[2]==null || column[2].equals(""))){
                    column[2] = s[3].toString();
                }
                if((column[1]==null || column[1].equals("")) && (column[2]==null || column[2].equals(""))){
                    column[1] = s[3].toString();
                }
                Object[] object = new Object[7] ;//组装后的数据
                setObject(object,column[0],column[1],column[2]);
                setList(s[0].toString(),object,type,lists);
            }*//*
        }


        for(List questionList :lists ){
            //修改千分号
            for(int j =0 ;j < questionList.size();j++){
                Object[] object = (Object[])questionList.get(j);
                object[1] = formatMoney(object[0].toString(),object[1].toString());
                if(object[2]!=null && !object[2].equals("")){
                    object[2] = formatMoney(object[1].toString(),object[2].toString());
                }
            }
            //修改第一列合并的列数
            int x = 1;
            for(int i = 0;i<questionList.size()-1;i++){
                Object[] object = (Object[])questionList.get(i);
                Object[] object1 = (Object[])questionList.get(i+1);
                if(object[0].equals(object1[0])){
                    x++;
                    if(i==questionList.size()-2){
                        if(x>1){
                            for(int z = 0 ;z <x; z++){
                                Object[] objectx = (Object[])questionList.get(i-z+1);
                                objectx[3] =x;
                            }
                            x=1;
                        }
                    }
                }else{
                    if(x>1){
                        for(int z = 0 ;z <x; z++){
                            Object[] objectx = (Object[])questionList.get(i-z);
                            objectx[3] =x;
                        }
                        x=1;

                    }
                }
            }
            //修改第二列合并的列数
            int y = 1;
            for(int i = 0;i<questionList.size()-1;i++){
                Object[] object = (Object[])questionList.get(i);
                Object[] object1 = (Object[])questionList.get(i+1);
                if(object[0].equals(object1[0]) && object[1] !=null && object[1].equals(object1[1])){
                    y++;
                    if(i==questionList.size()-2){
                        if(y>1){
                            for(int z = 0 ;z <y; z++){
                                Object[] objecty = (Object[])questionList.get(i-z+1);
                                objecty[4] =y;
                            }
                            y=1;
                        }
                    }
                }else{
                    if(y>1){
                        for(int z = 0 ;z <y; z++){
                            Object[] objecty = (Object[])questionList.get(i-z);
                            objecty[4] =y;
                        }
                        y=1;
                    }
                }
            }
            //主问题是否显示（查看界面显示）
            for(int i =0 ;i < questionList.size();i++){
                Object[] object = (Object[])questionList.get(i);
                if(object[3]==null || object[3] == ""){
                    object[3] =1;
                }
                if(Integer.parseInt(object[3].toString())>1){
                    object[5] = 1;
                    i = i +Integer.parseInt(object[3].toString())-1;
                }else{
                    object[5] = 1;
                }
            }
            //副题目是否显示（查看界面显示）
            for(int i =0 ;i < questionList.size();i++){
                Object[] object = (Object[])questionList.get(i);
                if(Integer.parseInt(object[4].toString())>1){
                    object[6] = 1;
                    i = i +Integer.parseInt(object[4].toString())-1;
                }else{
                    object[6] = 1;
                }
            }
        }
        List nameList = fbQuestionRecordRepository.findQuestionName(type);*/
        Map<String,List > map= findQuestionNameAndList(measureId,type);
        List lists=map.get("lists");
        List nameList=map.get("nameList");
        List schedulerList = this.measureWordRepository.findSchedulerByMeasureId(measureId);
        Object[] scheduler = (Object[])schedulerList.get(0);

        Map<String,String> schedulerMap = new HashMap<String,String>();
        schedulerMap.put("COMPILATION_NAME",scheduler[0].toString());
        schedulerMap.put("APPOINTMENT_DATE",chengeTime(stringToDate(scheduler[1].toString())));
        schedulerMap.put("APPOINTMENT_USER_NAME",scheduler[2].toString());
        schedulerMap.put("NOW_DATE",chengeTime(new Date()));
        String buffPath = systemConfigRepository.findByID("FILE_BUFF_PATH").getKeyvalue()+new Date().getTime()+".pdf";
        //ItextUtils.itexts(list,buffPath,title);
        PdfUtil.createPdf(lists,buffPath,nameList,schedulerMap);
        return buffPath;
    }

    public Map<String ,List> findQuestionNameAndList(String measureId,String type){
        List list = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,"");
        setQuestionAnswerDate(list);
        List<Object[]> list1 = new ArrayList<>();
        List<Object[]> list2 = new ArrayList<>();
        List<Object[]> list3 = new ArrayList<>();
        List<Object[]> list4 = new ArrayList<>();
        List<Object[]> list5 = new ArrayList<>();
        List<List> lists = new ArrayList<>();
        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
        lists.add(list4);
        lists.add(list5);
        for(int i = 0;i < list.size();i++ ){
            Object[] s = (Object[])list.get(i);//s[0] 是问卷类型 ，s[1] 关联子题目 ，s[2] 题目，s[3] 答案 s[4] 问题层次 s[5] 题目类型
            //首先查询出该主题目的所以副题目
            String fatherQuestion = s[1].toString();//得到fatherId
            //String[] column = new String[10];//每一列的数据
            //Object[] object = new Object[7] ;//组装后的数据
            //首先判断主问题是否有"。"
            if(s[2].toString().indexOf("。")!=-1){
                //1.主问题带。
                String[] sz = s[2].toString().split("。");
                //1.1主问题带一个。
                if(sz.length==2){
                    //查询该主问题是否存在问题（包含副问题和二级副问题）
                    List listChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion);
                    setQuestionAnswerDate(listChilds);
                    if(listChilds.size()>0){
                        Object[] sco = (Object[])listChilds.get(0);//获取到主问题的第一道关联问题
                        //判断是否存在二级副问题
                        if(sco[4]!=null &&  sco[4].toString().equals("3")){//存在二级副问题
                            String fatherThQuestion = sco[1].toString();
                            String third = s[3].toString()+"。"+sco[2]+"："+sco[3];
                            List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                            setQuestionAnswerDate(listThChilds);
                            for(int y = 0 ; y < listThChilds.size();y++){
                                Object[] sthc = (Object[])listThChilds.get(y);
                                third = third +"。"+sthc[2]+"："+sthc[3];
                            }
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,sz[0].toString(),sz[1].toString(),third);
                            setList(s[0].toString(),object,type,lists);
                        }else{//不存在二级副问题
                            String third = s[3].toString();
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,sz[0].toString(),sz[1].toString(),third);
                            setList(s[0].toString(),object,type,lists);
                        }
                        for(int j = 0;j < listChilds.size();j++){//循环问题
                            Object[] sc = (Object[])listChilds.get(j);//获取到该副问题
                            //判断是否存在副问题
                            if(sc[4]!=null &&  sc[4].toString().equals("2")){//存在副问题
                                String third = sc[3].toString();
                                String fatherTwoQuestion = sc[1].toString();
                                //查询该副问题关联的二级副问题
                                List listTwoChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherTwoQuestion);
                                setQuestionAnswerDate(listTwoChilds);
                                //判断该副问题是否存在二级副问题
                                if(listTwoChilds.size()>0){
                                    for(int x = 0 ;x < listTwoChilds.size();x++){
                                        Object[] stc = (Object[])listTwoChilds.get(x);
                                        third = third +"。"+stc[2]+"："+stc[3];
                                        String fatherThQuestion = stc[1].toString();
                                        List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                                        setQuestionAnswerDate(listThChilds);
                                        for(int y = 0 ; y < listThChilds.size();y++){
                                            Object[] sthc = (Object[])listThChilds.get(y);
                                            third = third +"。"+sthc[2]+"："+sthc[3];
                                        }
                                    }
                                    Object[] object = new Object[7] ;//组装后的数据
                                    setObject(object,sz[0].toString(),sc[2].toString(),third);
                                    setList(s[0].toString(),object,type,lists);
                                }else{//没有二级副题目
                                    Object[] object = new Object[7] ;//组装后的数据
                                    setObject(object,sz[0].toString(),sc[2].toString(),sc[3].toString());
                                    setList(s[0].toString(),object,type,lists);
                                }
                            }
                        }

                    }else{//不存在关联问题
                        Object[] object = new Object[7] ;//组装后的数据
                        setObject(object,sz[0].toString(),sz[1].toString(),s[3].toString());
                        setList(s[0].toString(),object,type,lists);
                    }
                }
                if(sz.length==3){
                    //获取到所以关联问题
                    List listChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion);
                    setQuestionAnswerDate(listChilds);
                    //判断是否存在问题
                    if(listChilds.size()>0){
                        Object[] sco = (Object[])listChilds.get(0);//获取到主问题的第一道关联问题
                        //判断是否存在三级副问题
                        if(sco[4]!=null &&  sco[4].toString().equals("4")){//存在三级副问题
                            String fatherThQuestion = sco[1].toString();
                            String third = sz[2]+"："+s[3].toString()+"。"+sco[2]+"："+sco[3];
                            List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                            setQuestionAnswerDate(listThChilds);
                            for(int y = 0 ; y < listThChilds.size();y++){
                                Object[] sthc = (Object[])listThChilds.get(y);
                                third = third +"。"+sthc[2]+"："+sthc[3];
                            }
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,sz[0].toString(),sz[1].toString(),third);
                            setList(s[0].toString(),object,type,lists);
                        }else{//不存在三级副问题
                            String third = sz[2]+"："+s[3].toString();
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,sz[0].toString(),sz[1].toString(),third);
                            setList(s[0].toString(),object,type,lists);
                        }
                        //循环问题判断是否存在副题目和二级副题目
                        for(int j = 0;j < listChilds.size();j++){//循环问题
                            Object[] sc = (Object[])listChilds.get(j);//获取到该副问题
                            //判断是否存在二级副问题
                            if(sc[4]!=null &&  sc[4].toString().equals("3")){
                                Object[] stc = (Object[])listChilds.get(j);
                                String third = stc[2]+"："+stc[3];
                                String fatherThQuestion = stc[1].toString();
                                List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                                setQuestionAnswerDate(listThChilds);
                                for(int y = 0 ; y < listThChilds.size();y++){
                                    Object[] sthc = (Object[])listThChilds.get(y);
                                    third = third +"。"+sthc[2]+"："+sthc[3];
                                }
                                Object[] object = new Object[7] ;//组装后的数据
                                setObject(object,sz[0].toString(),sz[1].toString(),third);
                                setList(s[0].toString(),object,type,lists);
                            }
                            //判断是否存在副问题
                            if(sc[4]!=null &&  sc[4].toString().equals("2")){//存在副问题
                                String third = sc[3].toString();
                                String fatherTwoQuestion = sc[1].toString();
                                //查询该副问题关联的二级副问题
                                List listTwoChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherTwoQuestion);
                                setQuestionAnswerDate(listTwoChilds);
                                //判断该副问题是否存在二级副问题
                                if(listTwoChilds.size()>0){
                                    for(int x = 0 ;x < listTwoChilds.size();x++){
                                        Object[] stc = (Object[])listTwoChilds.get(x);
                                        third = third +"。"+stc[2]+"："+stc[3];
                                        String fatherThQuestion = stc[1].toString();
                                        List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                                        setQuestionAnswerDate(listThChilds);
                                        for(int y = 0 ; y < listThChilds.size();y++){
                                            Object[] sthc = (Object[])listThChilds.get(y);
                                            third = third +"。"+sthc[2]+"："+sthc[3];
                                        }
                                    }
                                    Object[] object = new Object[7] ;//组装后的数据
                                    setObject(object,sz[0].toString(),sc[2].toString(),third);
                                    setList(s[0].toString(),object,type,lists);
                                }else{//没有二级副题目
                                    Object[] object = new Object[7] ;//组装后的数据
                                    setObject(object,sz[0].toString(),sc[2].toString(),sc[3].toString());
                                    setList(s[0].toString(),object,type,lists);
                                }
                            }
                        }

                    }else{
                        Object[] object = new Object[7] ;//组装后的数据
                        setObject(object,sz[0].toString(),sz[1].toString(),sz[2]+"："+s[3].toString());
                        setList(s[0].toString(),object,type,lists);
                    }
                }
                //1.2主问题带两个。
            }else{
                //2.主问题不带。
                // 查询出该主问题关联的副问题
                List listChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherQuestion);
                setQuestionAnswerDate(listChilds);
                //判断该主问题是否存在副问题
                if(listChilds.size()>0){//存在副问题
                    for(int j = 0;j < listChilds.size();j++){//循环副问题
                        Object[] sc = (Object[])listChilds.get(j);//获取到该副问题
                        String third = sc[2]+"："+sc[3];
                        String fatherTwoQuestion = sc[1].toString();
                        //查询该副问题关联的二级副问题
                        List listTwoChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherTwoQuestion);
                        setQuestionAnswerDate(listTwoChilds);
                        //判断该副问题是否存在二级副问题
                        if(listTwoChilds.size()>0){
                            for(int x = 0 ;x < listTwoChilds.size();x++){
                                Object[] stc = (Object[])listTwoChilds.get(x);
                                third = third +"。"+stc[2]+"："+stc[3];
                                String fatherThQuestion = stc[1].toString();
                                List listThChilds = this.fbQuestionRecordRepository.findFatherQuestionByMeasureId(measureId,fatherThQuestion);
                                setQuestionAnswerDate(listThChilds);
                                for(int y = 0 ; y < listThChilds.size();y++){
                                    Object[] sthc = (Object[])listThChilds.get(y);
                                    third = third +"。"+sthc[2]+"："+sthc[3];
                                }
                            }
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,s[2].toString(),s[3].toString(),third);
                            setList(s[0].toString(),object,type,lists);
                        }else{//没有二级副题目
                            Object[] object = new Object[7] ;//组装后的数据
                            setObject(object,s[2].toString(),s[3].toString(),sc[2]+"："+sc[3]);
                            setList(s[0].toString(),object,type,lists);
                        }
                    }

                }else{//不存在副问题
                    Object[] object = new Object[7] ;//组装后的数据
                    setObject(object,s[2].toString(),s[3].toString(),"");
                    setList(s[0].toString(),object,type,lists);
                }
            }
        }


        for(List questionList :lists ){
            //修改千分号
            for(int j =0 ;j < questionList.size();j++){
                Object[] object = (Object[])questionList.get(j);
                object[1] = formatMoney(object[0].toString(),object[1].toString());
                if(object[2]!=null && !object[2].equals("")){
                    object[2] = formatMoney(object[1].toString(),object[2].toString());
                }
            }
            //修改第一列合并的列数
            int x = 1;
            for(int i = 0;i<questionList.size()-1;i++){
                Object[] object = (Object[])questionList.get(i);
                Object[] object1 = (Object[])questionList.get(i+1);
                if(object[0].equals(object1[0])){
                    x++;
                    if(i==questionList.size()-2){
                        if(x>1){
                            for(int z = 0 ;z <x; z++){
                                Object[] objectx = (Object[])questionList.get(i-z+1);
                                objectx[3] =x;
                            }
                            x=1;
                        }
                    }
                }else{
                    if(x>1){
                        for(int z = 0 ;z <x; z++){
                            Object[] objectx = (Object[])questionList.get(i-z);
                            objectx[3] =x;
                        }
                        x=1;

                    }
                }
            }
            //修改第二列合并的列数
            int y = 1;
            for(int i = 0;i<questionList.size()-1;i++){
                Object[] object = (Object[])questionList.get(i);
                Object[] object1 = (Object[])questionList.get(i+1);
                if(object[0].equals(object1[0]) && object[1] !=null && object[1].equals(object1[1])){
                    y++;
                    if(i==questionList.size()-2){
                        if(y>1){
                            for(int z = 0 ;z <y; z++){
                                Object[] objecty = (Object[])questionList.get(i-z+1);
                                objecty[4] =y;
                            }
                            y=1;
                        }
                    }
                }else{
                    if(y>1){
                        for(int z = 0 ;z <y; z++){
                            Object[] objecty = (Object[])questionList.get(i-z);
                            objecty[4] =y;
                        }
                        y=1;
                    }
                }
            }
            //主问题是否显示（查看界面显示）
            for(int i =0 ;i < questionList.size();i++){
                Object[] object = (Object[])questionList.get(i);
                if(object[3]==null || object[3] == ""){
                    object[3] =1;
                }
                if(Integer.parseInt(object[3].toString())>1){
                    object[5] = 1;
                    i = i +Integer.parseInt(object[3].toString())-1;
                }else{
                    object[5] = 1;
                }
            }
            //副题目是否显示（查看界面显示）
            for(int i =0 ;i < questionList.size();i++){
                Object[] object = (Object[])questionList.get(i);
                if(Integer.parseInt(object[4].toString())>1){
                    object[6] = 1;
                    i = i +Integer.parseInt(object[4].toString())-1;
                }else{
                    object[6] = 1;
                }
            }
        }
        List nameList = fbQuestionRecordRepository.findQuestionName(type);
        Map<String ,List> map =new HashMap<>();
        map.put("lists",lists);
        map.put("nameList",nameList);
        return map;
    }
    //生成pdf显示的数据
    public void setObject(Object[] o,String first,String second,String third){
        o[0] = first;
        o[1] = second;
        o[2] = third;
        o[3] = 1;
        o[4] = 1;
        o[5] = 0;
        o[6] = 0;
    }
    //将数据放到list中
    public void setList(String type,Object[] o,String industryType,List<List> list){//1.问卷类型 3.问卷行业别类型
        if(type.equals("04")){
            list.get(0).add(o);
        }
        if(type.equals(industryType)){
            list.get(1).add(o);
        }
        if(type.equals("05")){
            list.get(2).add(o);
        }
        if(type.equals("06")){
            list.get(3).add(o);
        }
        if(type.equals("13")){
            list.get(4).add(o);
        }
    }

    public void setQuestionAnswerDate(List list){
        if(list == null || list.size()==0){
            return ;
        }
        for(int i = 0 ;i < list.size() ;i++){
            Object[] object = (Object[])list.get(i);
            if(object[5]!=null && object[5].equals("05")
                    && object[3]!=null && object[3].toString().indexOf("-")!=-1){
                String[] s = object[3].toString().split("-");
                object[3] = "民國"+Integer.parseInt(s[0])+"年"+Integer.parseInt(s[1])+"月";
            }
            if(object[5]!=null && object[5].equals("07")
                    && object[3]!=null && object[3].toString().indexOf("-")!=-1){
                String[] s = object[3].toString().split("-");
                object[3] = Integer.parseInt(s[0])+"年"+Integer.parseInt(s[1])+"月";
            }
        }
    }
}
