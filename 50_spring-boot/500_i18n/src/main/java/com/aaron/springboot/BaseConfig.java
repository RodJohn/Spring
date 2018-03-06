

package com.aaron.springboot;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
@EnableAutoConfiguration
public class BaseConfig {

	
	@Bean()
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		////排在前面的会被加载
		source.setBasenames(
				"static/i18n/market/messages"
				,"static/i18n/messages"
				,"i18n/market/messages"
				,"i18n/messages"
				);
		source.setDefaultEncoding("UTF-8");
		source.setCacheSeconds(600000);
				//缓存对java标签有效
				//对js没有效果
		source.setFallbackToSystemLocale(true);
		return source;
	}
	
	/*对应application.properties文件
	spring.messages.basename=i18n/messages
	spring.messages.cache-seconds=5
	spring.messages.encoding=UTF-8
	spring.messages.fallback-to-system-locale=true
	*/
	
	
	@Bean Object myFreeMarkerPropertie(FreeMarkerProperties properties) {
		
		Map<String, String> settings = new HashMap<String,String>();
		settings.put("auto_import", "spring.ftl as aaron");
		properties.setSettings(settings);
		return "";
	}
	
	/*
	# FREEMARKER (FreeMarkerAutoConfiguration)
	spring.freemarker.allow-request-override=false
	spring.freemarker.allow-session-override=false
	spring.freemarker.cache=false
	spring.freemarker.charset=UTF-8
	spring.freemarker.check-template-location=true
	spring.freemarker.content-type=text/html
	spring.freemarker.enabled=true
	spring.freemarker.expose-request-attributes=false
	spring.freemarker.expose-session-attributes=false
	spring.freemarker.expose-spring-macro-helpers=true
	spring.freemarker.prefer-file-system-access=true
	spring.freemarker.prefix=
	spring.freemarker.request-context-attribute=
	spring.freemarker.suffix=.ftl
	spring.freemarker.template-loader-path=classpath:/templates/
	spring.freemarker.settings.auto_import=spring.ftl as aaron
	freemarkerVariables
	#spring.freemarker.viewNames= # whitelist of view names that can be resolved
	spring.freemarker.settings.number_format=#
	*/
	
}
