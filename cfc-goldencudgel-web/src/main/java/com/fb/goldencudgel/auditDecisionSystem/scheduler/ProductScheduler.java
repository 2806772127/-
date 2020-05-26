package com.fb.goldencudgel.auditDecisionSystem.scheduler;

import com.fb.goldencudgel.auditDecisionSystem.domain.fbProdQuestion.FbProdQuestion;
import com.fb.goldencudgel.auditDecisionSystem.domain.fbProduct.FbProduct;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbProdQuestionRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.FbProductRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 产品上线定时器
 */
@Component
public class ProductScheduler {

    @Autowired
    private FbProductRepository fbProductRepository;

    @Autowired
    private FbProdQuestionRepository fbProdQuestionRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Scheduled(cron = "0 */1 * * * ?")
    private void online(){
        List<FbProduct> prodList = fbProductRepository.findOnlineProd();
        for(FbProduct prod : prodList) {
            List<FbProdQuestion> prodQuestionList = fbProdQuestionRepository.findQuestion(prod.getProductVersion(),prod.getProdId());
            for(FbProdQuestion prodQuestion : prodQuestionList) {
                //讲同类型的问卷设置为禁用
                questionnaireRepository.updateEnableStatus("0",prodQuestion.getQuestionType());
                //启用该产品配置的问卷
                questionnaireRepository.updateStatusById("1",prodQuestion.getQuestionId());
            }
            //更新上线状态
            prod.setOnlineState("01");
            fbProductRepository.saveAndFlush(prod);
        }
    }
}
