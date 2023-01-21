package com.log;

import java.util.concurrent.TimeUnit;

public class LoggerTask implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.asyncWriteLogs();
        }
    }
}
