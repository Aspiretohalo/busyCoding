package com.busymango.busyCoding.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.busymango.busyCoding.annotation.AuthCheck;
import com.busymango.busyCoding.common.BaseResponse;
import com.busymango.busyCoding.common.ErrorCode;
import com.busymango.busyCoding.common.ResultUtils;
import com.busymango.busyCoding.constant.UserConstant;
import com.busymango.busyCoding.exception.BusinessException;
import com.busymango.busyCoding.model.dto.question.QuestionQueryRequest;
import com.busymango.busyCoding.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.busymango.busyCoding.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.busymango.busyCoding.model.entity.Question;
import com.busymango.busyCoding.model.entity.QuestionSubmit;
import com.busymango.busyCoding.model.entity.User;
import com.busymango.busyCoding.model.vo.QuestionSubmitVO;
import com.busymango.busyCoding.service.QuestionSubmitService;
import com.busymango.busyCoding.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *

 */
@RestController
//@RequestMapping("/question_submit")
@Slf4j
@Deprecated
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

//    /**
//     * 提交题目
//     *
//     * @param questionSubmitAddRequest
//     * @param request
//     * @return 提交记录的 id
//     */
//    @PostMapping("/")
//    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
//            HttpServletRequest request) {
//        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // 登录才能点赞
//        final User loginUser = userService.getLoginUser(request);
//        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
//        return ResultUtils.success(questionSubmitId);
//    }
//
//    /**
//     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
//     *
//     * @param questionSubmitQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/list/page")
//    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
//                                                                         HttpServletRequest request) {
//        long current = questionSubmitQueryRequest.getCurrent();
//        long size = questionSubmitQueryRequest.getPageSize();
//        // 从数据库中查询原始的题目提交分页信息
//        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
//                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
//        final User loginUser = userService.getLoginUser(request);
//        // 返回脱敏信息
//        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
//    }


}
