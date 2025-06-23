package com.busymango.busyCodingCodesandbox;


import com.busymango.busyCodingCodesandbox.model.ExecuteCodeRequest;
import com.busymango.busyCodingCodesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
