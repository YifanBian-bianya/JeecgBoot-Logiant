package com.logiant.modules.test.rocketmq.listener;//package com.logiant.modules.cloud.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.jeecg.common.base.BaseMap;
import com.logiant.modules.test.rocketmq.constant.CloudConstant;
import org.springframework.stereotype.Component;

/**
 * 定义接收者（可以定义N个接受者，消息会均匀的发送到N个接收者中）
 *
 * RabbitMq接受者2
 * （@RabbitListener声明类上，一个类只能监听一个队列）
 * @author: zyf
 * @date: 2022/04/21
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = CloudConstant.MQ_JEECG_PLACE_ORDER, consumerGroup = "helloReceiver2")
public class HelloReceiver2 implements RocketMQListener<BaseMap> {

    public void onMessage(BaseMap baseMap) {
        log.info("helloReceiver2接收消息：" + baseMap);
    }

}