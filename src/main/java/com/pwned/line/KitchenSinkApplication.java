package com.pwned.line;

import com.pwned.line.job.PushKMB;
import com.pwned.line.job.PushThanksgiving;
import com.pwned.line.job.PushWeather;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class KitchenSinkApplication {

	public static Path downloadedContentDir;
	public static int builtVersion = 212;
	public static String version = "0.2.3";

	public static void main(String[] args) throws Exception {
		KitchenSinkApplication.downloadedContentDir = Files.createTempDirectory("line-bot");
		SpringApplication.run(KitchenSinkApplication.class, args);
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();
		scheduler.scheduleJob(PushWeather.buildJob(PushWeather.class), PushWeather.buildTrigger(300));
		scheduler.scheduleJob(PushKMB.buildJob(PushKMB.class), PushKMB.buildTrigger(300));
		scheduler.scheduleJob(PushThanksgiving.buildJob(PushThanksgiving.class), PushThanksgiving.buildTrigger(24));
	}

}
