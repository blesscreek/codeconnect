package com.co.backend.controller;

import com.co.backend.service.sse.SseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-06-12 16:25
 */
@RestController
@RequestMapping("/sse")
@Slf4j
public class SSEController {
    @Autowired
    private SseService sseService;
    /**
     * 创建SSE连接
     *
     * @return
     */
    @GetMapping(path = "/connect/{qid}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sse(@PathVariable("qid") String qid) {
        return sseService.connect(qid);
    }

    @GetMapping(value = "/close/{userId}")
    public void close(@PathVariable("userId") String userId){
        sseService.close(userId);
    }


}
