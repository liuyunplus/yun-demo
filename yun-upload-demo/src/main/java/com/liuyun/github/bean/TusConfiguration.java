package com.liuyun.github.bean;

import me.desair.tus.server.TusFileUploadService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TusConfiguration {

    @Bean
    public TusFileUploadService tusFileUploadService() {
        return new TusFileUploadService()
                .withUploadURI("/upload")
                .withStoragePath("/home/liuyun/Downloads/test/")
                .withUploadExpirationPeriod(7200000l);
    }

}
