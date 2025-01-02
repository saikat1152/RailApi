package com.jbl.t24.rest.api.config;

import javax.imageio.ImageIO;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageIOPluginInitializer {

@Bean
public ApplicationRunner applicationRunner() {
return args -> ImageIO.scanForPlugins();
}
}