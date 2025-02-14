package com.logiant.modules.demo.cloud.service;

import com.logiant.common.api.vo.Result;

/**
 * @Description: JcloudDemoService接口
 * @author: jeecg-boot
 */
public interface JcloudDemoService {

    /**
     * 获取信息（测试）
     * @param name 姓名
     * @return "Hello，" + name
     */
    String getMessage(String name);
}
