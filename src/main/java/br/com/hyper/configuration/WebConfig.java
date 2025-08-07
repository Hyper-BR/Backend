package br.com.hyper.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static br.com.hyper.constants.BaseUrls.STORAGE;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler( STORAGE + "/**")
                .addResourceLocations("file:" + STORAGE + "/");

        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/");


    }
}
