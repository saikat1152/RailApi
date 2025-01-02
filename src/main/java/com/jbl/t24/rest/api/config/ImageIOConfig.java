package com.jbl.t24.rest.api.config;

import com.twelvemonkeys.servlet.image.IIOProviderContextListener;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageIOConfig {

@Bean
public ServletContextInitializer servletContextInitializer() {
return servletContext -> {
servletContext.addListener(new IIOProviderContextListener());
};
}
}