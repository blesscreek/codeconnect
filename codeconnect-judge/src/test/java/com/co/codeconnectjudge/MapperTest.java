package com.co.codeconnectjudge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-03-25 20:21
 */
@SpringBootTest
public class MapperTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void TestBCryptPasswordEncoder() {
//        String encode = passwordEncoder.encode("123");
//        System.out.println(encode);
        System.out.println(passwordEncoder.matches("123","$2a$10$hkH8KN.m/210sorJY/KgFeNr1fvP0KBOSITHTa9VDFT09ytMDDLoe"));

    }
}
