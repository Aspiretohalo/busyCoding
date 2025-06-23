package com.busymango.busyCoding.judge;

import com.busymango.busyCoding.judge.strategy.DefaultJudgeStrategy;
import com.busymango.busyCoding.judge.strategy.JavaLanguageJudgeStrategy;
import com.busymango.busyCoding.judge.strategy.JudgeContext;
import com.busymango.busyCoding.judge.strategy.JudgeStrategy;
import com.busymango.busyCoding.judge.codesandbox.model.JudgeInfo;
import com.busymango.busyCoding.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
