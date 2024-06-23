package com.co.backend.service.sse;

import com.co.common.model.JudgeInfo;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-06-12 16:28
 */

public interface SseService {
        SseEmitter createSseConnect(String qid, String uid);
        void ssePushMsg( JudgeInfo judgeInfo);
        void close(String qid, String uid);
}
