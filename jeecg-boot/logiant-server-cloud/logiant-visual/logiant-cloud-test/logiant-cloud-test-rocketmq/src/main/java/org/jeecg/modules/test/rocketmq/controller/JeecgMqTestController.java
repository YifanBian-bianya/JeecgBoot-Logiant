package com.logiant.modules.test.rocketmq.controller;


import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.logiant.boot.starter.rabbitmq.client.RabbitMqClient;
import com.logiant.common.api.vo.Result;
import org.jeecg.common.base.BaseMap;
import com.logiant.modules.test.rocketmq.constant.CloudConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * RocketMqClient发送消息
 * @author: zyf
 * @date: 2022/04/21
 */
@RestController
@RequestMapping("/sys/test")
@Api(tags = "【微服务】MQ单元测试")
public class JeecgMqTestController {

    @Autowired
    private RabbitMqClient rabbitMqClient;


    /**
     * 测试方法：快速点击发送MQ消息
     *  观察三个接受者如何分配处理消息：HelloReceiver1、HelloReceiver2、HelloReceiver3，会均衡分配
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/rocketmq")
    @ApiOperation(value = "测试rocketmq", notes = "测试rocketmq")
    public Result<?> rabbitMqClientTest(HttpServletRequest req) {
        //rabbitmq消息队列测试
        BaseMap map = new BaseMap();
        map.put("orderId", RandomUtil.randomNumbers(10));
        rabbitMqClient.sendMessage(CloudConstant.MQ_JEECG_PLACE_ORDER, map);
        rabbitMqClient.sendMessage(CloudConstant.MQ_JEECG_PLACE_ORDER_TIME, map,2);
        return Result.OK("MQ发送消息成功");
    }

    @GetMapping(value = "/rocketmq2")
    @ApiOperation(value = "rocketmq消息总线测试", notes = "rocketmq消息总线测试")
    public Result<?> rabbitmq2(HttpServletRequest req) {

        //rabbitmq消息总线测试
        BaseMap params = new BaseMap();
        params.put("orderId", "123456");
        rabbitMqClient.publishEvent(CloudConstant.MQ_DEMO_BUS_EVENT, params);
        return Result.OK("MQ发送消息成功");
    }
}
