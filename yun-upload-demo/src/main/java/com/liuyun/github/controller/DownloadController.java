package com.liuyun.github.controller;

import com.google.common.util.concurrent.RateLimiter;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DownloadController {

    public static final String FILE_PATH = "/tmp";
    public static final Integer BUFF_SIZE = 8192;

    @Autowired
    private MeterRegistry meterRegistry;
    private Counter totalBandwidthCounter;
    private Counter limitBandwidthCounter;
    private RateLimiter rateLimiter = RateLimiter.create(1024); // 1024kb

    @PostConstruct
    public void init() {
        totalBandwidthCounter = meterRegistry.counter("total_request_mb");
        limitBandwidthCounter = meterRegistry.counter("limit_request_mb");
    }

    @GetMapping("/download")
    public String download(@RequestParam String filename, HttpServletResponse response) {
        File file = new File(FILE_PATH + "/" + filename);
        if(!file.exists()){
            return "file not exist";
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[BUFF_SIZE];
            OutputStream os  = response.getOutputStream();
            int len;
            while ((len = bis.read(buff)) != -1) {
                // 请求令牌，未获取到则阻塞
                rateLimiter.acquire(BUFF_SIZE / 1024);
                os.write(buff, 0, len);
                os.flush();
            }
        } catch (IOException e) {
            log.error("download error", e);
            return "download error";
        }
        return "success";
    }

}
