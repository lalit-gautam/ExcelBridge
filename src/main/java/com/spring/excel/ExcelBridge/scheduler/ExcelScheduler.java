package com.spring.excel.ExcelBridge.scheduler;

import com.spring.excel.ExcelBridge.service.ExcelProcessorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ExcelScheduler {

    @Autowired
    private ExcelProcessorService excelProcessorService;
    private static final Logger logger = LogManager.getLogger(ExcelScheduler.class);

    @Value("${schedule.time}")
    private Long scheduleTime;

    @Scheduled(fixedRateString = "${schedule.time}") // every 2 minutes
    public void scheduleExcelProcessing(){
        logger.info("Scheduler Started ... AT : {} ", new Date(System.currentTimeMillis()));
        excelProcessorService.processExcelFiles();
        logger.info("Scheduler Ended ...");
    }
}
