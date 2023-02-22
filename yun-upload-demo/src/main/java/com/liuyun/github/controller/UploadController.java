package com.liuyun.github.controller;

import com.liuyun.github.service.UploadService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadController {

    @Autowired
    private TusFileUploadService tusFileUploadService;
    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = { "/upload", "/upload/**" },
            method = { RequestMethod.POST, RequestMethod.PATCH, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.GET })
    public void upload(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException, TusException {
        tusFileUploadService.process(servletRequest, servletResponse);
    }

    @GetMapping("/complete")
    public String complete(@RequestParam("uri") String uri) {
        uploadService.complete(uri);
        return "SUCCESS";
    }

}
