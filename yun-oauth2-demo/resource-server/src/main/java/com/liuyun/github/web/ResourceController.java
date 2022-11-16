package com.liuyun.github.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/product")
    public String getProduct() {
        return "获取产品成功!";
    }

    @GetMapping("/order")
    public String getOrder() {
        return "获取订单成功!";
    }

}
