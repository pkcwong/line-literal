package com.pwned.line;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Slf4j
public class KitchenSinkWebMvcConfigurer extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String downloadedContentUri = KitchenSinkApplication.downloadedContentDir.toUri().toASCIIString();
		//log.info("downloaded dir: {}", downloadedContentUri);
		registry.addResourceHandler("/downloaded/**").addResourceLocations(downloadedContentUri);
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}

}
