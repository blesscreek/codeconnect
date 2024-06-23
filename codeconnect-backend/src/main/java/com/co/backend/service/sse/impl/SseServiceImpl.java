package com.co.backend.service.sse.impl;


import com.co.common.model.JudgeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.co.backend.service.sse.SseService;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-06-12 16:29
 */

@Service
@Slf4j
public class SseServiceImpl implements SseService{

    private static Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();
    public SseEmitter createSseConnect(String qid, String uid) {
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);
        String clientId = qid + ":" + uid;
        sseCache.put(clientId, sseEmitter);
        // 连接断开回调
        sseEmitter.onCompletion(() -> {
            sseCache.get(clientId).complete();
            sseCache.remove(clientId);
        });
        // 连接超时
        sseEmitter.onTimeout(()-> {
            sseCache.get(clientId).complete();
            sseCache.remove(clientId);
        });
        // 连接报错
        sseEmitter.onError((throwable) ->  {
            sseCache.get(clientId).complete();
            sseCache.remove(clientId);
        });

        return sseEmitter;
    }

    public void ssePushMsg(JudgeInfo judgeInfo) {
        String clientId = judgeInfo.getQid() + ":" + judgeInfo.getUid();
        if (CollectionUtils.isEmpty(sseCache)) {
            return;
        }
        SseEmitter.SseEventBuilder event = event();
        event.id(judgeInfo.getId().toString());
        event.name("message");
        event.data(judgeInfo);
        SseEmitter sseEmitter = sseCache.get(clientId);
        if (sseEmitter == null) {
            log.error("没有id为{}的连接",clientId);
        }
        try {
            sseEmitter.send(event);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
    @Override
    public void close(String qid, String uid) {
        String clientId = qid + ":" + uid;
        log.info("type: SseSession Close, session Id : {}", clientId);
        sseCache.remove(qid);
    }

}
