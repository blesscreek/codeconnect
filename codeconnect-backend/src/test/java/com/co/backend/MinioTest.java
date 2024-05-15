package com.co.backend;

import io.minio.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description 测试MinIO
 * @author Mr.M
 * @date 2022/9/11 21:24
 * @version 1.0
 */
@SpringBootTest
public class MinioTest {

    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://123.60.15.140:9001")
                    .credentials("root", "Software.")
                    .build();

    //上传文件
    @Test
    public  void upload() {
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("questionimages")
//                    .object("test1.mp4")
                    .object("p1/P1_1.in")//添加子目录
                    .filename("E:\\develop\\upload\\P1_1.in")
//                        .contentType("video/mp4")//默认根据扩展名确定文件内容类型，也可以指定
                    .build();
            minioClient.uploadObject(testbucket);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }

    }

    @Test
    public void delete(){
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket("testcasefiles").object("p1/P1_1.in").build());
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除失败");
        }
    }

}