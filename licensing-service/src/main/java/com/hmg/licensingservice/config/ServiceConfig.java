package com.hmg.licensingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig {

    @Value("${example.property}") // Spring boot config server 에서 가져온 프로퍼티 읽기
    private String exampleProperty;

    public String getExampleProperty() {
        return exampleProperty;
    }
}
