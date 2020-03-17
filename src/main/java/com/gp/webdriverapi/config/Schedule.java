package com.gp.webdriverapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
public class Schedule {

    @Scheduled(cron = "0 0 0 * * *")
    public void clearRecycleBin() {
        System.out.println("定时任务执行" + new Date());
    }

}
