package com.satvik.stockpdfspringboot.Common;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Date;
import java.util.LinkedHashMap;

@Configuration
public class Config {

    @Bean
    public LinkedHashMap<Long, Date> unVerifiedUsers(){
        return new LinkedHashMap<>();
    }
    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
