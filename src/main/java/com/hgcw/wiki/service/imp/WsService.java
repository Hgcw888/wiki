package com.hgcw.wiki.service.imp;

import com.hgcw.wiki.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hgcw
 * @date 2021/5/2 16:26
 * 异步类
 */
@Component
public class WsService {
    @Resource
    private WebSocketServer webSocketServer;

    /**
     *
     * @param message
     */
    @Async
    public void sendInfo(String message,String loggingId){
       MDC.put("LOGGING_ID",loggingId);
        //将需要异步的方法调用到方法中
        webSocketServer.sendInfo(message);
    }
}
