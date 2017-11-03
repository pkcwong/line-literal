package com.pwned.line;

import com.pwned.line.job.DefaultJob;
import com.pwned.line.job.PushWeather;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class KitchenSinkApplication {

	public static Path downloadedContentDir;

	public static void main(String[] args) throws Exception {
		KitchenSinkApplication.downloadedContentDir = Files.createTempDirectory("line-bot");
		SpringApplication.run(KitchenSinkApplication.class, args);
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();
		scheduler.scheduleJob(DefaultJob.buildJob(PushWeather.class), DefaultJob.buildTrigger(5));
	}

}
