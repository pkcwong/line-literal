package com.pwned.line;

import com.pwned.line.service.PushWeather;
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
		PushWeather.updateWeather();
		KitchenSinkApplication.downloadedContentDir = Files.createTempDirectory("line-bot");
		SpringApplication.run(KitchenSinkApplication.class, args);
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		JobDetail pushWeather = JobBuilder.newJob(PushWeather.class)
				.withIdentity("dummyJobName", "group1").build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("PushWeather", "quartz")
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInSeconds(5).repeatForever())
				.build();
		scheduler.start();
		scheduler.scheduleJob(pushWeather, trigger);
	}

}
