package com.co.backend.controller;

import com.co.backend.service.sse.SseService;
import com.co.common.model.JudgeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(value = "/createSseConn")
    public SseEmitter createSseConnect(@RequestParam String qid, @RequestParam String uid) {
        return sseService.createSseConnect(qid, uid);
    }
    @GetMapping(value = "/ssePushMsg")
    public void ssePushMsg() {
        String qid = String.valueOf(3);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setId(10L);
        judgeInfo.setUid(2L);
        judgeInfo.setQid(10L);
        judgeInfo.setCode("#include <aa>");
        judgeInfo.setUName("张三");
        sseService.ssePushMsg( judgeInfo);
    }
    @GetMapping(value = "/close")
    public void close(@RequestParam String qid, @RequestParam String uid){
        sseService.close(qid, uid);
    }


}
