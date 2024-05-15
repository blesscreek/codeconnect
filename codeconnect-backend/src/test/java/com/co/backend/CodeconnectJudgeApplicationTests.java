//package com.co.codeconnectjudge;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.BufferedReader;
//
//@SpringBootTest
//class CodeconnectJudgeApplicationTests {
//
//    public class Chapter3 {
//        public static void main(String[] args) throws Exception{
//            Configuration conf = new Configuration();
//            String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
//            //检查传入参数args，确保有且只有两个参数（输入文件路径和输出文件路径）。
//            if (otherArgs.length != 2)
//            {      System.err.println("Usage: wordcount <in> <out>");
//                System.exit(2);
//            }
//            //创建一个新的Job实例，用于配置和提交MapReduce作业，作业名称为"word count"。
//            Job job = new Job(conf,"word count");
//            //设置作业的JAR文件，这里使用当前类WordCount所在的JAR。
//            job.setJarByClass(WordCount.class);
//            //设置Mapper类为MyMapper，这是自定义的映射器类，负责将输入数据分割成键值对。
//            job.setMapperClass(MyMapper.class);
//            //设置Reducer类为MyReducer，这是自定义的规约器类，负责将Mapper输出的键值对进行规约。
//            job.setReducerClass(MyReducer.class);
//            //指定输出键的类型为Text，通常表示字符串。
//            job.setOutputKeyClass(Text.class);
//            //指定输出值的类型为IntWritable，用于存储整数值。
//            job.setOutputValueClass(IntWritable.class);
//            //添加输入路径，即第一个命令行参数，作为MapReduce作业的输入数据源。
//            FileInputFormat.addInputPath(job,new Path(otherArgs[0]));
//            //设置输出路径，即第二个命令行参数，作为MapReduce作业处理后的结果保存位置。
//            FileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
//            //等待作业完成，如果成功返回0，否则返回1，结束程序。
//            System.exit(job.waitForCompletion(true)?0:1);
//        }
//
//
