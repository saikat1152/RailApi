package com.jbl.t24.rest.api.config;

import javax.annotation.PostConstruct;
import javax.imageio.spi.IIORegistry;

import org.springframework.context.annotation.Configuration;

import com.twelvemonkeys.imageio.plugins.jpeg.JPEGImageReaderSpi;

@Configuration
public class TwelveMonkeysConfig {
     @PostConstruct
    public void registerTwelveMonkeys() {
        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new JPEGImageReaderSpi());
        // registry.registerServiceProvider(new PNGImageReaderSpi());
        // Register other formats as needed
    }
    
}
