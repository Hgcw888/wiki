package com.hgcw.wiki.rocketmq;

import com.hgcw.wiki.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hgcw
 * @date 2021/5/2 17:29
 * rocketmq 消费端接受消息(发送端发送消息在docServiceimp中)
 * 使用中间件mq是又是业务量比较大，直接使用异步推送的，业务量比较大可能回出问题，
 * 使用mq的话执行到业务将服务端推送的消息发到mq存放，再拿出来推送给客户端这样业务大也不影响
 */
@Component
@Slf4j
//监听rocketma中default组，发送端的VOTE_TOPIC
@RocketMQMessageListener(consumerGroup = "default", topic = "VOTE_TOPIC")
public class VoteTopicConsumer implements RocketMQListener<MessageExt> {
@Resource
private WebSocketServer webSocketServer;
    @Override
    public void onMessage(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        log.info("ROCKETMQ收到的消息：{}", new String(body));
       //在mq中获取消息或在使用websocketserver推送给客户端
        webSocketServer.sendInfo(new String(body));
    }
}
