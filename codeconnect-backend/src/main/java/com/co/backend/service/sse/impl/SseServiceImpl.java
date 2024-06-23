package com.co.backend.service.sse.impl;

import com.co.common.model.JudgeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.co.backend.service.sse.SseService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-06-12 16:29
 */

@Service
@Slf4j
public class SseServiceImpl implements SseService{
    //messageId的SseEmitter对象映射集
    private static Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    @Override
    public SseEmitter connect(String userId) {
        SseEmitter sseEmitter = new SseEmitter();
        // 连接成功需要返回数据，否则会出现待处理状态
        try {
            sseEmitter.send(SseEmitter.event().comment("welcome"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 连接断开
        sseEmitter.onCompletion(() -> {
            sseEmitterMap.remove(userId);
        });

        // 连接超时
        sseEmitter.onTimeout(() -> {
            sseEmitterMap.remove(userId);
        });

        // 连接报错
        sseEmitter.onError((throwable) -> {
            sseEmitterMap.remove(userId);
        });

        sseEmitterMap.put(userId, sseEmitter);

        return sseEmitter;
    }

    @Override
    public void sendJudgeRes(JudgeInfo judgeInfo) {
        Long uid = judgeInfo.getUid();
        SseEmitter sseEmitter = sseEmitterMap.get(uid.toString());
        if (sseEmitter != null) {
            try {
                sseEmitter.send(judgeInfo, MediaType.APPLICATION_JSON);
                Thread.sleep(100); // 100 毫秒的间隔
            } catch (IOException | InterruptedException e) {
                log.error("SseEmitter send message error", e);
                sseEmitter.completeWithError(e);
            }
        } else {
            log.error("User Id " + uid + " not Found");
        }
    }

    @Override
    public void close(String userId) {
        log.info("type: SseSession Close, session Id : {}", userId);
        sseEmitterMap.remove(userId);
    }
}
