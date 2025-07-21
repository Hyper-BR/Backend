package br.com.hyper.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static br.com.hyper.constants.BaseUrls.BASE_URL;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler( BASE_URL + "/**")
                .addResourceLocations("file:" + BASE_URL + "/");

        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/");


    }
}
