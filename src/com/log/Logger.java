package com.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

public class Logger {

    private static LinkedBlockingQueue<String> logQueue = new LinkedBlockingQueue<String>();
    private static Thread thread;
    private static  String DIR = "/Users/suyuzhou/project/springboot/asyncLogger/log/";
    private static  String FILE_NAME = "server.log";
    private static Path path  ;
    static {
        initPath();
        initLoggerTask();
    }
    public static void initPath() {
        File f = new File(DIR);
        if(!f.exists()){
            f.mkdirs(); //创建目录
        }
        path = Paths.get(DIR + FILE_NAME);
    }

    public static void initLoggerTask() {
        LoggerTask loggerTask = new LoggerTask();
        thread = new Thread(loggerTask);
        thread.start();
    }

    public static void log(String message) {

        logQueue.offer(message);
    }

    public static void asyncWriteLogs() {
        String message;
        try (BufferedWriter fileWriter = Files.newBufferedWriter(path, StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            while ((message = logQueue.poll()) != null) {
                StringWriter writer = new StringWriter();
                writer.write(new Date().toString());
                writer.write(": ");
                writer.write(message);
                fileWriter.write(writer.toString());
                fileWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for(int i=0;i < 100; i++) {
            Logger.log("hello test");
            Logger.log("hello test2");
            Logger.log("hello test3");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
