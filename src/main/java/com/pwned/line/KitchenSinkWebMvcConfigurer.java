package com.pwned.line;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/***
 * Java Spring Configurator
 */
@Configuration
public class KitchenSinkWebMvcConfigurer extends WebMvcConfigurerAdapter {

	/***
	 * Configurator for MVC
	 * @param registry registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String downloadedContentUri = KitchenSinkApplication.downloadedContentDir.toUri().toASCIIString();
		registry.addResourceHandler("/downloaded/**").addResourceLocations(downloadedContentUri);
	}

}
