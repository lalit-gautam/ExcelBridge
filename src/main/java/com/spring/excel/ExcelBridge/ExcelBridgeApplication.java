package com.spring.excel.ExcelBridge;

import com.spring.excel.ExcelBridge.service.ExcelProcessorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExcelBridgeApplication {

	public static void main(String[] args) {
	   ApplicationContext context = SpringApplication.run(ExcelBridgeApplication.class, args);
	   ExcelProcessorService service = context.getBean(ExcelProcessorService.class);
	   System.out.println(service.getDirName());
	}

}
