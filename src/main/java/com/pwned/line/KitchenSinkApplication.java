package com.pwned.line;

import com.pwned.line.job.DefaultJob;
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
		JobDetail pushWeather = JobBuilder.newJob(DefaultJob.class)
				.withIdentity("Default", "service").build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("trigger", "trigger")
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInSeconds(5).repeatForever())
				.build();
		scheduler.start();
		scheduler.scheduleJob(pushWeather, trigger);
	}

}
