package com.sda.config;

import com.sda.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final BidService bidService;

    @Autowired
    public SchedulerConfig(BidService bidService) {
        this.bidService = bidService;
    }

    @Scheduled(fixedDelay = 5000)//milliseconds
    public void regularJob(){
        System.out.println("Running job at " + LocalDateTime.now());
        bidService.assigneWinners();
    }
}
