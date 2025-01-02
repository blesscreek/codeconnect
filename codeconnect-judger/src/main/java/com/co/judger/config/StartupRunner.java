package com.co.judger.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.co.common.model.JudgeServer;
import com.co.judger.dao.JudgeServerEntityService;
import com.co.judger.messager.MQJudgeReceiver;
import com.co.judger.utils.IpUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-10-13 16:26
 */
@Component
@Slf4j
public class StartupRunner implements CommandLineRunner {
    private String ip;
    @Value("${server.port}")
    private Integer port;

    @Value("${oj-judge-server.name}")
    private String name;
    @Value("${oj-judge-server.max-task-num}")
    private Integer maxTaskNum;

    @Autowired
    JudgeServerEntityService judgeServerEntityService;
    @Autowired
    MQJudgeReceiver mqJudgeReceiver;

    private static final int cpuNum = Runtime.getRuntime().availableProcessors();
    @Override
    @Transactional
    public void run(String... args) {
        ip = IpUtils.getServiceIp();
        maxTaskNum = cpuNum + 1;

        UpdateWrapper<JudgeServer> judgeServerQueryWrapper = new UpdateWrapper<>();
        log.info("ip：port " + ip +":" + port);
        judgeServerQueryWrapper.eq("ip",ip).eq("port",port);

        judgeServerEntityService.remove(judgeServerQueryWrapper);
        boolean save = judgeServerEntityService.save(new JudgeServer()
                .setCpuCore(cpuNum)
                .setIp(ip)
                .setPort(port)
                .setUrl(ip + ":" + port)
                .setMaxTaskNumber(maxTaskNum)
                .setIsRemote(false)
                .setName(name));

        if (!save) {
            log.error("初始化判题机信息到数据库失败，请重新启动试试");
        }

        mqJudgeReceiver.init();

    }
}
