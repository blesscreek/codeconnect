package com.co.backend;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(scanBasePackages = {"com.co.backend","com.co.common"})
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class CodeconnectBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeconnectBackendApplication.class, args);
    }

}
