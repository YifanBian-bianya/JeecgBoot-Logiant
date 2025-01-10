package com.logiant.modules.demo.cloud.service.impl;

import com.logiant.common.api.vo.Result;
import com.logiant.modules.demo.cloud.service.JcloudDemoService;
import org.springframework.stereotype.Service;

/**
 * @Description: JcloudDemoServiceImpl实现类
 * @author: jeecg-boot
 */
@Service
public class JcloudDemoServiceImpl implements JcloudDemoService {
    @Override
    public String getMessage(String name) {
        String resMsg = "Hello，我是jeecg-demo服务节点，收到你的消息：【 "+ name +" 】";
        return resMsg;
    }
}
