package com.hmg.licensingservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/confsvr")
public class ConfigController {
    @Autowired
    private ServiceConfig serviceConfig;

    @GetMapping(value = "/getExampleProperty")
    public void getExampleProperty() {
        System.out.println(serviceConfig.getExampleProperty()); // "I AM IN THE DEFAULT"
    }
}
