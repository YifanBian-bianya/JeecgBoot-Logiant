package com.logiant.modules.test.feign.fallback;

import com.logiant.common.api.vo.Result;

import lombok.Setter;
import com.logiant.modules.test.feign.client.JeecgTestClient;


/**
* 接口fallback实现
* 
* @author: scott
* @date: 2022/4/11 19:41
*/
public class JeecgTestFallback implements JeecgTestClient {

    @Setter
    private Throwable cause;


    @Override
    public String getMessage(String name) {
        return "访问超时, 自定义FallbackFactory";
    }
}
