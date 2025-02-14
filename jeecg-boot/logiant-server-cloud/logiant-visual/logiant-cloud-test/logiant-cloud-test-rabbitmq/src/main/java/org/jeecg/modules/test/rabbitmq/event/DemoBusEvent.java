package com.logiant.modules.test.rabbitmq.event;

import com.logiant.boot.starter.rabbitmq.event.EventObj;
import com.logiant.boot.starter.rabbitmq.event.JeecgBusEventHandler;
import org.jeecg.common.base.BaseMap;
import com.logiant.modules.test.rabbitmq.constant.CloudConstant;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理器【发布订阅】
 * @author: zyf
 * @date: 2022/04/21
 */
@Slf4j
@Component(CloudConstant.MQ_DEMO_BUS_EVENT)
public class DemoBusEvent implements JeecgBusEventHandler {


    @Override
    public void onMessage(EventObj obj) {
        if (ObjectUtil.isNotEmpty(obj)) {
            BaseMap baseMap = obj.getBaseMap();
            String orderId = baseMap.get("orderId");
            log.info("业务处理----订单ID:" + orderId);
        }
    }
}
